/**
* @Title: RetMsg.java
* @Description: TODO
* @author: Calvinyang
* @date: Oct 12, 2014 3:38:36 PM
* Copyright: Copyright (c) 2013
* @version: 1.0
*/
package edu.fudan.webclient.util;

/**
 * @author: Calvinyang
 * @Description: 前端回复错误相关
 * @date: Oct 12, 2014 3:38:36 PM
 */
public class RetMsg {
	/**
	 * 
	 * @author: Calvinyang
	 * @Description: 错误码
	 * @date: Oct 12, 2014 3:39:28 PM
	 */
	public interface ErrCode {
		int OK = 0;
		int GUEST = 1;
	}
	
	/**
	 * 错误信息
	 */
	private static String[] msgs = new String[] {
			"",
			"未登录或登录超时"
	};
	
	/**
	 * 
	* @Title: getMsg
	* @Description: 查询错误信息
	* @param errCode
	* @return
	 */
	public static String getMsg(int errCode) {
		return msgs[errCode];
	}
}
