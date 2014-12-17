package edu.fudan.weixin.actions;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import edu.fudan.eservice.common.struts.GuestActionBase;
import edu.fudan.weixin.utils.WeixinFollowerHelper;


@SuppressWarnings("serial")
@ParentPackage(value = "servicebase")
@Actions({@Action(value = "refreshFollowers")})
@Results({@Result(name = "success",location = "valid.jsp")})

public class RefreshFollowers extends GuestActionBase {
	
	public String execute() {
		WeixinFollowerHelper.FetchAllWeixinFollowers("");
		return SUCCESS;
		
	}
	
	
	

}
