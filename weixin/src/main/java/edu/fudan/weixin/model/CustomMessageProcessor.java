package edu.fudan.weixin.model;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;

import edu.fudan.eservice.common.utils.MongoUtil;
import edu.fudan.weixin.model.processor.MessageProcessor;
import edu.fudan.weixin.utils.AccessTokenHelper;
import edu.fudan.weixin.utils.WeixinFollowerHelper;
import edu.fudan.weixin.utils.WeixinMessageHelper;

/**
 * 客服消息处理
 * @author niezx
 *
 */

public class CustomMessageProcessor implements MessageProcessor {
	
	private Log log=LogFactory.getLog(WeixinFollowerHelper.class);
	
	@Override
	public Map<String, Object> process(Map<String, Object> message) {
		String messagestr = WeixinMessageHelper.msg2jsonstr(message);
		String urlstr = "https://api.weixin.qq.com/cgi-bin/message/custom/"
				+ "send?access_token="+AccessTokenHelper.getInstance().getToken(AccessTokenHelper.WEIXIN);
		String ret="";
		try {
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url
                .openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestProperty("Content-Type",
                "application/json; charset=utf-8");
			connection.connect();

			DataOutputStream out = new DataOutputStream(
                connection.getOutputStream());
			out.write(messagestr.getBytes("utf-8"));
			DataInputStream in = new DataInputStream(
					connection.getInputStream());
			BufferedReader reader=new BufferedReader(new InputStreamReader(in,"utf-8"));
			String s;
			while((s=reader.readLine())!=null) {
				ret+=s;
			}
			message.put("CreateTime", (int)(System.currentTimeMillis()/1000));
			message.put("ServerReturn", JSON.parse(ret));
			MongoUtil.getInstance().getDB().getCollection("Messages")
			.save(new BasicDBObject(message));
			log.info("send message:" + WeixinMessageHelper.msg2jsonstr(message));
			out.flush();
			out.close();
			connection.disconnect();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		}
		return WeixinMessageHelper.jsonString2dbo(ret);
		
	}

}
