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

public class ServiceInitListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
		//start kafka consumer
		try{
		KafkaConsumerHelper hp=	KafkaConsumerHelper.getInstance();			
		hp.addCallback(new EcardCallback());		
		hp.start();
		
		Calendar c=Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 20);
		c.set(Calendar.MINUTE, 0);
		long now=System.currentTimeMillis();
		long diff=c.getTimeInMillis()-now;
		if(diff<0)
			diff+=3600000L*24;
		ThreadPoolHelper.getInstance().getSchPool().scheduleAtFixedRate(new DailyBalanceThread(), diff, 3600000L*24,TimeUnit.MILLISECONDS);
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
