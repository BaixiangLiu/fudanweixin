package edu.fudan.weixin.entity.payment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import edu.fudan.eservice.common.utils.CommonUtil;
import edu.fudan.eservice.common.utils.Config;
import edu.fudan.eservice.common.utils.PaymentUtil;

public class PaymentResult {
	private String sysCert;
	
	private boolean simpleMode;
	
	private PaymentResponseCode resultCode;
	
	private Map<String, Object> resultSummary;
	
//	private Map<String, Object> msg;
	
	private List<Map> resultData;
	

	
	/**
	 * @param simpleMode 简单模式仅包含 resultCode
	 */
	public PaymentResult(boolean simpleMode) {
		this.sysCert = Config.getInstance().get("PAYMENT.SYSCERT");
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
		String[] signParam = {};
		boolean needSign = true;
		
		PaymentResponseCode code = PaymentUtil.getReturnCode(resultSummary);

		switch(code) {
			case SUCCESS:
			case SUCCESS_DEL:
				signParam = PaymentUtil.createResponseSignParam;
			case PAID:
			case NOTPAID:
				signParam = PaymentUtil.singleDetailSignParam;
			case SUCCESS_BATCH:
				signParam = PaymentUtil.batchSummarySignParam;
			default:
				needSign = false;
		}
		
		if(needSign) {
			if(PaymentUtil.checkSign(sysCert, signParam, resultSummary))
				this.resultSummary = resultSummary;
		} else 
			this.resultSummary = resultSummary;
			
			
	}
	
	public PaymentResponseCode getCodeFromSummary() {
		if(!CommonUtil.isEmpty(resultSummary.get("returnCode")))
			return PaymentResponseCode.translate(String.valueOf(resultSummary.get("returnCode")));
		else 
			return PaymentResponseCode.INIT_FAIL;
	}
	
	public void addResultDetail(Map<String, Object> pd) {
		if(PaymentUtil.checkSign(sysCert, PaymentUtil.batchDetailSignParam, pd))
			resultData.add(pd);
	}
	
	public int getResultDetailCount() {
		return resultData.size();
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("CODE:");
		sb.append(resultCode);
		if(!simpleMode) {
			sb.append("\nSUMMARY:\n");
			sb.append(this.resultSummary.toString());
//			if(!CommonUtil.isEmpty(msg)) {
//				sb.append("\nMSG:\n");
//				sb.append(this.msg.toString());
//			}
			sb.append("\nDATA:");
			sb.append(this.resultData.size());
			sb.append("\n");
			for(Map data : resultData) {
				sb.append(data.toString());
				sb.append("\n");
			}
		}
		return sb.toString();
	}
}