package edu.fudan.weixin.model;

import java.util.Random;

import edu.fudan.eservice.common.utils.Config;
import edu.fudan.eservice.common.utils.EncodeHelper;

public class WiscomPayModel {
	
	
	public static String formupDirecturl(String openid,String uisid)
	{
		Config conf=Config.getInstance();
		StringBuffer sb=new StringBuffer();
		sb.append("openId=");
		sb.append(openid);
		sb.append("&rand=");
		Random rand=new Random();
		for(int i=0;i<32;i++)
		{
			int c=rand.nextInt(62);
			if(c>=36)
				c+=61; 
			else	if(c>=10)
				c+=55;
			else
				c+=48;
			sb.append((char)c);
		}
		sb.append("&userId=");
		sb.append(uisid);
		String hash=EncodeHelper.hmac("SHA1"	, sb.toString()	, conf.get("wispay.key"));
		sb.append("&sign=");
		sb.append(hash);
		sb.insert(0, conf.get("wispay.url")+"?");
		return sb.toString();
	}

}
