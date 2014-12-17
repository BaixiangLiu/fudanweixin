<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>复旦微信后台 - 用户登录</title>
<link type="text/css" rel="stylesheet" href="resource/thirdparty/bootstrap-3.2/css/bootstrap.min.css" />
<link type="text/css" rel="stylesheet" href="resource/thirdparty/bootstrap-3.2/css/bootstrap-theme.min.css" />
<link type="text/css" rel="stylesheet" href="resource/thirdparty/uitotop/css/ui.totop.css" />
<script type="text/javascript" src="resource/thirdparty/jquery-1.11/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="resource/thirdparty/jquery-1.11/jquery-migrate-1.2.1.min.js"></script>
<!--[if lt IE 9]>
<script src="resource/thirdparty/html5shiv/html5shiv.min.js"></script>
<script src="resource/thirdparty/respond/respond.min.js"></script>
<![endif]-->
</head>
<body>
	<div class="container">
		<div class="row" style="margin-top: 100px;">
			<div class="col-md-6 col-md-offset-3">
				<div class="alert alert-info" role="alert">
					<h1>复旦大学微信后台登录</h1>
					<form action="webuserlogin.act" method="post">
						<div class="form-group">
							<label for="UserName">UserName</label> <input type="text" class="form-control" name="uname" placeholder="UserName" required="required">
						</div>
						<div class="form-group">
							<label for="upwd">Password</label> <input type="password" class="form-control" name="upwd" placeholder="Password" required="required">
						</div>
						<div style="color: red;">
						${loginTip }
						</div>
						<button type="submit" class="btn btn-lg btn-danger">Login</button>
					</form>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="resource/thirdparty/bootstrap-3.2/js/bootstrap.min.js"></script>
</body>
</html>