package edu.fudan.weixin.subscribe;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import edu.fudan.eservice.common.utils.MongoUtil;
import edu.fudan.weixin.model.TACOAuth2Model;
import edu.fudan.weixin.model.TemplateMessage;

/**
 * 发送每日余额提醒
 * @author wking
 *
 */
public class DailyBalanceThread implements Runnable {

	private static Log log=LogFactory.getLog(DailyBalanceThread.class);
	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		
		DBObject msg=new BasicDBObject("first","截止到当前您的一卡通余额信息：").append("remark", "本提醒不作为账户凭证，最终余额以一卡通系统为准。");
		DB db=MongoUtil.getInstance().getDB();
		DBCursor cs=db.getCollection("Books").find(new BasicDBObject("book",true).append("item", "ecard_balance"));
		TACOAuth2Model m=new TACOAuth2Model();
		while(cs.hasNext())
		{
			String openid=String.valueOf(cs.next().get("openid"));
			DBObject user=db.getCollection("Bindings").findOne(new BasicDBObject("openid",openid));
			if(user!=null &&user.get("binds")!=null&&user.get("binds") instanceof List)
			{	
				BasicDBList l=m.yktxx(user);
				for(Object obj:l)
				{
					if(obj!=null &&obj instanceof DBObject)
					{
						DBObject dbo=(DBObject)obj;
						Object balance=dbo.get("card_balance");
						if(balance!=null)
						{
							msg.put("name", dbo.get("username")+"("+dbo.get("uisid")+")");
							msg.put("money",balance+"元");
							log.info(dbo.get("uisid")+"--"+TemplateMessage.send("ecard_balance", openid, msg));
						}
					}
				}
				
			}
			try {
				Thread.sleep(100L);
			} catch (InterruptedException e) {
				break;
			}
			
		}
		

	}

}
