package edu.fudan.weixin.model.processor;

import java.math.BigDecimal;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import edu.fudan.eservice.common.utils.CommonUtil;
import edu.fudan.eservice.common.utils.Config;
import edu.fudan.eservice.common.utils.MongoUtil;
import edu.fudan.weixin.model.TACOAuth2Model;
import edu.fudan.weixin.model.message.NewsJSONMessageBuilder;
import edu.fudan.weixin.model.message.NewsMessageBuilder;
import edu.fudan.weixin.model.message.StaticMessageBuilder;

/**
 * 成绩查询处理
 * 
 * @author wking
 * 
 */
public class ScoreMessageProcessor extends LongTermProcessor {

	private static final Pattern p = Pattern
			.compile(
					"^(成绩|cj)(查询|cx)?((20)?([0-2][0-9])((20)([0-2][0-9]))?(0)?([1-3]))?$",
					Pattern.CASE_INSENSITIVE);

	@Override
	public Map<String, Object> process(Map<String, Object> message) {
		String content = null, msgtype = String.valueOf(message.get("MsgType"));
		content = String.valueOf(message.get("Content"));

		if (!CommonUtil.isEmpty(content)
				&& p.matcher(content.trim()).matches()
				|| "event".equalsIgnoreCase(msgtype)
				&& "CLICK"
						.equalsIgnoreCase(String.valueOf(message.get("Event")))
				&& "score".equalsIgnoreCase(String.valueOf(message
						.get("EventKey"))))
			return super.process(message);
		else
			return null;
	}

	public Map<String, Object> _process(Map<String, Object> message) {
		String term = "";
		String content = String.valueOf(message.get("Content"));
		if (!CommonUtil.isEmpty(content)) {
			Matcher m = p
					.matcher(String.valueOf(message.get("Content")).trim());

			if (m.matches())

			{
				String byear = m.group(5);
				String tm = m.group(10);
				if (!CommonUtil.isEmpty(byear) && !CommonUtil.isEmpty(tm))
					term = "20" + byear + "20" + (Integer.parseInt(byear) + 1)
							+ "0" + tm;
			}
		}

		Object openid = message.get("FromUserName");
		DBCollection coll = MongoUtil.getInstance().getDB()
				.getCollection("Bindings");
		DBObject dbo = coll.findOne(new BasicDBObject("openid", openid));

		if (dbo == null || CommonUtil.isEmpty(dbo.get("binds")))
			return StaticMessageBuilder.buildJSONAuthMessage();
		TACOAuth2Model om = new TACOAuth2Model();
		BasicDBList ls = om.score(dbo, term);	
		NewsMessageBuilder mb = new NewsJSONMessageBuilder();
		StringBuffer info = new StringBuffer();
		for (int i = 0; i < ls.size(); i++) {
			DBObject uis = (DBObject) ls.get(i);
			String uisid = String.valueOf(uis.get("uisid"));			
				Object ret =uis.get("list");			
				if (i > 0)
					info.append("\n\n");
				info.append("学/工号: " + uisid);

				if (ret instanceof BasicDBList) {
					BasicDBList list = (BasicDBList) ret;

					String realname = (String) uis.get("username");
					if (realname != null) {
						info.append(" 姓名: " + realname);
					}
					if (list != null && list.size() > 0) {
						double cc = 0, cp = 0;
						for (Object obj : list) {
							DBObject r = (DBObject) obj;
							if (r != null
									&& !CommonUtil.isEmpty(r.get("grade"))) {

								info.append("\n\n" + r.get("course_name"));
								info.append("(" + r.get("course_id") + ") ");
								info.append("\n成绩:" + r.get("grade"));
								info.append("(" + r.get("point") + ") ");
								info.append(r.get("term") + "");
								info.append(" 学分:" + r.get("credit"));

								double p = Double.parseDouble(String.valueOf(r
										.get("point")));
								if (p > 0
										|| p == 0
										&& "F".equalsIgnoreCase(String
												.valueOf(r.get("grade")))) {
									double c = Double.parseDouble(String
											.valueOf(r.get("credit")));
									cc += c;
									cp += c * p;
								}
							}
						}
						if (cc > 0)
							info.append("\n\n平均绩点："
									+ BigDecimal
											.valueOf(cp / cc)
											.setScale(2,
													BigDecimal.ROUND_HALF_UP)
											.doubleValue());

					} else {
						info.append("\n该学期尚无成绩信息，可能是没有选课或者尚未登分。");
					}

				} else {
					BasicDBObject r = (BasicDBObject) ret;

					if (r == null || "access_denied".equals(r.get("error")))
						info.append("\n尚未完成绑定操作，请重新对此UIS账号进行绑定。");
					if ("invalid_scope".equals(r.get("error"))
							&& String.valueOf(r.get("error_description"))
									.indexOf("grant") > 0)
						info.append("\n未对此UIS账号的成绩项目授权，请发送语音或文字消息【修改授权】并按提示进行操作");
					else {

						info.append("\n该UIS账号绑定的身份不是学生，没有成绩可查。/::D");

					}
				}
			}
		
		if (info.length() > 0) {
			mb.addArticle("成绩信息", info.toString(), Config.getInstance().get("weixin.context")+"wxlogin.act?redir=score.act", "");
			mb.setContent(null);
			return mb.getMessage();
		} else
			return StaticMessageBuilder.buildJSONAuthMessage();
	}
}
