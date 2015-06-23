package edu.fudan.weixin;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import edu.fudan.eservice.common.utils.CommonUtil;
import edu.fudan.weixin.model.processor.ElectricMessageProcessor;
import edu.fudan.weixin.utils.WeixinMessageHelper;

public class TestMessage {
	private static String urlstr="http://localhost:8080/weixin/message";
	private Map<String,Object> buildTxtMsg(String content)
	{
		Map<String,Object> ret=new HashMap<String,Object>();		
		ret.put("FromUserName", "oew-cuP8H1G_B2a-Rx4q5_Z3TEeA");
		ret.put("MsgType", "text");
		ret.put("Content",content);
		return ret;
	}
	private Map<String,Object> buildEventMsg(String key)
	{
		Map<String,Object> ret=new HashMap<String,Object>();
		ret.put("FromUserName","oew-cuP8H1G_B2a-Rx4q5_Z3TEeA");
		ret.put("MsgType", "event");
		ret.put("Event", "CLICK");
		ret.put("EventKey", key);
		return ret;
	}
	
	@Test
	public void testElectric() throws Exception
	{
		Map<String,Object> msg=buildTxtMsg("电费");
		System.out.println(new ElectricMessageProcessor().process(msg));
		
	//	System.out.println(CommonUtil.postWebRequest(urlstr, WeixinMessageHelper.xml2str(WeixinMessageHelper.dbo2xml(msg)).getBytes("utf-8"), "application/xml; charset=utf-8"));

	}
	
	public void testPhoneyp() throws Exception
	{
		Map<String,Object> msg=buildTxtMsg("dh信息办");
		System.out.println(CommonUtil.postWebRequest(urlstr, WeixinMessageHelper.xml2str(WeixinMessageHelper.dbo2xml(msg)).getBytes("utf-8"), "application/xml; charset=utf-8"));
	}
	
	
	public void testKB() throws Exception
	{
		Map<String,Object> msg=buildTxtMsg("kb信息办");
		System.out.println(CommonUtil.postWebRequest(urlstr, WeixinMessageHelper.xml2str(WeixinMessageHelper.dbo2xml(msg)).getBytes("utf-8"), "application/xml; charset=utf-8"));
	}
	
	public void testBalance() throws Exception
	{
		Map<String,Object> msg=buildTxtMsg("ykt");
		System.out.println(CommonUtil.postWebRequest(urlstr, WeixinMessageHelper.xml2str(WeixinMessageHelper.dbo2xml(msg)).getBytes("utf-8"), "application/xml; charset=utf-8"));
	}
	
	public void testConsume() throws Exception
	{
		Map<String,Object> msg=buildTxtMsg("yktxf");
		System.out.println(CommonUtil.postWebRequest(urlstr, WeixinMessageHelper.xml2str(WeixinMessageHelper.dbo2xml(msg)).getBytes("utf-8"), "application/xml; charset=utf-8"));
		 msg=buildEventMsg("yktxf");
			System.out.println(CommonUtil.postWebRequest(urlstr, WeixinMessageHelper.xml2str(WeixinMessageHelper.dbo2xml(msg)).getBytes("utf-8"), "application/xml; charset=utf-8"));
	}

	
	public void testScore() throws Exception
	{
		Map<String,Object> msg=buildTxtMsg("cj");
		System.out.println(CommonUtil.postWebRequest(urlstr, WeixinMessageHelper.xml2str(WeixinMessageHelper.dbo2xml(msg)).getBytes("utf-8"), "application/xml; charset=utf-8"));
	}
	
	
	public void testLesson() throws Exception {
		Map<String,Object> msg = buildTxtMsg("课程091");
		System.out.println(CommonUtil.postWebRequest(urlstr, WeixinMessageHelper.xml2str(WeixinMessageHelper.dbo2xml(msg)).getBytes("utf-8"), "application/xml; charset=utf-8"));
	}
	
	public void testschoolbus() throws Exception {
		Map<String,Object> msg = buildTxtMsg("校车jh");
		System.out.println(CommonUtil.postWebRequest(urlstr, WeixinMessageHelper.xml2str(WeixinMessageHelper.dbo2xml(msg)).getBytes("utf-8"), "application/xml; charset=utf-8"));
		
	}
}
