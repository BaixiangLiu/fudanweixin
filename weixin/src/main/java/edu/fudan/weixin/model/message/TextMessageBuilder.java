package edu.fudan.weixin.model.message;

import java.util.HashMap;
import java.util.Map;

/**
 * 生成返回微信服务器的消息 对象
 * @author wking
 *
 */
public class TextMessageBuilder implements MessageBuilder {
	
	protected Map<String,Object> message;
	
	public TextMessageBuilder()
	{
		message=new HashMap<String,Object>();		
	}
	/**
	 * 设置值属性对
	 * @param key 属性名
	 * @param value 值
	 */
	public void set(String key,Object value)
	{
		message.put(key, value);
	}
	/**
	 * 设置收件的openid
	 * @param value
	 */
	public void setTo(Object value)
	{
		message.put("ToUserName", value);
	}
	
	public void setFrom(Object value)
	{
		message.put("FromUserName",value);
	}
	/**
	 * 设置消息内容
	 * @param text
	 */
	public void setContent(Object text)
	{
		set("MsgType","text");
		set("Content",text);
	}
	/**
	 * 将map格式的message封装成消息
	 * 
	 */
	public void setMessage(Map<String,Object> message) {
		this.message = message;
	}
	
	/**
	 * 返回消息的内容，可以通过new BasicDBObject(message)转换为用于存储和生成XML的dbo对象
	 * @return
	 */
	public Map<String,Object> getMessage()
	{
		
		return this.message;
	}

}
