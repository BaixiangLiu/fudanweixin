package edu.fudan.weixin.entity;

public class EcardConsume {
	/**
	 * 交易参考号
	 */
	String refno;
	/**
	 * 交易日期
	 */
	String transdate;
	/**
	 * 交易时间
	 */
	String transtime;
	/**
	 * 交易终端号
	 */
	long termid;
	/**
	 * 商户 
	 */
	String shop;
	/**
	 * 交易代码
	 */

	int transcode;
	/**
	 * 持卡人学工号
	 */
	String stuempno;
	/**
	 * 持卡人姓名
	 */
	String custname;
	/**
	 * 交易标志 1充值     2消费
	 */
	int transflag;
	/**
	 * 充值类型 1、现金，2支票，3经费本
	 */
	int paytype;
	
	/**
	 * 票据号码
	 */
	String voucherno ;
	
	/**
	 * 交易状态  3为正常，其他不正常
	 */
	int status;
	/**
	 * 错误代码
	 */
	long errcode;

	/**
	 * 说明
	 */
	String remark;
	/**
	 * 交易前金额
	 */
	float cardbefbal;
	/**
	 * 交易后金额
	 */
	float cardaftbal;
	/**
	 * 交易额
	 */
	float amount;
	
	public String getNiceTranstime()
	{
		if(transdate!=null &&transtime!=null&&transdate.length()==8&&transtime.length()==6)
		{
			StringBuffer ret=new StringBuffer();
			ret.append(transdate.subSequence(0, 4)).append("-").append(transdate.subSequence(4, 6))
			.append("-").append(transdate.subSequence(6, 8)).append(" ").append(transtime.subSequence(0, 2))
			.append(":").append(transtime.subSequence(2, 4)).append(":").append(transtime.subSequence(4, 6));
			return ret.toString();
		}else{
		return "";
		}
	}
	public String getRefno() {
		return refno;
	}
	public void setRefno(String refno) {
		this.refno = refno;
	}
	public String getTranstime() {
		return transtime;
	}
	public void setTranstime(String transtime) {
		this.transtime = transtime;
	}
	public long getTermid() {
		return termid;
	}
	public void setTermid(long termid) {
		this.termid = termid;
	}
	public String getShop() {
		return shop;
	}
	public void setShop(String shop) {
		this.shop = shop;
	}
	public int getTranscode() {
		return transcode;
	}
	public void setTranscode(int transcode) {
		this.transcode = transcode;
	}
	public String getStuempno() {
		return stuempno;
	}
	public void setStuempno(String stuempno) {
		this.stuempno = stuempno;
	}
	public String getCustname() {
		return custname;
	}
	public void setCustname(String custname) {
		this.custname = custname;
	}
	public int getTransflag() {
		return transflag;
	}
	public void setTransflag(int transflag) {
		this.transflag = transflag;
	}
	public int getPaytype() {
		return paytype;
	}
	public void setPaytype(int paytype) {
		this.paytype = paytype;
	}
	public String getVoucherno() {
		return voucherno;
	}
	public void setVoucherno(String voucherno) {
		this.voucherno = voucherno;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public long getErrcode() {
		return errcode;
	}
	public void setErrcode(long errcode) {
		this.errcode = errcode;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public float getCardbefbal() {
		return cardbefbal;
	}
	public void setCardbefbal(float cardbefbal) {
		this.cardbefbal = cardbefbal;
	}
	public float getCardaftbal() {
		return cardaftbal;
	}
	public void setCardaftbal(float cardaftbal) {
		this.cardaftbal = cardaftbal;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	
	public String getTransdate() {
		return transdate;
	}
	public void setTransdate(String transdate) {
		this.transdate = transdate;
	}
	@Override
	public String toString() {
		return "EcardConsume [refno=" + refno + ", transdate=" + transdate
				+ ", transtime=" + transtime + ", termid=" + termid + ", shop="
				+ shop + ", transcode=" + transcode + ", stuempno=" + stuempno
				+ ", custname=" + custname + ", transflag=" + transflag
				+ ", paytype=" + paytype + ", voucherno=" + voucherno
				+ ", status=" + status + ", errcode=" + errcode + ", remark="
				+ remark + ", cardbefbal=" + cardbefbal + ", cardaftbal="
				+ cardaftbal + ", amount=" + amount + "]";
	}

	
	
}
