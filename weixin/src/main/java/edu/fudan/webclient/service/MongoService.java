/**
* @Title: MongoService.java
* @Description: TODO
* @author: Calvinyang
* @date: Oct 12, 2014 12:19:22 PM
* Copyright: Copyright (c) 2013
* @version: 1.0
*/
package edu.fudan.webclient.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import edu.fudan.eservice.common.utils.Config;
import edu.fudan.eservice.common.utils.MongoUtil;
import edu.fudan.webclient.entity.IMongoEntity;

/**
 * @author: Calvinyang
 * @Description: TODO
 * @date: Oct 12, 2014 12:19:22 PM
 */
public class MongoService {
	
	/**
	 * 禁止构造此类对象，全部方法为静态
	 */
	private MongoService() {}
	
	/**
	 * 
	* @Title: add
	* @Description: 添加记录
	* @param entity
	* @return
	 */
	public static void add(IMongoEntity entity) {
		DBCollection dbCollection = MongoUtil.getInstance().getCollection(entity.getCollectionName());
		dbCollection.insert(entity.toDBObject());
	}
	
	/**
	 * 
	* @Title: addList
	* @Description: 批量添加记录
	* @param list
	 */
	public static void addList(List<IMongoEntity> list) {
		if (list == null || list.isEmpty()) {
			return ;
		}
		DBCollection dbCollection = MongoUtil.getInstance().getCollection(list.get(0).getCollectionName());
		for(IMongoEntity entity : list) {
			dbCollection.insert(entity.toDBObject());
		}
	}
	
	/**
	 * 
	* @Title: delete
	* @Description: 删除一个记录
	* @param query
	 */
	public static void delete(IMongoEntity query) {
		DBCollection dbCollection = MongoUtil.getInstance().getCollection(query.getCollectionName());
		dbCollection.remove(query.toDBObject());
	}
	
	/**
	 * 
	* @Title: deleteList
	* @Description: 批量删除记录
	* @param list
	 */
	public static void deleteList(List<IMongoEntity> list) {
		if (list == null || list.isEmpty()) {
			return ;
		}
		DBCollection dbCollection = MongoUtil.getInstance().getCollection(list.get(0).getCollectionName());
		for(IMongoEntity entity : list) {
			dbCollection.remove(entity.toDBObject());
		}
	}
	
	/**
	 * 
	* @Title: getCount
	* @Description: 统计总条数
	* @return
	 */
	public static long getCount(String collectionName) {
		return MongoUtil.getInstance().getCollection(collectionName).getCount();
	}
	
	/**
	 * 
	* @Title: getCount
	* @Description: 统计满足条件总条数
	* @param query
	* @return
	 */
	public static long getCount(IMongoEntity query) {
		if (query == null) {
			throw new NullPointerException("param query null");
		}
		return MongoUtil.getInstance().getCollection(query.getCollectionName()).getCount(query.toDBObject());
	}
	
	/**
	 * 
	* @Title: getObj
	* @Description: 查询返回db obj
	* @param query
	* @return
	 */
	private static DBObject getObj(IMongoEntity query) {
		return MongoUtil.getInstance().getCollection(query.getCollectionName()).findOne(query.toDBObject());
	}
	
	/**
	 * 
	* @Title: get
	* @Description: 条件查询一条记录
	* @param query
	* @return
	 */
	public static IMongoEntity get(IMongoEntity query) {
		DBObject obj = getObj(query);
		try {
			return obj == null ? null : query.getClass().newInstance().fromDBObject(obj);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	* @Title: getList
	* @Description: 条件+排序查询
	* @param query
	* @param order
	* @return
	 */
	public static List<IMongoEntity> getList(IMongoEntity query, DBObject order) {
		return getList(query, order, 0, Config.getInstance().getInt("query.pagesize"));
	}
	
	/**
	 * 
	* @Title: getList
	* @Description: 条件+排序+分页查询
	* @param query
	* @param order
	* @param start
	* @param limit
	* @return
	 */
	public static List<IMongoEntity> getList(IMongoEntity query, DBObject order, int start, int limit) {
		DBCollection dbCollection = MongoUtil.getInstance().getCollection(query.getCollectionName());
		DBCursor dbCursor = query.toDBObject() == null ? dbCollection.find() : dbCollection.find(query.toDBObject());
		if (order != null) {
			dbCursor.sort(order);
		}
		if (start > 0) {
			dbCursor.skip(start);
		}
		if (limit <= 0) {
			limit = Config.getInstance().getInt("query.pagesize");
		}
		dbCursor.limit(limit);
		List<IMongoEntity> list = new ArrayList<IMongoEntity>();
		Class<?> targetClass = query.getClass();
		while(dbCursor.hasNext()) {
			 try {
				IMongoEntity entity = (IMongoEntity) targetClass.newInstance();
				entity.fromDBObject(dbCursor.next());
				list.add(entity);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		dbCursor.close();
		return list;
	}
	
	/**
	 * 
	* @Title: update
	* @Description: 更新一条记录 (此方法按照id更新记录)
	* @param entity
	 */
	public static void update(IMongoEntity entity) {
		if (StringUtils.isEmpty(entity.getId())) {
			throw new IllegalArgumentException("param's id attribute required");
		}
		try {
			IMongoEntity query = entity.getClass().newInstance();
			query.setId(entity.getId());
			entity.setId(null);
			update(query, entity);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	* @Title: update
	* @Description: 条件批量更新
	* @param entity
	* @param query
	 */
	public static void update(IMongoEntity query, IMongoEntity entity) {
		DBObject obj = new BasicDBObject();
		obj.put("$set", entity.toDBObject());
		MongoUtil.getInstance().getCollection(entity.getCollectionName()).updateMulti(query.toDBObject(), obj);
	}
	
}
