package edu.fudan.weixin.model.message;

import java.util.HashMap;
import java.util.Map;

import edu.fudan.eservice.common.utils.CommonUtil;

/**
 * 客服图片消息
 * @author wking
 *
 */
public class ImageJSONMessageBuilder extends JSONMessageBuilder {

	public TextMessageBuilder toXMLMessageBuilder()
	{
		ImageMessageBuilder text=new ImageMessageBuilder();
		Object textobj=message.get("image");
		if(!CommonUtil.isEmpty(textobj)&&textobj instanceof Map)
		{
			text.setContent(((Map)textobj).get("media_id"));
		}
		return text;
		
	}
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
