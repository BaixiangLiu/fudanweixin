package edu.fudan.weixin.model.processor;

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
public class BalanceMessageProcessor extends LongTermProcessor {

	private static final Pattern p = Pattern.compile(
			"^(一卡通|ecard|ykt)(余额|ye|信息|xx)?$", Pattern.CASE_INSENSITIVE);

	public Map<String, Object> process(Map<String, Object> message) {
		String content = null, msgtype = String.valueOf(message.get("MsgType"));
		content = String.valueOf(message.get("Content"));
		if (!CommonUtil.isEmpty(content)
				&& p.matcher(content).matches()
				|| "event".equalsIgnoreCase(msgtype)
				&& "CLICK"
						.equalsIgnoreCase(String.valueOf(message.get("Event")))
				&& "yktxx".equalsIgnoreCase(String.valueOf(message
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
		BasicDBList ls=om.yktxx(dbo);		
		NewsJSONMessageBuilder mb = new NewsJSONMessageBuilder();
		StringBuffer info = new StringBuffer();
		for (int i = 0; i < ls.size(); i++) {
			DBObject uis = (DBObject) ls.get(i);
			String uisid = String.valueOf(uis.get("uisid"));			
				if (i > 0)
					info.append("\n\n");
				info.append("学/工号: " + uisid);
				if ( !CommonUtil.isEmpty(uis.get("xgh"))) {
					info.append("\n卡类型: " + uis.get("card_type"));
					info.append("\n卡状态: " + uis.get("card_state"));
					info.append("\n余额: " + uis.get("card_balance") + " 元");

				} else {
					if ("access_denied".equals(uis.get("error")))
						info.append("\n尚未完成绑定操作，请重新对此UIS账号进行绑定。");
					if ("invalid_scope".equals(uis.get("error")))
						info.append("\n未对此UIS账号的一卡通项目授权，请发送语音或文字消息【修改授权】并按提示进行操作");

				}
			}

	
		
		if (info.length() > 0) {
			mb.addArticle("一卡通信息", info.toString(), Config.getInstance().get("weixin.context")+"wxlogin.act?redir=ecard.act", "");
			mb.setContent(null);
			return mb;
		} else
			return StaticMessageBuilder.authBuilder();

	}

}
