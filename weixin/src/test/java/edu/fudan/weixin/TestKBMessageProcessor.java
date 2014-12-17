package edu.fudan.weixin;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import edu.fudan.weixin.model.processor.KBMessageProcessor;
import edu.fudan.weixin.utils.WeixinMessageHelper;

public class TestKBMessageProcessor {
	
	@Test()
	public void test()
	{
		KBMessageProcessor p=new KBMessageProcessor();
		Map<String,Object> msg=new HashMap<String,Object>();
		msg.put("Content", "kb:��Ϣ��");
		msg.put("MsgType"	, "text");
		
		System.out.println(WeixinMessageHelper.xml2str(WeixinMessageHelper.dbo2xml(p.process(msg))));
	}

}
