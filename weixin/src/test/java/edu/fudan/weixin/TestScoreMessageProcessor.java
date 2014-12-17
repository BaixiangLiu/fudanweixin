package edu.fudan.weixin;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import edu.fudan.weixin.model.processor.ConsumeMessageProcessor;
import edu.fudan.weixin.model.processor.ScoreMessageProcessor;
import edu.fudan.weixin.utils.WeixinMessageHelper;

public class TestScoreMessageProcessor {
	
	@Test
	public void test() {
		ScoreMessageProcessor p=new ScoreMessageProcessor();
		Map<String,Object> msg=new HashMap<String,Object>();
		msg.put("Content", "�ɼ���ѯ132");
		msg.put("MsgType"	, "text");
		//msg.put("FromUserName", "oew-cuDIYJFk31yYJMFKAw6Iy1oU");
		msg.put("FromUserName", "oew-cuP8H1G_B2a-Rx4q5_Z3TEeA");
		
		System.out.println(WeixinMessageHelper.xml2str(WeixinMessageHelper.dbo2xml(p.process(msg))));
		
		msg=new HashMap<String,Object>();
		msg.put("MsgType", "event");
		msg.put("Event"	, "CLICK");
		msg.put("EventKey", "score");
		msg.put("FromUserName", "oew-cuEhbVTNdQTR8kxzwbaCQEtU");
		//msg.put("FromUserName", "oew-cuP8H1G_B2a-Rx4q5_Z3TEeA");
		
		System.out.println(WeixinMessageHelper.xml2str(WeixinMessageHelper.dbo2xml(p.process(msg))));
	}

}
