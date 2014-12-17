<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>复旦微信后台 - 在线问答</title>
<jsp:include page="topInclude.jsp"></jsp:include>
<style type="text/css">
.userlist a {
	width: 100%;
	margin-top: 5px;
}
</style>
</head>
<body>
	<jsp:include page="menu.jsp"></jsp:include>

	<div class="container">
		<div class="row">
			<div class="col-md-3">
				<div class="thumbnail userlist">
					<div class="alert alert-info" role="alert">接入用户:</div>
					<a class="btn btn-default" href="javascript:void(0);"">xxx</a> <a class="btn btn-primary" href="javascript:void(0);">yyy</a> <a class="btn btn-default" href="javascript:void(0);">zzz</a>
				</div>
			</div>
			<div class="col-md-9">
				<div class="thumbnail">
					<div class="alert alert-info" role="alert">
						回复区:
						<form role="form" style="padding-left: 10px; padding-right: 10px;">
							<div class="form-group">
								<textarea rows="5" id="reply-content" class="form-control"></textarea>
							</div>

							<button type="button" class="btn btn-primary" id="reply-btn">回复</button>

						</form>

					</div>
					<div class="alert alert-success" role="alert" style="margin-top: 10px;">
						最近消息:

						<ol style="list-style: none;">
							<li>
								<p>
									(12:30) <strong>xxx</strong>
								</p>
							</li>
							<li>
								<p>
									(12:28) <strong>yyy</strong>
								</p>
							</li>
						</ol>
					</div>
				</div>
			</div>
		</div>
	</div>

	<jsp:include page="bottomInclude.jsp"></jsp:include>
</body>
</html>