package edu.fudan.weixin.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import edu.fudan.eservice.common.utils.CommonUtil;
import edu.fudan.eservice.common.utils.MongoUtil;
import edu.fudan.eservice.common.utils.OAuth2Helper;
import edu.fudan.weixin.utils.TACOAuth2Helper;


public class TACOAuth2Model {

	/**
	 * 包装TACOAuth2Helper中的方法，当access_token过期的时候自动通过refresh_token重新获取
	 * 
	 * @param binding
	 *            用户的绑定信息
	 * @param method
	 *            调用的方法
	 * @param args
	 *            参数
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings("unchecked")
	public BasicDBList fetchResource(DBObject binding, Method method,
			Object... args) throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		BasicDBList ret = new BasicDBList();
		boolean changed = false;
		if (!CommonUtil.isEmpty(binding)
				&& !CommonUtil.isEmpty(binding.get("binds"))) {
			for (DBObject dbo : (List<DBObject>) binding.get("binds")) {
				Object[] narg = new Object[args.length + 1];
				narg[0] = dbo.get("uistoken");
				if (args.length >= 1)
					for (int i = 1; i < narg.length; i++) {
						narg[i] = args[i - 1];
					}
				Object robj=null;
				if (!CommonUtil.isEmpty(dbo.get("uistoken"))
						&& ((Number) dbo.get("uisexpired")).longValue() - 60000 > System
								.currentTimeMillis()) {
					robj=method.invoke(null, narg);					
					
				} else if (!CommonUtil.isEmpty(dbo.get("uisrefresh"))) {
					Map<String, Object> newtk = OAuth2Helper.getToken(dbo
							.get("uisrefresh").toString(), true);
					if (newtk != null) {
						if (!CommonUtil.isEmpty(newtk.get("access_token"))) {
							dbo.put("uistoken", newtk.get("access_token"));
							dbo.put("uisrefresh", newtk.get("refresh_token"));
							dbo.put("uisexpired", System.currentTimeMillis()
									+ 1000 * (int) newtk.get("expires_in"));
							changed = true;
							// MongoUtil.getInstance().getDB().getCollection("Bindings").save(binding);
							narg[0] = dbo.get("uistoken");
							robj=method.invoke(null, narg);
						}
					}
				}
				if(robj instanceof DBObject)
				{
					DBObject bobj;
					if(robj instanceof List)
					{
						bobj=new BasicDBObject();
						bobj.put("list", robj);
					}else
					bobj=(DBObject)robj;
					bobj.put("uisid", dbo.get("uisid"));
					bobj.put("username", dbo.get("username"));
					ret.add(bobj);
				}
			}
			if (changed)
				MongoUtil.getInstance().getDB().getCollection("Bindings")
						.save(binding);
		}
		return ret;
	}

	/**
	 * 获取用户信息
	 * 
	 * @param binding
	 * @return
	 */
	public BasicDBList fetchUserinfo(DBObject binding) {
		try {
			return fetchResource(binding,
					TACOAuth2Helper.class.getDeclaredMethod("fetchUser",
							String.class));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new BasicDBList();
	}

	/**
	 * 一卡通信息
	 * 
	 * @param binding
	 * @return
	 */
	public BasicDBList yktxx(DBObject binding) {
		try {
			return fetchResource(binding,
					TACOAuth2Helper.class.getDeclaredMethod("yktxx",
							String.class));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new BasicDBList();
	}

	/**
	 * 一卡通每日消费
	 * 
	 * @param binding
	 * @param bdate
	 * @param edate
	 * @return
	 */
	public BasicDBList yktxf(DBObject binding, String bdate, String edate) {
		try {
			return fetchResource(binding,
					TACOAuth2Helper.class.getDeclaredMethod("yktxf",
							String.class, String.class, String.class), bdate,
					edate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new BasicDBList();
	}

	/**
	 * 成绩
	 * 
	 * @param binding
	 * @param term
	 * @return
	 */
	public BasicDBList score(DBObject binding, String term) {
		try {
			return fetchResource(binding,
					TACOAuth2Helper.class.getDeclaredMethod("score",
							String.class, String.class), term);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new BasicDBList();

	}


	/**
	 * 解除绑定
	 * 
	 * @param binding
	 * @return
	 */
	public BasicDBList lesson(DBObject binding, String term) {
		try {
			return fetchResource(binding,
					TACOAuth2Helper.class.getDeclaredMethod("lesson",
							String.class, String.class), term);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new BasicDBList();
	}

	public BasicDBList revokeToken(DBObject binding) {
		try {
			return fetchResource(binding,
					TACOAuth2Helper.class.getDeclaredMethod("revokeToken",
							String.class));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new BasicDBList();
	}

	/**
	 * 获取token
	 * 
	 * @param code
	 * @param isrefresh
	 * @return
	 */
	public DBObject getToken(String code, boolean isrefresh) {

		return new BasicDBObject(OAuth2Helper.getToken(code, isrefresh));
	}
}
