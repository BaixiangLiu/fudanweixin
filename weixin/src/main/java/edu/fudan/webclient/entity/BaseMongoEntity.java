/**
* @Title: BaseMongoEntity.java
* @Description: TODO
* @author: Calvinyang
* @date: Oct 12, 2014 1:57:16 PM
* Copyright: Copyright (c) 2013
* @version: 1.0
*/
package edu.fudan.webclient.entity;

import java.lang.reflect.Field;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * @author: Calvinyang
 * @Description: TODO
 * @date: Oct 12, 2014 1:57:16 PM
 */
public abstract class BaseMongoEntity implements IMongoEntity {

	@Override
	public DBObject toDBObject() {
		DBObject obj = new BasicDBObject();
		Field[] fields = getClass().getDeclaredFields();
		try {
			for (Field field : fields) {
				field.setAccessible(true);
				if (field.get(this) != null) {
					obj.put(field.getName(), field.get(this));
				}
				field.setAccessible(false);
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		if (obj.containsField("id")) {
			obj.put("_id", new ObjectId(obj.get("id").toString()));
			obj.removeField("id");
		}
		return obj;
	}

}
