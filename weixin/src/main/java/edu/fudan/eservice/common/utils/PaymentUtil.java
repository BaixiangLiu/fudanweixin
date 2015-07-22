package edu.fudan.eservice.common.utils;

import java.util.Map;

import edu.fudan.weixin.entity.payment.PaymentResponseCode;

public class PaymentUtil {
	
	public static String[] createResponseSignParam = {
		"returnCode",
		"sysId",
		"itemId",
		"objId",
		"otherId",
		"objName",
		"amount",
		"projectId",
		"payId",
		"payPassword",
		"specialValue",
		"returnURL"
		};
	
	public static String[] batchSummarySignParam = {
		"returnCode",
		"sysId", 
		"itemId", 
		"projectId", 
		"count"};
	
	public static String[] singleDetailSignParam = {
		"returnCode",
		"sysId",
		"itemId",
		"objId",
		"otherId",
		"objName",
		"amount",
		"paid",
		"refund",
		"overTime",
		"status",
		"projectId",
		"payId",
		"payPassword",
		"specialValue",
		"payType"};
	
	public static String[] batchDetailSignParam = {
		"objId",
		"otherId",
		"objName",
		"amount",
		"paid",
		"refund",
		"overTime",
		"status",
		"payId",
		"payPassword",
		"specialValue",
		"payType"};
	
	
	public static PaymentResponseCode getReturnCode(Map<String, Object> values) {
		if(!CommonUtil.isEmpty(values)) {
			String returnCode = (String)values.get("returnCode");
			if(!CommonUtil.isEmpty(returnCode))
				return PaymentResponseCode.translate(returnCode);
		}
		return PaymentResponseCode.EMPTY_SUMMARY;
	}
	
	/**
	 * 检查签名字串有效性
	 * @return 
	 */
	public static boolean checkSign(String Sign, String sysCert, String[] signParameters, Map<String, Object> values) {
		return Sign.equals(createSign(sysCert, signParameters, values));
			
	}
	
	public static boolean checkSign(String sysCert, String[] signParameters, Map<String, Object> values) {
		if(!CommonUtil.isEmpty(values)) {
			String sign = (String)values.get("sign");
			if(!CommonUtil.isEmpty(sign))
				return checkSign(sign, sysCert, signParameters, values);
		}
		return false;
	}
	
	/**
	 * 生成签名字串
	 * @return 
	 */
	public static String createSign(String sysCert, String[] signParameters, Map<String, Object> values) {
		char firstSC = sysCert.charAt(0);
		
		StringBuffer buf = new StringBuffer(512);
		for (int i = 0; i < signParameters.length; i++) 
		{
			Object value = values.get(signParameters[i]);
			//即使参数为空依然添加首字母
			buf.append(firstSC);
			if (value == null) 
			{
				continue;
			}
			buf.append(value);
		}
		buf.append(sysCert);
		return EncodeHelper.digest(buf.toString(), "MD5");
	}

}
