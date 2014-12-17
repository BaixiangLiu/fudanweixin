package edu.fudan.weixin.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import edu.fudan.eservice.common.struts.GuestActionBase;
import edu.fudan.eservice.common.utils.CommonUtil;
import edu.fudan.eservice.common.utils.MongoUtil;
import edu.fudan.weixin.model.TACOAuth2Model;
import edu.fudan.weixin.utils.OperateResult;

@ParentPackage("servicebase")

public class UnbindAction extends GuestActionBase {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7838893449325562995L;
	private String uisid;	

	private List<DBObject> list;
	@SuppressWarnings("unchecked")
	@Action(value="unbind",results={@Result(location="binds.jsp")})
	public String execute()
	{
		if(!CommonUtil.isEmpty(uisid))
		{
			Object openid=getSession().get("openid");
			if(!CommonUtil.isEmpty(openid))
			{
				DBCollection c=MongoUtil.getInstance().getDB().getCollection("Bindings");
				DBObject bd=c.findOne(new BasicDBObject("openid",openid));
				if(!CommonUtil.isEmpty(bd))
				{
					Object bns=bd.get("binds");
					if(!CommonUtil.isEmpty(bns))
					{
						Iterator<DBObject> i=((List<DBObject>)bns).iterator();
						while(i.hasNext())
						{
							DBObject o=i.next();
							if(uisid.equals(o.get("uisid"))){
								i.remove();
								new TACOAuth2Model().revokeToken(o);
							}
						}
						c.save(bd);
					}
				}
			}else
			{
				addActionError(OperateResult.NOPRG.getErrdesc());
			}
		}else
		{
			addActionError(OperateResult.BADREQ.getErrdesc());
		}
		return SUCCESS;
	}
	

	@Action(value="bindlist",results={@Result(location="bindlist.jsp")})
	public String list()
	{
		if(!CommonUtil.isEmpty(uisid))
		{
			Object openid=getSession().get("openid");
			if(!CommonUtil.isEmpty(openid))
			{
				DBCollection c=MongoUtil.getInstance().getDB().getCollection("Bindings");
				DBCollection w=MongoUtil.getInstance().getDB().getCollection("weixinuser");
				DBCursor bd=c.find(new BasicDBObject("binds",new BasicDBObject("$elemMatch",new BasicDBObject("uisid",uisid))));
				list=new ArrayList<DBObject> ();
				while(bd.hasNext())
				{
					list.add(w.findOne(new BasicDBObject("openid",bd.next().get("openid"))));
				}
			}else
			{
				addActionError(OperateResult.NOPRG.getErrdesc());
			}
		}else
		{
			addActionError(OperateResult.BADREQ.getErrdesc());
		}
		return SUCCESS;
	}
	
	
	
	
	public List<DBObject> getList() {
		return list;
	}

	public void setList(List<DBObject> list) {
		this.list = list;
	}

	public String getUisid() {
		return uisid;
	}
	public void setUisid(String uisid) {
		this.uisid = uisid;
	}

}
