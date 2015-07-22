package edu.fudan.weixin.model;

import java.util.Random;

import edu.fudan.eservice.common.utils.CommonUtil;
import edu.fudan.eservice.common.utils.Config;
import edu.fudan.eservice.common.utils.EncodeHelper;

public class WiscomPayModel {
	
	/**
	 * �����ض��򵽽���֧��ƽ̨�����ӣ����û� ���д��������
	 * @param openid
	 * @param uisid
	 * @return
	 */
	public static String formupDirecturl(String openid,String uisid)
	{
		return formupDirecturl(openid,uisid,null);
	}
	
	
/**
 * �����ض��򵽽���֧��ƽ̨�����ӣ����pwd��Ϊ������ض������Ѷ�����Ϣ��
 * @param openid
 * @param uisid
 * @param pwd
 * @return
 */
	public static String formupDirecturl(String openid, String uisid, String pwd) {
		Config conf = Config.getInstance();
		StringBuffer sb = new StringBuffer();
		sb.append("openId=");
		sb.append(openid);
		if (!CommonUtil.isEmpty(pwd)) {
			sb.append("&pwd=");
			sb.append(pwd);
		}
		sb.append("&rand=");
		Random rand = new Random();
		for (int i = 0; i < 32; i++) {
			int c = rand.nextInt(62);
			if (c >= 36)
				c += 61;
			else if (c >= 10)
				c += 55;
			else
				c += 48;
			sb.append((char) c);
		}
		if (!CommonUtil.isEmpty(uisid)) {
			sb.append("&userId=");
			sb.append(uisid);
		}
		String hash = EncodeHelper.hmac("SHA1", sb.toString(), conf.get("wispay.key"));
		sb.append("&sign=");
		sb.append(hash);
		sb.insert(0, conf.get("wispay.url") + "?");
		return sb.toString();
	}

}
