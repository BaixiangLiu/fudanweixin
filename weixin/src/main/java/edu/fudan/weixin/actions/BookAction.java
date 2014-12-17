package edu.fudan.weixin.actions;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import edu.fudan.eservice.common.struts.GuestActionBase;
import edu.fudan.eservice.common.utils.CommonUtil;
import edu.fudan.eservice.common.utils.MongoUtil;
import edu.fudan.weixin.utils.OperateResult;

@ParentPackage("servicebase")
@Results({@Result(type="json",params={"root","result"})})
public class BookAction extends GuestActionBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6289673478650624253L;
	private Object[] items;
	private String item;
	private boolean book;
	private OperateResult result;
	@Action("book")
	public String execute()
	{
		Object openid=getSession().get("openid");
		if(CommonUtil.isEmpty(openid))
		{
			result=OperateResult.NOPRG;
		}else
		{
			if(CommonUtil.isEmpty(item))
				result=OperateResult.BADREQ;
			else
			{
				DBCollection c=MongoUtil.getInstance().getDB().getCollection("Books");
				BasicDBObject dbo=new BasicDBObject("openid",openid).append("item", item);
				DBObject o=c.findOne(dbo);
				if(book&&CommonUtil.isEmpty(o))
				{
					c.save(dbo.append("booktime", System.currentTimeMillis()).append("book", book));
				}
				if(!CommonUtil.isEmpty(o))
				{
					if(!CommonUtil.eq(o.get("book"), book)){
					o.put("booktime", System.currentTimeMillis());
					o.put("book", book);					
					c.save(o);
					}
				}
				result=OperateResult.OK;
			}
		}
		return SUCCESS;
	}
	@Action(value="booklist",results={@Result(location="booklist.jsp")})
	public String list()
	{
		Object openid=getSession().get("openid");
		DBCursor cs=MongoUtil.getInstance().getDB().getCollection("Books").find(new BasicDBObject("openid",openid).append("book",true));
		items=new Object[cs.count()];
		int i=0;
		while(cs.hasNext())
		{
			items[i++]=cs.next().get("item");
		}
		return SUCCESS;
	}
	public Object[] getItems() {
		return items;
	}
	public void setItems(Object[] items) {
		this.items = items;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public boolean isBook() {
		return book;
	}
	public void setBook(boolean book) {
		this.book = book;
	}
	public OperateResult getResult() {
		return result;
	}
	public void setResult(OperateResult result) {
		this.result = result;
	}
	
}
