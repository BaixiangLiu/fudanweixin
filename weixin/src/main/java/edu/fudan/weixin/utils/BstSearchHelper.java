/**
* @Title: BstSearchHelper.java
* @Description: TODO
* @author: Calvinyang
* @date: Dec 22, 2014 4:40:33 PM
* Copyright: Copyright (c) 2013
* @version: 1.0
*/
package edu.fudan.weixin.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.fudan.eservice.common.utils.CommonUtil;
import edu.fudan.weixin.model.message.NewsJSONMessageBuilder;
import edu.fudan.weixin.model.message.NewsMessageBuilder;

/**
 * @author: Calvinyang
 * @Description: TODO
 * @date: Dec 22, 2014 4:40:33 PM
 */
public class BstSearchHelper {
	private static Log log = LogFactory.getLog(BstSearchHelper.class);
	
	/**
	 * 
	* @Title: getNewsForKeywords
	* @Description: 根据指定关键词搜索百事通 并将结果解析为图文列表数据
	* @param keywords
	* @return
	 */
	public static NewsMessageBuilder getNewsForKeywords(String keywords) {
		String urlstr = null;
		try {
			urlstr = "http://baishitong.fudan.edu.cn/index.php?limit=10&fulltext=Search&search=" + URLEncoder.encode(keywords, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return null;
		}

		StringBuffer ret = null;
		try {
			ret = CommonUtil.getWebContent(urlstr);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return null;
		}

		int pos = ret.indexOf("mw-search-result-heading");
		NewsMessageBuilder mb = new NewsJSONMessageBuilder();
		int atcount = 0;
		while (pos > 0 && atcount < 10) {
			pos = ret.indexOf("href=", pos);
			String ttitle = "", turl = "", tcontent = "";
			int pos2 = -1;
			if (pos > 0) {
				pos = ret.indexOf("\"", pos + 1);
				pos2 = ret.indexOf("\"", pos + 1);
				turl = "http://baishitong.fudan.edu.cn" + ret.substring(pos + 1, pos2);
			}
			pos = ret.indexOf("title=", pos2);
			if (pos > 0) {
				pos = ret.indexOf("\"", pos + 1);
				pos2 = ret.indexOf("\"", pos + 1);
				ttitle = ret.substring(pos + 1, pos2);
			}
			pos = ret.indexOf("searchresult", pos2);
			if (pos > 0) {
				pos = ret.indexOf(">", pos + 1);
				pos2 = ret.indexOf("</div>", pos + 1);
				tcontent = ret.substring(pos + 1, pos2);
				tcontent = tcontent.replaceAll("(<.+>|\\[|\\])", "");
			}

			atcount++;
			mb.addArticle(ttitle, tcontent, turl, "");
			pos = ret.indexOf("mw-search-result-heading", pos2);
		}
		if (atcount == 0)
			mb.addArticle("没有查询到相关信息", "Nothing was found", "", "");
		mb.setContent(null);
		System.gc();
		return mb;
	}
}
