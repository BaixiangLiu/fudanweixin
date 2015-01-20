package edu.fudan.weixin.utils;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mongodb.util.JSON;

import edu.fudan.eservice.common.utils.CommonUtil;
import edu.fudan.eservice.common.utils.Config;
import edu.fudan.weixin.utils.AccessTokenHelper;

public class TACOAuth2Helper {
	private static Log log = LogFactory.getLog(TACOAuth2Helper.class);

	/**
	 * 获取用户信息
	 * 
	 * @param accesstoken
	 * @return
	 */
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> fetchUser(String accesstoken) {
		String urlstr = Config.getInstance().get("tac.userinfourl")+"?access_token="
				+ accesstoken;
		try {
			Map<String, Object> ret = (Map<String, Object>) JSON
					.parse(CommonUtil.getWebContent(urlstr).toString());
			return ret;

		} catch (Exception e) {
			log.error(e);
		}
		return new HashMap<String, Object>();

	}

	/**
	 * 获取一卡通消费信息
	 * 
	 * @param accesstoken
	 * @param bdate
	 *            起始日期
	 * @param edate
	 *            结束日期 两个日期都不写取昨天的消费统计数据，写其中一个返回指定日期的消息统计数据
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> yktxf(String accesstoken, String bdate,
			String edate) {
		String urlstr = "https://tac.fudan.edu.cn/resource/yktxf.act?access_token="
				+ accesstoken + "&bdate=" + bdate + "&edate=" + edate;
		try {
			return (Map<String, Object>) JSON.parse("{\"list\":"
					+ CommonUtil.getWebContent(urlstr).toString() + "}");

		} catch (Exception e) {
			log.error(e);
		}
		return new HashMap<String, Object>();

	}

	/**
	 * 获取一卡通信息
	 * 
	 * @param accesstoken
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> yktxx(String accesstoken) {
		String urlstr = "https://tac.fudan.edu.cn/resource/yktxx.act?access_token="
				+ accesstoken;
		try {
			Map<String, Object> ret = (Map<String, Object>) JSON
					.parse(CommonUtil.getWebContent(urlstr).toString());
			return ret;

		} catch (Exception e) {
			log.error(e);
		}
		return new HashMap<String, Object>();

	}

	
	/**
	 * 获取学生成绩
	 * 
	 * @param accesstoken
	 * @param term
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> score(String accesstoken, String term) {

		String urlstr = "https://tac.fudan.edu.cn/resource/score.act?access_token="
				+ accesstoken + "&term=" + term;
		try {
			Map<String, Object> ret = (Map<String, Object>) JSON
					.parse("{\"list\":"
							+ CommonUtil.getWebContent(urlstr).toString() + "}");
			return ret;

		} catch (Exception e) {
			log.error(e);
		}
		return new HashMap<String, Object>();

	}
	
	
	/**
	 * 获取学生选课信息
	 * 
	 * @param accesstoken
	 * @param term
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> lesson(String accesstoken, String term) {

		String urlstr = "https://tac.fudan.edu.cn/resource/lesson.act?access_token="
				+ accesstoken + "&term=" + term;
		try {
			Map<String, Object> ret = (Map<String, Object>) JSON
					.parse("{\"list\":"
							+ CommonUtil.getWebContent(urlstr).toString() + "}");
			return ret;

		} catch (Exception e) {
			log.error(e);
		}
		return new HashMap<String, Object>();

	}

	/**
	 * 获取校车信息
	 * 
	 * @param accesstoken
	 * @param from
	 *            开车校区
	 * @param to
	 *            到达校区
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> schoolbus(String from, String to) {

		String urlstr = "https://tac.fudan.edu.cn/resource/schoolbus.act?access_token="
				+ AccessTokenHelper.getInstance().getToken(
						AccessTokenHelper.TAC) + "&from=" + from + "&to=" + to;
		try {
			Map<String, Object> ret = (Map<String, Object>) JSON
					.parse("{\"list\":"
							+ CommonUtil.getWebContent(urlstr).toString() + "}");
			return ret;

		} catch (Exception e) {
			log.error(e);
		}
		return new HashMap<String, Object>();

	}

	/**
	 * 获取电话黄页
	 * @param qkey
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> phoneyp(String qkey) {

		try {
			String urlstr = "https://tac.fudan.edu.cn/resource/phoneyp.act?access_token="
					+ AccessTokenHelper.getInstance().getToken(
							AccessTokenHelper.TAC)
					+ "&qkey="
					+ URLEncoder.encode(qkey, "utf-8");

			Map<String, Object> ret = (Map<String, Object>) JSON
					.parse("{\"list\":"
							+ CommonUtil.getWebContent(urlstr).toString() + "}");
			return ret;

		} catch (Exception e) {
			log.error(e);
		}
		return new HashMap<String, Object>();

	}
	/**
	 * 获取学期信息
	 * @param term
	 * @param date
	 * @param week
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> schoolterm(String term,String date,String week) {

		try {
			String urlstr = "https://tac.fudan.edu.cn/resource/schoolterm.act?access_token="
					+ AccessTokenHelper.getInstance().getToken(
							AccessTokenHelper.TAC)
					+ "&term="
					+ term+"&date="+date+"&week="+week;

			Map<String, Object> ret = (Map<String, Object>) JSON
					.parse(CommonUtil.getWebContent(urlstr).toString());
			return ret;

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
	public static Map<String, Object> revokeToken(String access_token) {
		Config conf = Config.getInstance();
		try {
			String urlstr = "https://tac.fudan.edu.cn/oauth2/token.act?client_id="
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

	
	/*
	public static String startclass(int startclass) {
		String ret = "";
		switch (startclass) {
		case 1:ret+="8:00";break;
		case 2:ret+="8:55";break;
		case 3:ret+="9:55";break;
		case 4:ret+="10:50";break;
		case 5:ret+="11:45";break;
		case 6:ret+="13:30";break;
		case 7:ret+="14:25";break;
		case 8:ret+="15:25";break;
		case 9:ret+="16:20";break;
		case 10:ret+="17:15";break;
		case 11:ret+="18:30";break;
		case 12:ret+="19:25";break;
		case 13:ret+="20:20";break;
		}
		return ret;
	}
	
	public static String endclass(int endclass) {
		String ret = "";
		switch (endclass) {
		case 1:ret+="8:45";break;
		case 2:ret+="9:40";break;
		case 3:ret+="10:40";break;
		case 4:ret+="11:35";break;
		case 5:ret+="12:30";break;
		case 6:ret+="14:15";break;
		case 7:ret+="15:10";break;
		case 8:ret+="16:10";break;
		case 9:ret+="17:05";break;
		case 10:ret+="18:00";break;
		case 11:ret+="19:15";break;
		case 12:ret+="20:10";break;
		case 13:ret+="21:05";break;
		}
		return ret;
	}
	*/
	
	/**
	 * 生成上课时间
	 * @param evenodd,startclass,endclass,weekday
	 * @return
	 */
	/*
	public static String classtime(int evenodd, int startclass, int endclass, int weekday) {
		String ret="";
		switch (evenodd) {
		case 0:ret+="每周";break;
		case 1:ret+="单周";break;
		case 2:ret+="双周";break;
		}
		switch (weekday) {
		case 0:ret+="日";break;
		case 1:ret+="一";break;
		case 2:ret+="二";break;
		case 3:ret+="三";break;
		case 4:ret+="四";break;
		case 5:ret+="五";break;
		case 6:ret+="六";break;
		}		
		return ret+startclass(startclass)+"-"+endclass(endclass);
	}*/
}
