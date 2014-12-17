package edu.fudan.weixin;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import edu.fudan.eservice.common.utils.CommonUtil;
import edu.fudan.weixin.utils.WeixinMessageHelper;

public class TestMessage {
	private static String urlstr="http://localhost:8080/weixin/message";
	private Map<String,Object> buildTxtMsg(String content)
	{
		Map<String,Object> ret=new HashMap<String,Object>();
		//ret.put("FromUserName","oew-cuDIYJFk31yYJMFKAw6Iy1oU");
		ret.put("FromUserName", "oew-cuP8H1G_B2a-Rx4q5_Z3TEeA");
		ret.put("MsgType", "text");
		ret.put("Content",content);
		return ret;
	}
	private Map<String,Object> buildEventMsg(String key)
	{
		Map<String,Object> ret=new HashMap<String,Object>();
		ret.put("FromUserName","oew-cuDIYJFk31yYJMFKAw6Iy1oU");
		ret.put("MsgType", "event");
		ret.put("Event", "CLICK");
		ret.put("EventKey", key);
		return ret;
	}
	
	
	public void testKB() throws Exception
	{
		Map<String,Object> msg=buildTxtMsg("kb��Ϣ��");
		System.out.println(CommonUtil.postWebRequest(urlstr, WeixinMessageHelper.xml2str(WeixinMessageHelper.dbo2xml(msg)).getBytes("utf-8"), "application/xml; charset=utf-8"));
	}
	//@Test
	public void testBalance() throws Exception
	{
		Map<String,Object> msg=buildTxtMsg("ykt");
		System.out.println(CommonUtil.postWebRequest(urlstr, WeixinMessageHelper.xml2str(WeixinMessageHelper.dbo2xml(msg)).getBytes("utf-8"), "application/xml; charset=utf-8"));
	}
	@Test
	public void testConsume() throws Exception
	{
		Map<String,Object> msg=buildTxtMsg("yktxf");
		System.out.println(CommonUtil.postWebRequest(urlstr, WeixinMessageHelper.xml2str(WeixinMessageHelper.dbo2xml(msg)).getBytes("utf-8"), "application/xml; charset=utf-8"));
		 msg=buildEventMsg("yktxf");
			System.out.println(CommonUtil.postWebRequest(urlstr, WeixinMessageHelper.xml2str(WeixinMessageHelper.dbo2xml(msg)).getBytes("utf-8"), "application/xml; charset=utf-8"));
	}

	@Test
	public void testScore() throws Exception
	{
		Map<String,Object> msg=buildTxtMsg("cj");
		System.out.println(CommonUtil.postWebRequest(urlstr, WeixinMessageHelper.xml2str(WeixinMessageHelper.dbo2xml(msg)).getBytes("utf-8"), "application/xml; charset=utf-8"));
	}
	
	@Test
	public void testLesson() throws Exception {
		Map<String,Object> msg = buildTxtMsg("ѡ�β�ѯ091");
		System.out.println(CommonUtil.postWebRequest(urlstr, WeixinMessageHelper.xml2str(WeixinMessageHelper.dbo2xml(msg)).getBytes("utf-8"), "application/xml; charset=utf-8"));
	}
	
	public void testschoolbus() throws Exception {
		Map<String,Object> msg = buildTxtMsg("У��jh");
		System.out.println(CommonUtil.postWebRequest(urlstr, WeixinMessageHelper.xml2str(WeixinMessageHelper.dbo2xml(msg)).getBytes("utf-8"), "application/xml; charset=utf-8"));
		
	}
}
