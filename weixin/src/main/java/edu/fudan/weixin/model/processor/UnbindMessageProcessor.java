package edu.fudan.weixin.model.processor;

import java.util.Map;
import java.util.regex.Pattern;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import edu.fudan.eservice.common.utils.CommonUtil;
import edu.fudan.eservice.common.utils.MongoUtil;
import edu.fudan.weixin.model.message.JSONMessageBuilder;

/**
 * 一卡通余额查询处理
 * @author wking
 *
 */
public class UnbindMessageProcessor extends LongTermProcessor {

	@Override
	public Map<String, Object> process(Map<String, Object> message) {
		String content=null, msgtype = String.valueOf(message.get("MsgType"));
		content = String.valueOf(message.get("Content"));
		if(!CommonUtil.isEmpty(content)&&Pattern.compile("^(解绑|解除绑定|unbind)?$",Pattern.CASE_INSENSITIVE).matcher(content).matches()||"event".equalsIgnoreCase(msgtype)&&"CLICK".equalsIgnoreCase(String.valueOf(message.get("Event")))&&"unbind".equalsIgnoreCase(String.valueOf(message.get("EventKey"))))
		return super.process(message);
		else
			return null;
	}
	public Map<String, Object> _process(Map<String, Object> message) {
		{
			Object openid=message.get("FromUserName");
			DBCollection coll=MongoUtil.getInstance().getDB().getCollection("Bindings");
			DBObject dbo=coll.findOne(new BasicDBObject("openid",openid));
			String msg="";
			if(dbo==null||CommonUtil.isEmpty(dbo.get("binds"))) msg="您从未绑定过任何账号";
			else{
			
				msg="成功解除所有账号的绑定！如需要再次绑定请输入文本或语音[绑定]或者通过菜单个人中心->绑定UIS账号进行操作";
			
			coll.remove(dbo);
			}
			JSONMessageBuilder mb=new JSONMessageBuilder();
			mb.setContent(msg);
			return mb.getMessage();
		}
	}

}
