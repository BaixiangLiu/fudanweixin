package edu.fudan.weixin.actions;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;







import edu.fudan.eservice.common.struts.GuestActionBase;
import edu.fudan.weixin.model.CustomMessageProcessor;
import edu.fudan.weixin.model.message.ImageJSONMessageBuilder;
import edu.fudan.weixin.utils.WeixinMessageHelper;


@SuppressWarnings("serial")
@ParentPackage(value = "servicebase")
@Actions({@Action(value = "sendmessage")})
@Results({@Result(name = "success",location = "success.jsp")})


public class SendMessage extends GuestActionBase {
	
	private String openid;
	
	private String content;
	
	public String execute() {
	
	    //JSONMessageBuilder message = new JSONMessageBuilder();
		//message.setContent(content);
		//message.setTo(openid);
		
		
		ImageJSONMessageBuilder message = new ImageJSONMessageBuilder();
		message.setTo(openid);
	    message.setContent("N4D9nDldp8IxffmUTmGfsHoOH3r5HHehoIIEc67yGvny7GtC0Nk_rpARsapTQOdy");
		
		System.out.println(WeixinMessageHelper.msg2jsonstr(message.getMessage()));
		
		
		new CustomMessageProcessor().process(message.getMessage());
		
	return SUCCESS;
	
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
	
	
	

}
