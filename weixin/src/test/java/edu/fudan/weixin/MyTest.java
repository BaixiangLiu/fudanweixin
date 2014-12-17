package edu.fudan.weixin;

import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import edu.fudan.eservice.common.utils.MongoUtil;
import edu.fudan.weixin.model.TACOAuth2Model;
import edu.fudan.weixin.utils.TACOAuth2Helper;

public class MyTest {

	//@Test
	public void test() {
		DBObject dbo=MongoUtil.getInstance().getDB().getCollection("Bindings").findOne(new BasicDBObject("openid","oew-cuDIYJFk31yYJMFKAw6Iy1oU"));
		System.out.println(dbo.toString());
	}
	
	
	//@Test
	public void test1() {
		DBObject dbo = new BasicDBObject();
		dbo.put("uistoken", "356f66af-bc2b-49fd-a07e-595f8e68ab32");
		dbo.put("uisexpired", 1404992067704L);
		dbo.put("uisid","12210240096");
		dbo.put("uisrefresh","96a96f30-eb1e-4eaa-9a50-86d47b248a32");
		dbo.put("uisscope","username email department ecard score lesson");
		TACOAuth2Model om=new TACOAuth2Model();
		System.out.println("cc");
		Object ret=om.lesson(dbo,"2012201301").get("list");
		System.out.println("dd");
		System.out.println(ret.toString());
		
		
		
	}
	
	@Test
	public void test2() {
		String from = "H",to="Z";
		Object ret = TACOAuth2Helper.schoolbus(from, to).get("list");
		System.out.println(ret.toString());
	}
}
