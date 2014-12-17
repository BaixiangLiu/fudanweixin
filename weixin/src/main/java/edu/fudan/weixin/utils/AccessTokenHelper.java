package edu.fudan.weixin.utils;

import java.net.URLEncoder;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import edu.fudan.eservice.common.utils.CommonUtil;
import edu.fudan.eservice.common.utils.Config;
import edu.fudan.eservice.common.utils.ThreadPoolHelper;

/**
 * Access Token工具类，过期时间前20s可自动重新获取。
 * 
 * @author wking
 * 
 */
public class AccessTokenHelper {

	public static final int WEIXIN = 0;
	public static final int TAC = 1;
	private static AccessTokenHelper instance = null;
	private ScheduledFuture<?>[] schfuture;

	protected AccessTokenHelper() {
		Config conf = Config.getInstance();
		this.schfuture = new ScheduledFuture[2];
		
			long exp =0;
			try{
			exp=Long.parseLong(conf.get("weixin.token_expires"));
			}catch(Exception e){}
			String tk= conf.get("weixin.access_token");
			if (exp > (System.currentTimeMillis() + 20000)&&!CommonUtil.isEmpty(tk)) {				
				schfuture[WEIXIN] = ThreadPoolHelper
						.getInstance()
						.getSchPool()
						.schedule(new TokenFetcher(WEIXIN),
								exp - 20000 - System.currentTimeMillis(),
								TimeUnit.MILLISECONDS);
			}else
			{
				refetch(WEIXIN);
			}
			
			exp=0;
			try{
			exp = Long.parseLong(conf.get("tac.token_expires"));
			}catch(Exception e){}
			tk=  conf.get("tac.access_token");
			if (exp > (System.currentTimeMillis() + 20000)&&!CommonUtil.isEmpty(tk)) {
				
				schfuture[TAC] = ThreadPoolHelper
						.getInstance()
						.getSchPool()
						.schedule(new TokenFetcher(TAC),
								exp - 20000 - System.currentTimeMillis(),
								TimeUnit.MILLISECONDS);
			}else
			{
				refetch(TAC);
			}

		
	}

	/**
	 * 获取单件实例
	 * 
	 * @return
	 */
	public synchronized static AccessTokenHelper getInstance() {
		if (instance == null)
			instance = new AccessTokenHelper();
		return instance;
	}

	/**
	 * 获取当前有效的token
	 * 
	 * @return
	 */
	public String getToken(int authserver) {
		String tk="";
		long exp=0;
		Config conf=Config.getInstance();
		if(authserver==WEIXIN){
			tk=conf.get("weixin.access_token");
			exp = Long.parseLong(conf.get("weixin.token_expires"));}
		else{
			tk=Config.getInstance().get("tac.access_token");
			exp = Long.parseLong(conf.get("tac.token_expires"));}
		if (CommonUtil.isEmpty(tk)||exp<=(System.currentTimeMillis() + 20) ){
			refetch(authserver);
			if(authserver==WEIXIN)
				tk=Config.getInstance().get("weixin.access_token");
			else
				tk=Config.getInstance().get("tac.access_token");
		}
		return tk;
	}

	/**
	 * 重新获取access_token 只有当确定当前的token无效时才需要调用。 微信限制每天调用次数，绝不可随意调用。
	 */
	public void refetch(int authserver) {
		new TokenFetcher(authserver).run();
	}

	/**
	 * 能过HTTPS协议获取Access_token，在过期前20s会自动重新获取
	 * 
	 * @author wking
	 * 
	 */
	public class TokenFetcher implements Runnable {
		private int authserver;
		private Log log = LogFactory.getLog(TokenFetcher.class);

		public TokenFetcher(int authserver) {
			this.authserver = authserver;
		}

		public void run() {
			Config conf = Config.getInstance();

			String urlstr;
			try {
				if (this.authserver == WEIXIN)
					urlstr = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
							+ conf.get("weixin.appid")
							+ "&secret="
							+ conf.get("weixin.secret");
				else
					urlstr = "https://tac.fudan.edu.cn/oauth2/token.act?client_id="
							+ conf.get("tac.clientid")
							+ "&client_secret="
							+ URLEncoder
									.encode(conf.get("tac.secret"), "utf-8")
							+ "&grant_type=client_credentials";

				String ret = CommonUtil.getWebContent(urlstr).toString();
				Object o = JSON.parse(ret);
				if (o instanceof DBObject) {
					DBObject dbo = (DBObject) o;
					int exp = Integer
							.parseInt(dbo.get("expires_in").toString());
					String tk= dbo.get("access_token").toString();
					if (this.authserver == WEIXIN) {
						conf.update("weixin.access_token", tk);
						conf.update("weixin.token_expires",
								System.currentTimeMillis() + 1000l * exp);
					} else {
						conf.update("tac.access_token", tk);
						conf.update("tac.token_expires",
								System.currentTimeMillis() + 1000l * exp);
					}
					// Token 到期前20s去取Token
					// 如果有其他的还没有执行的计划取token的任务，那就取消它
					if (schfuture[this.authserver] != null
							&& !schfuture[this.authserver].isDone())
						schfuture[this.authserver].cancel(true);
					schfuture[this.authserver] = ThreadPoolHelper
							.getInstance()
							.getSchPool()
							.schedule(new TokenFetcher(this.authserver),
									exp - 20, TimeUnit.SECONDS);

				} else {
					log.error("Access Token Fetch Failed:" + ret + " SERVER:"
							+ this.authserver);
				}
			} catch (Exception e) {
				log.error(e);
			}

		}
	}

}
