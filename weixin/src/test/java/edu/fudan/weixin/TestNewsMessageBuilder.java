package edu.fudan.weixin;

import org.junit.Test;

import edu.fudan.weixin.model.message.NewsXMLMessageBuilder;
import edu.fudan.weixin.utils.WeixinMessageHelper;

public class TestNewsMessageBuilder {

	
	@Test
	public void test()
	{
		NewsXMLMessageBuilder nmb=new NewsXMLMessageBuilder();
		for(int i=0;i<10;i++)
			nmb.addArticle("test"+i, "description"+i, "url"+i, "picurl"+i);
		nmb.setContent(null);
		System.out.println(WeixinMessageHelper.xml2str(WeixinMessageHelper.dbo2xml(nmb.getMessage())));
	}
}
