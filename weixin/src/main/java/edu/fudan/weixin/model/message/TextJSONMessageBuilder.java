package edu.fudan.weixin.model.message;

import java.util.HashMap;

/**
 * 客服文本消息
 * @author wking
 *
 */
public class TextJSONMessageBuilder extends JSONMessageBuilder{

	public TextJSONMessageBuilder() {
		super();
		set("msgtype","image");
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
