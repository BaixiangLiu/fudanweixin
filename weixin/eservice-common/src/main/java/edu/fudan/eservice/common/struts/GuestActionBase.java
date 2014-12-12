package edu.fudan.eservice.common.struts;
import org.apache.struts2.interceptor.ApplicationAware;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
@SuppressWarnings("serial")
public class GuestActionBase extends ActionSupport implements ApplicationAware,SessionAware{

	public static final String PRIVILEGE="privilege";
	

	protected Map<String, Object> application;
	
	private String token=null;

	protected Map<String, Object> session=new HashMap<String, Object>();

	public Map<String, Object> getSession() {
		return this.session;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	public Map<String, Object> getApplication() {
		return application;
	}

	public void setApplication(Map<String, Object> application) {
		this.application = application;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	/**
	 * 防止浏览器cache
	 */
	protected void setNocache()
	{
		HttpServletResponse response=org.apache.struts2.ServletActionContext.getResponse();
		   response.setHeader( "Pragma", "no-cache" );
		   response.setHeader( "Cache-Control", "no-cache" );
		   response.setDateHeader( "Expires", 0 );
	}
}
