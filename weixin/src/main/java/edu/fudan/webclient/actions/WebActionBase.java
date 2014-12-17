/**
* @Title: WebActionBase.java
* @Description: TODO
* @author: Calvinyang
* @date: Oct 12, 2014 11:05:14 AM
* Copyright: Copyright (c) 2013
* @version: 1.0
*/
package edu.fudan.webclient.actions;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.alibaba.fastjson.JSONObject;
import com.opensymphony.xwork2.ActionSupport;

import edu.fudan.webclient.util.RetMsg;
import edu.fudan.webclient.util.RetMsg.ErrCode;

/**
 * @author: Calvinyang
 * @Description: TODO
 * @date: Oct 12, 2014 11:05:14 AM
 */
@SuppressWarnings("serial")
public class WebActionBase extends ActionSupport implements ServletRequestAware {
	public HttpServletRequest request;
	protected JSONObject ret;
	protected JSONObject data;
	
	/**
	 * 
	* @Title: isset
	* @Description: 判断是否设置某个字段
	* @param attrName
	* @return
	 */
	public boolean isset(String attrName) {
		return request.getParameterMap().containsKey(attrName);
	}
	
	/**
	 * 
	* @Title: getParam
	* @Description: 获取某个请求参数的值
	* @param attrName
	* @return
	 */
	protected String getParam(String attrName) {
		return request.getParameter(attrName);
	}
	
	/**
	 * 
	* @Title: response
	* @Description: ajax响应
	* @return
	* @throws Exception
	 */
	public String response() throws Exception {
		HttpServletResponse resp = ServletActionContext.getResponse();
		resp.setContentType("text/json;charset=UTF-8");
		ret.put("msg", RetMsg.getMsg(ret.getIntValue("r")));
		PrintWriter out = resp.getWriter();
		out.write(ret.toString());
		out.flush();
		out.close();
		return null;
	}
	
	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		request = arg0;
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (isAjax()) {
			ret = new JSONObject();
			data = new JSONObject();
			ret.put("data", data);
			ret.put("r", ErrCode.OK);
		}
	}
	
	/**
	 * 
	* @Title: isAjax
	* @Description: 判断是否ajax请求
	* @return
	 */
	public boolean isAjax() {
		String header = request.getHeader("X-Requested-With");
		return header != null && header.equals("XMLHttpRequest");
	}
	
	/**
	 * 
	* @Title: isGet
	* @Description: 判断是否get请求
	* @return
	 */
	protected boolean isGet() {
		return request.getMethod().equalsIgnoreCase("GET");
	}
}
