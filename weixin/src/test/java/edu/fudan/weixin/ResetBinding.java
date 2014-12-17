package edu.fudan.weixin;

import org.junit.Test;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import edu.fudan.eservice.common.utils.MongoUtil;

public class ResetBinding {

	@Test
	public void clear()
	{
		DBCollection coll = MongoUtil.getInstance().getDB()
				.getCollection("Bindings");
		DBCursor i=coll.find();
		System.out.println(i.count());
		while(i.hasNext())
		{
			DBObject dbo=i.next();
			dbo.removeField("uisid");
			dbo.removeField("uistoken");
			dbo.removeField("uisexpired");
			dbo.removeField("uisscope");
			dbo.removeField("uisrefresh");
			dbo.removeField("usertype");
			dbo.removeField("email");
			dbo.removeField("username");
		
		
			coll.save(dbo);
		}
	}
	
	public void tttt()
	{
		DBCollection coll = MongoUtil.getInstance().getDB()
				.getCollection("Bindings");
		DBCursor i=coll.find();
		while(i.hasNext())
		{
			DBObject dbo=i.next();
			BasicDBList ls=new BasicDBList();
			if(dbo.get("uisid")!=null){
			DBObject o=new BasicDBObject();
			o.put("uisid", dbo.get("uisid"));
			o.put("uistoken", dbo.get("uistoken"));
			o.put("uisexpired", dbo.get("uisexpired"));
			o.put("uisscope", dbo.get("uisscope"));
			o.put("uisrefresh", dbo.get("uisrefresh"));
			o.put("usertype", dbo.get("usertype"));
			o.put("email", dbo.get("email"));
			o.put("username", dbo.get("username"));
			ls.add(o);}
			dbo.put("binds", ls);
			coll.save(dbo);
		}
	}
}
