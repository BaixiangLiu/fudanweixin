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
public class NewsJSONMessageBuilder extends JSONMessageBuilder implements NewsMessageBuilder{

	private List<Map<String,String>> articles;
	
	public TextMessageBuilder toXMLMessageBuilder()
	{
		NewsXMLMessageBuilder builder=new NewsXMLMessageBuilder();
		for(Map<String,String> news:articles)
		{
			builder.addArticle(news.get("title"), news.get("description"), news.get("url"), news.get("picurl"));
		}
		builder.setContent(null);
		return builder;
	}
	
	public NewsJSONMessageBuilder() {
		super();
		set("msgtype","news");
		articles=new ArrayList<Map<String,String>>();
	}
	/**
	 * 
	 * @param news
	 */
	public void setContent(Object news)
	{
		
		//set("msgtype","news");
		if(news!=null)
			message.put("news", news);
		else
		{
			Map<String,Object> entry=new HashMap<String,Object>();
			entry.put("articles", articles);
			message.put("news", entry);
		}
		
	}
	
	public void addArticle(String title,String description, String url, String picurl)
	{
		Map<String,String> article=new HashMap<String,String>();
		article.put("title",title);
		article.put("description", description);
		article.put("url", url);
		article.put("picurl",picurl);
		articles.add(article);
	}
	@Override
	public int getCount() {
		return articles.size();
	}

}
