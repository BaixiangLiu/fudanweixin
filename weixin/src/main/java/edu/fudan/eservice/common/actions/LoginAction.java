package edu.fudan.eservice.common.actions;

import java.io.IOException;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import edu.fudan.eservice.common.struts.GuestActionBase;
import edu.fudan.eservice.common.utils.CommonUtil;

@ParentPackage(value = "servicebase")
@Results({ @Result(name = "success", type = "httpheader"), @Result(name = "input", location = "login.jsp") })
@InterceptorRef(value = "guest")
public class LoginAction extends GuestActionBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5634293581978321104L;

	private String enmsg;

	private String redir;

	public String execute() throws IOException {
		if (CommonUtil.isEmpty(enmsg))
			return INPUT;
		String uid = checkMsg(enmsg, true);
		if (!CommonUtil.isEmpty(uid)) {
			if (CommonUtil.isEmpty(redir) || redir.indexOf("login.") >= 0)
				redir = ServletActionContext.getServletContext().getContextPath();
			ServletActionContext.getResponse().sendRedirect(redir);
		} else {
			return INPUT;
		}
		return NONE;
	}

	private String checkMsg(String enmsg2, boolean b) {

		return null;
	}

	public String getEnmsg() {
		return enmsg;
	}

	public void setEnmsg(String enmsg) {
		this.enmsg = enmsg;
	}

}
