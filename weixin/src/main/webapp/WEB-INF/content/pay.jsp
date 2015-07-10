<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
<title>复旦信息办--准备支付</title>
<link href="css/bootstrap.min.css" rel="stylesheet">
 <link href="css/bootstrap-theme.min.css" rel="stylesheet">
 <link href="css/font-awesome.min.css" rel="stylesheet" >
<script src="js/jquery-1.11.1.min.js"></script>
<script src="js/bootstrap.min.js"></script>
</head>

<body>

	<div class="container-fluid">
				<s:include value="/common/wxnav.jsp" />
			
					<s:actionerror />
					
					<table class="table table-striped table-condensed">
					<s:iterator value="binds">
					<tr><td><s:property value="uisid" /></td><td><s:property value="username" /></td><td><s:property value="usertype"/></td>
					<td><a class="btn btn-warning btn-sm"  href="pay.act?uisid=<s:property value="uisid" />" >使用此身份</a></td>
					</tr>
					</s:iterator>
					</table>
					<p align="center">
					<a class="btn btn-success btn-sm"  href="bindin.act">增加绑定账号</a>
					</p>
					</div>

			

				<s:include value="/common/foot.jsp" />


	
	
</body>
</html>
