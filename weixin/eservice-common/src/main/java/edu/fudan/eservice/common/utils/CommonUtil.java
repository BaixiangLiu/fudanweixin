package edu.fudan.eservice.common.utils;


import java.io.BufferedReader;
import java.io.DataOutputStream;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import java.net.URL;


import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;




public class CommonUtil {

	public static String getRemoteip()
	{
		HttpServletRequest request=ServletActionContext.getRequest();
		String rip=request.getHeader("x-forwarded-for");
		if(rip==null || rip.trim().equals(""))
			rip=request.getRemoteAddr();
		else
			rip+=request.getRemoteAddr();
		return rip;
	}
	
	public static String getLoginURL()
	{
		String context=ServletActionContext.getServletContext().getContextPath();		
		String uisUrl=context+(context.endsWith("/")?"":"/")+"login.act";		
		uisUrl+="?goto="+ServletActionContext.getRequest().getRequestURL();
		return uisUrl;
	}
	
	public static boolean isEmpty(Object obj)
	{
		if(obj==null)
			return true;
		if(obj instanceof String)
		{
			String s=(String) obj;
			if("".equals(s.trim()))
				return true;
		}
		return false;
	}
	
	public static boolean eq(Object o1,Object o2)
	{
		return o1==null && o2==null || o1!=null &&o1.equals(o2);
	}
	
	public static StringBuffer getWebContent(String urlstr) throws Exception
	{
		HttpURLConnection  url =(HttpURLConnection )new URL(urlstr).openConnection();
		url.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.124 Safari/537.36");
		BufferedReader reader =null;
		if(url.getResponseCode()>=400)
			reader=new BufferedReader(new InputStreamReader(url.getErrorStream(),"utf-8"));
		else reader=
		new BufferedReader(
				new InputStreamReader(url.getInputStream(),"utf-8"));
		String s;
		StringBuffer ret=new StringBuffer();
		while ((s = reader.readLine()) != null) {
			ret.append(s);
		}
		return ret;
		
	}
	
	public static StringBuffer postWebRequest(String urlstr,byte[] content,String content_type) throws Exception
	{
			HttpURLConnection  url =(HttpURLConnection )new URL(urlstr).openConnection();
			url.setRequestMethod("POST");
			url.setDoOutput(true);
			if(!isEmpty(content_type))
			{
				url.setRequestProperty("Content-Type", content_type);
			}
			DataOutputStream out = new DataOutputStream(
	                url.getOutputStream());
				out.write(content);
				out.close();
			BufferedReader reader =null;
			if(url.getResponseCode()>=400)
				reader=new BufferedReader(new InputStreamReader(url.getErrorStream(),"utf-8"));
			else reader=
			new BufferedReader(
					new InputStreamReader(url.getInputStream(),"utf-8"));
			String s;
			StringBuffer ret=new StringBuffer();
			while ((s = reader.readLine()) != null) {
				ret.append(s);
			}
			return ret;
			
	
	}
}
