<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String base = request.getContextPath();
%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<script type="text/javascript">
var baseUrl = "<%=base %>/";
</script>
</head>
<body>
	<nav class="navbar navbar-default navbar-fixed-top navbar-inverse" role="navigation">
		<div class="container">
			<div class="navbar-header">
				<buton type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#dropdown-menu"> <span class="sr-only">下拉导航</span> <span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span> </buton>
				<a class="navbar-brand" href="<%=base%>/webhome.act" style="font-weight: bolder; color: white;"> 复旦微信后台 </a>
			</div>
			<div class="collapse navbar-collapse" id="dropdown-menu">
				<ul class="nav navbar-nav">
					<li class="<% if(request.getRequestURL().indexOf("webhome") > 0){out.print("active");}%>">
						<a href="<%=base %>/webhome.act">首页</a>
					</li>
					<li class="<% if(request.getRequestURL().indexOf("webonlineReply") > 0){out.print("active");}%>">
						<a href="<%=base %>/webonlineReply.act">在线问答</a>
					</li>
					<li class="<% if(request.getRequestURL().indexOf("webfollowers") > 0){out.print("active");}%>">
						<a href="<%=base %>/webfanslist.act">粉丝管理</a>
					</li>
				</ul>

				<ul class="nav navbar-nav navbar-right">
					<li>
						<a id="quit" href="<%=base %>/webuserlogout.act">(${session.webuser.name }) 注销登录</a>
					</li>
				</ul>
			</div>
		</div>
	</nav>
	
	<div style="height: 60px;">
	</div>
	
</body>
</html>