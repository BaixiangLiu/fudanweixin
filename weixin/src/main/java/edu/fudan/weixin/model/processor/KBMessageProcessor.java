package edu.fudan.weixin.model.processor;

import java.net.URLEncoder;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.fudan.eservice.common.utils.CommonUtil;
import edu.fudan.weixin.model.message.NewsJSONMessageBuilder;
import edu.fudan.weixin.model.message.NewsMessageBuilder;

/**
 * 知识库查询处理
 * @author wking
 *
 */
public class KBMessageProcessor extends LongTermProcessor {

	private Log log=LogFactory.getLog(KBMessageProcessor.class);

	private static Pattern p = Pattern.compile("(kb|bst|知识库|百事通)(.+)",
			Pattern.CASE_INSENSITIVE);
	
	public Map<String, Object> process(Map<String, Object> message) {
		String content=null;
			content = String.valueOf(message.get("Content"));
		if(!CommonUtil.isEmpty(content)&&p.matcher(content).matches())
			return super.process(message);
		else
			return null;
	}
	
	@Override
	public Map<String, Object> _process(Map<String, Object> message) {
		
				String content = String.valueOf(message.get("Content"));
			
				
				Matcher m = p.matcher(content.trim());
				if (m.matches()) {
					content = m.group(2);

					try {
						String urlstr = "http://baishitong.fudan.edu.cn/index.php?limit=10&fulltext=Search&search="
								+ URLEncoder.encode(content, "utf-8");
						
						StringBuffer ret=CommonUtil.getWebContent(urlstr);
						
						int pos=ret.indexOf("mw-search-result-heading");
						NewsMessageBuilder mb=new NewsJSONMessageBuilder();
						int atcount=0;
						while(pos>0&&atcount<10)
						{
							pos=ret.indexOf("href=", pos);
							String ttitle="",turl="",tcontent="";
							int pos2=-1;
							if(pos>0)
							{
								pos=ret.indexOf("\"",pos+1);
								pos2=ret.indexOf("\"",pos+1);
								turl="http://baishitong.fudan.edu.cn"+ret.substring(pos+1,pos2 );
							}
							pos=ret.indexOf("title=", pos2);
							if(pos>0)
							{
								pos=ret.indexOf("\"",pos+1);
								pos2=ret.indexOf("\"",pos+1);
								ttitle=ret.substring(pos+1, pos2);
							}
							pos=ret.indexOf("searchresult",pos2);	
							if(pos>0)
							{
								pos=ret.indexOf(">",pos+1);
								pos2=ret.indexOf("</div>",pos+1);
								tcontent=ret.substring(pos+1, pos2);
								tcontent=tcontent.replaceAll("(<.+>|\\[|\\])", "");
							}
							
							atcount++;
							mb.addArticle(ttitle, tcontent, turl, "");
							 pos=ret.indexOf("mw-search-result-heading",pos2);
						}
					if(atcount==0)
						mb.addArticle("没有查询到相关内容", "Nothing was found", "", "");
					mb.setContent(null);
					return mb.getMessage();

					} catch (Exception ex) {
						log.error(ex);
					}
				}
		
	
		return null;
	}

}
