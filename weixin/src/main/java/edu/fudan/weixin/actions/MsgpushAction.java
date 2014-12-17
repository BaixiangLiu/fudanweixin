package edu.fudan.weixin.actions;

import java.io.BufferedReader;
import java.io.IOException;
import java.security.GeneralSecurityException;
import javax.servlet.http.HttpServletResponse;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import edu.fudan.eservice.common.struts.GuestActionBase;
import edu.fudan.eservice.common.utils.CommonUtil;
import edu.fudan.eservice.common.utils.Config;
import edu.fudan.eservice.common.utils.EncodeHelper;
import edu.fudan.eservice.common.utils.MongoUtil;

@ParentPackage("servicebase")
@Namespace("/")

public class MsgpushAction extends GuestActionBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -87130337020325361L;
	private String clientid;
	private String userenc;
	private long timestamp;
	private int msgid;
	private String errormsg;
	
	private Log log=LogFactory.getLog(MsgpushAction.class);
	@Action("msgpush")
	public String msgpush() throws Exception
	{
		BufferedReader r=ServletActionContext.getRequest().getReader();
		StringBuffer ret = null;
		StringBuffer sb=new StringBuffer();
		String s;
		while((s=r.readLine())!=null)
		{
			sb.append(s);
		}
		DBObject req=(DBObject)JSON.parse(sb.toString());
		DBObject head=(DBObject)req.get("head");
		timestamp=(long)head.get("timestamp");
		clientid=String.valueOf(head.get("clientid"));
		userenc=String.valueOf(head.get("userenc"));
		String touser=String.valueOf(head.get("touser"));
		DB db=MongoUtil.getInstance().getDB();
		if(checkTime(timestamp))
		{
			if(checkenc(db,timestamp,clientid,userenc))
			{
				if(checkmsgsum(req.get("data"),touser,userenc,String.valueOf(head.get("checksum"))))
				{
					Cache cache=CacheManager.getInstance().getCache("MsgCheck");
					if(cache.get(head.get("checksum"))==null)
					{
						cache.put(new Element(head.get("checksum"),null));
						DBObject user=db.getCollection("Bindings").findOne(new BasicDBObject("binds",new BasicDBObject("$elemMatch",new BasicDBObject("uisid",touser))));
						if(!CommonUtil.isEmpty(user)&&!CommonUtil.isEmpty(user.get("openid")))
						{
							if(db.getCollection("Books").findOne(new BasicDBObject("openid",user.get("openid")).append("item", head.get("template")).append("book"	, true))!=null)
							{
							DBObject msg=db.getCollection("Templates").findOne(new BasicDBObject("name",head.get("template")));
							if(!CommonUtil.isEmpty(msg))
							{
								msg.removeField("name");
								msg.removeField("_id");
								msg.put("touser", user.get("openid"));
								DBObject data=(DBObject)req.get("data");
								if(!CommonUtil.isEmpty(data.get("url")))
									msg.put("url", data.get("url"));
								DBObject msgdata=(DBObject)(msg.get("data"));
								for(String k:msgdata.keySet())
								{
									Object v=data.get(k);
									if(!CommonUtil.isEmpty(v))
									((DBObject)(msgdata.get(k))).put("value",  v);
								}	
								log.info(JSON.serialize(msg));
								 ret=CommonUtil.postWebRequest("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+Config.getInstance().get("weixin.access_token"), JSON.serialize(msg).getBytes("utf-8"), null);
								 DBObject retobj=(DBObject)JSON.parse(ret.toString());
								 retobj.put("touser", touser);
								 retobj.put("timestamp", timestamp);
								 retobj.put("clientid", clientid);
								 db.getCollection("Pushmsgs").save(retobj);
							}else
							{
								errormsg="Template not found";
							}}else
							{
								errormsg="Message not booked";
							}
						}else
						{
							errormsg="Touser not binded";
						}
						
					}else
					{
						errormsg="Same message is sent too frequently";
					}
				}else
				{
					errormsg="Message checksum error";
				}
				
			}else
			{
				errormsg="User not authorized";
			}
		}else
			errormsg="Timestamp outof range";
		
		
		HttpServletResponse resp=ServletActionContext.getResponse();
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("application/json");
		if(!CommonUtil.isEmpty(ret))
			resp.getWriter().print(ret.toString());
		else
			resp.getWriter().write("{\"errcode\":50000,\"errmsg\":\""+errormsg+"\"}");
		
		return NONE;
	}
	
	
	@Action("pushresult")
	public String pushresult() throws IOException
	{
		DB db=MongoUtil.getInstance().getDB();
		BasicDBObject ret=null;
		if(checkTime(timestamp))
		{
			if(checkenc(db,timestamp,clientid,userenc))
			{
				DBObject dbo=db.getCollection("Pushmsgs").findOne(new BasicDBObject("msgid",msgid));
				if(!CommonUtil.isEmpty(dbo))
				{
					ret=new BasicDBObject().append("msgid", dbo.get("msgid")).append("status", dbo.get("status"));
				}else
				{
					errormsg="Message not found";
				}
			}else
			{
				errormsg="User not authorized";
			}
		}else
			errormsg="Timestamp outof range";
		
		
		HttpServletResponse resp=ServletActionContext.getResponse();
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("application/json");
		if(!CommonUtil.isEmpty(ret))
			resp.getWriter().print(JSON.serialize(ret));
		else
			resp.getWriter().write("{\"errcode\":50000,\"errmsg\":\""+errormsg+"\"}");
		
		return NONE;
	}
	
	
	
	private boolean checkTime(long stamp)
	{
		long diff=System.currentTimeMillis()-stamp;
		if(diff>60000||diff<-60000)
			return false;
		else
			return true;
	}
	
	private DBObject getClientInfo(DB db,String clientid)
	{
		return db.getCollection("Clients").findOne(new BasicDBObject("clientid",clientid));
	}
	
	private boolean checkenc(DB db,long stamp,String clientid,String userenc)
	{
		DBObject clientinfo=getClientInfo(db,clientid);
		if(clientinfo==null) return false;
		String enckey=String.valueOf(clientinfo.get("enckey"));
		try {
			String userdec=new String(EncodeHelper.dencrypt("DESede", EncodeHelper.hex2bytes(userenc), EncodeHelper.hex2bytes(enckey), null));
			if(CommonUtil.eq(userdec, String.valueOf(clientinfo.get("password"))+stamp))
				return true;
			else
				return false;
		} catch (GeneralSecurityException e) {
			log.error(e);
			errormsg=e.getMessage();
			return false;
		}
	}
	
	private boolean checkmsgsum(Object data,String touser,String userenc,String checksum)
	{
		String sdata=JSON.serialize(data);
		sdata=sdata.replaceAll("[ \\n\\r\\t]", "");
		log.info(sdata);
		if(CommonUtil.eq(checksum, EncodeHelper.digest(sdata+touser+userenc, "SHA")))
			return true;
		else
			return false;
	}






	public String getClientid() {
		return clientid;
	}






	public void setClientid(String clientid) {
		this.clientid = clientid;
	}






	public String getUserenc() {
		return userenc;
	}






	public void setUserenc(String userenc) {
		this.userenc = userenc;
	}






	public long getTimestamp() {
		return timestamp;
	}






	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}






	public int getMsgid() {
		return msgid;
	}






	public void setMsgid(int msgid) {
		this.msgid = msgid;
	}






	public String getErrormsg() {
		return errormsg;
	}
	
	
}
