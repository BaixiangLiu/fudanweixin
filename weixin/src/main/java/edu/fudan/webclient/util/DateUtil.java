/**
* @Title: DateUtil.java
* @Description: TODO
* @author: Calvinyang
* @date: 2013-2-28 涓嬪崍4:52:34
* Copyright: Copyright (c) 2013
* @version: 1.0
*/
package edu.fudan.webclient.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: Calvinyang
 * @Description: TODO
 * @date: 2013-2-28 涓嬪崍4:52:34
 */
public class DateUtil {
	
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String DATE_FORMAT_NEW = "yyyy/MM/dd";
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String MERGE_FORMAT = "yyyyMMddHHmmss";
	
	/**
	 * @throws CloudException 
	 * 
	* @Title: format
	* @param: date
	* @param: format
	* @return: Date
	* @throws
	 */
	public static Date format(String date, String format) {
		try {
			return new SimpleDateFormat(format).parse(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @throws CloudException 
	* @Title: format
	* @param: date
	* @return: Date
	* @throws
	 */
	public static Date format(String date) {
		return format(date, DATE_FORMAT);
	}
	
	/**
	 * 
	* @Title: format
	* @param: date
	* @param: format
	* @return: String
	* @throws
	 */
	public static String format(Date date, String format) {
		return new SimpleDateFormat(format).format(date);
	}
	
	/**
	 * 
	* @Title: format
	* @param: date
	* @return: String
	* @throws
	 */
	public static String format(Date date) {
		return format(date, DATE_FORMAT);
	}
	
}
