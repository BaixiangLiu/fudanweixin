package edu.fudan.weixin;


import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.mail.MessagingException;

import org.junit.Test;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import edu.fudan.eservice.common.smtp.SMTP;
import edu.fudan.eservice.common.smtp.ServiceMail;
import edu.fudan.eservice.common.utils.CommonUtil;
import edu.fudan.eservice.common.utils.MongoUtil;
import edu.fudan.weixin.model.message.JSONMessageBuilder;
import edu.fudan.weixin.utils.AccessTokenHelper;
import edu.fudan.weixin.utils.WeixinMessageHelper;
/**
 * 绑定安全风险处理
 * @author wking
 *
 */
public class ClearBinds {
	@Test
	public void findMultiUis() {
		DBCollection dc = MongoUtil.getInstance().getCollection("Bindings");
		DBCollection dc2 = MongoUtil.getInstance().getCollection("weixinuser");
		DBCursor c = dc.find();
		Map<String, Integer> holder = new HashMap<String, Integer>();
		while (c.hasNext()) {
			try {
				DBObject obj = c.next();
				BasicDBList binds = (BasicDBList) obj.get("binds");
				if (binds != null)
					for (Object o : binds) {
						DBObject user = (DBObject) o;
						String id = user.get("uisid").toString();
						Integer i = holder.get(id);
						if (i == null)
							i = 1;
						else
							i++;
						holder.put(id, i);
					}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		for (Entry<String, Integer> entry : holder.entrySet()) {
			if (entry.getValue() > 1) {
				System.out.println(entry.getKey() + ":" + entry.getValue());
				DBCursor c2 = dc.find(new BasicDBObject("binds",
						new BasicDBObject("$elemMatch", new BasicDBObject(
								"uisid", entry.getKey()))));
				
				String nickname = "";
				while (c2.hasNext()) {
					try {
						DBObject binding = c2.next();
						DBObject wxusr = dc2.findOne(new BasicDBObject(
								"openid", binding.get("openid")));
						nickname += "\"" + wxusr.get("nickname") + "\", ";

					} catch (Exception e) {

					}
				}
				System.out.println(nickname);
				if(entry.getKey().matches("\\d{7,}"))
				{
					SMTP smtp=new ServiceMail();
					smtp.addTo(entry.getKey()+"@fudan.edu.cn");
					smtp.setTitle("信息办微信公众号绑定风险提示");
					try {
						smtp.setMailbody("同学你好，\n我们发现你的学号绑定到了"+entry.getValue()+"个不同的微信号，昵称分别为："+nickname+"如果其中有不是你希望绑定到的微信号请尽快与信息办联系，电话65643207,邮件：urp@fudan.edu.cn", false);
						smtp.send();
						System.out.println("邮件发送："+entry.getKey());
					} catch (MessagingException e) {
					 e.printStackTrace();
					}
				
				}
			}
		}
	}

	public void clearDangerMultiBind() {

		DBCollection dc = MongoUtil.getInstance().getCollection("Bindings");
		DBCursor c = dc.find();
		JSONMessageBuilder bd = new JSONMessageBuilder();
		bd.setContent("由于我们发现您绑定了多个不同姓名的账号，为保护个人隐私我们已经将您所有的绑定信息清空，如需继续使用请重新绑定自己的UIS账号。我们不推荐帮助其他人查询个人信息。");

		while (c.hasNext()) {
			DBObject obj = c.next();
			try {
				BasicDBList binds = (BasicDBList) obj.get("binds");
				if (binds.size() > 1) {
					Object name = null;
					for (int i = 0; i < binds.size(); i++) {
						DBObject u = (DBObject) binds.get(i);
						if (i > 0) {
							if (u.get("username") == null
									|| !u.get("username").equals(name)) {
								dc.remove(obj);

								System.out.println(obj);
								bd.set("touser", obj.get("openid"));
								System.out
										.println(CommonUtil
												.postWebRequest(
														"https://api.weixin.qq.com/cgi-bin/message/custom/"
																+ "send?access_token="
																+ AccessTokenHelper
																		.getInstance()
																		.getToken(
																				AccessTokenHelper.WEIXIN),
														WeixinMessageHelper
																.msg2jsonstr(
																		bd.getMessage())
																.getBytes(
																		"utf-8"),
														"application/json; charset=utf-8"));

							}
							break;
						}
						name = u.get("username");
					}

				}
			} catch (Exception ex) {

			}
		}

	}

}
