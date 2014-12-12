package edu.fudan.eservice.common.actions;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import edu.fudan.eservice.common.struts.GuestActionBase;
import edu.fudan.eservice.common.utils.CommonUtil;

@Results({@Result(name="success",type="httpheader")})
public class LogoutAction extends GuestActionBase {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5029652016647856131L;

	public String execute()
	{
		getSession().clear();
		try {
			HttpServletRequest req=ServletActionContext.getRequest();
			HttpServletResponse response=org.apache.struts2.ServletActionContext.getResponse();
			for (Cookie c:req.getCookies())
			{
				if(!CommonUtil.isEmpty(c)&&"iPlanetDirectoryPro".equalsIgnoreCase(c.getName()))
				{
					Cookie cookie=new Cookie("iPlanetDirectoryPro","LOGOUT");
					cookie.setDomain(".fudan.edu.cn");
					cookie.setPath("/");
					response.addCookie(cookie);
				}
			}
			response.sendRedirect("index.act");
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return NONE;
	}

}
