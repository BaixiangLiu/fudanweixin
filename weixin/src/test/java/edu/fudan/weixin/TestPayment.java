package edu.fudan.weixin;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.fudan.eservice.common.utils.CommonUtil;
import edu.fudan.weixin.entity.payment.CreateDeal;
import edu.fudan.weixin.entity.payment.PaymentResponseCode;
import edu.fudan.weixin.entity.payment.QueryDeal;
import edu.fudan.weixin.model.PaymentInfoModel;

public class TestPayment {
	@Test
	public void testCDSign() throws Exception {
		CreateDeal pi = new CreateDeal();
		pi.setSysCert("abcdef");
		pi.setSysId("001");
		pi.setItemId("001-01");
		pi.setValue("objId", "1001");
		pi.setValue("objName", "测试");
		pi.setValue("amount", "10");
		pi.setValue("returnType", "data");
		assertEquals("5991dc6f9639150357a22ca78c7b83d9",pi.createSign());
	}
	
	@Test
	public void testQRSign() throws Exception {
		QueryDeal pi = new QueryDeal();
		pi.setSysCert("27ysZAzp5sHI21Tp6jSg2an41Br0F1gFEqvgnk2A2FpLqcNLnoPDhExwuTrPmrHkktHVXZVk6LTMCq98YdduD1pWGScp1Irnvhjs8Hk4pbKV8laH2ExhltiLorzEY2IY");
		pi.setSysId("032");
		pi.setItemId("032-01");
		pi.setValue("objId", "123");
		assertEquals("ada9d6ea837a98b85ae70b475ba41ddf",pi.createSign());
	}
	
	
	@Test
	public void testAddPayment() throws Exception {
		CreateDeal pi = new CreateDeal();
		pi.setSysCert("edCGF1MSbvSj5lWrhhph2mZxm9DnXTPOwh40zT47TxgF1SsFBVn9br4RPu9h7ZgaDCdaQJNMkYhuELnXB4cwOeQ27Y2MGoRAe3VTLwPvPku4f2xriCCU84h4ZDOwk6L2");
		pi.setSysId("001");
		pi.setItemId("001-01");
		pi.setValue("objId", "1001");
		pi.setValue("objName", "1001");
		pi.setValue("returnType", "1");
		
		PaymentInfoModel pim = new PaymentInfoModel(pi);
		StringBuffer status = CommonUtil.getWebContent("http://urp.test.fudan.edu.cn/pay/itemDeal3.html?"+pim.createURL());
		System.out.println(pi.getSysCert());
		System.out.println(pim.createURL());
		System.out.println(status.toString());
		System.out.println(PaymentResponseCode.translate(status.toString()));
	}
	
	@Test
	public void testCreateDeal() throws Exception {
		CreateDeal pi = new CreateDeal();
		pi.setSysCert("edCGF1MSbvSj5lWrhhph2mZxm9DnXTPOwh40zT47TxgF1SsFBVn9br4RPu9h7ZgaDCdaQJNMkYhuELnXB4cwOeQ27Y2MGoRAe3VTLwPvPku4f2xriCCU84h4ZDOwk6L2");
		pi.setSysId("001");
		pi.setItemId("001-01");
		pi.setValue("objId", "1001");
		pi.setValue("objName", "1001");
		pi.setValue("returnType", "data");
		PaymentInfoModel pim = new PaymentInfoModel(pi);
		System.out.print(pim.getResponse(false, "http://urp.test.fudan.edu.cn/pay/itemDeal3.html"));
	}
	
	@Test
	public void testQueryResult() throws Exception {
		QueryDeal pi = new QueryDeal();
		pi.setSysCert("edCGF1MSbvSj5lWrhhph2mZxm9DnXTPOwh40zT47TxgF1SsFBVn9br4RPu9h7ZgaDCdaQJNMkYhuELnXB4cwOeQ27Y2MGoRAe3VTLwPvPku4f2xriCCU84h4ZDOwk6L2");
		pi.setSysId("001");
		pi.setItemId("001-01");
		pi.setValue("objId", "1001");
		pi.setValue("objName", "1001");
		pi.setValue("returnType", "1");
		PaymentInfoModel pim = new PaymentInfoModel(pi);
		System.out.print(pim.getResponse(true, "http://urp.test.fudan.edu.cn/pay/queryPR3.html"));
	}

}

