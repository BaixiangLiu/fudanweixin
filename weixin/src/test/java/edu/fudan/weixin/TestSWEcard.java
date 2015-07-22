package edu.fudan.weixin;

import java.util.Date;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.alibaba.fastjson.JSON;

import edu.fudan.weixin.model.SWEcardModel;

public class TestSWEcard {

	@Test
	public void query()
	{
		//Map<String,Object> m=SWEcardModel.query("", "8738900", null	, null,1,10);
		Map<String,Object> m=SWEcardModel.query("04538", "", null	, null,1,10);
		System.out.println(JSON.toJSON(m));
		Assert.assertEquals(0,m.get("retcode"));
		
		System.out.println(JSON.toJSON(SWEcardModel.unpaid("04538")));
	}
	
	public void testorder()
	{
		Map<String,Object> m=SWEcardModel.order(null,"04538", 2);
		System.out.println(JSON.toJSON(m));
		Assert.assertEquals(0,m.get("retcode"));
	}
	
}
