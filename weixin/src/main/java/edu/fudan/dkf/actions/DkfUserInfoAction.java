/**
* @Title: CustomInfoAction.java
* @Description: 多客服接入用户相关信息
* @author: Calvinyang
* @date: Oct 19, 2014 3:18:53 PM
* Copyright: Copyright (c) 2013
* @version: 1.0
*/
package edu.fudan.dkf.actions;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import edu.fudan.eservice.common.struts.GuestActionBase;
import edu.fudan.webclient.entity.BindingEntity;
import edu.fudan.webclient.entity.IMongoEntity;
import edu.fudan.webclient.service.MongoService;

/**
 * @author: Calvinyang
 * @Description: 多客服接入用户相关信息
 * @date: Oct 19, 2014 3:18:53 PM
 */
@SuppressWarnings("serial")
public class DkfUserInfoAction extends GuestActionBase {
	private String openid;
	private IMongoEntity entity;
	
	@Action(value = "dkfuserinfo", results = {@Result(name = SUCCESS, location = "dkfuserinfo.jsp")})
	public String info() throws Exception {
		if (openid!= null) {
			BindingEntity	 en = new BindingEntity();
			en.setOpenid(openid);
			entity=MongoService.get(en);
		}
		return SUCCESS;
	}

	public IMongoEntity getEntity() {
		return entity;
	}


	public void setOpenid(String openid) {
		this.openid = openid;
	}
	
}
