package edu.fudan.weixin;

import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import edu.fudan.eservice.common.utils.MongoUtil;
import edu.fudan.weixin.model.TACOAuth2Model;
import edu.fudan.weixin.utils.TACOAuth2Helper;

public class TestOAuth2Model {
	
	
	private DBObject getBD()
	{
		return MongoUtil.getInstance().getDB().getCollection("Bindings").findOne(new BasicDBObject("binds",new BasicDBObject("$elemMatch",new BasicDBObject("uisid","09110240017"))));
	}
	
	//@Test
	public void testrevoke()
	{
		DBObject bd=getBD();
		TACOAuth2Model om=new TACOAuth2Model();
		System.out.println(om.revokeToken(bd));	
	}
	@Test
	public void testscore(){
		DBObject bd=getBD();
		System.out.println(bd.get("openid"));
		TACOAuth2Model om=new TACOAuth2Model();
		System.out.println(om.score(bd,"2009201002"));	
	}
	
	@Test
	public void testlesson(){
		DBObject bd=getBD();
		TACOAuth2Model om=new TACOAuth2Model();
		System.out.println(om.lesson(bd,"2009201001"));	
	}
	
	@Test
	public void testschoolbus() {
		System.out.println(TACOAuth2Helper.schoolbus("Z", "J"));
	}
	
	
	@Test
	public void testyktxf()
	{
		DBObject bd=getBD();
		TACOAuth2Model om=new TACOAuth2Model();
		System.out.println(om.yktxf(bd, "", ""));
	}
	
	
	
}
