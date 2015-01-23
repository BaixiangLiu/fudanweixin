<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>接入用户信息</title>
<link href="css/bootstrap.min.css" rel="stylesheet">
 <link href="css/bootstrap-theme.min.css" rel="stylesheet">
<script src="js/jquery-1.11.1.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script type="text/javascript">
function MCS_ClientNotify(EventData) {
    EventData = strToJson(EventData);
    switch(EventData['event']){
        case 'OnUserChange':{
            OnUserChange(EventData);
            break;
        }
        case 'OnMapMsgClick':{
            OnMapMsgClick(EventData);
            break;
        }
    }
}
function strToJson(str){
	var json = (new Function("return " + str))();
	return json;
}

function OnUserChange(EventData)
{
	$.getJSON("dkfuser.act?openid="+EventData.useraccount, function (data){
		if(data==null||data.binds==null||data.binds.length<=0)
		{
			$("#userinfo").html("该用户尚未绑定UIS");	
			}
		else
		{
			var txt="";
			for(i=0;i<data.binds.length;i++)
			{
				txt+="<p>学/工号："+data.binds[i].uisid+"</p><p>姓名："+data.binds[i].username+"</p><p>Email:"+data.binds[i].email;
				txt+="</p><p>用户类别："+data.binds[i].usertype+"</p><p> &nbsp; </p>";
				}
			}
		$("#userinfo").html(txt);
	});
}
</script>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-md-12">
				<div>
					
							
					<p>该用户已绑定UIS帐号如下：</p>
					<div id="userinfo" ></div>
					
					
				
				
				</div>
			</div>
		</div>
	</div>
</body>
</html>