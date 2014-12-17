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
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.Span;
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
public class DstAnnouceAction extends CrawlerBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 452084395312716298L;
	protected static final String RD = "http://dst.fudan.edu.cn";

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Action(value = "dstlist", results = { @Result(type = "json", params = {
			"root", "list" }) })
	public String list() {
		Cache c = CacheManager.getInstance().getCache("News");
		String ckey = "dstlist" + page;
		Element ele = c.get(ckey);
		if (!CommonUtil.isEmpty(ele)) {
			list = (List) ele.getObjectValue();

		} else {
			try {
			StringBuffer retstr = CommonUtil.postWebRequest(RD+"/news.aspx?info_lb=822", ("__EVENTTARGET=_ctl0$ContentPlaceHolder1$Pager22&__EVENTARGUMENT="+page).getBytes("utf-8"), "application/x-www-form-urlencoded");
			Parser p = Parser.createParser(retstr.toString(), "utf-8");
			list = new ArrayList<News>();
			
				NodeList ls = p
						.extractAllNodesThatMatch(new AttributeRegexFilter(
								"href", "show\\.aspx\\?.+"));
				SimpleNodeIterator i = ls.elements();
				while (i.hasMoreNodes()) {
					Node n = i.nextNode();
					if (n instanceof TagNode) {
						TagNode tn = (TagNode) n;
						News news = new News();
						String href = tn.getAttribute("href");
						news.setId(href);
						news.setTitle(tn.toPlainTextString().trim());
						Node tmp=tn.getParent().getNextSibling();
						while(tmp!=null &&!(tmp instanceof Span))
							tmp=tmp.getNextSibling();
						if(tmp!=null){
							String dtstr=tmp.toPlainTextString();
							if(dtstr!=null &&dtstr.length()>2)
							news.setPubdate(dtstr.substring(1,dtstr.length()-1));
						}
						list.add(news);
					}
				}
				c.put(new Element(ckey, list));
			} catch (Exception e) {

				e.printStackTrace();
			}
		}

		return SUCCESS;
	}

	@Action(value = "dstcontent", results = { @Result(type = "json", params = {
			"root", "en" }) })
	public String content() {
		Cache c = CacheManager.getInstance().getCache("News");
		String ckey = "dstcontent" + newsid;
		Element ele = c.get(ckey);
		if (!CommonUtil.isEmpty(ele)) {
			en = (News) ele.getObjectValue();
		} else {
			StringBuffer retstr = fetch(RD + "/" + newsid);
			Parser p = Parser.createParser(retstr.toString(), "utf-8");
			try {
				NodeList nl = p
						.extractAllNodesThatMatch(new OrFilter(
								new NodeFilter[] {new AndFilter(
										new AttributeRegexFilter("style",
												"font-size:.*"),
										new TagNameFilter("font")),
										new HasAttributeFilter("id",
												"size") }));
				SimpleNodeIterator i = nl.elements();
				en = new News();
				en.setId(newsid);
				while (i.hasMoreNodes()) {
					Node n = i.nextNode();
					if (n instanceof TagNode) {
						TagNode tn = (TagNode) n;
						if ("font".equalsIgnoreCase(tn.getTagName()))
							en.setTitle(tn.getParent().toPlainTextString().trim());
						
						if ("size".equalsIgnoreCase(
								tn.getAttribute("id"))) {
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
								String srcstr = it.extractImageLocn();
								if (!srcstr.startsWith("http")) {
									if (srcstr.startsWith("/"))
										srcstr = RD + srcstr;
									else {
										srcstr = RD + "/" + srcstr;
									}
								}
								String imgid = EncodeHelper.digest(srcstr,
										"MD5");
								BasicDBObject obj = new BasicDBObject("id",
										imgid);
								DBObject dbo = col.findOne(obj);
								if (dbo == null)
									col.save(obj.append("url", srcstr));
								it.setImageURL("crawler/image.act?id=" + imgid);
							}

							ls = new NodeList();
							tn.collectInto(ls, new NodeClassFilter(
									LinkTag.class));
							j = ls.elements();
							while (j.hasMoreNodes()) {
								LinkTag link = (LinkTag) j.nextNode();
								String linkstr = link.extractLink();
								if (link.isHTTPLikeLink()
										&& !linkstr.startsWith("http")) {
									if (linkstr.startsWith("/"))
										link.setLink(RD + link.extractLink());
									else {

										link.setLink(RD + "/" + linkstr);

									}
								}
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

}
