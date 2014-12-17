/**
 * @Title: WebUserEntity.java
 * @Description: TODO
 * @author: Calvinyang
 * @date: Oct 12, 2014 1:31:04 PM
 * Copyright: Copyright (c) 2013
 * @version: 1.0
 */
package edu.fudan.webclient.entity;

import com.mongodb.DBObject;

import edu.fudan.webclient.util.MongoConverter;

/**
 * @author: Calvinyang
 * @Description: TODO
 * @date: Oct 12, 2014 1:31:04 PM
 */
public class WebUserEntity extends BaseMongoEntity implements IMongoEntity {
	private String id;
	private String name;
	private String passwd;
	private String role;

	@Override
	public String getCollectionName() {
		return "WebUser";
	}

	@Override
	public IMongoEntity fromDBObject(DBObject obj) {
		MongoConverter converter = new MongoConverter(obj);
		setId(converter.getString("_id"));
		setName(converter.getString("name"));
		setPasswd(converter.getString("passwd"));
		setRole(converter.getString("role"));
		return this;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the passwd
	 */
	public String getPasswd() {
		return passwd;
	}

	/**
	 * @param passwd
	 *            the passwd to set
	 */
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * @param role
	 *            the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}

}
