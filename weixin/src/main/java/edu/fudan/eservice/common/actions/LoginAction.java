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
@Results({ @Result(name = "success", type = "httpheader") })
@InterceptorRef(value = "guest")
public class LoginAction extends GuestActionBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5634293581978321104L;

	private String enmsg;

	private String redir;

	public String execute() throws IOException {
		if (CommonUtil.isEmpty(getSession().get("openid"))) {
			String openid = null;

			if (!CommonUtil.isEmpty(openid)) {
				if (CommonUtil.isEmpty(redir) || redir.indexOf("login.") >= 0)
					redir = ServletActionContext.getServletContext()
							.getContextPath();
				ServletActionContext.getResponse().sendRedirect(redir);
			}
		}
		return NONE;
	}



	public String getEnmsg() {
		return enmsg;
	}

	public void setEnmsg(String enmsg) {
		this.enmsg = enmsg;
	}

}
