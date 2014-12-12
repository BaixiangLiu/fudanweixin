package edu.fudan.eservice.common.actions;

import java.io.IOException;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import edu.fudan.eservice.common.struts.GuestActionBase;

@ParentPackage(value = "servicebase")
@Actions({ @Action(value = "main") })
@Results({ @Result(name = "success", location = "valid.jsp") })
public class DummyAction extends GuestActionBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6158351996318173001L;
	private String req;
	private String echostr;

	public String getEchostr() {
		return echostr;
	}

	public void setEchostr(String echostr) {
		this.echostr = echostr;
	}

	@Override
	public String execute() throws IOException {
		return SUCCESS;
	}


	public String getReq() {
		return req;
	}

	public void setReq(String req) {
		this.req = req;
	}

}
