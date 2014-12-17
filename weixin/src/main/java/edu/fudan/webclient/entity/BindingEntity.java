/**
* @Title: BindingEntity.java
* @Description: TODO
* @author: Calvinyang
* @date: Oct 19, 2014 3:24:05 PM
* Copyright: Copyright (c) 2013
* @version: 1.0
*/
package edu.fudan.webclient.entity;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.DBObject;

import edu.fudan.webclient.util.MongoConverter;

/**
 * @author: Calvinyang
 * @Description: 用户绑定信息表
 * @date: Oct 19, 2014 3:24:05 PM
 */
public class BindingEntity extends BaseMongoEntity implements IMongoEntity {
	private String id;
	private String openid;
	private String weixintoken;
	private long weixinexpired;
	private String weixinscope;
	private String weixinrefresh;
	private List<BindInfoEntity> binds;
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
	 * @return the openid
	 */
	public String getOpenid() {
		return openid;
	}
	/**
	 * @param openid the openid to set
	 */
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	/**
	 * @return the weixintoken
	 */
	public String getWeixintoken() {
		return weixintoken;
	}
	/**
	 * @param weixintoken the weixintoken to set
	 */
	public void setWeixintoken(String weixintoken) {
		this.weixintoken = weixintoken;
	}
	/**
	 * @return the weixinexpired
	 */
	public long getWeixinexpired() {
		return weixinexpired;
	}
	/**
	 * @param weixinexpired the weixinexpired to set
	 */
	public void setWeixinexpired(long weixinexpired) {
		this.weixinexpired = weixinexpired;
	}
	/**
	 * @return the weixinscope
	 */
	public String getWeixinscope() {
		return weixinscope;
	}
	/**
	 * @param weixinscope the weixinscope to set
	 */
	public void setWeixinscope(String weixinscope) {
		this.weixinscope = weixinscope;
	}
	/**
	 * @return the weixinrefresh
	 */
	public String getWeixinrefresh() {
		return weixinrefresh;
	}
	/**
	 * @param weixinrefresh the weixinrefresh to set
	 */
	public void setWeixinrefresh(String weixinrefresh) {
		this.weixinrefresh = weixinrefresh;
	}
	/**
	 * @return the binds
	 */
	public List<BindInfoEntity> getBinds() {
		return binds;
	}
	/**
	 * @param binds the binds to set
	 */
	public void setBinds(List<BindInfoEntity> binds) {
		this.binds = binds;
	}
	@Override
	public String getCollectionName() {
		return "Bindings";
	}
	@Override
	public DBObject toDBObject() {
		DBObject obj = super.toDBObject();
		if (weixinexpired == 0) {
			obj.removeField("weixinexpired");
		}
		return obj;
	}
	
	@Override
	public IMongoEntity fromDBObject(DBObject obj) {
		MongoConverter converter = new MongoConverter(obj);
		setId(converter.getString("_id"));
		setOpenid(converter.getString("openid"));
		setWeixintoken(converter.getString("weixintoken"));
		setWeixinexpired(converter.getLong("weixinexpired"));
		setWeixinrefresh(converter.getString("weixinrefresh"));
		setWeixinscope(converter.getString("weixinscope"));
		List<IMongoEntity> list = converter.getList("binds", BindInfoEntity.class);
		List<BindInfoEntity> binds = new ArrayList<BindInfoEntity>();
		for(int i = 0 ; i < list.size() ; i ++) {
			binds.add(((BindInfoEntity)list.get(i)));
		}
		setBinds(binds);
		return this;
	}
	
}
