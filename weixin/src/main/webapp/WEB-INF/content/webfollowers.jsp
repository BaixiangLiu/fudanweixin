<%@page import="java.util.Date"%>
<%@page import="edu.fudan.webclient.entity.WeixinUserEntity"%>
<%@page import="edu.fudan.webclient.entity.IMongoEntity"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>复旦微信后台 - 粉丝列表</title>
<jsp:include page="topInclude.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="menu.jsp"></jsp:include>

	<% List<IMongoEntity> list = (List<IMongoEntity>) request.getAttribute("list"); %>

	<div class="container">
		<div class="row">
			<div class="col-md-12">
				<div>
					<div class="alert alert-info" role="alert" style="margin-top: 10px;">
						过滤选项
						<form action="" class="form-inline" role="form" style="margin-top: 5px;">
							<div class="form-group">
								<label for="nickname">昵称</label> <input type="text" id="nickname" class="form-control" placeholder="昵称" />
							</div>
							<div class="form-group">
								<label for="sex">性别</label> <select name="sex" class="form-control">
									<option>全部</option>
									<option value="">男</option>
									<option value="">女</option>
									<option value="">未知</option>
								</select>
							</div>
							<div class="form-group">
								<label for="subscribe">关注状态</label> <select name="subscribe" class="form-control">
									<option>全部</option>
									<option value="">关注</option>
									<option value="">未关注</option>
								</select>
							</div>
							<button type="submit" class="btn btn-danger">查询</button>
						</form>
					</div>
				</div>
			</div>
		</div>
		<div class="row" style="margin-top: 10px;">
			<div class="col-md-12">
				<table class="table table-striped table-bordered">
					<thead>
						<tr>
							<th>头像</th>
							<th>昵称</th>
							<th>性别</th>
							<th>关注状态</th>
							<th>国家</th>
							<th>省份</th>
							<th>城市</th>
							<th>关注时间</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<% for(IMongoEntity entity : list) {  %>
						<% WeixinUserEntity u = (WeixinUserEntity) entity; %>
						<tr data-uid="<%=u.getId() %>">
							<td><img class="lazy" width="40px" height="40px" src="<%=u.getHeadimgurl() %>" /></td>
							<td><%=u.getNickName() %></td>
							<td><%=u.getSexStr() %></td>
							<td><%=u.getSubscribeStr() %></td>
							<td><%=u.getCountry() %></td>
							<td><%=u.getProvince() %></td>
							<td><%=u.getCity() %></td>
							<td><%=u.getSubscribeTimeStr() %></td>
							<td><a href="javascript:void(0);">发送通知</a></td>
						</tr>
						<% } %>
					</tbody>
				</table>
			</div>
		</div>

		<div class="row">
			<div class="col-md-12">
				<div class="text-center">
					<ul class="pagination pagination-lg">
						<li><a href="#">&laquo;</a></li>
						<li><a href="">1</a></li>
						<li><a href="">2</a></li>
						<li><a href="">3</a></li>
						<li><a href="#">&raquo;</a></li>
					</ul>
				</div>
			</div>
		</div>
	</div>

	<jsp:include page="bottomInclude.jsp"></jsp:include>
	<script type="text/javascript" src="resource/thirdparty/lazyload/jquery.lazyload.min.js"></script>
	<script type="text/javascript">
		$(function() {
			$("img.lazy").lazyload();
		});
	</script>
</body>
</html>