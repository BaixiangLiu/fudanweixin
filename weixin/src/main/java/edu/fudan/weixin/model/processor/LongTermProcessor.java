package edu.fudan.weixin.model.processor;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mongodb.BasicDBObject;

import edu.fudan.eservice.common.utils.CommonUtil;
import edu.fudan.eservice.common.utils.MongoUtil;
import edu.fudan.eservice.common.utils.ThreadPoolHelper;
import edu.fudan.weixin.utils.AccessTokenHelper;
import edu.fudan.weixin.utils.WeixinMessageHelper;

public abstract class LongTermProcessor implements MessageProcessor{
	private static Log log=LogFactory.getLog(LongTermProcessor.class);
	
	public class LongTermRun implements Runnable
	{
		private Map<String,Object> message;
		public LongTermRun(Map<String,Object> message)
		{
			this.message=message;
		}
		@Override
		public void run() {
			try {
			Map<String,Object> ret=_process(message);
			if(!CommonUtil.isEmpty(ret))
			{
				
				 ret.put("touser", message.get("FromUserName"));
				log.info(	CommonUtil.postWebRequest("https://api.weixin.qq.com/cgi-bin/message/custom/"
							+ "send?access_token="+AccessTokenHelper.getInstance().getToken(AccessTokenHelper.WEIXIN), WeixinMessageHelper.msg2jsonstr(ret).getBytes("utf-8"), "application/json; charset=utf-8"));
				// 将回复的消息 存入MongoDB
				MongoUtil.getInstance().getDB().getCollection("Messages")
						.save(new BasicDBObject(ret));
			}	} catch (Exception e) {
				log.error(e);
			}
		}
		
	}
	

	public abstract Map<String,Object> _process(Map<String,Object> msg);

	public Map<String,Object> process(Map<String,Object> msg)
	{
		LongTermRun run=new LongTermRun(msg);
		ThreadPoolHelper.getInstance().getSchPool().submit(run);
		return new HashMap<String,Object>();
	}

}
