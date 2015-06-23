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
 * 电费每日余额提醒
 * 
 * @author wking
 *
 */
public class ElectricBalanceThread implements Runnable {

	private static Log log = LogFactory.getLog(ElectricBalanceThread.class);

	@Override
	public void run() {

		DBObject msg = new BasicDBObject(
				"remark", "本提醒不作为账户凭证，最终余额以电费充值系统为准。");
		DB db = MongoUtil.getInstance().getDB();
		DBCursor cs = db.getCollection("Books").find(
				new BasicDBObject("book", true).append("item",
						"electric_balance"));
		TACOAuth2Model m = new TACOAuth2Model();
		while (cs.hasNext()) {
			String openid = String.valueOf(cs.next().get("openid"));
			DBObject user = db.getCollection("Bindings").findOne(
					new BasicDBObject("openid", openid));
			if (user != null && user.get("binds") != null
					&& user.get("binds") instanceof List) {
				BasicDBList l = m.electric(user);
				for (Object obj : l) {
					if (obj != null && obj instanceof DBObject) {
						Object ol = ((DBObject) obj).get("list");
						if (ol != null && ol instanceof List) {
							List ll = (List) ol;
							for (Object oo : ll) {
								if (oo != null && oo instanceof DBObject) {
									DBObject dbo = (DBObject) oo;
									Object balance = dbo.get("canuse");
									if (balance != null) {
										msg.put("first", "截止到"+dbo.get("elecdate")+"您的电量余额信息：");
										msg.put("keynote1",
												dbo.get("person_name") + "("
														+ dbo.get("username")
														+ ")");
										msg.put("keynote2",
												String.valueOf(dbo
														.get("school_space"))
														+ dbo.get("room"));
										msg.put("keynote3", balance + "度");
										log.info(dbo.get("username")
												+ "--"
												+ TemplateMessage.send(
														"electric_balance",
														openid, msg));
									}
								}
							}
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
