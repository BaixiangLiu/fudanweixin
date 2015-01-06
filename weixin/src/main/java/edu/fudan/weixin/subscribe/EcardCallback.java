package edu.fudan.weixin.subscribe;

import com.alibaba.fastjson.JSON;

import edu.fudan.weixin.entity.EcardConsume;
import edu.fudan.weixin.kafka.PrintCallback;
import kafka.message.MessageAndMetadata;

public class EcardCallback extends PrintCallback {

	@Override
	public void process(MessageAndMetadata<String, String> m) {
		
		
		EcardConsume consume=JSON.parseObject(m.message(), EcardConsume.class);
		Object obj=BooksHolder.INSTANCE.getItem(consume.getStuempno(), "ecard_consume");
		if (obj instanceof Number)
		{
			if(consume.getAmount()>((Number)obj).floatValue())
			{
				sendConsume(consume);
			}
		}
		obj=BooksHolder.INSTANCE.getItem(consume.getStuempno(), "ecard_low");
		if (obj instanceof Number)
		{
			if(consume.getCardaftbal()<((Number)obj).floatValue())
			{
				sendLow(consume);
			}
		}
		
		System.out.println(consume);

	
}
	
	private void sendConsume(EcardConsume consume){}
	private void sendLow(EcardConsume consume){}
	@Override
	public String[] getSubscribeTopics() {	
		return new String[]{"ecardtest"};
	}
	
	
}
