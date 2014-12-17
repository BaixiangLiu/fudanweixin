package edu.fudan.weixin.model.message;

import java.util.Map;

public interface MessageBuilder {

	public void setContent(Object text);
	public Map<String,Object> getMessage();
	public void setMessage(Map<String,Object> message) ;
	public void setFrom(Object value);
	public void setTo(Object value);
	public void set(String key,Object value);
}
