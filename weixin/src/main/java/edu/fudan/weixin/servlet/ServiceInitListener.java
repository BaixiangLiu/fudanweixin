package edu.fudan.weixin.servlet;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import edu.fudan.eservice.common.utils.ThreadPoolHelper;
import edu.fudan.weixin.kafka.KafkaConsumerHelper;
import edu.fudan.weixin.kafka.PrintCallback;
import edu.fudan.weixin.subscribe.DailyBalanceThread;
import edu.fudan.weixin.subscribe.EcardCallback;
import edu.fudan.weixin.subscribe.ElectricBalanceThread;
import edu.fudan.weixin.subscribe.ElectricLowThread;

public class ServiceInitListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
		//start kafka consumer
		try{
		KafkaConsumerHelper hp=	KafkaConsumerHelper.getInstance();			
		hp.addCallback(new EcardCallback());		
		hp.start();
		
		//晚上8:00发送一卡通每日余额提醒
		Calendar c=Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 20);
		c.set(Calendar.MINUTE, 0);
		long now=System.currentTimeMillis();
		long diff=c.getTimeInMillis()-now;
		if(diff<0)
			diff+=3600000L*24;
		ThreadPoolHelper.getInstance().getSchPool().scheduleAtFixedRate(new DailyBalanceThread(), diff, 3600000L*24,TimeUnit.MILLISECONDS);
		
		//早上8:15发送电费提醒
		c.set(Calendar.HOUR_OF_DAY, 8);
		c.set(Calendar.MINUTE,15);
		diff=c.getTimeInMillis()-now;
		if(diff<0)
			diff+=3600000L*24;
		ThreadPoolHelper.getInstance().getSchPool().scheduleAtFixedRate(new ElectricLowThread(), diff, 3600000L*24,TimeUnit.MILLISECONDS);
		ThreadPoolHelper.getInstance().getSchPool().scheduleAtFixedRate(new ElectricBalanceThread(), diff+180000, 3600000L*24,TimeUnit.MILLISECONDS);

		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		KafkaConsumerHelper.getInstance().stop();
		ThreadPoolHelper.getInstance().getSchPool().shutdown();

	}

}
