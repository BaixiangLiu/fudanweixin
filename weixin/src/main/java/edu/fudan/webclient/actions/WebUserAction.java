/**
 * @Title: WenUserAction.java
 * @Description: TODO
 * @author: Calvinyang
 * @date: Oct 12, 2014 11:07:07 AM
 * Copyright: Copyright (c) 2013
 * @version: 1.0
 */
package edu.fudan.webclient.actions;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import edu.fudan.eservice.common.utils.EncodeHelper;
import edu.fudan.webclient.entity.IMongoEntity;
import edu.fudan.webclient.entity.WebUserEntity;
import edu.fudan.webclient.service.MongoService;

/**
 * @author: Calvinyang
 * @Description: web端用户相关操作
 * @date: Oct 12, 2014 11:07:07 AM
 */
@ParentPackage(value = "servicebase")
@SuppressWarnings("serial")
@Result(name = "input", location = "weblogin.jsp")
public class WebUserAction extends WebActionBase {

	@Action(value = "webuserlogin", results = { @Result(name = SUCCESS, location = "webhome.jsp")})
	public String login() throws Exception {
		WebUserEntity q = new WebUserEntity();
		// 简单判断
		if (!isset("uname") || !isset("upwd")) {
			return INPUT;
		}
		q.setName(getParam("uname"));
		q.setPasswd(EncodeHelper.MD5(getParam("upwd").getBytes()));
		IMongoEntity u = MongoService.get(q);
		if (u == null) {
			request.setAttribute("loginTip", "用户名或密码错误");
		}
		else {
			((WebUserEntity) u).setPasswd(null);
			request.getSession().setAttribute("webuser", u);
		}
		return u == null ? INPUT : SUCCESS;
	}

	@Action(value = "webuserlogout")
	public String logout() throws Exception {
		if (request.getSession().getAttribute("webuser") != null) {
			request.getSession().setAttribute("webuser", null);
		}
		return INPUT;
	}
	
	@Action(value = "webuseradd", results = {@Result(name = SUCCESS, location = "webuseradd.jsp")})
	public String add() throws Exception {
		if (isGet()) {
			return SUCCESS;
		}
		WebUserEntity u = new WebUserEntity();
		u.setName(getParam("name"));
		u.setPasswd(EncodeHelper.MD5(getParam("passwd").getBytes()));
		u.setRole("default");
		MongoService.add(u);
		return null;
	}

}
