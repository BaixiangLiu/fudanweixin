package edu.fudan.weixin.kafka;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;

public class PrintCallback implements ConsumeCallback {

	@Override
	public void process(MessageAndMetadata<String,String> m) {
		
		
	
			System.out.println(m.key()+"-"+m.message());
	
		
	}

	@Override
	public String[] getSubscribeTopics() {	
		return new String[]{ "test","ecardtest"};
	}
	
	@Override
	public int hashCode()
	{
		return this.getClass().getName().hashCode();
	}

}
