package edu.fudan.eservice.common.struts;


import java.util.Map;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

import edu.fudan.eservice.common.utils.CommonUtil;





@SuppressWarnings("rawtypes")
public class UISInterceptor implements Interceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2742868399909001739L;	
	public void destroy() {
		
		
	}

	public void init() {
		
		
	}

	public String intercept(ActionInvocation arg0) throws Exception {
		// 如果session中没有user对象，通过UIS进行认证
		Map session=arg0.getInvocationContext().getSession();
		String user=(String)session.get("user");
		
		if (user == null)
			ServletActionContext.getResponse().sendRedirect(CommonUtil.getLoginURL());
		return arg0.invoke();
	}	
	
	
}
