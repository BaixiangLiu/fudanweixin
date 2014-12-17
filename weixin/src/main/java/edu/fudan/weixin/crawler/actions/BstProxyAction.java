/**
 * @Title: BstProxyAction.java
 * @Description: TODO
 * @author: Calvinyang
 * @date: Nov 11, 2014 9:09:49 AM
 * Copyright: Copyright (c) 2013
 * @version: 1.0
 */
package edu.fudan.weixin.crawler.actions;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

/**
 * @author: Calvinyang
 * @Description: TODO
 * @date: Nov 11, 2014 9:09:49 AM
 */
@ParentPackage("servicebase")
@Namespace("/crawler")
@SuppressWarnings("serial")
public class BstProxyAction extends CrawlerBase {
	private InputStream inputStream;
	private String contentType;

	private static final String BaseUrl = "http://localhost:8080/fdweixin/crawler/bst.act?targetUrl=";
	private static final String SearchUrl = "http://localhost:8080/fdweixin/crawler/search.act";

	@Override
	@Action(value = "bst", results = { @Result(type = "stream") })
	public String execute() throws Exception {
		String targetUrl = ServletActionContext.getRequest().getParameter("targetUrl");
		if (targetUrl == null) {
			targetUrl = "http://baishitong.fudan.edu.cn/index.php?title=%E9%A6%96%E9%A1%B5";
		} else {
			String s = new String(targetUrl.getBytes("ISO-8859-1"),"UTF-8");
			targetUrl = "http://baishitong.fudan.edu.cn/index.php?title=" + s;
		}
		targetUrl += "&mobileaction=toggle_view_mobile";
		StringBuffer retstr = fetch(targetUrl);
		// 内链处理
		String html = retstr.toString();
		String html2 = retstr.toString();
		Pattern p = Pattern.compile("(?<=href=\").*?(?=\")");
		Matcher m = p.matcher(html);
		while (m.find()) {
			String link = m.group();
			if (link.startsWith("/wiki")) {
				html2 = html2.replace(link, BaseUrl + URLEncoder.encode(link.substring(6), "UTF-8"));
			}
		}
		p = Pattern.compile("(?<=action=\").*?(?=\")");
		m = p.matcher(html);
		while(m.find()) {
			String link = m.group();
			if (link.equals("/index.php")) {
				html2 = html2.replace(link, SearchUrl);
			}
		}
		inputStream = new ByteArrayInputStream(html2.getBytes("UTF-8"));
		return SUCCESS;
	}

	@Action(value = "search", results = { @Result(type = "stream") })
	public String search() throws Exception {
		String query = ServletActionContext.getRequest().getParameter("search");
		query = new String(query.getBytes("ISO-8859-1"),"UTF-8");
		String targetUrl = "http://baishitong.fudan.edu.cn/index.php?title=%E7%89%B9%E6%AE%8A:%E6%90%9C%E7%B4%A2&search=" + query + "&fulltext=search&mobileaction=toggle_view_mobile";
		StringBuffer retstr = fetch(targetUrl);
		// 内链处理
		String html = retstr.toString();
		String html2 = retstr.toString();
		Pattern p = Pattern.compile("(?<=href=\").*?(?=\")");
		Matcher m = p.matcher(html);
		while (m.find()) {
			String link = m.group();
			if (link.startsWith("/wiki")) {
				html2 = html2.replace(link, BaseUrl + URLEncoder.encode(link.substring(6), "UTF-8"));
			}
		}
		inputStream = new ByteArrayInputStream(html2.getBytes("UTF-8"));
		return SUCCESS;
	}
	
	/**
	 * @return the inputStream
	 */
	public InputStream getInputStream() {
		return inputStream;
	}

	/**
	 * @param inputStream
	 *            the inputStream to set
	 */
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return "text/html";
	}

	/**
	 * @param contentType
	 *            the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
}
