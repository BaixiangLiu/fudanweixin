package edu.fudan.weixin.kafka;

import org.junit.Test;

import edu.fudan.weixin.subscribe.DailyBalanceThread;
import edu.fudan.weixin.subscribe.ElectricBalanceThread;

public class TestDailyBalance {
	@Test
	public void test1()
	{
		new ElectricBalanceThread().run();
	}
	
	
	public void test()
	{
		new DailyBalanceThread().run();
	}

}
