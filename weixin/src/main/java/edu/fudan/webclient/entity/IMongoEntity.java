/**
* @Title: IMongoEntity.java
* @Description: TODO
* @author: Calvinyang
* @date: Oct 12, 2014 12:18:33 PM
* Copyright: Copyright (c) 2013
* @version: 1.0
*/
package edu.fudan.webclient.entity;

import com.mongodb.DBObject;

/**
 * @author: Calvinyang
 * @Description: TODO
 * @date: Oct 12, 2014 12:18:33 PM
 */
public interface IMongoEntity {
	
	/**
	 * 
	* @Title: setId
	* @Description: 设置id
	* @return
	 */
	void setId(String id);
	
	/**
	 * 
	* @Title: getId
	* @Description: 获取id
	* @return
	 */
	String getId();
	
	/**
	 * 
	* @Title: getCollectionName
	* @Description: 该实体属于哪个集合
	* @return
	 */
	String getCollectionName();
	
	/**
	 * 
	* @Title: toDBObject
	* @Description: 对象转为mongo db对象
	* @return
	 */
	DBObject toDBObject();
	
	/**
	 * 
	* @Title: fromDBObject
	* @Description: mongo db对象转为实体对象
	* @param obj
	* @return
	 */
	IMongoEntity fromDBObject(DBObject obj);
	
}
