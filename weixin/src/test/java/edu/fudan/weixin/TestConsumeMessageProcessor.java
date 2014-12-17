package edu.fudan.weixin;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import edu.fudan.weixin.model.processor.BalanceMessageProcessor;
import edu.fudan.weixin.model.processor.ConsumeMessageProcessor;
import edu.fudan.weixin.utils.WeixinMessageHelper;
public class TestConsumeMessageProcessor {
	
	@Test
	public void test() {
		ConsumeMessageProcessor p=new ConsumeMessageProcessor();
		Map<String,Object> msg;
		// msg=new HashMap<String,Object>();
		//msg.put("Content", "һ��ͨ���20140101");
		//msg.put("MsgType"	, "text");
		//msg.put("FromUserName", "oew-cuDIYJFk31yYJMFKAw6Iy1oU");
		//msg.put("FromUserName", "oew-cuP8H1G_B2a-Rx4q5_Z3TEeA");
		
		//System.out.println(WeixinMessageHelper.xml2str(WeixinMessageHelper.dbo2xml(p.process(msg))));
		
		msg=new HashMap<String,Object>();
		msg.put("MsgType", "event");
		msg.put("Event"	, "CLICK");
		msg.put("EventKey", "yktxf");
		//msg.put("FromUserName", "oew-cuDIYJFk31yYJMFKAw6Iy1oU");
		msg.put("FromUserName", "oew-cuP8H1G_B2a-Rx4q5_Z3TEeA");
		
		System.out.println(WeixinMessageHelper.xml2str(WeixinMessageHelper.dbo2xml(p.process(msg))));
	}

}
