package edu.fudan.weixin.model.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客服图文消息
 * @author wking
 *
 */
public class NewsXMLMessageBuilder extends TextMessageBuilder implements NewsMessageBuilder{

	private List<Map<String,Object>> articles;
	
	public NewsXMLMessageBuilder() {
		super();
		set("MsgType","news");
		articles=new ArrayList<Map<String,Object>>();
	}
	/**
	 * 
	 * @param news
	 */
	@SuppressWarnings("rawtypes")
	public void setContent(Object news)
	{
		if(news!=null)
		{
			
			if(news instanceof List)
			{
				set("ArticleCount",((List)news).size());
			}
			if(news.getClass().isArray())
			{
				set("ArticleCount",((Object[])news).length);
			}
			set("Articles", news);
		}
		else
		{
			
			set("ArticleCount",articles.size());
			
			set("Articles", articles);
			
		}
		
	}
	
	public void addArticle(String title,String description, String url, String picurl)
	{
		Map<String,Object> article=new HashMap<String,Object>();
		article.put("Title",title);
		article.put("Description", description);
		article.put("Url", url);
		article.put("PicUrl",picurl);
		articles.add(article);
		
	}
	@Override
	public int getCount() {
		return articles.size();
	}

}
