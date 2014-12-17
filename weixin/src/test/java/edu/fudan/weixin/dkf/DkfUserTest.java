/**
* @Title: DkfUserTest.java
* @Description: TODO
* @author: Calvinyang
* @date: Oct 19, 2014 4:23:16 PM
* Copyright: Copyright (c) 2013
* @version: 1.0
*/
package edu.fudan.weixin.dkf;

import org.junit.Test;

import edu.fudan.webclient.entity.BindingEntity;
import edu.fudan.webclient.entity.IMongoEntity;
import edu.fudan.webclient.service.MongoService;

/**
 * @author: Calvinyang
 * @Description: TODO
 * @date: Oct 19, 2014 4:23:16 PM
 */
public class DkfUserTest {
	
	@Test
	public void query() {
		BindingEntity entity = new BindingEntity();
		entity.setOpenid("oew-cuJZjLLapCozm0pyicCfLT58");
		IMongoEntity e = MongoService.get(entity);
		System.out.println(e);
	}
	
}
