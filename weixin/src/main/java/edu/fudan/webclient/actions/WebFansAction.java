/**
* @Title: WebFansAction.java
* @Description: TODO
* @author: Calvinyang
* @date: Oct 12, 2014 5:19:59 PM
* Copyright: Copyright (c) 2013
* @version: 1.0
*/
package edu.fudan.webclient.actions;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import edu.fudan.webclient.entity.IMongoEntity;
import edu.fudan.webclient.entity.WeixinUserEntity;
import edu.fudan.webclient.service.MongoService;

/**
 * @author: Calvinyang
 * @Description: 粉丝管理
 * @date: Oct 12, 2014 5:19:59 PM
 */
@ParentPackage(value = "servicebase")
@SuppressWarnings("serial")
public class WebFansAction extends WebActionBase {
	
	@Action(value = "webfanslist", results = {@Result(name = SUCCESS, location = "webfollowers.jsp")})
	public String list() throws Exception {
		List<IMongoEntity> list = MongoService.getList(new WeixinUserEntity(), null, 0, 40);
		request.setAttribute("list", list);
		return SUCCESS;
	}
	
}
