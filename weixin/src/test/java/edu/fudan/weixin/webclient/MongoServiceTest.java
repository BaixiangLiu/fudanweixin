/**
* @Title: MongoServiceTest.java
* @Description: TODO
* @author: Calvinyang
* @date: Oct 12, 2014 1:38:18 PM
* Copyright: Copyright (c) 2013
* @version: 1.0
*/
package edu.fudan.weixin.webclient;

import java.util.List;

import org.junit.Test;

import edu.fudan.eservice.common.utils.EncodeHelper;
import edu.fudan.webclient.entity.IMongoEntity;
import edu.fudan.webclient.entity.WebUserEntity;
import edu.fudan.webclient.entity.WeixinUserEntity;
import edu.fudan.webclient.service.MongoService;

/**
 * @author: Calvinyang
 * @Description: TODO
 * @date: Oct 12, 2014 1:38:18 PM
 */
public class MongoServiceTest {
	
	@Test
	public void testReflect() {
		WebUserEntity entity = new WebUserEntity();
		entity.setName("calvin");
		entity.setPasswd(EncodeHelper.MD5("098765".getBytes()));
		entity.setRole("admin");
		System.out.println(entity.toDBObject());
	}
	
	@Test
	public void testAdd() {
		WebUserEntity entity = new WebUserEntity();
		entity.setName("calvin");
		entity.setPasswd(EncodeHelper.MD5("098765".getBytes()));
		entity.setRole("admin");
		MongoService.add(entity);
	}
	
	@Test
	public void testDelete() {
		WebUserEntity entity = new WebUserEntity();
		entity.setName("name");
		MongoService.delete(entity);
	}
	
	@Test
	public void testCount() {
		System.out.println(MongoService.getCount("WebUser"));
	}
	
	@Test
	public void testCount2() {
		WebUserEntity entity = new WebUserEntity();
		entity.setName("name");
		System.out.println(MongoService.getCount(entity));
	}
	
	@Test
	public void testGet() {
		IMongoEntity entity = new WeixinUserEntity();
		print(MongoService.getList(entity, null, 0, 40));
	}

	/**
	* @Title: print
	* @param list
	*/
	private void print(List<IMongoEntity> list) {
		for(IMongoEntity entity : list) {
			System.out.println(entity.toDBObject());
		}
	}
	
	@Test
	public void testUpdate() {
		WebUserEntity entity = new WebUserEntity();
		entity.setName("name3");
		entity.setPasswd("pwd");
		WebUserEntity entity2 = new WebUserEntity();
		entity2.setName("name2");
		System.out.println(MongoService.getCount(entity2));
		MongoService.update(entity2, entity);
	}
}
