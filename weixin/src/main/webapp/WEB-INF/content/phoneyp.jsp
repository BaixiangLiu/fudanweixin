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
<title>复旦信息办--电话黄页</title>
<link href="css/bootstrap.min.css" rel="stylesheet">
 <link href="css/bootstrap-theme.min.css" rel="stylesheet">
 <link href="css/font-awesome.min.css" rel="stylesheet" >
<script src="js/jquery-1.11.1.min.js"></script>
<script src="js/bootstrap.min.js"></script>

<script type="text/javascript">
  $(function(){
	 
	 $("#qkey").val(getParameterByName("qkey"));
  });
  function getParameterByName(name) {
	    var match = RegExp('[?&]' + name + '=([^&]*)').exec(window.location.search);
	    return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
	}
	function subform() {
		$
				.getJSON(
						"resource/phoneyp.act?"+$("#qform").serialize(),
						{},
						function(resp) {
							var htm="";
							if (resp.result.errcode == 0) {
								 if (resp.list.length==0||resp.list.error == "access_denied"){
										htm = "<div class='alert alert-warning'>该OAuth2.0客户端未认证</div>";}
								if (resp.list.error == "invalid_scope"){
										htm = "<div class='alert alert-warning'>该OAuth2.0客户端无查询权限</div>";}
								if (resp.list.error == null) {
								var htm = "<table class='table table-striped table-condensed '><tr><th>部门</th><th>外线号码</th><th>内线号码</th></tr> ";
								for (var i = 0; i < resp.list.list.length; i++) {
									var data = resp.list.list[i];
								
										
										htm += "<tr><td>"
												+ data.departname + "</td>";
										htm +="<td>"+data.telephonenumber+"</td>"	;	
										htm += "<td>"
												+ data.internalnumber 
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
					<div class="form-group">	<label class="control-label" for="qkey">部门名称或电话号码</label>
					
					<input type="text" class="input-sm form-control" name="qkey" id="qkey" />	</div>
										
					
					<button type="submit" class="btn btn-success btn-sm">电话查询</button>
					
					</form>
					</div>
					<div class="panel-body" id="pcontent"></div>
				</div>
			</div>

		<s:include value="/common/foot.jsp" />


	

</body>
</html>
