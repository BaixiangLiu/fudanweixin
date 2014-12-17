<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>接入用户信息</title>
<jsp:include page="topInclude.jsp"></jsp:include>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-md-12">
				<div>
					
					<s:if test="entity == null || entity.getBinds() == null || entity.getBinds().size() <= 0">
					<p>该用户还没有绑定UIS帐号！</p>
					</s:if>
					<s:else>					
					<p>该用户已绑定UIS帐号如下：</p>
					<s:iterator value="entity.binds">
					<p>学号：<s:property value="uisid" /></p>
					<p>姓名：<s:property value="username" /></p>
					<p>邮箱：<s:property value="email" /></p>
					<p>类别：<s:property value="usertype" /></p>
					</s:iterator>
				</s:else>	
				</div>
			</div>
		</div>
	</div>
</body>
</html>