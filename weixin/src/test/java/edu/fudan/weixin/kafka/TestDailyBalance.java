package edu.fudan.weixin.kafka;

import org.junit.Test;

import edu.fudan.weixin.subscribe.DailyBalanceThread;

public class TestDailyBalance {
	@Test
	public void test()
	{
		new DailyBalanceThread().run();
	}

}
