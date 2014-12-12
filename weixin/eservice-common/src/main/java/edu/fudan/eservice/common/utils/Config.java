package edu.fudan.eservice.common.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * 从数据库中同步Config数据，每分钟从数据库中更新
 * @author wking
 *
 */
public class Config {
	
	private static Config mc=null;
	Map<String,Object> props;
	Config()
	{
		
			props=new HashMap<String,Object>();
			reload();
			ThreadPoolHelper.getInstance().getSchPool().scheduleAtFixedRate(new DBConfigLoader(), 20, 20, TimeUnit.SECONDS);
		
	}
	/**
	 * 手动重载，一般不需要
	 */
	public void reload()
	{
		new DBConfigLoader().run();
	}
	/**
	 * 取对象
	 * @return
	 */
	public static synchronized Config getInstance()
	{
		if(mc==null)
			mc=new Config();
		return mc;
	}
	
	/**
	 * 
	* @Title: getLong
	* @Description: 取配置值并转换为long型
	* @param key
	* @return
	 */
	public int getInt(String key) {
	 	String value = get(key);
	 	return StringUtils.isEmpty(value) ? 0 : Integer.parseInt(value);
	}
	
	public String get(String key)
	{
		Object v=props.get(key);
		if(v!=null)
		return String.valueOf(v);
		else
			return null;
	}
	/**
	 * 更新配置项
	 * @param key
	 * @param value
	 */
	public void update(String key,Object value)
	{
		DBCollection dbc=MongoUtil.getInstance().getDB().getCollection("Config");
		DBObject q=new BasicDBObject("key",key);
		
		DBObject dbo=dbc.findOne(q);
			if(dbo==null)
				dbo=new BasicDBObject("key",key).append("value", value);
			else
				dbo.put("value", value);
		dbc.save(dbo);
			
	}
	/**
	 * 重新加载数据线程
	 * @author wking
	 *
	 */
	class DBConfigLoader implements Runnable
	{

		public void run() {
			DBCursor dbc=MongoUtil.getInstance().getDB().getCollection("Config").find();
			while(dbc.hasNext())
			{
				DBObject dbo=dbc.next();
				props.put(String.valueOf(dbo.get("key")),dbo.get("value"));
			}
			
		}
		
	}

}
