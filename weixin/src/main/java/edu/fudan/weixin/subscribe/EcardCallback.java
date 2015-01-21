package edu.fudan.weixin.subscribe;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import edu.fudan.weixin.entity.EcardConsume;
import edu.fudan.weixin.kafka.PrintCallback;
import edu.fudan.weixin.model.TemplateMessage;
import kafka.message.MessageAndMetadata;

public class EcardCallback extends PrintCallback {
	static Log log=LogFactory.getLog(EcardCallback.class);
	@Override
	public void process(MessageAndMetadata<String, String> m) {
		
		
		
		
		
		Object obj=BooksHolder.INSTANCE.getItem(m.key(), "ecard_consume");
		String openid=BooksHolder.INSTANCE.getOpenid(m.key());
		EcardConsume consume=null;
		if (obj instanceof Number)
		{
			consume=JSON.parseObject(m.message(), EcardConsume.class);
			if(consume.getAmount()>((Number)obj).floatValue())
			{
				sendConsume(consume,openid);
			}
		}
		obj=BooksHolder.INSTANCE.getItem(m.key(), "ecard_low");
		if (obj instanceof Number)
		{
			if(consume==null)
				consume=JSON.parseObject(m.message(), EcardConsume.class);
			if(consume.getCardaftbal()<((Number)obj).floatValue())
			{
				sendLow(consume,openid);
			}
		}
		if(consume!=null)
		log.info(consume);
		

	
}
	
	private void sendConsume(EcardConsume consume,String openid){
		
		if(consume==null||consume.getTransflag()!=1 &&consume.getTransflag()!=2) return;
		DBObject data=new BasicDBObject().append("first", "亲爱的"+consume.getCustname()+"("+consume.getStuempno()+")，您的一卡通在 "+consume.getShop()+" 产生了一笔交易。");
		data.put("keyword1", consume.getTranstime());		
		switch(consume.getTransflag())
		{
		case 1: data.put("keyword2", "充值");break;
		case 2:data.put("keyword2", "消费");break;
		default: data.put("keyword2","未知");		
		}
		data.put("keyword3", consume.getAmount()+"元");
		data.put("keyword4",consume.getStatus()==3?"交易成功":consume.getRemark());
		data.put("keyword5", consume.getCardaftbal()+"元");
		data.put("remark", "本提醒不作为入账凭证，最终交易结果和余额以一卡通系统为准。");
		
		log.info(TemplateMessage.send("ecard_consume", openid, data));
		
	}
	private void sendLow(EcardConsume consume,String openid){
		
		if(consume==null||consume.getTransflag()!=1 &&consume.getTransflag()!=2) return;
		DBObject data=new BasicDBObject().append("first", "您的一卡通余额即将用完。");
		data.put("name", consume.getCustname()+"("+consume.getStuempno()+")");		
		
		data.put("money", consume.getCardaftbal()+"元");
		data.put("remark", "为了不影响您的正常消费请及时充值。");		
		log.info(TemplateMessage.send("ecard_low", openid, data));
		
	}
	@Override
	public String[] getSubscribeTopics() {	
		return new String[]{"ecardtest","ecard"};
	}
	
	
}
