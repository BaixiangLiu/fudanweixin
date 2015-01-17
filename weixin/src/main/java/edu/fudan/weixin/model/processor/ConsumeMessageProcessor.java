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
import edu.fudan.eservice.common.utils.EncodeHelper;
import edu.fudan.eservice.common.utils.MongoUtil;
import edu.fudan.weixin.model.TACOAuth2Model;
import edu.fudan.weixin.model.message.JSONMessageBuilder;
import edu.fudan.weixin.model.message.NewsJSONMessageBuilder;
import edu.fudan.weixin.model.message.NewsMessageBuilder;
import edu.fudan.weixin.model.message.StaticMessageBuilder;

public class ConsumeMessageProcessor extends LongTermProcessor {

	private static final Pattern p=Pattern.compile(
			"^(一卡通|ecard|ykt)(消费|xf)(\\d{8})?(\\d{8})?$",
			Pattern.CASE_INSENSITIVE);
	public Map<String, Object> process(Map<String, Object> message) {
		String content = null, msgtype = String.valueOf(message.get("MsgType"));	
			content = String.valueOf(message.get("Content"));

		if (!CommonUtil.isEmpty(content)&&p.matcher(content.trim()).matches()
				|| "event".equalsIgnoreCase(msgtype)
				&& "CLICK"
						.equalsIgnoreCase(String.valueOf(message.get("Event")))
				&& "yktxf".equalsIgnoreCase(String.valueOf(message
						.get("EventKey")))) 
			return super.process(message);
			else
				return null;
		}
			// String bdate = "",edate = "";
			// if (content.length()>15) {
			// bdate =
			// content.substring(content.length()-16,content.length()-8);
			// edate = content.substring(content.length()-8);
			// }
			// else if (content.length()>7) {
			// bdate = content.substring(content.length()-8);
			// }
	public JSONMessageBuilder _process(Map<String, Object> message) {
			String bdate = null, edate = null;
			String 	content = String.valueOf(message.get("Content"));
			if (!CommonUtil.isEmpty(content)){
				Matcher m = p.matcher(content);
				if (m.matches())
					{
				bdate = m.group(3);
				edate = m.group(4);
					}
			}
			if (CommonUtil.isEmpty(bdate))
				bdate = "";
			if (CommonUtil.isEmpty(edate))
				edate = "";
			Object openid = message.get("FromUserName");
			DBCollection coll = MongoUtil.getInstance().getDB()
					.getCollection("Bindings");
			DBObject dbo = coll.findOne(new BasicDBObject("openid", openid));
			if (dbo == null || CommonUtil.isEmpty(dbo.get("binds")))
				return StaticMessageBuilder.authBuilder();
			TACOAuth2Model om = new TACOAuth2Model();
			BasicDBList ls =om.yktxf(dbo, bdate, edate) ;			
			NewsJSONMessageBuilder mb = new NewsJSONMessageBuilder();
			StringBuffer info = new StringBuffer();
			for (int i = 0; i < ls.size(); i++) {
				DBObject uis = (DBObject) ls.get(i);
				String uisid = String.valueOf(uis.get("uisid"));				
					if (i > 0)
						info.append("\n\n");
					info.append("学/工号: " + uisid);

			
			
			Object msg=uis.get("list");
			if (msg instanceof BasicDBList) {
				BasicDBList list = (BasicDBList) msg;
				
				for (Object obj : list) {
					DBObject ret = (DBObject) obj;
					if (ret != null && !CommonUtil.isEmpty(ret.get("fsrq"))) {
						info.append("\n消费日期: " + ret.get("fsrq"));
						info.append(" 消费金额: " + ret.get("amount") + "元  ");

					}
				}
				
			} else {
				BasicDBObject ret = (BasicDBObject) msg;
				if ("access_denied".equals(ret.get("error")))
					info.append("\n尚未完成绑定操作，请重新对此UIS账号进行绑定。");
				if ("invalid_scope".equals(ret.get("error")))
					info.append("\n未对此UIS账号的一卡通项目授权，请发送语音或文字消息【修改授权】并按提示进行操作");


			}
				}
				
			
			if (info.length() > 0) {
			mb.addArticle("日消费信息", info.toString(), Config.getInstance().get("weixin.context")+"wxlogin.act?redir="+EncodeHelper.encode("ecarddaily.act?bdate="+bdate+"&edate="+edate, "URL"), "");
			mb.setContent(null);
			return mb;
			} else
				return StaticMessageBuilder.authBuilder();
	}
}
