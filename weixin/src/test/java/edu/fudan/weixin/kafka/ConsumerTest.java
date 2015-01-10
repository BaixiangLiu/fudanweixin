package edu.fudan.weixin.kafka;

import edu.fudan.eservice.common.utils.ThreadPoolHelper;
import edu.fudan.weixin.subscribe.BooksHolder;
import edu.fudan.weixin.subscribe.EcardCallback;

public class ConsumerTest {
	
	public static void main(String... args) 
	{
		KafkaConsumerHelper hp=	KafkaConsumerHelper.getInstance();
		
		hp.addCallback(new PrintCallback());
		
		hp.addCallback(new EcardCallback());
		
		hp.start();
		
		try {
			Thread.sleep(30000L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	//	hp.stop();
	//	ThreadPoolHelper.getInstance().getSchPool().shutdown();
	}

}
