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
public class LibAnnouceAction extends CrawlerBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8626906879074745926L;
	protected static final String RD = "http://mlib.fudan.edu.cn";

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Action(value = "liblist", results = { @Result(type = "json", params = {
			"root", "list" }) })
	public String list() {
		Cache c = CacheManager.getInstance().getCache("News");
		String ckey = "liblist" + page;
		Element ele = c.get(ckey);
		if (!CommonUtil.isEmpty(ele)) {
			list = (List) ele.getObjectValue();

		} else {
			StringBuffer retstr = fetch(RD
					+ "/ddlib/getPublishInfoList.shtml?tid=1012&k=&p="
					+ (page - 1));
			Parser p = Parser.createParser(retstr.toString(), "utf-8");
			list = new ArrayList<News>();
			try {
				NodeList ls = p
						.extractAllNodesThatMatch(new AttributeRegexFilter(
								"href", "publishInfo\\.shtml\\?.+"));
				SimpleNodeIterator i = ls.elements();
				while (i.hasMoreNodes()) {
					Node n = i.nextNode();
					if (n instanceof TagNode) {
						TagNode tn = (TagNode) n;
						News news = new News();
						String href = tn.getAttribute("href");
						news.setId(href);
						news.setTitle(tn.toPlainTextString());
						Node tmp = tn.getNextSibling();
						if (tmp != null && tmp instanceof TextNode) {
							if (tmp.getText() != null)
								news.setPubdate(tmp.getText().replaceAll(
										"&nbsp;", ""));
						}
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

	@Action(value = "libcontent", results = { @Result(type = "json", params = {
			"root", "en" }) })
	public String content() {
		Cache c = CacheManager.getInstance().getCache("News");
		String ckey = "libcontent" + newsid;
		Element ele = c.get(ckey);
		if (!CommonUtil.isEmpty(ele)) {
			en = (News) ele.getObjectValue();
		} else {
			StringBuffer retstr = fetch(RD + "/ddlib/" + newsid);
			Parser p = Parser.createParser(retstr.toString(), "utf-8");
			try {
				NodeList nl = p
						.extractAllNodesThatMatch(new OrFilter(
								new NodeFilter[] {
										new HasAttributeFilter("class",
												"font"),
										new HasAttributeFilter("class",
												"date"),
										new HasAttributeFilter("class",
												"xiugaiyemian") }));
				SimpleNodeIterator i = nl.elements();
				en = new News();
				en.setId(newsid);
				while (i.hasMoreNodes()) {
					Node n = i.nextNode();
					if (n instanceof TagNode) {
						TagNode tn = (TagNode) n;
						if (tn.getAttribute("class").equalsIgnoreCase("font"))
							en.setTitle(tn.toPlainTextString());
						if (tn.getAttribute("class").equalsIgnoreCase("date"))
							en.setPubdate(tn.toPlainTextString());
						if (tn.getAttribute("class").equalsIgnoreCase(
								"xiugaiyemian")) {
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
										srcstr = RD + "/ddlib/" + srcstr;
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

										link.setLink(RD + "/ddlib/" + linkstr);

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
