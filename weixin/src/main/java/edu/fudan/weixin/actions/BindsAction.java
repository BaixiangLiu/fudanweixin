package edu.fudan.weixin.actions;


import java.util.ArrayList;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import edu.fudan.eservice.common.struts.GuestActionBase;
import edu.fudan.eservice.common.utils.CommonUtil;
import edu.fudan.weixin.utils.BindingHelper;

@ParentPackage(value = "servicebase")
@Namespace("/")
@Actions({
	@Action(value="binds",results={@Result(location="binds.jsp")}),
	@Action(value="score",results={@Result(location="score.jsp")}),
	@Action(value="lesson",results={@Result(location="lesson.jsp")}),
	@Action(value="ecarddaily",results={@Result(location="ecarddaily.jsp")})
	})
public class BindsAction extends GuestActionBase{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1752755265539060382L;
	
	private Object binds;
	
	public String execute()
	{
		Object openid=getSession().get("openid");
		if(!CommonUtil.isEmpty(openid))
			binds=BindingHelper.getBinding(String.valueOf(openid));
		else
			binds=new ArrayList();
		return SUCCESS;
	}

	public Object getBinds() {
		return binds;
	}

	public void setBinds(Object binds) {
		this.binds = binds;
	}

}
