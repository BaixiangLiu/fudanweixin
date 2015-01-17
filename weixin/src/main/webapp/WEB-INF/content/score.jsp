<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
<title>复旦信息办--成绩查询</title>
<link href="css/bootstrap.min.css" rel="stylesheet">
 <link href="css/bootstrap-theme.min.css" rel="stylesheet">
<script src="js/jquery-1.11.1.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<link href="css/bootstrap-select.css" rel="stylesheet" >
<script src="js/bootstrap-select.js"  type="text/javascript" > </script>
<script type="text/javascript">
	$(function() {
		var ys = $("#termy")[0];
		var ty = 2014;
		for (var i = 0; i < 10; i++)
			ys.options
					.add(new Option(((ty - i) + "-" + (ty + 1 - i)), (ty - i)));

		$.getJSON("resource/schoolterm.act", {}, function(resp) {
			if (resp.result.errcode == 0) {
				$("#pcontent").html(
						"<div class='alert alert-success'>现在是"
								+ resp.list.termname + "学期的第" + resp.list.week
								+ "周</div>");
			} else {
				$("#pcontent").html(
						"<div class='alert alert-danger'>"
								+ resp.result.errdesc + "</div>");
			}
		});

	});
	function getParameterByName(name) {
		var match = RegExp('[?&]' + name + '=([^&]*)').exec(
				window.location.search);
		return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
	}
	function subform() {
		$
				.getJSON(
						"resource/score.act",
						{
							"uisid" : $("#uisid").val(),
							"term" : $("#termy").val() + "" + $("#termt").val()
						},
						function(resp) {
							var htm = "";
							if (resp.result.errcode == 0) {
								if (resp.list.length == 0
										|| resp.list[0].list.error == "access_denied")
									htm = "<div class='alert alert-warning'>尚未完成绑定操作，请重新对此UIS账号进行绑定。</div>";
								if (resp.list[0].list.error == "invalid_scope")
									if (resp.list[0].list.error_description
											.indexOf('grant') > 0)
										htm = "<div class='alert alert-warning'>未对此UIS账号的成绩项目授权，请发送语音或文字消息【修改授权】并按提示进行操作</div>";
									else
										htm = "<div class='alert alert-warning'>该UIS账号绑定的身份不是学生，没有成绩可查。</div>";

								if (resp.list[0].list.error == null) {
									if (resp.list[0].list.length == 0)
										htm = "<div class='alert alert-warning'>所查学期无成绩信息</div>";
									else {
										htm = "<div class='table-responsive'><table class='table table-striped table-condensed'><tr><th colspan='5'>"
												+ resp.list[0].list[0].term
												+ "</th></tr><tr><th>课程代码</th><th>课程名称</th><th>学分</th><th>成绩</th></tr> ";
										for (var i = 0; i < resp.list[0].list.length; i++) {
											var data = resp.list[0].list[i];
											htm += "<tr><td>" + data.course_id
													+ "</td>";
											htm += "<td>" + data.course_name
													+ "</td>";
											htm += "<td>" + data.credit
													+ "</td>";											
											htm += "<td>" + data.grade
													+ "</td></tr>";										
										}
										htm += "</table></div>";
									}
								}

								$("#pcontent").html(htm);
							} else {
								$("#pcontent").html(
										"<div class='alert alert-danger'>"
												+ resp.result.errdesc
												+ "</div>");
							}
						});
		return false;
	}
</script>
</head>

<body>

	<div class="container-fluid">
		<s:include value="/common/wxnav.jsp" />

				<s:actionerror />
				<div class=" panel panel-primary">
					<div class="panel-heading">
						<form class="form-inline" role="form" onsubmit="return subform()"
							id="qform">
							<div class="form-group">
								<s:select
									list="binds"
									cssClass="selectpicker" name="uisid"
									listValue="uisid+' ('+username+' )'" listKey="uisid" />
							</div>
							<div class="form-group">
								<div class="input-group">
									<select class="selectpicker" id="termy"></select> <span
										class="input-group-addon" >学年</span> <select
										class="selectpicker" id="termt"><option
											value="01">秋季</option>
										<option value="02">春季</option>
										<option value="03">暑期</option></select>
								</div>
							</div>

							<button type="submit" class="btn btn-success btn-sm">成绩查询</button>

						</form>
					</div>
					<div class="panel-body" id="pcontent"></div>
				</div>
			</div>



		<s:include value="/common/foot.jsp" />


	

</body>
</html>
