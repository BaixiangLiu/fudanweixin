package edu.fudan.weixin.crawler.actions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.nodes.TextNode;
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

@ParentPackage("servicebase")
@Namespace("/crawler")
public class CampusEventAction extends CrawlerBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8934539069991515679L;
	protected static final String RD="http://news.fudan.edu.cn";
	@SuppressWarnings("rawtypes")
	@Action(value = "eventlist")
	public String list() throws IOException {
		Cache c = CacheManager.getInstance().getCache("News");
		String ckey = "eventlist"+page ;
		Element ele = c.get(ckey);
		if (!CommonUtil.isEmpty(ele)) {
			list = (List) ele.getObjectValue();

		} else {
			StringBuffer retstr = fetch(RD+"/calendar/?a=list&&m=recent&range=30&_="+System.currentTimeMillis()+"&type=0&place=0&type="+page	);
			Parser p = Parser.createParser(retstr.toString(), "utf-8");
			list = new ArrayList<News>();
			try {
				NodeList ls = p
						.extractAllNodesThatMatch(new HasAttributeFilter("class","clear"));
				if(ls.size()==2)
				{
					int tk1=ls.elementAt(0).getEndPosition();
					int tk2=ls.elementAt(1).getStartPosition();
					ServletActionContext.getResponse().setCharacterEncoding("utf-8");
					p=Parser.createParser(retstr.substring(tk1+6, tk2), "utf-8");
					NodeList nl=p.parse(null);
					NodeList links=nl.extractAllNodesThatMatch(new NodeClassFilter(LinkTag.class),true);
					SimpleNodeIterator i=links.elements();
					while(i.hasMoreNodes())
					{
						LinkTag lt=(LinkTag)i.nextNode();
						NodeList ll=new NodeList();
						ll.add(new TextNode(lt.getAttribute("title")));
						lt.setChildren(ll);
						lt.removeAttribute("title");
					}
					
					
					ServletActionContext.getResponse().getWriter().print(nl.toHtml());
				}
			} catch (ParserException e) {
				e.printStackTrace();
			}
		}

		return NONE;
	}

	@Action(value = "eventcontent", results = { @Result(type = "json", params = {
			"root", "en" }) })
	public String content() {
		Cache c = CacheManager.getInstance().getCache("News");
		String ckey = "eventcontent" + newsid;
		Element ele = c.get(ckey);
		if (!CommonUtil.isEmpty(ele)) {
			en = (News) ele.getObjectValue();
		} else {
			StringBuffer retstr = fetch(RD+"/calendar/?a=one&evid="
					+ newsid+"&_="+System.currentTimeMillis());
			Parser p = Parser.createParser(retstr.toString(), "utf-8");
			try {
				NodeList nl = p.extractAllNodesThatMatch(new OrFilter(
						new TagNameFilter("h1"), new TagNameFilter("table")));
				SimpleNodeIterator i = nl.elements();
				en = new News();
				en.setId(newsid);
				while (i.hasMoreNodes()) {
					Node n = i.nextNode();
					if (n instanceof TagNode) {
						TagNode tn = (TagNode) n;
						if (tn.getTagName().equalsIgnoreCase("h1"))
							en.setTitle(tn.toPlainTextString());
						if (tn.getTagName().equalsIgnoreCase("table")) {
						en.setContent(tn.toHtml());							
							 
							}
							
						}

					}
				 String str=retstr.toString().trim();
				 int tk=retstr.indexOf("imageurl");
				 if(tk>0)
				 {
					 tk=retstr.indexOf("'",tk);
					 int tk1=retstr.indexOf("'", tk+1);
					 
				  String imgurl=RD+str.substring(tk+1,tk1);
					String imgid = EncodeHelper.digest(
							imgurl, "MD5");
					BasicDBObject obj = new BasicDBObject("id",
							imgid);
					DBCollection col = MongoUtil.getInstance().getDB()
							.getCollection("CrawlerImages");							
					DBObject dbo = col.findOne(obj);
					if (dbo == null)
						col.save(obj.append("url",imgurl));
					en.setPubdate(imgid);	
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
