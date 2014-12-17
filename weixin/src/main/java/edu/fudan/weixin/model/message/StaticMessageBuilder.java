package edu.fudan.weixin.model.message;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Random;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import edu.fudan.eservice.common.utils.Config;



public class StaticMessageBuilder {

	public static Map<String,Object> buildXMLAuthMessage()
	{
		return buildAuthMessage(new NewsXMLMessageBuilder());
	}
	
	public static Map<String,Object> buildJSONAuthMessage()
	{
		return buildAuthMessage(new NewsJSONMessageBuilder());
	}
	public static Map<String,Object> buildAuthMessage(NewsMessageBuilder builder)
	{
		
		Config conf=Config.getInstance();
		long st=new Random().nextLong(); 
		//放进一个使用EhCache维护的容器，当用户从微信的OAuth2.0拿到code后检查这个链接是不是由此链接生成的。
		CacheManager.getInstance().getCache("WXStates").put(new Element(String.valueOf(st),st));
		try {
			builder.addArticle("UIS账号绑定", "通过OAuth2.0接口将您的微信账号与复旦大学UIS账号进行绑定，以获取个性化信息服务。\n请点击查看全文并在打开的页面中输入学/工号和UIS口令，并完成授权操作。",
					"https://open.weixin.qq.com/connect/oauth2/authorize?appid="+conf.get("weixin.appid")+"&redirect_uri="+URLEncoder.encode(conf.get("uis.bindurl"),"utf-8")+"&response_type=code&scope=snsapi_base&state="+st+"#wechat_redirect",
					conf.get("weixin.context")+"images/uis.gif");
		} catch (UnsupportedEncodingException e) {
			
			e.printStackTrace();
		}
		builder.setContent(null);
		return builder.getMessage();
	}
	
	public static Map<String,Object> buildXMLScopeMessage(String scope)
	{
		return buildScopeMessage(new NewsXMLMessageBuilder(),scope);
	}
	
	public static Map<String,Object> buildJSONScopeMessage(String scope)
	{
		return buildScopeMessage(new NewsJSONMessageBuilder(),scope);
	}
	
	public static String buildScopeUrl()
	{
		Config conf=Config.getInstance();	
		try {
			return "https://tac.fudan.edu.cn/oauth2/auth_input.act?client.id="+conf.get("tac.clientid")+"&scope="+URLEncoder.encode(conf.get("tac.scope"),"utf-8");
		} catch (UnsupportedEncodingException e) {
			return "https://tac.fudan.edu.cn/oauth2/auth_input.act?client.id="+conf.get("tac.clientid");
		}
	}
	public static Map<String,Object> buildScopeMessage(NewsMessageBuilder builder,String scope)
	{
		
			

			builder.addArticle("需要增加授权", "您要访问的信息需要向复旦信息办微信公众号授权获取您的"+(scope==null?"更多":scope)+"权限。\n请点击查看全文并完成所需权限的授权。",
					buildScopeUrl(),
					"");
		
		builder.setContent(null);
		return builder.getMessage();
	}
}
