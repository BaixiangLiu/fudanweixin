package edu.fudan.weixin.crawler.actions;

import java.util.List;

import edu.fudan.eservice.common.struts.GuestActionBase;
import edu.fudan.eservice.common.struts.JSONPActionBase;
import edu.fudan.eservice.common.utils.CommonUtil;
import edu.fudan.weixin.entity.News;

@SuppressWarnings("serial")
public class CrawlerBase extends JSONPActionBase {
	
	protected List<News> list;
	protected int page=1;
	protected News en;
	protected String newsid;
	
	protected StringBuffer fetch(String url)
	{
		try {
			return CommonUtil.getWebContent(url);
		} catch (Exception e) {
			return new StringBuffer();
		}
	}
	
	

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public List<News> getList() {
		return list;
	}	

	public News getEn() {
		return en;
	}

	public String getNewsid() {
		return newsid;
	}

	public void setNewsid(String newsid) {
		this.newsid = newsid;
	}
	
	
	
}
