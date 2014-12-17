package edu.fudan.weixin.utils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import edu.fudan.eservice.common.utils.CommonUtil;
import edu.fudan.eservice.common.utils.MongoUtil;
import edu.fudan.eservice.common.utils.ThreadPoolHelper;



public class WeixinFollowerHelper {
	
	private static Log log=LogFactory.getLog(WeixinFollowerHelper.class);
	
	private static WeixinFollowerHelper instance = null;
	
	private WeixinFollowerHelper(){
		refetch();
		ThreadPoolHelper.getInstance().getSchPool().scheduleAtFixedRate(new AutoFetchWeixinFollower(), 1800, 1800, TimeUnit.SECONDS);
	}
	
	
	//手动刷新
	public void refetch(){
		new AutoFetchWeixinFollower().run();
	}
	
	
	public static synchronized WeixinFollowerHelper getInstance(){
		if (instance == null)
			return new WeixinFollowerHelper();
		return instance;
	}
	
	
	
	//获取用户资料（UnionID机制）
	public static void FetchWeixinUserInfo(String openid) {
		if (!CommonUtil.isEmpty(openid)){
			try {
				String urlstr="https://api.weixin.qq.com/cgi-bin/user/info?access_token="+AccessTokenHelper.getInstance().getToken(AccessTokenHelper.WEIXIN)
					+"&openid="+openid+"&lang=zh_CN";
				Object obj = JSON.parse(CommonUtil.getWebContent(urlstr).toString());
				if(obj instanceof DBObject)
				{
					DBObject dbo = (DBObject)obj;
					MongoUtil.getInstance().getDB().getCollection("weixinuser").
					update(new BasicDBObject("openid",openid), dbo, true, false);
					log.info("save user:" + obj.toString());
			
				}
				else{
					log.error(" Fetch " + openid + "'s info Failed:"+obj.toString());
				}
			}
			catch (Exception e){
				log.error(e.toString());
			}
			
		}
		else{
			log.error("openid is empty!");
		}
		
	}
	
	
	//将所有用户置为未关注状态*/
	public static void SetAllFollowersUnSubscribed() {
		BasicDBObject dbo = new BasicDBObject().append("$set",new BasicDBObject().append("subscribe", 0));
		MongoUtil.getInstance().getDB().getCollection("weixinuser").update(new BasicDBObject(),dbo,false,true);
		
	}


	public static void FetchAllWeixinFollowers(String next_openid) {
		
		String urlstr="https://api.weixin.qq.com/cgi-bin/user/get?access_token="+AccessTokenHelper.getInstance().getToken(AccessTokenHelper.WEIXIN);

		do {
			if (!CommonUtil.isEmpty(next_openid)) {
				urlstr+="&next_openid="+next_openid;
			}
			try {
				Object obj = JSON.parse(CommonUtil.getWebContent(urlstr).toString());
				if(obj instanceof DBObject){
					DBObject dbo= (DBObject)obj;
					if (dbo.containsField("errcode")){
						log.error("server returns error! "+dbo.toString());
							return;
					}
					//更新拉取到的信息
					if (dbo.containsField("data")) {
						Object datadbo = JSON.parse(dbo.get("data").toString());
						if (datadbo instanceof DBObject){
							@SuppressWarnings("unchecked")
							List<String> openids = (List<String>)((DBObject)datadbo).get("openid");
							for(String openid : openids){
							 	FetchWeixinUserInfo(openid);
							}
						}
					}
					next_openid = dbo.get("next_openid").toString();
				}
			//当next_openid非空时继续拉取
			}
			catch (Exception e){
				log.error(e.toString());
			}
		} while (!CommonUtil.isEmpty(next_openid));
	}
	
	
	public class AutoFetchWeixinFollower implements Runnable {
		
		public void run() {
			SetAllFollowersUnSubscribed();
			FetchAllWeixinFollowers("");
		}
		
	}
	
}
