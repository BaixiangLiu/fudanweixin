package edu.fudan.weixin.model.message;

import java.util.HashMap;

/**
 * 客服视频消息
 * @author wking
 *
 */
public class VideoJSONMessageBuilder extends JSONMessageBuilder{

	public VideoJSONMessageBuilder() {
		super();
		set("MsgType","video");
	}
	/**
	 * 
	 * @param mediaid
	 */
	public void setContent(Object mediaid)
	{
		HashMap<String,Object> video=new HashMap<String,Object>();
		video.put("media_id", mediaid);
		set("Video",video);
	}

}
