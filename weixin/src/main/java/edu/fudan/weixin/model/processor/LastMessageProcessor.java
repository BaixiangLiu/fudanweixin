package edu.fudan.weixin.model.processor;

import java.util.Map;

import edu.fudan.eservice.common.utils.Config;
import edu.fudan.weixin.model.message.TextMessageBuilder;
import edu.fudan.weixin.utils.WeixinFollowerHelper;


/**
 * 生成一条纯文本回复
 * @author wking
 *
 */
public class LastMessageProcessor implements MessageProcessor {

	@Override
	public Map<String, Object> process(Map<String, Object> message) {
		TextMessageBuilder mb=new TextMessageBuilder();
		
		if("event".equalsIgnoreCase(message.get("MsgType").toString())&&"subscribe".equalsIgnoreCase(message.get("Event").toString())||"text".equalsIgnoreCase(message.get("MsgType").toString()))
		{
			mb.setContent( Config.getInstance().get("weixin.help"));
			
			WeixinFollowerHelper.FetchWeixinUserInfo(String.valueOf(message.get("FromUserName")));
		}
		if("voice".equalsIgnoreCase(String.valueOf(message.get("MsgType"))))
			mb.setContent("我暂时还听不懂您说的：“"+message.get("Recognition")+"”，我会好好学习的。/:,@f");
		if("event".equalsIgnoreCase(String.valueOf(message.get("MsgType")))&&"click".equalsIgnoreCase(String.valueOf(message.get("Event"))))
			mb.setContent("我们的攻城狮正在疯狂打造中，敬请期待。/::P");
		return mb.getMessage();
	}

}
