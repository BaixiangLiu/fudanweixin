package edu.fudan.weixin.model.processor;

import java.util.Map;
import java.util.regex.Pattern;

import edu.fudan.eservice.common.utils.CommonUtil;
import edu.fudan.weixin.model.message.StaticMessageBuilder;

/**
 * 生成一条纯文本回复
 * 
 * @author wking
 * 
 */
public class AuthMessageProcessor implements MessageProcessor {

	@Override
	public Map<String, Object> process(Map<String, Object> message) {
		String content = null, msgtype = String.valueOf(message.get("MsgType"));

		content = String.valueOf(message.get("Content"));

		if (!CommonUtil.isEmpty(content)
				&& Pattern.compile("^(绑定|认证|UIS)$", Pattern.CASE_INSENSITIVE)
						.matcher(content).matches()
				|| "event".equalsIgnoreCase(msgtype)
				&& "CLICK"
						.equalsIgnoreCase(String.valueOf(message.get("Event")))
				&& "binding".equalsIgnoreCase(String.valueOf(message
						.get("EventKey")))) {
			return StaticMessageBuilder.buildXMLAuthMessage();
		}
		if (!CommonUtil.isEmpty(content)
				&& Pattern.compile("^(修改授权)$", Pattern.CASE_INSENSITIVE)
						.matcher(content).matches())
			return StaticMessageBuilder.buildXMLScopeMessage(null);
		return null;
	}

}
