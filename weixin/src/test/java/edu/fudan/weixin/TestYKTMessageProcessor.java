package edu.fudan.weixin;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import edu.fudan.weixin.model.processor.BalanceMessageProcessor;
import edu.fudan.weixin.utils.WeixinMessageHelper;

public class TestYKTMessageProcessor {

	@Test
	public void test()
	{
		BalanceMessageProcessor p=new BalanceMessageProcessor();
		Map<String,Object> msg=new HashMap<String,Object>();
		msg.put("Content", "ecard");
		msg.put("MsgType"	, "text");
		//msg.put("FromUserName", "oew-cuDIYJFk31yYJMFKAw6Iy1oU");
		msg.put("FromUserName", "oew-cuP8H1G_B2a-Rx4q5_Z3TEeA");
		
		System.out.println(WeixinMessageHelper.xml2str(WeixinMessageHelper.dbo2xml(p.process(msg))));
	}
}
