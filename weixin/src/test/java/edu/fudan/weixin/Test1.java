package edu.fudan.weixin;

import org.apache.struts2.ServletActionContext;
import org.junit.Test;

import edu.fudan.eservice.common.utils.Config;

public class Test1 {
	
	@Test
	public void test(){
		//System.out.println(ServletActionContext.getServletContext().getContextPath());
		System.out.println(Config.getInstance().get("weixin.lang"));
	}

}
