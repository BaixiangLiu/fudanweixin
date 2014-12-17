package edu.fudan.weixin.model.processor;

import java.util.HashMap;
import java.util.Map;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import edu.fudan.eservice.common.utils.CommonUtil;
import edu.fudan.eservice.common.utils.MongoUtil;

/**
 * 生成一条纯文本回复
 * 
 * @author wking
 * 
 */
public class PushResultMessageProcessor implements MessageProcessor {

	@Override
	public Map<String, Object> process(Map<String, Object> message) {
		String msgtype = String.valueOf(message.get("MsgType"));
		if(CommonUtil.eq("event", msgtype)&&CommonUtil.eq("TEMPLATESENDJOBFINISH", message.get("Event")))
		{
			try{
			DBCollection coll=MongoUtil.getInstance().getDB().getCollection("Pushmsgs");
			int msgid=Integer.parseInt(String.valueOf(message.get("MsgID")));
			DBObject dbo=coll.findOne(new BasicDBObject("msgid",msgid));
			if(!CommonUtil.isEmpty(dbo)){
			dbo.put("status",message.get("Status"));
			coll.save(dbo);
			}
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			return new HashMap<String,Object>();
		}
		return null;
	}

}
