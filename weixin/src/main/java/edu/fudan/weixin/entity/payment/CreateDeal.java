package edu.fudan.weixin.entity.payment;


public class CreateDeal extends PaymentRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2294415331873752070L;
	
	private static String[] signParameters = {
			"sysId", 
			"itemId", 
			"objId", 
			"otherId", 
			"objName",
	        "amount", 
	        "remove",
	        "returnType",
	        "specialValue", 
	        "returnURL"};
	
	private static String[] otherParameters = {
			"autoSubmit"
		};
	
	public CreateDeal(){
		super(signParameters, otherParameters);
	}
	

}
