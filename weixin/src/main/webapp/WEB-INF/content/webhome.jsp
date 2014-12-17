<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>复旦微信后台 - 首页</title>
<jsp:include page="topInclude.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="menu.jsp"></jsp:include>

	<div class="container">
		<div class="row">
			<div class="col-md-12">
				<div>
					<div class="alert alert-info" role="alert">概览</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-4">
				<div class="thumbnail">
					<p>在线客服数量: 2</p>
					<p>在线客服:</p>
					<p>
						<a href="">xxx</a>
					</p>
					<p>
						<a href="">yyy</a>
					</p>
				</div>
			</div>
			<div class="col-md-4">
				<div class="thumbnail">
					<p>一小时内在线用户数: 99</p>
					<p>今日服务用户数: 3</p>
				</div>
			</div>
			<div class="col-md-4">
				<div class="thumbnail">
					<p>最新提问:</p>
					<p>啥啥啥啥?</p>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div>
					<div class="alert alert-info" role="alert">其它</div>
				</div>
			</div>
		</div>
	</div>

	<jsp:include page="bottomInclude.jsp"></jsp:include>
</body>
</html>