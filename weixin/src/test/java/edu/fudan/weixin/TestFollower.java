package edu.fudan.weixin;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.fudan.weixin.utils.AccessTokenHelper;
import edu.fudan.weixin.utils.WeixinFollowerHelper;

public class TestFollower {

	@Test
	public void test() {
		//fail("Not yet implemented");
		//System.out.println(AccessTokenHelper.getInstance().getToken(AccessTokenHelper.TAC));
		WeixinFollowerHelper.FetchAllWeixinFollowers("");
		//new WeixinFollowerHelper().SetAllFollowersUnSubscribed();
		//new WeixinFollowerHelper().FetchAllWeixinFollowers("");
		//new FollowerHelper().FetchWeixinUserInfo("o9Q0DtzUvvnXZCta6181VIP1uxF8");
	}

}
