package edu.fudan.weixin.actions;

import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.convention.annotation.Result;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import edu.fudan.eservice.common.smtp.SMTP;
import edu.fudan.eservice.common.smtp.ServiceMail;
import edu.fudan.eservice.common.struts.GuestActionBase;
import edu.fudan.eservice.common.utils.CommonUtil;
import edu.fudan.eservice.common.utils.Config;
import edu.fudan.eservice.common.utils.EncodeHelper;
import edu.fudan.eservice.common.utils.MongoUtil;
import edu.fudan.eservice.common.utils.OAuth2Helper;
import edu.fudan.weixin.utils.TACOAuth2Helper;

@ParentPackage(value = "servicebase")
@Namespace("/")
@Results({ @Result(location = "result.jsp") })
public class BindAction extends GuestActionBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3364128562759884183L;
	private String code;
	private String state;
	private String scope;

	@Action("bindin")
	public String input() {
		Config conf = Config.getInstance();
		long st = new Random().nextLong();
		// 放进一个使用EhCache维护的容器，当用户从微信的OAuth2.0拿到code后检查这个链接是不是由此链接生成的。
		CacheManager.getInstance().getCache("WXStates").put(new Element(String.valueOf(st), st));
		try {
			org.apache.struts2.ServletActionContext.getResponse().sendRedirect("https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + conf.get("weixin.appid") + "&redirect_uri=" + URLEncoder.encode(conf.get("uis.bindurl"), "utf-8") + "&response_type=code&scope=snsapi_base&state=" + st + "#wechat_redirect");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return NONE;
	}

	@Action("uisbind")
	public String uis() {

		if (CacheManager.getInstance().getCache("WXStates").get(state) != null) {

			Config conf = Config.getInstance();
			// 获取微信的access_token
			String urlstr = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + conf.get("weixin.appid") + "&secret=" + conf.get("weixin.secret") + "&code=" + code + "&grant_type=authorization_code";
			try {
				String ret = CommonUtil.getWebContent(urlstr).toString();
				DBObject retobj = (DBObject) JSON.parse(ret);
				Object acctk = retobj.get("access_token");
				if (!CommonUtil.isEmpty(acctk)) {
					DBCollection c = MongoUtil.getInstance().getDB().getCollection("Bindings");
					DBObject obj = c.findOne(new BasicDBObject("openid", retobj.get("openid")));
					if (CommonUtil.isEmpty(obj)) {
						obj = new BasicDBObject().append("openid", retobj.get("openid"));
					}
					obj.put("weixintoken", acctk);
					obj.put("weixinexpired", System.currentTimeMillis() + 1000 * (int) retobj.get("expires_in"));
					obj.put("weixinscope", retobj.get("scope"));
					obj.put("wexinrefresh", retobj.get("refresh_token"));
					c.save(obj);
					// DBObject userinfo=new
					// TACOAuth2Model().fetchUserinfo(obj);
					// if (!CommonUtil.isEmpty(userinfo.get("user_id"))) {
					// addActionMessage("UIS已经绑定为" + userinfo.get("user_id"));
					// } else {
					// 重定向到复旦TAC的授权页面
					org.apache.struts2.ServletActionContext.getResponse().sendRedirect(conf.get("tac.codeurl")+"?response_type=code&scope=" + URLEncoder.encode(conf.get("tac.scope"), "utf-8") + "&client_id=" + conf.get("tac.clientid") + "&redirect_uri=" + URLEncoder.encode(conf.get("tac.redirecturi"), "utf-8") + "&state=" + EncodeHelper.bytes2hex(EncodeHelper.encrypt("AES", obj.get("openid").toString().getBytes(), EncodeHelper.hex2bytes(conf.get("tac.enckey")), null)));
					return NONE;
					// }
				} else {
					addActionError("Error in Weixin OAuth2 : " + retobj.get("errcode") + retobj.get("errmsg"));
				}
			} catch (Exception e) {
				e.printStackTrace();
				addActionError("Error in Weixin OAuth2: " + e.getMessage());
			}
		} else {
			addActionError("该绑定请求的来源可能有风险，请通过“复旦信息办”微信公众账号获取最新的绑定链接！");
		}
		return SUCCESS;

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Action("uisbinddo")
	public String uisbinddo() {
		Config conf = Config.getInstance();
		// 获取access_token

		try {
			Map<String, Object> retobj = OAuth2Helper.getToken(code, false);
			Object acctk = retobj.get("access_token");
			if (!CommonUtil.isEmpty(acctk)) {
				DBCollection c = MongoUtil.getInstance().getDB().getCollection("Bindings");
				String openid = new String(EncodeHelper.dencrypt("AES", EncodeHelper.hex2bytes(state), EncodeHelper.hex2bytes(conf.get("tac.enckey")), null));
				DBObject idobj = c.findOne(new BasicDBObject("openid", openid));
				if (!CommonUtil.isEmpty(idobj)) {
					BasicDBObject obj = new BasicDBObject();
					obj.put("uistoken", acctk);
					obj.put("uisexpired", System.currentTimeMillis() + 1000 * (int) retobj.get("expires_in"));
					obj.put("uisscope", scope);
					obj.put("uisrefresh", retobj.get("refresh_token"));
					// 获取用户信息

					retobj = TACOAuth2Helper.fetchUser(String.valueOf(acctk));
					if (!CommonUtil.isEmpty(retobj.get("user_id"))) {
						Object email = retobj.get("email");
						Object userid = retobj.get("user_id");
						Object bds = idobj.get("binds");
						if (!CommonUtil.isEmpty(bds) && bds instanceof List) {
							Iterator i = ((List) bds).iterator();
							while (i.hasNext()) {
								Object o = i.next();
								if (!CommonUtil.isEmpty(o)) {
									DBObject bd = (DBObject) o;
									if (CommonUtil.eq(userid, bd.get("uisid")))
										i.remove();
								} else
									i.remove();
							}

						} else {
							bds = new BasicDBList();
							idobj.put("binds", bds);
						}
						obj.put("uisid", userid);
						obj.put("email", email);
						obj.put("username", retobj.get("user_name"));
						obj.put("usertype", retobj.get("user_type"));
						((List) bds).add(obj);
						addActionMessage("成功将此微信号与" + retobj.get("user_id") + "完成了绑定");
						// 如果可以获取到Email发个通知
						if (String.valueOf(userid).matches("\\d{7,}"))
							email = userid + "@fudan.edu.cn";
						if (!CommonUtil.isEmpty(email)) {
							try {

								SMTP smtp = new ServiceMail();
								smtp.addTo(email.toString());
								smtp.setTitle("微信账号与UIS账号绑定成功");
								String mb = conf.get("bind.mail");
								mb = mb.replaceAll("%uis%", String.valueOf(retobj.get("user_id")));
								DBObject user = MongoUtil.getInstance().getDB().getCollection("weixinuser").findOne(new BasicDBObject("openid", openid));
								if (!CommonUtil.isEmpty(user))
									mb = mb.replaceAll("%nickname%", String.valueOf(user.get("nickname")));
								smtp.setMailbody(mb, false);
								smtp.send();
							} catch (Exception e) {
								addActionError("Error in Send Mail:" + e);
							}
						}

					} else {
						addActionError("Error in TAC Resource:" + retobj.get("error"));
					}
					c.save(idobj);

				}
			} else {
				addActionError("Error in TAC OAuth2 : " + retobj.get("error"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			addActionError("Error in TAC OAuth2: " + e.getMessage());
		}
		return SUCCESS;
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

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

}
