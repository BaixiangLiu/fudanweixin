package edu.fudan.weixin.model.message;

public interface NewsMessageBuilder extends MessageBuilder {

	public void addArticle(String title,String description, String url, String picurl);
}
