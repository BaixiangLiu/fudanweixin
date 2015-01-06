package edu.fudan.weixin.model;

import java.io.UnsupportedEncodingException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import edu.fudan.eservice.common.utils.CommonUtil;
import edu.fudan.eservice.common.utils.Config;
import edu.fudan.eservice.common.utils.MongoUtil;

public class TemplateMessage {
	
	public static String send(String template,String openid,DBObject data)
	{
		DB db=MongoUtil.getInstance().getDB();
		DBObject msg=db.getCollection("Templates").findOne(new BasicDBObject("name",template));
		if(!CommonUtil.isEmpty(msg))
		{
			msg.removeField("name");
			msg.removeField("_id");
			msg.put("touser", openid);
			
			if(!CommonUtil.isEmpty(data.get("url")))
				msg.put("url", data.get("url"));
			DBObject msgdata=(DBObject)(msg.get("data"));
			for(String k:msgdata.keySet())
			{
				Object v=data.get(k);
				if(!CommonUtil.isEmpty(v))
				((DBObject)(msgdata.get(k))).put("value",  v);
			}	
			
			StringBuffer ret;
			try {
				ret = CommonUtil.postWebRequest("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+Config.getInstance().get("weixin.access_token"), JSON.serialize(msg).getBytes("utf-8"), null);
			 return ret.toString();
			 } catch (Exception e) {
				return e.getMessage();
			} 
			
		}else
		{
			return "Template not found";
		}
	}

}
