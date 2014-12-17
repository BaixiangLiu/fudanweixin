package edu.fudan.weixin;

import org.junit.Test;

import edu.fudan.eservice.common.utils.Config;

public class TestDB {
	
	@Test
	public void testconf()
	{
		Config conf=Config.getInstance();
		//conf.update("tac.scope", "username email department");
		//conf.update("tac.redirecturi", Config.getInstance().get("weixin.context")+"uisbinddo.act");
		//conf.update("tac.clientid", "869ea958-b26c-4944-bc6d-2b5acebd0596");
		//conf.update("tac.secret", ":urYR4&@B0De");
		//conf.update("tac.enckey", "601fa897b86fed0d2d654701ccf0737d");
		//conf.update("mail.user","09110240017");
		//conf.update("mail.pass", "wangbin");
		conf.update("bind.mail", "�װ����û�����á�\n ���Ѿ���΢������������΢�ź�%nickname%�븴����ѧUIS�˺�%uis%�İ󶨲������粻��������Ϊ��������ϵ������Ϣ�죬�绰:65643207��email:urp@fudan.edu.cn��\n ������ѧУ԰��Ϣ���칫��");
		//conf.update("weixin.help","����Ҫ���������<a href='http://baishitong.fudan.edu.cn/wiki/%E5%A4%8D%E6%97%A6%E4%BF%A1%E6%81%AF%E5%8A%9E%E5%BE%AE%E4%BF%A1%E5%85%AC%E4%BC%97%E5%8F%B7'>���߰���ҳ��</a>");
		//conf.update("thread.poolsize", 20);
	}

}
