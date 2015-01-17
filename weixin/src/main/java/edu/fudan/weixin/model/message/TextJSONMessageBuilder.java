package edu.fudan.weixin.model.message;

import java.util.HashMap;
import java.util.Map;

import edu.fudan.eservice.common.utils.CommonUtil;

/**
 * 客服文本消息
 * @author wking
 *
 */
public class TextJSONMessageBuilder extends JSONMessageBuilder{
	
	public TextMessageBuilder toXMLMessageBuilder()
	{
		TextMessageBuilder text=new TextMessageBuilder();
		Object textobj=message.get("text");
		if(!CommonUtil.isEmpty(textobj)&&textobj instanceof Map)
		{
			text.setContent(((Map)textobj).get("content"));
		}
		return text;
		
	}

	public TextJSONMessageBuilder() {
		super();
		//set("msgtype","image");
	}
	/**
	 * 设置消息文本
	 * @param text
	 */
	public void setContent(Object text)
	{
		HashMap<String,Object> img=new HashMap<String,Object>();
		img.put("content", text);
		set("text",img);
	}

}
