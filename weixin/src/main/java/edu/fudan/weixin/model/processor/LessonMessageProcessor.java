package edu.fudan.weixin.model.processor;


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
import edu.fudan.weixin.model.message.JSONMessageBuilder;
import edu.fudan.weixin.model.message.NewsJSONMessageBuilder;
import edu.fudan.weixin.model.message.NewsMessageBuilder;
import edu.fudan.weixin.model.message.StaticMessageBuilder;

/**
 * 课表查询处理
 * 
 * @author wking
 * 
 */
public class LessonMessageProcessor extends LongTermProcessor {

	private static final Pattern p = Pattern
			.compile(
					"^(选课|课程|xuanke|xk|lesson|kecheng|kc)(查询|cx|信息|xx)?((20)?([0-2][0-9])((20)([0-2][0-9]))?(0)?([1-3]))?$",
					Pattern.CASE_INSENSITIVE);

	@Override
	public Map<String, Object> process(Map<String, Object> message) {
		String content = null, msgtype = String.valueOf(message.get("MsgType"));
		content = String.valueOf(message.get("Content"));
		if (!CommonUtil.isEmpty(content)
				&& p.matcher(content).matches()
				|| "event".equalsIgnoreCase(msgtype)
				&& "CLICK"
						.equalsIgnoreCase(String.valueOf(message.get("Event")))
				&& "lesson".equalsIgnoreCase(String.valueOf(message
						.get("EventKey"))))
			return super.process(message);
		else
			return null;
	}

	public JSONMessageBuilder _process(Map<String, Object> message) {
		String term = "";
		String content = String.valueOf(message.get("Content")).trim();
		if (!CommonUtil.isEmpty(content)) {
			Matcher m = p.matcher(content);
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
			return StaticMessageBuilder.authBuilder();

		
		TACOAuth2Model om = new TACOAuth2Model();
		BasicDBList ls =om.lesson(dbo, term) ;
		NewsJSONMessageBuilder mb = new NewsJSONMessageBuilder();
		StringBuffer info = new StringBuffer();
		for (int i = 0; i < ls.size(); i++) {
			DBObject uis = (DBObject) ls.get(i);
			String uisid = String.valueOf(uis.get("uisid"));		
				if (i > 0)
					info.append("\n\n");
				info.append("学/工号: " + uisid);
				Object ret=uis.get("list");
				if (ret instanceof BasicDBList) {
					BasicDBList list = (BasicDBList) ret;
					String realname = (String) uis.get("username");
					if (realname != null) {
						info.append(" 姓名: " + realname);
					}
					
					if (list != null && list.size() > 0) {
						for (int j=0;j<list.size();j++ ) {
							DBObject r = (DBObject) list.get(j);
							if (r != null && !CommonUtil.isEmpty(r.get("id"))) {
								if(j==0)
									info.append(" 学期："+r.get("term"));
								info.append("\n\n" + r.get("course_type"));
								info.append("\n" + r.get("course_name"));
								info.append("(" + r.get("course_id") + ")");
								info.append("(" + r.get("credit") + "学分)");
								info.append("\n主讲教师:" + r.get("teacher"));
								info.append("\n上课时间地点：\n" + r.get("sksj"));
							}

						}

					} else {
						info.append("\n该学期尚无课程信息。");
					}

				} else {
					BasicDBObject r = (BasicDBObject) ret;
					if ("access_denied".equals(r.get("error")))
						info.append("\n尚未完成绑定操作，请重新对此UIS账号进行绑定。");
					if ("invalid_scope".equals(r.get("error")))
						info.append("\n未对此UIS账号的课程项目授权，请发送语音或文字消息【修改授权】并按提示进行操作");

				}
			}
		
		if (info.length() > 0) {
			mb.addArticle("课程信息", info.toString(), Config.getInstance().get("weixin.context")+"wxlogin.act?redir=lesson.act", "");
			mb.setContent(null);
			return mb;
		} else
			return StaticMessageBuilder.authBuilder();
	}

}
