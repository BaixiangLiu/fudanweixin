package edu.fudan.weixin;

import org.junit.Test;

import edu.fudan.weixin.utils.AccessTokenHelper;

public class TestAccessToken {
	
	
	@Test
	public void testAccessToken() {
		System.out.println(AccessTokenHelper.getInstance().getToken(AccessTokenHelper.TAC));
	}

}
