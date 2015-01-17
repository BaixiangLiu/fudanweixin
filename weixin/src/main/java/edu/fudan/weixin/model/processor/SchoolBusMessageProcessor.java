package edu.fudan.weixin.model.processor;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;

import edu.fudan.eservice.common.utils.CommonUtil;
import edu.fudan.weixin.model.message.JSONMessageBuilder;
import edu.fudan.weixin.model.message.NewsJSONMessageBuilder;
import edu.fudan.weixin.model.message.NewsMessageBuilder;
import edu.fudan.weixin.utils.TACOAuth2Helper;

public class SchoolBusMessageProcessor extends LongTermProcessor {
	
	private static final Pattern p = Pattern
			.compile(
					"^(校车|bus|schoolbus|xc)(查询|cx|信息|xx)?(邯郸|张江|枫林|江湾|h|z|f|j)(邯郸|张江|枫林|江湾|h|z|f|j)$",
					Pattern.CASE_INSENSITIVE);
	
	
	public Map<String,Object> process(Map<String,Object> message) {
		String content = null, msgtype = String.valueOf(message.get("MsgType"));
		content = String.valueOf(message.get("Content")).trim();
		if (!CommonUtil.isEmpty(content)
				&& p.matcher(content).matches()
				|| "event".equalsIgnoreCase(msgtype)
				&& "CLICK"
						.equalsIgnoreCase(String.valueOf(message.get("Event")))
				&& "schoolbus".equalsIgnoreCase(String.valueOf(message
						.get("EventKey"))))
			return super.process(message);
		else
			return null;
	}

	@Override
	public JSONMessageBuilder _process(Map<String, Object> message) {
		// TODO Auto-generated method stub
		String from = "",to="",startplace="",remark="",fromplace="",toplace="";
		String content = String.valueOf(message.get("Content")).trim();
		Matcher m = p.matcher(content);
		if (m.matches()) {
			from = m.group(3);
			to = m.group(4);
		}
		Object ret = TACOAuth2Helper.schoolbus(from, to).get("list");
		NewsJSONMessageBuilder mb = new NewsJSONMessageBuilder();
		StringBuffer info = new StringBuffer();
		if (ret instanceof BasicDBList) {
			BasicDBList list = (BasicDBList) ret;
			if (list != null && list.size()>0) {
				for (Object obj : list) {
					DBObject r = (DBObject)obj;
					if (r!=null && r.get("remark").equals("工作日")) {
						if (startplace.equals(""))
							startplace = r.get("startplace").toString();
						if (fromplace.equals("")) {
							fromplace = r.get("from").toString();
							toplace = r.get("to").toString();
							info.append(fromplace+"校区发往"+toplace+"校区的校车时刻表如下:");	
						}
						if (remark.equals(""))
							remark = r.get("remark").toString();
						info.append("\n"+r.get("from")+" --> "+r.get("to")+"  "+r.get("time"));
					}
					
				}
			}
			else {
				info.append("\n你所查的校区之间没有班车往来。");
			}
			
		}
		if (!fromplace.equals("")&&!toplace.equals(""))
			info.append("\n"+fromplace+"校区上车地点为:\n"+startplace);
		if (!remark.equals(""))
			info.append("\n本校车时刻表仅适用于"+remark+".");
		mb.addArticle("校车信息", info.toString(), "", "");
		mb.setContent(null);
		return mb;

	}

}
