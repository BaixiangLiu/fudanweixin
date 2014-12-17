package edu.fudan.weixin.model.message;

import java.util.HashMap;

/**
 * 客服图片消息
 * @author wking
 *
 */
public class ImageJSONMessageBuilder extends JSONMessageBuilder {

	public ImageJSONMessageBuilder() {
		super();
	}
	/**
	 * @see ImageMessageBuilder#setImage(Object)
	 * @param mediaid
	 */
	public void setContent(Object mediaid)
	{
		HashMap<String,Object> img=new HashMap<String,Object>();
		img.put("media_id", mediaid);
		set("image",img);
		set("msgtype","image");
	}

}
