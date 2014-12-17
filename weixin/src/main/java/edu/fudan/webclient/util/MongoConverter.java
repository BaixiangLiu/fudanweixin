/**
* @Title: MongoConverter.java
* @Description: TODO
* @author: Calvinyang
* @date: Oct 12, 2014 2:06:48 PM
* Copyright: Copyright (c) 2013
* @version: 1.0
*/
package edu.fudan.webclient.util;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.DBObject;

import edu.fudan.webclient.entity.IMongoEntity;

/**
 * @author: Calvinyang
 * @Description: mongo数据转换器
 * @date: Oct 12, 2014 2:06:48 PM
 */
public class MongoConverter {
	private DBObject obj;
	
	public MongoConverter(DBObject obj) {
		this.obj = obj;
	}
	
	/**
	 * 
	* @Title: getBoolean
	* @Description: 转为bool
	* @param key
	* @return
	 */
	public boolean getBoolean(String key) {
		Object value = obj.get(key);
		return value != null && value.toString().equals("1");
	}
	
	/**
	 * 
	* @Title: getString
	* @Description: 转为string
	* @param obj
	* @param key
	* @return
	 */
	public String getString(String key) {
		Object value = obj.get(key);
		return value == null ? null : value.toString();
	}
	
	/**
	 * 
	* @Title: getLong
	* @Description: 转为long
	* @param obj
	* @param key
	* @return
	 */
	public long getLong(String key) {
		Object value = obj.get(key);
		return value == null ? null : Long.parseLong(value.toString());
	}
	
	/**
	 * 
	* @Title: getList
	* @Description: 获取一个数组
	* @param key
	* @return
	 */
	@SuppressWarnings("unchecked")
	public List<IMongoEntity> getList(String key, Class<?> targetClass) {
		List<DBObject> list = (List<DBObject>) obj.get(key);
		List<IMongoEntity> ret = new ArrayList<IMongoEntity>();
		if (list != null && list.size() > 0) {
			for(int i = 0 ; i < list.size() ; i ++) {
				try {
					IMongoEntity entity = (IMongoEntity) targetClass.newInstance();
					entity.fromDBObject(list.get(i));
					ret.add(entity);
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return ret;
	}
}
