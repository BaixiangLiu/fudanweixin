package edu.fudan.weixin.utils;

import java.util.Iterator;
import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import edu.fudan.eservice.common.utils.CommonUtil;
import edu.fudan.eservice.common.utils.MongoUtil;

public class BindingHelper {

	public static Object getBinding(String openid)
	{
		if(!CommonUtil.isEmpty(openid)){
			DBObject user= MongoUtil.getInstance().getDB()
		.getCollection("Bindings").findOne(new BasicDBObject("openid",openid));
			if(!CommonUtil.isEmpty(user)&&!CommonUtil.isEmpty(user.get("binds")))
				return user.get("binds");
		}
		
			return new BasicDBList();
	}
	@SuppressWarnings("unchecked")
	public static DBObject removeOthers(DBObject obj,String uisid)
	{
		
		Iterator<DBObject> i=((List<DBObject>)obj.get("binds")).iterator();
		while(i.hasNext())
		{
			DBObject o=i.next();
			if(!CommonUtil.eq(uisid, o.get("uisid"))) 
				i.remove();
		}
		return obj;
	}
}
