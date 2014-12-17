package edu.fudan.weixin.model.message;

import java.util.HashMap;

/**
 * 客服音乐消息
 * @author wking
 *
 */
public class MusicJSONMessageBuilder extends JSONMessageBuilder{

	public MusicJSONMessageBuilder() {
		super();
		set("MsgType","music");
	}
	/**
	 * 
	 * @param mediaid
	 */
	public void setContent(Object mediaid)
	{
		HashMap<String,Object> music=new HashMap<String,Object>();
		music.put("media_id", mediaid);
		set("Music",music);
	}

}
