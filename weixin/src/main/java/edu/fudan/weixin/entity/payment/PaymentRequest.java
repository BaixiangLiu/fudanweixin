package edu.fudan.weixin.entity.payment;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import edu.fudan.eservice.common.utils.CommonUtil;
import edu.fudan.eservice.common.utils.Config;
import edu.fudan.eservice.common.utils.EncodeHelper;

public abstract class PaymentRequest implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 8777998961581111274L;
	
	/**
	 * 编码参数需顺序严格按照数组顺序组装,
	 */
	private String[] signParameters;
	
	private String[] otherParameters;
		
	
	public String[] getSignParameters() {
		return signParameters;
	}

	public void setSignParameters(String[] signParameters) {
		this.signParameters = signParameters;
	}

	public String[] getOtherParameters() {
		return otherParameters;
	}

	public void setOtherParameters(String[] otherParameters) {
		this.otherParameters = otherParameters;
	}

	private Map<String, Object> values;
	

	public Map<String, Object> getValues() {
		return values;
	}

	public void setValues(Map<String, Object> values) {
		this.values = values;
	}
	
	private String sysCert = "0";

	public String getSysCert() {
		return sysCert;
	}

	public void setSysCert(String sysCert) {
		if(!CommonUtil.isEmpty(sysCert))
			this.sysCert = sysCert;
	}
	
	
	public PaymentRequest(String[] signParameters, String[] otherParameters) {
		this.signParameters = signParameters;
		this.otherParameters = otherParameters;
		setSysCert(Config.getInstance().get("PAYMENT.SYSCERT"));
		initValues();
	}
	
	private void initValues() {
		values = new HashMap<String, Object>();
		//默认返回数据为 data 方式
		setReturnType("data");
	}

	/**
	 * 生成签名字串
	 * @return 
	 */
	public String createSign() {
		char firstSC = sysCert.charAt(0);
		
		StringBuffer buf = new StringBuffer(512);
		for (int i = 0; i < this.signParameters.length; i++) 
		{
			Object value = this.values.get(this.signParameters[i]);
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
	
	public void setValue(String key, String value) {
		if(!CommonUtil.isEmpty(value))
			values.put(key, value);
	}
	
	public void setSysId(String sysId) {
		setValue("sysId", sysId);
	}
	
	public void setItemId(String itemId) {
		setValue("itemId", itemId);
	}

	public String getReturnType() {
		return values.get("returnType").toString();
	}

	public void setReturnType(String returnType) {
		setValue("returnType", returnType);
	}
	

}
