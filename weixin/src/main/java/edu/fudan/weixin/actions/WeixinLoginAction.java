package edu.fudan.weixin.actions;

import java.io.IOException;
import java.util.Random;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import edu.fudan.eservice.common.struts.GuestActionBase;
import edu.fudan.eservice.common.utils.CommonUtil;
import edu.fudan.eservice.common.utils.Config;
import edu.fudan.eservice.common.utils.EncodeHelper;
import edu.fudan.eservice.common.utils.MongoUtil;

@ParentPackage(value = "servicebase")
public class WeixinLoginAction extends GuestActionBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7080148262465268414L;
	private String redir = "index.act";
	private String code;
	private String state;
	private static final Log log = LogFactory.getLog(WeixinLoginAction.class);

	@Action("wxlogin")
	public String execute() {
		if (CommonUtil.isEmpty(getSession().get("openid"))) {
			Config conf = Config.getInstance();
			try {
				byte[] bs = new byte[16];
				new Random().nextBytes(bs);
				String st = EncodeHelper.bytes2hex(bs);
				// 放进一个使用EhCache维护的容器，当用户从微信的OAuth2.0拿到code后检查这个链接是不是由此链接生成的。
				// CacheManager.getInstance().getCache("WXStates")
				// .put(new Element(st, redir));
				getSession().put("wxstate", st);
				getSession().put("redir", redir);
				redir = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
						+ conf.get("weixin.appid")
						+ "&redirect_uri="
						+ EncodeHelper.encode(conf.get("weixin.context")
								+ "wxlogindo.act", "URL")
						+ "&response_type=code&scope=snsapi_base&state="
						+ st
						+ "#wechat_redirect";
			} catch (Exception e) {
				log.error(e);
			}
		}
		try {
			org.apache.struts2.ServletActionContext.getResponse().sendRedirect(
					redir);
		} catch (IOException e) {
			log.error(e);
		}

		return NONE;
	}

	@Action("wxlogindo")
	public String logindo() {
		// Cache cache= CacheManager.getInstance().getCache("WXStates");
		// Element el=cache.get(state);
		if (!CommonUtil.isEmpty(code) && !CommonUtil.isEmpty(state)
				&& state.equals(getSession().remove("wxstate"))) {

			redir = String.valueOf(getSession().remove("redir"));
			// cache.removeElement(el);
			Config conf = Config.getInstance();
			// 获取微信的access_token
			String urlstr = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
					+ conf.get("weixin.appid")
					+ "&secret="
					+ conf.get("weixin.secret")
					+ "&code="
					+ code
					+ "&grant_type=authorization_code";
			try {
				String ret = CommonUtil.getWebContent(urlstr).toString();
				DBObject retobj = (DBObject) JSON.parse(ret);
				Object acctk = retobj.get("access_token");
				if (!CommonUtil.isEmpty(acctk)) {
					// 更新Bindings库中的access_token
					DBCollection c = MongoUtil.getInstance().getDB()
							.getCollection("Bindings");
					Object openid = retobj.get("openid");
					// 设置Session
					getSession().put("openid", openid);
					DBObject obj = c
							.findOne(new BasicDBObject("openid", openid));
					if (CommonUtil.isEmpty(obj)) {
						obj = new BasicDBObject().append("openid",
								retobj.get("openid"));
					}
					obj.put("weixintoken", acctk);
					obj.put("weixinexpired", System.currentTimeMillis() + 1000
							* (int) retobj.get("expires_in"));
					obj.put("weixinscope", retobj.get("scope"));
					obj.put("wexinrefresh", retobj.get("refresh_token"));
					c.save(obj);
					/*
					 * if(obj.get("binds")!=null &&obj.get("binds") instanceof
					 * List) { List<BasicDBObject> ls=new
					 * ArrayList<BasicDBObject>(); for(DBObject
					 * ob:(List<DBObject>)obj.get("binds")) {
					 * if(!CommonUtil.isEmpty(ob)){ BasicDBObject bdo=new
					 * BasicDBObject(); bdo.put("uisid", ob.get("uisid"));
					 * bdo.put("username",ob.get("username"));
					 * bdo.put("usertype", ob.get("usertype")); ls.add(bdo); } }
					 * getSession().put("binds", ls); }
					 */

					DBObject user = MongoUtil.getInstance().getDB()
							.getCollection("weixinuser")
							.findOne(new BasicDBObject("openid", openid));
					if (!CommonUtil.isEmpty(user))
						getSession().put("nickname", user.get("nickname"));

				} else {
					log.error(ret);
				}
			} catch (Exception e) {
				log.error(e);
			}
			try {
				org.apache.struts2.ServletActionContext.getResponse()
						.sendRedirect(redir);
			} catch (Exception e) {
				log.error(e);
			}
		} else {
			try {
				org.apache.struts2.ServletActionContext.getResponse()
						.getWriter().write("Unreconginzed reqest!");
			} catch (IOException e) {
				log.error(e);
			}
		}
		return NONE;
	}

	public String getRedir() {
		return redir;
	}

	public void setRedir(String redir) {
		this.redir = redir;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
