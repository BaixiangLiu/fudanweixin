package edu.fudan.weixin.model.processor;

import java.util.List;
import java.util.Map;
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
import edu.fudan.weixin.model.message.StaticMessageBuilder;

/**
 * 一卡通余额查询处理
 * 
 * @author wking
 * 
 */
public class ElectricMessageProcessor extends LongTermProcessor {

	private static final Pattern p = Pattern.compile("^电费$",
			Pattern.CASE_INSENSITIVE);

	public Map<String, Object> process(Map<String, Object> message) {
		String content = null, msgtype = String.valueOf(message.get("MsgType"));
		content = String.valueOf(message.get("Content"));
		if (!CommonUtil.isEmpty(content)
				&& p.matcher(content).matches()
				|| "event".equalsIgnoreCase(msgtype)
				&& "CLICK"
						.equalsIgnoreCase(String.valueOf(message.get("Event")))
				&& "electric".equalsIgnoreCase(String.valueOf(message
						.get("EventKey"))))
			return super.process(message);
		else
			return null;
	}

	public JSONMessageBuilder _process(Map<String, Object> message) {

		Object openid = message.get("FromUserName");
		DBCollection coll = MongoUtil.getInstance().getDB()
				.getCollection("Bindings");
		DBObject dbo = coll.findOne(new BasicDBObject("openid", openid));
		if (dbo == null || CommonUtil.isEmpty(dbo.get("binds")))
			return StaticMessageBuilder.authBuilder();

		TACOAuth2Model om = new TACOAuth2Model();
		BasicDBList ls = om.electric(dbo);
		NewsJSONMessageBuilder mb = new NewsJSONMessageBuilder();
		StringBuffer info = new StringBuffer();
		int i = 0;
		for (Object usrs : ls) {
			if (usrs != null && usrs instanceof DBObject) {
				if (i > 0)
					info.append("\n\n");
				info.append("学/工号: " +((DBObject) usrs).get("uisid") );
				Object elecs = ((DBObject) usrs).get("list");
				if (elecs != null && elecs instanceof List) {
					for (Object elec : (List) elecs) {
						if (elec != null && elec instanceof DBObject) {
							DBObject ele = (DBObject) elec;
							
							

							info.append("\n房间: " + ele.get("school_space")
									+ ele.get("room"));
							info.append("\n已用电量: " + ele.get("usedamp") + "度");
							info.append("\n可用电量: " + ele.get("canuse") + "度");
							info.append("\n昨日用量: " + ele.get("useelec") + "度");
							info.append("\n抄表时间: " + ele.get("elecdate"));

							i++;
						}
					}
				} else {
					if ("access_denied".equals(((DBObject)((DBObject) usrs).get("list")).get("error")))
						info.append("\n尚未完成绑定操作，请重新对此UIS账号进行绑定。");
					if ("invalid_scope".equals(((DBObject)((DBObject) usrs).get("list")).get("error")))
						info.append("\n未对此UIS账号的电费信息项目授权，请发送语音或文字消息【修改授权】并按提示进行操作");

				}
			}

		}

		if (info.length() > 0) {
			mb.addArticle("电费信息", info.toString(),
					Config.getInstance().get("weixin.context")
							+ "wxlogin.act?redir=electric.act", "");
			mb.setContent(null);
			return mb;
		} else
			return StaticMessageBuilder.authBuilder();

	}

}
