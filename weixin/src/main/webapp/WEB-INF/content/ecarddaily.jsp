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
<title>复旦信息办--一卡通每日消费</title>
<link href="css/bootstrap.min.css" rel="stylesheet">
 <link href="css/bootstrap-theme.min.css" rel="stylesheet">
<script src="js/jquery-1.11.1.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<link href="css/datepicker3.css" rel="stylesheet" />
<script src="js/bootstrap-datepicker.js" ></script>

<script type="text/javascript">
  $(function(){
	 
	  $("#bdate").val(getParameterByName("bdate"));
	  $("#edate").val(getParameterByName("edate"));
	  $(".input-daterange").datepicker({format:"yyyymmdd", language: "zh-CN", orientation: "top auto",autoclose:true});	  
  });
  function getParameterByName(name) {
	    var match = RegExp('[?&]' + name + '=([^&]*)').exec(window.location.search);
	    return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
	}
	function subform() {
		$
				.getJSON(
						"resource/yktxf.act?"+$("#qform").serialize(),
						{},
						function(resp) {
							var htm="";
							if (resp.result.errcode == 0) {
								 if (resp.list.length==0||resp.list[0].error == "access_denied"){
										htm = "<div class='alert alert-warning'>尚未完成绑定操作，请重新对此UIS账号进行绑定。</div>";}
								if (resp.list[0].error == "invalid_scope"){
										htm = "<div class='alert alert-warning'>未对此UIS账号的一卡通项目授权，请发送语音或文字消息【修改授权】并按提示进行操作</div>";}
								if (resp.list[0].error == null) {
								var htm = "<table class='table table-striped table-condensed '><tr><th>日期</th><th>消费金额</th></tr> ";
								for (var i = 0; i < resp.list[0].list.length; i++) {
									var data = resp.list[0].list[i];
								
										
										htm += "<tr><td>"
												+ data.fsrq + "</td>";
										htm += "<td>"
												+ data.amount
												+ "</td></tr>";										
									}
									htm+="</table>";
								}

								$("#pcontent").html(htm);
							} else {
								$("#pcontent").html(
										"<div class='alert alert-danger'>"
												+ resp.result.errdesc
												+ "</div>");
							}
						});
		return false;
	}
</script>
</head>

<body>

	<div class="container-fluid">
	<s:include value="/common/wxnav.jsp" />

				<s:actionerror />
				<div class=" panel panel-primary">
					<div class="panel-heading">
					<form class="form-inline" role="form" onsubmit="return subform()" id="qform">
					<div class="form-group">					
					<s:select list="@edu.fudan.weixin.utils.BindingHelper@getBinding(#session.openid)" cssClass="input-sm form-control" name="uisid" listValue="uisid+' ('+username+' )'" listKey="uisid" />
					</div>
					<div class="form-group">
					<div class="input-daterange input-group" id="datepicker">
    <input type="text" class="input-sm form-control" name="bdate" id="bdate" />
    <span class="input-group-addon">至</span>
    <input type="text" class="input-sm form-control" name="edate"  id="edate"/>
</div>
					</div>
					
					<button type="submit" class="btn btn-success btn-sm">查 &nbsp; 询</button>
					
					</form>
					</div>
					<div class="panel-body" id="pcontent"></div>
				</div>
			</div>

	

		<s:include value="/common/foot.jsp" />


	

</body>
</html>
