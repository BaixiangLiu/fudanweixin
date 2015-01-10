package edu.fudan.weixin.kafka;


import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;







import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import org.junit.Test;

import com.alibaba.fastjson.JSON;

import edu.fudan.weixin.entity.EcardConsume;

public class ProducerTest {

	
	public static void main(String[] args)
	{
		Properties  conf=new Properties();
		conf.put("metadata.broker.list", "kafka1.fudan.edu.cn:9092");
		conf.put("serializer.class", "kafka.serializer.StringEncoder");
		//conf.put("request.required.acks", "1");
		
		Producer<String,String> p=new Producer<String,String>(new ProducerConfig(conf));
		String topic="ecardtest";
		for (int i=0;i<4;i++){
			EcardConsume ec=new EcardConsume();
			ec.setAmount(new Random().nextInt(40));
			ec.setCardbefbal(40);
			ec.setCardaftbal(ec.getCardbefbal()-ec.getAmount());
			ec.setStuempno("09110240017");
			ec.setCustname("王彬");
			ec.setShop("测试商户");
			ec.setTransflag(2);
			ec.setStatus(3);
			KeyedMessage<String,String> rec=new KeyedMessage<String, String>(topic, ec.getStuempno(),JSON.toJSONString(ec));
		p.send(rec);
		System.out.println("sent "+i+rec);
		
		}
		p.close();
	}
}
