package edu.fudan.eservice.common.smtp;

import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.List;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import java.util.*;

import org.apache.commons.logging.*;

public abstract class SMTP {

	private static Log log = LogFactory.getLog(SMTP.class);

	protected String username;

	protected String password;

	protected String host;

	protected String to;

	protected String cc;

	protected String bcc;

	protected String reply;

	protected Address from;

	protected String title;

	private MimeMultipart top = null;

	//private MimeMultipart related = null;

	/**
	 * 获取SMTP连接的会话，由子类实现
	 */
	protected abstract Session getSession();
	
	/**
	 * 设置登录服务器的用户名
	 * @param loginUser
	 */
	public void setLoginUser(String loginUser) {
		username = loginUser;
	}
	/**
	 * 设置登录服务器的密码
	 * @param password
	 */
	public void setLoginPassword(String password) {
		this.password = password;
	}
	/**
	 * 添加收件人地址，不同地址间以,分隔
	 * @param addresses
	 */
	public void addTo(String addresses) {
		if (to == null)
			to = addresses;
		else
			to += "," + addresses;
	}
	/**
	 * 添加收件人地址，List中的每个地址也可以为,分隔的多个收件人
	 * @param address
	 */
	public void addTo(List<String> address) {
		for (String add : address) {
			if (to == null)
				to = add;
			else
				to += "," + add;
		}
	}
	/**
	 * 添加抄送人地址，要求同收件人
	 * @param addresses
	 */
	public void addCc(String addresses) {
		if (cc == null)
			cc = addresses;
		else
			cc += "," + addresses;
	}
	/**
	 * 添加抄送人地址，要求同收收件人
	 * @param address
	 */
	public void addCc(List<String> address) {
		for (String add : address) {
			if (cc == null)
				cc = add;
			else
				cc += "," + add;
		}
	}
	/**
	 * 添加暗送人地址，要求同收件人
	 * @param addresses
	 */
	public void addBcc(String addresses) {
		if (bcc == null)
			bcc = addresses;
		else
			bcc += "," + addresses;
	}
	/**
	 * 添加暗送人地址，要求同收件人
	 * @param address
	 */
	public void addBcc(List<String> address) {
		for (String add : address) {
			if (bcc == null)
				bcc = add;
			else
				bcc += "," + add;
		}
	}
	/**
	 * 设置邮件标题
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * 设置发信人
	 * @param address
	 * @throws AddressException
	 */
	public void setFrom(String address) throws AddressException {
		try {
			from = new InternetAddress(address);
		} catch (AddressException ex) {
			log.error("[setFrom]:" + address + "\n" + ex.getMessage());
			throw ex;
		}
	}
	/**
	 * 设置发信人
	 * @param address
	 */
	public void setFrom(Address address)
	{
		from=address;
	}
	/**
	 * 设置SMTP服务器地址
	 * @param host
	 */
	public void setHost(String host) {
		this.host = host;
	}
	/**
	 * 设置回复地址
	 * @param reply
	 */
	public void setReply(String reply) {
		this.reply = reply;
	}
	/**
	 * 设置邮件内容
	 * @param body
	 * @param isHTML 内容是否为HTML格式
	 * @throws MessagingException
	 */
	public void setMailbody(String body, boolean isHTML)
			throws MessagingException {

		String conText = null;
		BodyPart bp = new MimeBodyPart();
		if (top == null)
			top = new MimeMultipart();
		try {
			if (isHTML) {
				conText = "<HTML><HEAD><TITLE>" + title + "</TITLE><BODY>"
						+ body + "</BODY></HTML>";
				bp.setContent(conText, "text/html; charset=gbk");
			} else {
				bp.setContent(body, "text/plain; charset=gbk");

			}
			top.addBodyPart(bp);
		} catch (MessagingException ex) {
			log.error("[setMailBody]:" + body + "\nHTML:" + isHTML + "\n"
					+ ex.getMessage());
			throw ex;
		}

	}
	/**
	 * 添加附件
	 * @param filename 附件路径，可为URL或文件系统路径
	 * @throws MessagingException
	 * @throws MalformedURLException
	 */
	public void addAttachment(String filename)
			throws MessagingException, MalformedURLException {
		if (top == null)
			top = new MimeMultipart();
		else
		{
			String subtype=top.getContentType();
			if(subtype.equals("multipart/related"));
			{
				BodyPart old=new MimeBodyPart();
				old.setContent(top);
				top=new MimeMultipart();
				top.addBodyPart(old);
			}
		}
		BodyPart bp = new MimeBodyPart();
		try {
			if (filename.indexOf("://")>0) {
				URLDataSource uds = new URLDataSource(new URL(filename));
				bp.setDataHandler(new DataHandler(uds));
				bp.setFileName(MimeUtility.encodeWord(uds.getName(), "GBK",
						null));
			} else {
				FileDataSource fds = new FileDataSource(filename);
				bp.setDataHandler(new DataHandler(fds));
				bp.setFileName(MimeUtility.encodeWord(fds.getName(), "GBK",
						null));
			}
			top.addBodyPart(bp);
		} catch (MessagingException ex) {
			log.error("[addAttachment]:" + filename + "\n"
					+ ex.getMessage());
			throw ex;
		} catch (UnsupportedEncodingException ex) {
			log.error("[addAttachment]:" + ex.getMessage());
		} catch (MalformedURLException ex) {
			log.error("[addAttachment URL]:" + filename + "\n"
					+ ex.getMessage());
			throw ex;
		}
	}
	/**
	 * 添加相关附件，一般为图片
	 * 当filename为URL时，HTML中<img>的src写为此URL
	 * 当filename为文件路径是，<img>的src写为"cid:文件名"，注意此处文件名不要有中文，不然必乱
	 * @param filename 相关附件地址(URL或文件路径)
	 * @throws MessagingException
	 * @throws MalformedURLException 
	 */
	public void addRelated(String filename) throws MessagingException, MalformedURLException {
		if (top == null)
			top = new MimeMultipart("related");
		else
			top.setSubType("related");
		BodyPart bp = new MimeBodyPart();
		try {
			
			if (filename.indexOf("://")>0) {
				URLDataSource uds = new URLDataSource(new URL(filename));
				bp.setDataHandler(new DataHandler(uds));
				bp.setFileName(MimeUtility.encodeWord(uds.getName(), "GBK",
						null));
				bp.setHeader("Content-Location", MimeUtility.encodeWord(filename, "GBK", null));
			} else {
				FileDataSource fds = new FileDataSource(filename);
				bp.setDataHandler(new DataHandler(fds));
				bp.setFileName(MimeUtility.encodeWord(fds.getName(), "GBK",
						null));
				bp.setHeader("Content-ID", "<" + MimeUtility
						.encodeWord(fds.getName(), "GBK", null) + ">");
			}
			
			
			
			top.addBodyPart(bp);
		} catch (MessagingException ex) {
			log.error("[addRelated]:" + filename + "\n" + ex.getMessage());
			throw ex;
		} catch (UnsupportedEncodingException ex) {
			log.error("[addRelated]:" + ex.getMessage());
		}catch (MalformedURLException ex) {
			log.error("[addAttachment URL]:" + filename + "\n"
					+ ex.getMessage());
			throw ex;
		}

	}
	/**
	 * 发送邮件
	 * @throws MessagingException
	 */
	public void send() throws MessagingException {

		Session s = getSession();
		//s.setDebug(true);
		try {
			Transport trans = s.getTransport("smtp");
			trans.connect(host, username, password);

			MimeMessage msg = new MimeMessage(s);

			msg.setFrom(from);
			msg.setSubject(title,"GBK");
			msg.setRecipients(Message.RecipientType.TO, InternetAddress
					.parse(to));
			if (cc != null)
				msg.setRecipients(Message.RecipientType.CC, InternetAddress
						.parse(cc));
			if (bcc != null)
				msg.setRecipients(Message.RecipientType.BCC, InternetAddress
						.parse(bcc));
			if (reply != null)
				msg.setReplyTo(InternetAddress.parse(reply));

			
			msg.setContent(top);	

			msg.setHeader("X-Mailer", "ESERVICE");
			msg.setSentDate(new Date());
			trans.sendMessage(msg, msg.getAllRecipients());
			trans.close();
			
		} catch (MessagingException ex) {
			if (ex instanceof AddressException)
				log.error("[send]: AddressError"
						+ ((AddressException) ex).getRef());
			log.error("[send]:" + ex.toString());
			throw ex;
		}

	}

}
