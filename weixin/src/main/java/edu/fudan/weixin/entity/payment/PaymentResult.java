package edu.fudan.weixin.entity.payment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.fudan.eservice.common.utils.CommonUtil;

public class PaymentResult {
	private boolean simpleMode;
	
	private PaymentResponseCode resultCode;
	
	private Map<String, Object> resultSummary;
	
	private List<Map> resultData;
	
	/**
	 * @param simpleMode 简单模式仅包含 resultCode
	 */
	public PaymentResult(boolean simpleMode) {
		this.simpleMode = simpleMode;
		this.resultCode = PaymentResponseCode.INIT_FAIL;
		resultSummary = new HashMap<String, Object>();
		resultData = new ArrayList<Map>();
	}

	public boolean isSimpleMode() {
		return simpleMode;
	}

	public void setSimpleMode(boolean simpleMode) {
		this.simpleMode = simpleMode;
	}

	public PaymentResponseCode getResultCode() {
		return resultCode;
	}

	public void setResultCode(PaymentResponseCode resultCode) {
		this.resultCode = resultCode;
	}
	
	public void setResultCode(String resultCode) {
		this.resultCode = PaymentResponseCode.translate(resultCode);
	}

	public Map<String, Object> getResultSummary() {
		return resultSummary;
	}

	public void setResultSummary(Map<String, Object> resultSummary) {
		this.resultSummary = resultSummary;
	}
	
	public PaymentResponseCode getCodeFromSummary() {
		if(!CommonUtil.isEmpty(resultSummary.get("returnCode")))
			return PaymentResponseCode.translate(String.valueOf(resultSummary.get("returnCode")));
		else 
			return PaymentResponseCode.INIT_FAIL;
	}
	
	public void addResultDetail(Map<String, Object> pd) {
		if(!CommonUtil.isEmpty(pd))
			resultData.add(pd);
	}
	
	public int getResultDetailCount() {
		return resultData.size();
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("CODE:");
		sb.append(resultCode);
		sb.append("\nSUMMARY:\n");
		sb.append(this.resultSummary.toString());
		sb.append("\nDATA:");
		sb.append(this.resultData.size());
		sb.append("\n");
		return sb.toString();
	}
}