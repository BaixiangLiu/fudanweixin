package edu.fudan.weixin.resource.actions;

import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import edu.fudan.eservice.common.struts.GuestActionBase;
import edu.fudan.eservice.common.utils.CommonUtil;
import edu.fudan.weixin.utils.OperateResult;
import edu.fudan.weixin.utils.TACOAuth2Helper;

@ParentPackage("servicebase")
@Namespace("/resource")
@Results({ @Result(type = "json") })
public class SchoolTermAction extends GuestActionBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2315471541475600223L;
	private OperateResult result;
	private Map<String,Object> list;
	private String date="";
	private String term = "";
	private String week="";

	@Action("schoolterm")
	public String execute() {
		Object openid = getSession().get("openid");
		if (CommonUtil.isEmpty(openid)) {
			result = OperateResult.NOPRG;

		} else {
			
				list = TACOAuth2Helper.schoolterm(term, date, week);
				result = OperateResult.OK;
			
			}
		
		return SUCCESS;
	}

	public OperateResult getResult() {
		return result;
	}

	public void setResult(OperateResult result) {
		this.result = result;
	}

	public Map<String, Object> getList() {
		return list;
	}

	public void setList(Map<String, Object> list) {
		this.list = list;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	

}
