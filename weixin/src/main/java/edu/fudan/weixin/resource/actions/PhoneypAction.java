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
public class PhoneypAction extends GuestActionBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4186021868840534087L;
	private OperateResult result;
	private Map<String,Object> list;
	private String qkey;

	@Action("phoneyp")
	public String execute() {
		Object openid = getSession().get("openid");
		if (CommonUtil.isEmpty(openid)) {
			result = OperateResult.NOPRG;

		} else {
			if(!CommonUtil.isEmpty(qkey))
			{
				list = TACOAuth2Helper.phoneyp(qkey);
				result = OperateResult.OK;
			}else
			{
				result=OperateResult.BADREQ;
			}
			
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

	public String getQkey() {
		return qkey;
	}

	public void setQkey(String qkey) {
		this.qkey = qkey;
	}

	

}
