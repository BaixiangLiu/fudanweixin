package edu.fudan.weixin.utils;

public class OperateResult {

	public static final OperateResult OK=new OperateResult(0);
	public static final OperateResult BADREQ=new OperateResult(400);
	public static final OperateResult NOPRG=new OperateResult(501);
	public static final OperateResult NOTBIND=new OperateResult(600);
	
	
	private int errcode;
	
	public String getErrdesc()
	{
		switch(this.errcode)
		{
		case 0: return "操作成功";
		case 400: return "请求参数不合法";
		case 501: return "无权限";
		case 600: return "尚未绑定任何账户";
		default: return "服务器内部错误";
		}
	}
	
	public OperateResult(){}

	public OperateResult(int errcode) {
		super();
		this.errcode = errcode;
	}

	public int getErrcode() {
		return errcode;
	}

	public void setErrcode(int errcode) {
		this.errcode = errcode;
	}
	
	
}
