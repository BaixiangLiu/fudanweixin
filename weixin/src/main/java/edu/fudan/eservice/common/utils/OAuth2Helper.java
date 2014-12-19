package edu.fudan.eservice.common.utils;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mongodb.util.JSON;

public class OAuth2Helper {
	private static Log log=LogFactory.getLog(OAuth2Helper.class);
	
	/**
	 * 获取access_token
	 * 
	 * @param code
	 * @param isrefresh
	 *            是否通过refresh_token获取
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getToken(String code, boolean isrefresh) {
		Config conf = Config.getInstance();
		try {
			String urlstr = conf.get("tac.tokenurl")+"?client_id="
					+ conf.get("tac.clientid")
					+ "&client_secret="
					+ URLEncoder.encode(conf.get("tac.secret"), "utf-8");
			if (isrefresh) {
				urlstr += "&grant_type=refresh_token&refresh_token=" + code;
			} else {
				urlstr += "&grant_type=authorization_code&code="
						+ code
						+ "&redirect_uri="
						+ URLEncoder.encode(conf.get("tac.redirecturi"),
								"utf-8");
			}
			String ret;

			ret = CommonUtil.getWebContent(urlstr).toString();
			Map<String, Object> retobj = (Map<String, Object>) JSON.parse(ret);
			// if(!CommonUtil.isEmpty(retobj.get("access_token")))
			return retobj;
		} catch (Exception e) {
			log.error(e);
		}
		return new HashMap<String, Object>();

	}


	/**
	 * 废除access_token，即解绑
	 * 
	 * @param access_token
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> revokeToken(String access_token) {
		Config conf = Config.getInstance();
		try {
			String urlstr = conf.get("tac.tokenurl")+"?client_id="
					+ conf.get("tac.clientid")
					+ "&client_secret="
					+ URLEncoder.encode(conf.get("tac.secret"), "utf-8");

			urlstr += "&grant_type=revoke_token&access_token=" + access_token;

			String ret;

			ret = CommonUtil.getWebContent(urlstr).toString();
			Map<String, Object> retobj = (Map<String, Object>) JSON.parse(ret);
			return retobj;
		} catch (Exception e) {
			log.error(e);
		}
		return new HashMap<String, Object>();
	}

}
