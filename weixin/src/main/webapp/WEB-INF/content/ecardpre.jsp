<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
<title>复旦信息办--一卡通充值</title>
<link href="css/bootstrap.min.css" rel="stylesheet">
 <link href="css/bootstrap-theme.min.css" rel="stylesheet">
 <link href="css/font-awesome.min.css" rel="stylesheet" >
<script src="js/jquery-1.11.1.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script type="text/javascript">
function pay(idx)
{
	amount=0;
	if($("#account"+idx+"0")[0].checked)
		amount=200;
	if($("#account"+idx+"1")[0].checked)
		amount=100;
	if($("#account"+idx+"2")[0].checked)
		amount=50;
	if(amount<=0)
		amount=$("#naccount"+idx).val();
	amount=Math.abs(Math.floor(amount));
	$.post("ecardpay.act",{"uisid":$("#uisid"+idx).val(),"amount":amount},
	function (data){
		if(data.retcode==0) 
			location=decodeURIComponent(data.url);
		else
			alert(data.retmsg);
	},"json"	);
	}
</script>

</head>

<body>

	<div class="container-fluid">
				<s:include value="/common/wxnav.jsp" />
			
					<s:actionerror />
					
				<div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
					<s:iterator value="binds" status="lst">
		
					
  <div class="panel panel-default">
    <div class="panel-heading"  role="tab"  id="heading<s:property value="#lst.index" />" >
      <h4 class="panel-title">
        <a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapse<s:property value="#lst.index" />" aria-expanded="true" aria-controls="collapse<s:property value="#lst.index" />">
          <s:property value="username" />(<s:property value="uisid" />)
        </a>
      </h4>
    </div>
    <div id="collapse<s:property value="#lst.index" />" class="panel-collapse collapse <s:if test="#lst.first"> in </s:if>" role="tabpanel" aria-labelledby="heading<s:property value="#lst.index" />">
      <div class="panel-body">
      
      <s:if test="unpaid!=null&& unpaid.size()>0">
    <h4><span class="label label-primary" > 您的未付款订单(可直接进行付款)</span></h4>
      <table class="table table-striped table-condensed">
     
      <tr><th>学/工号</th><th>订单金额</th><th>生成时间</th><th>支付</th></tr>
     
      <s:iterator value="unpaid">
      <tr><td><s:property value="stuempno" /></td><td><s:property value="amount"  /></td><td><s:property value="datetime.substring(0,8)" /></td><td><a href="pay.act?uisid=<s:property value="stuempno" />" class="btn btn-success" >支付</a></td></tr>
      </s:iterator>
    
      </table>
      </s:if>
      <h4><span class="label label-primary">新订单</span></h4>
      <form class="form-inline">
      <input type="hidden" id="uisid<s:property value="#lst.index" />" value="<s:property value="uisid" />" />
      <label class="radio-inline">
  <input type="radio" name="account<s:property value="#lst.index" />" id="account<s:property value="#lst.index" />0" value="200"> 200元
</label>
 <label class="radio-inline">
  <input type="radio" name="account<s:property value="#lst.index" />" id="account<s:property value="#lst.index" />1" value="100" checked="checked"> 100元
</label>
 <label class="radio-inline">
  <input type="radio" name="account<s:property value="#lst.index" />" id="account<s:property value="#lst.index" />2" value="50"> 50元
</label>
 <label class="radio-inline">
  <input type="radio" name="account<s:property value="#lst.index" />" id="account<s:property value="#lst.index" />3" value="0"> 
  <input type="text"  class="form-control" name="naccount<s:property value="#lst.index" />" id="naccount<s:property value="#lst.index" />"   placeholder="请输入整数金额" style="width:100px" />元
</label>
<button type="button" class="btn btn-warning" onclick="pay(<s:property value="#lst.index" />)">充值</button>
</form>
       </div>
    </div>
  </div>
					
					
					</s:iterator>
				</div>
				
				
					<p align="center">
					<a class="btn btn-success btn-sm"  href="bindin.act">增加绑定账号</a>
					</p>
					</div>

			

				<s:include value="/common/foot.jsp" />


	
	
</body>
</html>
