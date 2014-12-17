package edu.fudan.weixin.entity;

import java.io.Serializable;

public class News implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6320578582090308462L;
	private String id;
	private String title;
	private String pubdate;
	private String content;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPubdate() {
		return pubdate;
	}
	public void setPubdate(String pubdate) {
		this.pubdate = pubdate;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public News(String id, String title, String pubdate, String content) {
		super();
		this.id = id;
		this.title = title;
		this.pubdate = pubdate;
		this.content = content;
	}
	public News() {
		
	}
	@Override
	public String toString() {
		return "News [id=" + id + ", title=" + title + ", pubdate=" + pubdate
				+ ", content=" + content + "]";
	}
	
	

}
