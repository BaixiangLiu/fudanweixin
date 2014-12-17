package edu.fudan.weixin.crawler.actions;

import java.util.ArrayList;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.SimpleNodeIterator;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import edu.fudan.eservice.common.utils.CommonUtil;
import edu.fudan.eservice.common.utils.EncodeHelper;
import edu.fudan.eservice.common.utils.MongoUtil;
import edu.fudan.weixin.entity.News;
import edu.fudan.weixin.utils.AttributeRegexFilter;

@ParentPackage("servicebase")
@Namespace("/crawler")
public class SudyPageAction extends CrawlerBase {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1266884952585718508L;
	protected String domain;
	protected String listid;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Action(value = "sdlist", results = { @Result(type = "json", params = {
			"root", "list" }) })
	public String list() {
		Cache c = CacheManager.getInstance().getCache("News");
		String ckey =domain+listid + page;
		Element ele = c.get(ckey);
		if (!CommonUtil.isEmpty(ele)) {
			list = (List) ele.getObjectValue();

		} else {
			StringBuffer retstr = fetch(domain+"/"+listid+"/list"
					+ page+".htm");
			Parser p = Parser.createParser(retstr.toString(), "utf-8");
			list = new ArrayList<News>();
			try {
				NodeList ls = p
						.extractAllNodesThatMatch(new AttributeRegexFilter(
								"href", ".*/page\\.htm"));
				SimpleNodeIterator i = ls.elements();
				while (i.hasMoreNodes()) {
					Node n = i.nextNode();
					if (n instanceof TagNode) {
						TagNode tn = (TagNode) n;
						News news = new News();
						String href = tn.getAttribute("href");						
						news.setId(href);
						news.setTitle(tn.getAttribute("alt"));
						Node tmp=tn.getParent().getNextSibling();
						while(tmp!=null &&!(tmp instanceof TableColumn))
							tmp=tmp.getNextSibling();
						if(tmp!=null)
							news.setPubdate(tmp.toPlainTextString());
						list.add(news);
					}
				}
				c.put(new Element(ckey, list));
			} catch (ParserException e) {

				e.printStackTrace();
			}
		}

		return SUCCESS;
	}

	@Action(value = "sdcontent", results = { @Result(type = "json", params = {
			"root", "en" }) })
	public String content() {
		Cache c = CacheManager.getInstance().getCache("News");
		String ckey = domain+ newsid;
		Element ele = c.get(ckey);
		if (!CommonUtil.isEmpty(ele)) {
			en = (News) ele.getObjectValue();
		} else {
			StringBuffer retstr = fetch(domain
					+ newsid);
			Parser p = Parser.createParser(retstr.toString(), "utf-8");
			try {
				NodeList nl = p.extractAllNodesThatMatch(new OrFilter(new NodeFilter[]{
						new HasAttributeFilter("class","Article_Title"), new HasAttributeFilter("class",
								"Article_PublishDate"),new HasAttributeFilter("class","Article_Content")}));
				SimpleNodeIterator i = nl.elements();
				en = new News();
				en.setId(newsid);
				while (i.hasMoreNodes()) {
					Node n = i.nextNode();
					if (n instanceof TagNode) {
						TagNode tn = (TagNode) n;
						if (tn.getAttribute("class").equalsIgnoreCase("Article_Title"))
							en.setTitle(tn.toPlainTextString());
						if (tn.getAttribute("class").equalsIgnoreCase("Article_PublishDate"))
							en.setPubdate(tn.toPlainTextString());
						if (tn.getAttribute("class").equalsIgnoreCase("Article_Content")) {
							NodeList ls = new NodeList();
							tn.collectInto(ls, new NodeClassFilter(
									ImageTag.class));
							SimpleNodeIterator j = ls.elements();
							DBCollection col = MongoUtil.getInstance().getDB()
									.getCollection("CrawlerImages");
							while (j.hasMoreNodes()) {
								ImageTag it = (ImageTag) j.nextNode();
								it.removeAttribute("width");
								it.removeAttribute("height");
								it.removeAttribute("style");
								it.setAttribute("class", "img-responsive");
								String srcstr=it.extractImageLocn();
								if(!srcstr.startsWith("http"))
								{
									if(srcstr.startsWith("/"))
										srcstr=domain+srcstr;
									else
									{
										int tk=newsid.lastIndexOf("/");
										if(tk>0)
										{
											srcstr=domain+newsid.substring(0,tk+1)+srcstr;
										}else
										{
											srcstr=domain+srcstr;
										}
									}
								}
								String imgid = EncodeHelper.digest(
										srcstr, "MD5");
								BasicDBObject obj = new BasicDBObject("id",
										imgid);
								DBObject dbo = col.findOne(obj);
								if (dbo == null)
									col.save(obj.append("url",srcstr));
								it.setImageURL("crawler/image.act?id=" + imgid);
							}
							
							ls=new NodeList();
							tn.collectInto(ls, new NodeClassFilter(LinkTag.class));
							 j = ls.elements();
							while (j.hasMoreNodes()) {
								LinkTag link=(LinkTag)j.nextNode();
								String linkstr=link.extractLink();
								if(link.isHTTPLikeLink()&&!linkstr.startsWith("http")){
									if(linkstr.startsWith("/"))
										link.setLink(domain+link.extractLink());
									else
									{
										int tk=newsid.lastIndexOf("/");
										if(tk>0)
										{
											link.setLink(domain+newsid.substring(0,tk+1)+linkstr);
										}else
										{
											link.setLink(domain+linkstr);
										}
									}
								}
								}
							
						
							
							ls=new NodeList();
							tn.collectInto(ls, new HasAttributeFilter("class","wp_pdf_player"));
							 j = ls.elements();
							while (j.hasMoreNodes()) {
								TagNode pdf=(TagNode)j.nextNode();
								String pdfurl=pdf.getAttribute("pdfsrc");
								pdf.removeAttribute("flexpaper");
								pdf.removeAttribute("swsrc");
								pdf.removeAttribute("pdfsrc");
								pdf.setTagName("a");								
								pdf.setAttribute("href",domain+pdfurl);
								NodeList tnl=new NodeList();
								tnl.add(new TextNode("PDF正文"));
								pdf.setChildren(tnl);
								}
							en.setContent(tn.toHtml());
						}

					}
				}
			} catch (ParserException e) {

				e.printStackTrace();
			}
			if (!CommonUtil.isEmpty(en) && !CommonUtil.isEmpty(en.getContent()))
				c.put(new Element(ckey, en));
		}
		return SUCCESS;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getListid() {
		return listid;
	}

	public void setListid(String listid) {
		this.listid = listid;
	}

}
