/**
* @Title: WeixinUserEntity.java
* @Description: TODO
* @author: Calvinyang
* @date: Oct 12, 2014 5:26:52 PM
* Copyright: Copyright (c) 2013
* @version: 1.0
*/
package edu.fudan.webclient.entity;

import java.util.Date;

import com.mongodb.DBObject;

import edu.fudan.webclient.util.DateUtil;
import edu.fudan.webclient.util.MongoConverter;

/**
 * @author: Calvinyang
 * @Description: 粉丝
 * @date: Oct 12, 2014 5:26:52 PM
 */
public class WeixinUserEntity extends BaseMongoEntity implements IMongoEntity {
	private String id;
	private String subscribe;
	private String openid;
	private String nickName;
	private String sex;
	private String lanuage;
	private String city;
	private String province;
	private String country;
	private String headimgurl;
	private String subscribe_time;
	private String remark;
	
	public String getSexStr() {
		return sex.equals("1") ? "男" : sex.equals("0") ? "女" : "未知";
	}
	
	public String getSubscribeStr() {
		return subscribe.equals("1") ? "关注" : "未关注";
	}
	
	public String getSubscribeTimeStr() {
		Date date = new Date();
		date.setTime(Long.parseLong(getSubscribe_time()) * 1000);
		return DateUtil.format(date, DateUtil.DATE_TIME_FORMAT);
	}
	
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
	 * @return the subscribe
	 */
	public String getSubscribe() {
		return subscribe;
	}
	/**
	 * @param subscribe the subscribe to set
	 */
	public void setSubscribe(String subscribe) {
		this.subscribe = subscribe;
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
	 * @return the nickName
	 */
	public String getNickName() {
		return nickName;
	}
	/**
	 * @param nickName the nickName to set
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	/**
	 * @return the sex
	 */
	public String getSex() {
		return sex;
	}
	/**
	 * @param sex the sex to set
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}
	/**
	 * @return the lanuage
	 */
	public String getLanuage() {
		return lanuage;
	}
	/**
	 * @param lanuage the lanuage to set
	 */
	public void setLanuage(String lanuage) {
		this.lanuage = lanuage;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @return the province
	 */
	public String getProvince() {
		return province;
	}
	/**
	 * @param province the province to set
	 */
	public void setProvince(String province) {
		this.province = province;
	}
	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}
	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	/**
	 * @return the headimgurl
	 */
	public String getHeadimgurl() {
		return headimgurl;
	}
	/**
	 * @param headimgurl the headimgurl to set
	 */
	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}
	/**
	 * @return the subscribe_time
	 */
	public String getSubscribe_time() {
		return subscribe_time;
	}
	/**
	 * @param subscribe_time the subscribe_time to set
	 */
	public void setSubscribe_time(String subscribe_time) {
		this.subscribe_time = subscribe_time;
	}
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String getCollectionName() {
		return "weixinuser";
	}

	@Override
	public IMongoEntity fromDBObject(DBObject obj) {
		MongoConverter converter = new MongoConverter(obj);
		setCity(converter.getString("city"));
		setCountry(converter.getString("country"));
		setHeadimgurl(converter.getString("headimgurl"));
		setId(converter.getString("_id"));
		setLanuage(converter.getString("language"));
		setNickName(converter.getString("nickname"));
		setOpenid(converter.getString("openid"));
		setProvince(converter.getString("province"));
		setRemark(converter.getString("remark"));
		setSex(converter.getString("sex"));
		setSubscribe(converter.getString("subscribe"));
		setSubscribe_time(converter.getString("subscribe_time"));
		return this;
	}
	
}
