package edu.fudan.weixin.model.processor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mongodb.BasicDBObject;

import edu.fudan.eservice.common.utils.CommonUtil;
import edu.fudan.eservice.common.utils.MongoUtil;
import edu.fudan.eservice.common.utils.ThreadPoolHelper;
import edu.fudan.weixin.model.message.JSONMessageBuilder;
import edu.fudan.weixin.utils.AccessTokenHelper;
import edu.fudan.weixin.utils.WeixinMessageHelper;

public abstract class LongTermProcessor implements MessageProcessor {
	private static Log log = LogFactory.getLog(LongTermProcessor.class);

	public class LongTermRun implements Callable<JSONMessageBuilder> {
		private Map<String, Object> message;
		private volatile boolean customsend=false;
		public LongTermRun(Map<String, Object> message) {
			this.message = message;
		}
		
		public void setCustom()
		{
			this.customsend=true;
		}

		@Override
		public JSONMessageBuilder call() {
			JSONMessageBuilder ret = null;
			try {
				ret = _process(message);
				
				// 如果当前线程已经超时被设置了interrupt标志则表明主线程已经不再等待消息处理结束，需要使用客服接口回复用户。
				if (!CommonUtil.isEmpty(ret)
						&& customsend) {
					Map<String,Object> msg=ret.getMessage();
					if(CommonUtil.isEmpty(msg)) return ret;
					msg.put("touser", message.get("FromUserName"));
					log.info(CommonUtil
							.postWebRequest(
									"https://api.weixin.qq.com/cgi-bin/message/custom/"
											+ "send?access_token="
											+ AccessTokenHelper
													.getInstance()
													.getToken(
															AccessTokenHelper.WEIXIN),
									WeixinMessageHelper.msg2jsonstr(msg)
											.getBytes("utf-8"),
									"application/json; charset=utf-8"));
					// 将回复的消息 存入MongoDB
					MongoUtil.getInstance().getDB().getCollection("Messages")
							.save(new BasicDBObject(msg));
				}
			} catch (Exception e) {
				log.error(e);
			}
			return ret;
		}

	}

	public abstract JSONMessageBuilder _process(Map<String, Object> msg);

	public Map<String, Object> process(Map<String, Object> msg) {
		LongTermRun run = new LongTermRun(msg);
		Future<JSONMessageBuilder> future=ThreadPoolHelper.getInstance().getSchPool().submit(run);
		try {
			//等待4.5s，如果此时间内未返回则由客服接口返回信息
			JSONMessageBuilder jb= future.get(4500L, TimeUnit.MILLISECONDS);
			if(!CommonUtil.isEmpty(jb))
				return jb.toXMLMessageBuilder().getMessage();
			else
				return null;
		} catch (InterruptedException e) {
		} catch (ExecutionException e) {
			log.error(e);
		} catch (TimeoutException e) {
			run.setCustom();
		}
		
		return new HashMap<String, Object>();
	}

}
