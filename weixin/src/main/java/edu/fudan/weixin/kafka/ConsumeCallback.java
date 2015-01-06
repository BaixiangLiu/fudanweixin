package edu.fudan.weixin.kafka;

import kafka.message.MessageAndMetadata;

public interface ConsumeCallback {
	
	void process(MessageAndMetadata<String,String> record);
	String[] getSubscribeTopics();
	
}
