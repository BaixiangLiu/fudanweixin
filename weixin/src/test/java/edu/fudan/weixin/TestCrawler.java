package edu.fudan.weixin;

import java.io.IOException;

import org.junit.Test;

import edu.fudan.weixin.crawler.actions.CampusAnnouceAction;
import edu.fudan.weixin.crawler.actions.CampusEventAction;
import edu.fudan.weixin.crawler.actions.CampusNewsAction;
import edu.fudan.weixin.crawler.actions.DstAnnouceAction;
import edu.fudan.weixin.crawler.actions.LibAnnouceAction;
import edu.fudan.weixin.crawler.actions.SudyPageAction;
import edu.fudan.weixin.entity.News;

public class TestCrawler {

	@Test
	
	public void campusnews() throws IOException
	{
		CampusNewsAction ca=new CampusNewsAction();
		ca.setListid("xxyw");
		ca.setPage(2);
		ca.list();
		System.out.println(ca.getList().size()+":"+ca.getList());
		ca.setNewsid(((News)(ca.getList().get(2))).getId());
		ca.content();
		System.out.println(ca.getEn());
	}
	
	public void dstlist()
	{
		DstAnnouceAction ca=new DstAnnouceAction();
		ca.setPage(2);
		ca.list();
		System.out.println(ca.getList().size()+":"+ca.getList());
	ca.setNewsid(((News)(ca.getList().get(2))).getId());
		ca.content();
		System.out.println(ca.getEn());
	}
	public void liblist()
	{
		LibAnnouceAction ca=new LibAnnouceAction();
		ca.list();
		System.out.println(ca.getList().size()+":"+ca.getList());
	ca.setNewsid(((News)(ca.getList().get(2))).getId());
		ca.content();
		System.out.println(ca.getEn());
	}
	
	public void jwclist()
	{
		SudyPageAction ca=new SudyPageAction();
		//ca.setPage(2);
		ca.setDomain("http://www.jwc.fudan.edu.cn");
		ca.setListid("3167");
		ca.list();
		System.out.println(ca.getList().size()+":"+ca.getList());
		ca.setNewsid(((News)(ca.getList().get(2))).getId());
		ca.content();
		System.out.println(ca.getEn());
	}
	
	public void xzylist()
	{
		CampusAnnouceAction ca=new CampusAnnouceAction();
		//ca.setPage(2);
		ca.list();
		System.out.println(ca.getList().size()+":"+ca.getList());
		ca.setNewsid("592");
		ca.content();
		System.out.println(ca.getEn());
	}
}
