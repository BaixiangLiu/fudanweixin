package edu.fudan.weixin.entity.payment;

/**
 *   返回的代码分别为： 
 * 00：成功 
 * 01：删除成功 
 * 02：已付
 * 03：未付
 * 05：批量查询完成
 * 11：签名信息不正确
 * 12：两次信息不匹配，当收费记录已存在时会出现此错误提示 
 * 13：错误的系统编号 
 * 21：收费记录不存在，在删除时会出现此错误提示 
 * 22：收费项目不存在
 * 23：收费记录无法删除，在删除时会出现此错误提示 
 * 31：金额格式不正确 
 * 32：otherId长度超出 
 * 35：objName长度超出
 * 36：金额不在收费区间内 
 * 99：其他异常错误
 * 
 * 
 *
 * @author baixiangliu
 *
 */
public enum PaymentResponseCode {
	SIGN_NOTMATCH("-3"),
	EMPTY_SUMMARY("-2"),
	INIT_FAIL("-1"),
	SUCCESS("00"),
	SUCCESS_DEL("01"),
	PAID("02"),
	NOTPAID("03"),
	SUCCESS_BATCH("05"),
	ERROR_SIGN("11"),
	ERROR_NOTMATCH("12"),
	ERROR_SYSNO("13"),
	ERROR_NORECORD("21"),
	ERROR_NOITEM("22"),
	ERROR_DELFAIL("23"),
	ERROR_AMOUNT("31"),
	ERROR_OTHERID("32"),
	ERROR_OBJNAME("35"),
	ERROR_REGION("36"),
	ERROR_OTHER("99");
	
	private final String Code;
	
	PaymentResponseCode(String code) {
		this.Code = code;
	}
	
 	public static PaymentResponseCode translate(String code) {
		switch(code)
		{
			case "-3":
				return PaymentResponseCode.SIGN_NOTMATCH;
			case "-2":
				return PaymentResponseCode.EMPTY_SUMMARY;
			case "-1":
				return PaymentResponseCode.INIT_FAIL;
			case "00":
				return PaymentResponseCode.SUCCESS;
			case "01":
				return PaymentResponseCode.SUCCESS_DEL;
			case "02":
				return PaymentResponseCode.PAID;
			case "03":
				return PaymentResponseCode.NOTPAID;	
			case "05":
				return PaymentResponseCode.SUCCESS_BATCH;	
			case "11":
				return PaymentResponseCode.ERROR_SIGN;
			case "12":
				return PaymentResponseCode.ERROR_NOTMATCH;
			case "13":
				return PaymentResponseCode.ERROR_SYSNO;
			case "21":
				return PaymentResponseCode.ERROR_NORECORD;
			case "22":
				return PaymentResponseCode.ERROR_NOITEM;
			case "23":
				return PaymentResponseCode.ERROR_DELFAIL;
			case "31":
				return PaymentResponseCode.ERROR_AMOUNT;
			case "32":
				return PaymentResponseCode.ERROR_OTHERID;
			case "35":
				return PaymentResponseCode.ERROR_OBJNAME;
			case "36":
				return PaymentResponseCode.ERROR_REGION;
			default:
				return PaymentResponseCode.ERROR_OTHER;
		}
 	}
	

}
