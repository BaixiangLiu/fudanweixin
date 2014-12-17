
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<% 
if(request.getRemoteAddr().equals("127.0.0.1")||request.getRemoteAddr().equals("10.240.0.36"))
	session.setAttribute("openid","oew-cuP8H1G_B2a-Rx4q5_Z3TEeA"); 
%>
发送成功
</body>
</html>