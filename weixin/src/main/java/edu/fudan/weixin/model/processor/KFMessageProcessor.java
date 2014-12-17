package edu.fudan.weixin.model.processor;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import edu.fudan.eservice.common.utils.CommonUtil;

/**
 * 生成一条纯文本回复
 * 
 * @author wking
 * 
 */
public class KFMessageProcessor implements MessageProcessor {

	@Override
	public Map<String, Object> process(Map<String, Object> message) {
		String content = null, msgtype = String.valueOf(message.get("MsgType"));

		content = String.valueOf(message.get("Content"));

		if (!CommonUtil.isEmpty(content)
				&& Pattern.compile("^(客服|人工服务|kf|kefu)$", Pattern.CASE_INSENSITIVE)
						.matcher(content).matches()
				|| "event".equalsIgnoreCase(msgtype)
				&& "CLICK"
						.equalsIgnoreCase(String.valueOf(message.get("Event")))
				&& "kefu".equalsIgnoreCase(String.valueOf(message
						.get("EventKey")))) {
			Map<String,Object> msg=new HashMap<String,Object>();
			msg.put("MsgType", "transfer_customer_service");
			return msg;
					
		}
		
		return null;
	}

}
