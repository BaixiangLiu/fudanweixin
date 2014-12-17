package edu.fudan.weixin.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import edu.fudan.weixin.model.ProcessWeixinMessage;
import edu.fudan.weixin.utils.WeixinMessageHelper;

public class WeixinMessageServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5545865360217241428L;
	private Log log=LogFactory.getLog(WeixinMessageServlet.class);
	/**
	 * 接口验证
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	{
		String signature=request.getParameter("signature");
		String timestamp=request.getParameter("timestamp");
		String nonce=request.getParameter("nonce");
		String echostr=request.getParameter("echostr");
		try {
		if(WeixinMessageHelper.checksum(signature, timestamp, nonce))
		{
			
				response.getWriter().print(echostr);			
	
		}
		} catch (IOException e) {
			log.error(e);
		}
	}
	/**
	 * 消息处理
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	{		
	
			
				ProcessWeixinMessage.process(request, response);
			
	
	
	}
}
