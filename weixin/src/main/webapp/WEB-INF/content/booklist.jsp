<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
<title>复旦信息办--信息订阅</title>
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/bootstrap-theme.min.css" rel="stylesheet">
<script src="js/jquery-1.11.1.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script type="text/javascript">
function setbook(item,obj,threshold)
{
	$.post("book.act",{"item":item,"book":obj.checked,"threshold":threshold},function(resp){
			$("#mmc").html(resp.errdesc);
			$("#mm").modal("show");
		if(resp.errcode!=0)
			obj.checked=!obj.checked;
	},"json");
	}
</script>
</head>

<body>
	<div class="modal fade" tabindex="-1" role="dialog" aria-hidden="true"
		id="mm" style="margin-top: 100px">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">操作结果</h4>
				</div>
				<div class="modal-body" id="mmc">...</div>
			</div>
		</div>
	</div>

	<div class="container-fluid">
		<s:include value="/common/wxnav.jsp" />

		
		

		<div class=" col-sm-6 col-md-4 ">
			<div class=" panel panel-primary">
				<div class="panel-heading">一卡通</div>
				<div class="panel-body">
					<label><input type="checkbox"
						onclick="setbook('ecard_balance',this)"
						<s:if test="'ecard_balance' in items.keySet()">checked="checked"</s:if> />每日余额提醒
					</label><br /> <label><input type="checkbox"
						onclick="setbook('ecard_low',this,$('#balance_threshold').val())"
						<s:if test="'ecard_low' in items.keySet()">checked="checked"</s:if>
						id="balance_check" />低余额提醒：余额低于</label> <input type="text"
						class="input-sm form-control" style="width: 65px; display: inline"
						id="balance_threshold"
						value="<s:property value="items.get('ecard_low')==null?'10':items.get('ecard_low')" />"
						onchange="setbook('ecard_low',$('#balance_check')[0],this.value)" /><label>元时</label>
					<br /> <label><input type="checkbox"
						onclick="setbook('ecard_consume',this,$('#consume_threshold').val())"
						id="consume_check"
						<s:if test="'ecard_consume' in items.keySet()">checked="checked"</s:if> />大额消费入账提醒：金额大于
					</label> <input type="text" class="input-sm form-control"
						id="consume_threshold" style="width: 65px; display: inline"
						value="<s:property value="items.get('ecard_consume')==null?'20':items.get('ecard_consume')" />"
						onchange="setbook('ecard_consume',$('#consume_check')[0],this.value)" /><label>元时</label>
				</div>
			</div>
		</div>


</div>
<div class="alert alert-warning">本功能正在建设中，部分订阅消息可能无法收到</div>


	<s:include value="/common/foot.jsp" />



</body>
</html>
