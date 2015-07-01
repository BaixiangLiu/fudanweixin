package edu.fudan.weixin.entity.payment;


public class QueryDeal extends PaymentRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4121408945133966955L;

	private static String[] signParameters = { "sysId", "itemId", "objId", "otherId",
			"projectId", "batch", "status" };

	private static String[] otherParameters = { "beginTime", "endTime" };

	public QueryDeal() {
		super(signParameters, otherParameters);
	}
	
	public String getReturnType() {
		return "data";
	}
	
	public void setReturnType() {
		//Do nothing
		return;
	}
}
