package edu.fudan.eservice.common.smtp;

import java.util.Properties;

import javax.mail.Session;
import javax.mail.internet.*;


import edu.fudan.eservice.common.utils.Config;

public class ServiceMail extends SMTP {

	Properties prop=System.getProperties();
	private void initUser(String username,String password,String displayname,String mailhost)
	{
		setHost(mailhost);
		prop.put("mail.smtp.host", host);
		setLoginUser(username);
		prop.put("mail.smtp.user", this.username);
		setLoginPassword(password);
		prop.put("mail.smtp.password", this.password);
		prop.put("mail.smtp.auth", "true");
		try {
			setFrom(new InternetAddress(username+"@fudan.edu.cn",displayname,"gb2312"));
			setReply("noreply@fudan.edu.cn");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public ServiceMail()
	{
		Config conf=Config.getInstance();
		initUser(conf.get("mail.user"),conf.get("mail.pass"),conf.get("mail.display"),conf.get("mail.host"));
	}
	
	public ServiceMail(String username,String password,String displayname,String mailhost)
	{
		initUser(username,password,displayname,mailhost);
	}
	@Override
	protected Session getSession() {
		return Session.getInstance(prop,null);
	}

}
