package edu.fudan.weixin;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

import edu.fudan.eservice.common.utils.MongoUtil;

public class InitConfig {
	/**
	 * 初始化数据库中的配置
	 */
	public void initConfig()
	{
		
		
		DBCollection c=MongoUtil.getInstance().getCollection("Config");
		//微信公众号的ID，如fduxxb
		c.save(new BasicDBObject("key","weixin.id").append("value",""));
		//微信公众号开发接口的应用ID
		c.save(new BasicDBObject("key","weixin.appid").append("value",""));
		//微信公众号的应用密码
		c.save(new BasicDBObject("key","weixin.secret").append("value",""));
		//微信公众号的接口令牌
		c.save(new BasicDBObject("key","weixin.token").append("value",""));
		//微信公众号加解密消息的AESkey
		c.save(new BasicDBObject("key","weixin.aeskey").append("value",""));
		//微信应用部署的上下文地址,如http://xxb.weixin.fudan.edu.cn/xxb/
		c.save(new BasicDBObject("key","weixin.context").append("value",""));
		//微信的默认回复消息
		c.save(new BasicDBObject("key","weixin.help").append("value",""));
		//微信的默认处理消息对象，以逗号分隔的队列
		c.save(new BasicDBObject("key","weixin.processors").append("value",""));
		//账号绑定的通知邮件内容，%nickname%参数会换为用户的微信昵称，%uis%会换为绑定的账号，例如：
		//亲爱的用户，您好。
		// 您已经在微信软件中完成了微信号%nickname%与复旦大学UIS账号%uis%的绑定操作，如不是您本人行为，请速联系复旦信息办，电话:65643207，email:urp@fudan.edu.cn。
		 //复旦大学校园信息化办公室
		c.save(new BasicDBObject("key","bind.mail").append("value",""));
		//发送邮件SMTP服务器地址
		c.save(new BasicDBObject("key","mail.host").append("value",""));
		//邮件系统SMTP验证的用户名
		c.save(new BasicDBObject("key","mail.user").append("value",""));
		//邮件系统SMTP验证的口令
		c.save(new BasicDBObject("key","mail.pass").append("value",""));
		//邮件发信人的显示名
		c.save(new BasicDBObject("key","mail.display").append("value",""));
		
		//线程池的大小
		c.save(new BasicDBObject("key","thread.poolsize").append("value",""));
		
		//网页显示时的分页大小
		c.save(new BasicDBObject("key","query.pagesize").append("value",0));
		
		//账号绑定的入口 
		c.save(new BasicDBObject("key","uis.bindurl").append("value",""));
		//账号绑定的OAuth2.0 code地址
		c.save(new BasicDBObject("key","tac.codeurl").append("value",""));
		//账号绑定OAuth2.0 token地址
		c.save(new BasicDBObject("key","tac.tokenurl").append("value",""));
		//账号绑定OAuth2.0 客户端id
		c.save(new BasicDBObject("key","tac.clientid").append("value",""));
		//账号绑定OAuth2.0客户端口令
		c.save(new BasicDBObject("key","tac.secret").append("value",""));
		//账号绑定信息加密的AES密钥，小写16进制表示
		c.save(new BasicDBObject("key","tac.enckey").append("value",""));
		//账号绑定OAuth2.0的回调地址
		c.save(new BasicDBObject("key","tac.redirecturl").append("value",""));
		//账号绑定请求的scope
		c.save(new BasicDBObject("key","tac.scope").append("value",""));
		//账号绑定请求的用户信息url
		c.save(new BasicDBObject("key","tac.userinfourl").append("value",""));
		
		//Kafka接口地址
		c.save(new BasicDBObject("key","kafka.servers").append("value", ""));
		//Kafka处理消息线程数，建议与Kafka上监听的消息的Partition数一致
		c.save(new BasicDBObject("key","kafka.servers").append("value",2));
		
	}

}
