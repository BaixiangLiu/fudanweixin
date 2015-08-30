<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
<title>复旦信息办--一卡通</title>
<link href="css/bootstrap.min.css" rel="stylesheet">
 <link href="css/bootstrap-theme.min.css" rel="stylesheet">
 <link href="css/font-awesome.min.css" rel="stylesheet" >
<script src="js/jquery-1.11.1.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script type="text/javascript">
$(
	function(){		
		$.getJSON("resource/yktxx.act",{},function(resp){
			
			if(resp.result.errcode==0)
			{
				var htm="";
				for(var i=0;i<resp.list.length;i++)
				{
					var data=resp.list[i];
					htm+="<div class=' panel panel-primary'><div class='panel-heading'>"+data.uisid+" ("+data.username+")</div><div class='panel-body'>";
					if(data.error=="access_denied") 
						htm+="<div class='alert alert-warning'>尚未完成绑定操作，请重新对此UIS账号进行绑定。</div>";
					if(data.error=="invalid_scope")
						htm+="<div class='alert alert-warning'>未对此UIS账号的一卡通项目授权，请发送语音或文字消息【修改授权】并按提示进行操作</div>";
					if(data.error==null)
					{
						htm+="<table class='table table-striped table-condensed '>";
						htm+="<tr><td>卡类型</td><td>"+data.card_type+"</td></tr>";
						htm+="<tr><td>卡状态</td><td>"+data.card_state+"</td></tr>";
						htm+="<tr><td>余额</td><td>"+data.card_balance+"元</td></tr></table>";						
						}
					htm+="</div></div>";	
				}	
				
					$("#pcontent").html(htm);
			}else
			{
					$("#pcontent").html("<div class='alert alert-danger'>"+resp.result.errdesc+"</div>");
			}		
		});
	}
);
</script>
</head>

<body>

	<div class="container-fluid">
				<s:include value="/common/wxnav.jsp" />
				
	
					<div  id="pcontent">
					<s:actionerror />
					
					</div>
					<p align="center">
					<a class="btn btn-success btn-sm"  href="ecarddaily.act">日交易额查询</a>
					<a class="btn btn-success btn-sm"  href="ecardpre.act">一卡通充值</a>
					</p>
				</div>

				<s:include value="/common/foot.jsp" />


		
		
</body>
</html>
