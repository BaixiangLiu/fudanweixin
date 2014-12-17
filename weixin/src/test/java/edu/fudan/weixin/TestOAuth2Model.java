package edu.fudan.weixin;

import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import edu.fudan.eservice.common.utils.MongoUtil;
import edu.fudan.weixin.model.TACOAuth2Model;
import edu.fudan.weixin.utils.TACOAuth2Helper;

public class TestOAuth2Model {
	
	
	//@Test
	public void testrevoke()
	{
		DBObject bd=MongoUtil.getInstance().getDB().getCollection("Bindings").findOne(new BasicDBObject("uisid","09110240017"));
		TACOAuth2Model om=new TACOAuth2Model();
		System.out.println(om.revokeToken(bd));	
	}
	//@Test
	public void testscore(){
		DBObject bd=MongoUtil.getInstance().getDB().getCollection("Bindings").findOne(new BasicDBObject("uisid","09110240017"));
		TACOAuth2Model om=new TACOAuth2Model();
		System.out.println(om.score(bd,"2009201002"));	
	}
	
	@Test
	public void testlesson(){
		DBObject bd=MongoUtil.getInstance().getDB().getCollection("Bindings").findOne(new BasicDBObject("uisid","09110240017"));
		TACOAuth2Model om=new TACOAuth2Model();
		System.out.println(om.lesson(bd,"2009201001"));	
	}
	
	@Test
	public void testschoolbus() {
		System.out.println(TACOAuth2Helper.schoolbus("Z", "J"));
	}
	
	
	
	public void testyktxf()
	{
		DBObject bd=MongoUtil.getInstance().getDB().getCollection("Bindings").findOne(new BasicDBObject("uisid","12210240096"));
		TACOAuth2Model om=new TACOAuth2Model();
		System.out.println(om.yktxf(bd, "", ""));
	}
	
	
	
	public void test()
	{
		DBObject bd=MongoUtil.getInstance().getDB().getCollection("Bindings").findOne(new BasicDBObject("uisid","04538"));
		TACOAuth2Model om=new TACOAuth2Model();
		System.out.println(om.fetchUserinfo(bd));
		DBObject tm=om.getToken(bd.get("uisrefresh").toString(), true);
		System.out.println(tm);
		bd.put("uisrefresh", tm.get("refresh_token"));
		bd.put("uisexpired", System.currentTimeMillis()-100);
		System.out.println(om.fetchUserinfo(bd));
	}
}
