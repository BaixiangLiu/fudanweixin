package edu.fudan.weixin.model.message;

import java.util.HashMap;
/**
 * 返回微信消息调用的被动图片消息
 * @author wking
 *
 */
public class ImageMessageBuilder extends TextMessageBuilder {

	public ImageMessageBuilder()
	{
		super();
		message.put("MsgType", "image");
	}
	/**
	 * 设置图片的mediaid，如果该图片使用超过三天，需要重新上传到微信服务器以获得新的mediaid
	 * @param mediaid
	 */
	public void setContent(Object mediaid)
	{
		HashMap<String,Object> img=new HashMap<String,Object>();
		img.put("MediaId", mediaid);
		set("Image",img);
	}
}
