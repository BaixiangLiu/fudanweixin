/**
* @Title: BindInfoEntity.java
* @Description: TODO
* @author: Calvinyang
* @date: Oct 19, 2014 3:27:39 PM
* Copyright: Copyright (c) 2013
* @version: 1.0
*/
package edu.fudan.webclient.entity;

import com.mongodb.DBObject;

import edu.fudan.webclient.util.MongoConverter;

/**
 * @author: Calvinyang
 * @Description: 用户绑定帐号信息
 * @date: Oct 19, 2014 3:27:39 PM
 */
public class BindInfoEntity extends BaseMongoEntity implements IMongoEntity {
	private String id;
	private String uistoken;
	private long uisexpired;
	private String uisscope;
	private String uisrefresh;
	private String uisid;
	private String email;
	private String username;
	private String usertype;
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the uistoken
	 */
	public String getUistoken() {
		return uistoken;
	}
	/**
	 * @param uistoken the uistoken to set
	 */
	public void setUistoken(String uistoken) {
		this.uistoken = uistoken;
	}
	/**
	 * @return the uisscope
	 */
	public String getUisscope() {
		return uisscope;
	}
	/**
	 * @param uisscope the uisscope to set
	 */
	public void setUisscope(String uisscope) {
		this.uisscope = uisscope;
	}
	/**
	 * @return the uisrefresh
	 */
	public String getUisrefresh() {
		return uisrefresh;
	}
	/**
	 * @param uisrefresh the uisrefresh to set
	 */
	public void setUisrefresh(String uisrefresh) {
		this.uisrefresh = uisrefresh;
	}
	/**
	 * @return the uisid
	 */
	public String getUisid() {
		return uisid;
	}
	/**
	 * @param uisid the uisid to set
	 */
	public void setUisid(String uisid) {
		this.uisid = uisid;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the uisexpired
	 */
	public long getUisexpired() {
		return uisexpired;
	}
	/**
	 * @param uisexpired the uisexpired to set
	 */
	public void setUisexpired(long uisexpired) {
		this.uisexpired = uisexpired;
	}
	/**
	 * @return the usertype
	 */
	public String getUsertype() {
		return usertype;
	}
	/**
	 * @param usertype the usertype to set
	 */
	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}

	@Override
	public String getCollectionName() {
		return null;
	}
	@Override
	public IMongoEntity fromDBObject(DBObject obj) {
		MongoConverter converter = new MongoConverter(obj);
		setEmail(converter.getString("email"));
		setUisexpired(converter.getLong("uisexpired"));
		setUisid(converter.getString("uisid"));
		setUisrefresh(converter.getString("uisrefresh"));
		setUisscope(converter.getString("uisscope"));
		setUistoken(converter.getString("uistoken"));
		setUsername(converter.getString("username"));
		setUsertype(converter.getString("usertype"));
		return this;
	}
}
