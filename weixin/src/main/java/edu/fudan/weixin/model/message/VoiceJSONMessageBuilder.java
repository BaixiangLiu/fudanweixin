package edu.fudan.weixin.model.message;

import java.util.HashMap;

/**
 * 客服图片消息
 * @author wking
 *
 */
public class VoiceJSONMessageBuilder extends JSONMessageBuilder{

	public VoiceJSONMessageBuilder() {
		super();
		set("MsgType","voice");
	}
	/**
	 * 
	 * @param mediaid
	 */
	public void setContent(Object mediaid)
	{
		HashMap<String,Object> voice=new HashMap<String,Object>();
		voice.put("media_id", mediaid);
		set("Voice",voice);
	}

}
