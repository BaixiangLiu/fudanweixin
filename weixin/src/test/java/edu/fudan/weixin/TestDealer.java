package edu.fudan.weixin;

import java.io.IOException;
import java.io.StringReader;
import java.util.Date;

import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.junit.Test;

import com.mongodb.BasicDBObject;

import edu.fudan.eservice.common.utils.MongoUtil;
import edu.fudan.weixin.model.message.ImageJSONMessageBuilder;
import edu.fudan.weixin.model.message.TextMessageBuilder;
import edu.fudan.weixin.model.processor.LastMessageProcessor;
import edu.fudan.weixin.utils.WeixinMessageHelper;

public class TestDealer {
	
	@Test()
	public void test1()
	{
		TextMessageBuilder mb=new ImageJSONMessageBuilder();
		mb.setTo("SOMEONE");
		mb.setContent("media-id:234454555");
		System.out.println(WeixinMessageHelper.msg2jsonstr(mb.getMessage()));
	}
	
	
	public void test() throws JDOMException, IOException
	{
		String doc="<xml><ToUserName><![CDATA[gh_ef2c7ecd2009]]></ToUserName>"+
"<FromUserName><![CDATA[oew-cuP8H1G_B2a-Rx4q5_Z3TEeA]]></FromUserName>"+
"<CreateTime>1394539268</CreateTime>"+
"<MsgType><![CDATA[text]]></MsgType>"+
"<Content><![CDATA[ �Թ�˯����vg]]></Content>"+
"<MsgId>5989500549247835062</MsgId>"+
"</xml>";
		BasicDBObject obj=WeixinMessageHelper.xml2dbo(new SAXBuilder().build(new StringReader(doc)));
		System.out.println(obj);
		BasicDBObject dbo=new BasicDBObject(new LastMessageProcessor().process(obj));
		
		if(dbo!=null)
		{
			
			//�ظ��û�
			System.out.println(WeixinMessageHelper.xml2str(WeixinMessageHelper.dbo2xml(dbo)));
		}
	}

}
