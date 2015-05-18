package edu.fudan.eservice.common.struts;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

import edu.fudan.eservice.common.utils.CommonUtil;

@SuppressWarnings("serial")
public class LogInterceptor implements Interceptor {

	private static Log log = LogFactory.getLog(LogInterceptor.class);

	private int outing = 3;
	private String excludes=null;

	public void destroy() {
	}

	public void init() {
	}
	@SuppressWarnings("rawtypes")
	public String intercept(ActionInvocation actionInvocation) throws Exception {
		
		ActionContext ac = actionInvocation.getInvocationContext();
		String reqstr=actionInvocation.getProxy().getNamespace()+"/"+actionInvocation.getProxy().getActionName();
		//log.info(reqstr);
		if(!CommonUtil.isEmpty(excludes)) 
		{
			String[] excs=excludes.split(",");
			for(String exs:excs)
			{
				if(!CommonUtil.isEmpty(exs)&&reqstr.indexOf(exs)>=0)
					return actionInvocation.invoke();
			}
			
		}
		
		
		
		Map e = ac.getParameters();

		StringBuffer params = new StringBuffer();
		Iterator i = e.keySet().iterator();
		for (; i.hasNext();) {
			Object o = i.next();
			String param = o.toString();
			if (param.indexOf("dojo.") < 0) {
				params = params.append(param + ":");
				Object[] ps = (Object[]) e.get(param);
				for (Object oi : ps) {
					params.append(oi + ",");
				}
				params.append(";");
			}
		}
		HttpServletRequest request = org.apache.struts2.ServletActionContext.getRequest();
		String url = request.getRequestURI();
		String remoteip = CommonUtil.getRemoteip();

		String userid = (String) request.getSession().getAttribute("user");

		String res = actionInvocation.invoke();

		/*
		 * if((outing&2)==2) { DAO dao=null; try{ dao=DAOFactory.getDAO();
		 * GenericLog gl=new GenericLog();
		 * gl.setParam(params.toString()+"return:"+res);
		 * gl.setRemoteip(remoteip); gl.setUrl(url.toString());
		 * gl.setWorkid(userid); dao.save(gl); dao.commitTransaction();
		 * }catch(Exception ex) { if(dao!=null) dao.rollbackTransaction();
		 * //ex.printStackTrace(); log.error(ex); } }
		 */
		if ((outing & 1) == 1) {
			log.info(new Date() + "\t" + url + "\t" + res + "\t" + remoteip + "\t" + userid + "\n" + params);

		}

		return res;
	}

	public int getOuting() {
		return outing;
	}

	public void setOuting(int outing) {
		this.outing = outing;
	}

	public String getExcludes() {
		return excludes;
	}

	public void setExcludes(String excludes) {
		this.excludes = excludes;
	}
	
	

}
