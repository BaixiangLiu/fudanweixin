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
<title>复旦信息办--校园事件</title>
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/bootstrap-theme.min.css" rel="stylesheet">
<link href="css/font-awesome.min.css" rel="stylesheet" >
<script src="js/jquery-1.11.1.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script type="text/javascript">
	$(function() {
		var ttype=getParameterByName("type");
		fetch(ttype);
	});
	function fetch(ttype)
	{
		if(ttype!=null )
			$("#cpage").val(ttype);
		append();
	}
	function getParameterByName(name) {
		var match = RegExp('[?&]' + name + '=([^&]*)').exec(
				window.location.search);
		return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
	}
	
	function showimg(imgid)
	{
		
		$.get("crawler/imgurl.act",{id:imgid},function (resp){		
			location="shimg.act?url="+encodeURIComponent(resp);
		});
	}
	
	function append() {
		$
				.get(
						"crawler/eventlist.act",
						{
							"page" : $("#cpage").val()
						},
						function(resp) {
							var vlist = $("#nlist");							
							vlist.html(resp);
							$("#nlist").css("display", "block");
						});
		
		
	}

	function fn_one(newsid) {
		$("#nlist").css("display", "none");
		$("#ncontent").css("display", "block");
		$("#newscontent").html("正在加载...");
		$.getJSON("crawler/eventcontent.act", {
			"newsid" : newsid
		}, function(resp) {			
			$("#postimg").attr("src",null);
			$("#newscontent")
					.html("<h4  class='text-center'>" + resp.title + "</h4>" +(resp.pubdate!=null?("<img class=\"img-responsive\" onclick=\"showimg('"+resp.pubdate+"')\" src=\"crawler/image.act?id="+resp.pubdate+"\" />"):"")+ resp.content);

		});
	}
	function hidecontent() {
		$("#nlist").css("display", "block");
		$("#ncontent").css("display", "none");
	}
</script>
</head>

<body>



	<div class="container-fluid">
		<s:include value="/common/wxnav.jsp" />

		<s:actionerror />
		<div class="btn-group">
			<button type="button" class="btn btn-default" onclick="fetch(0)">全部</button>
			<button type="button" class="btn btn-default" onclick="fetch(1)">讲座</button>
			<button type="button" class="btn btn-default" onclick="fetch(2)">展览</button>
			<button type="button" class="btn btn-default" onclick="fetch(3)">社团</button>
			<button type="button" class="btn btn-default" onclick="fetch(4)">演出</button>
			<button type="button" class="btn btn-default" onclick="fetch(5)">体育</button>
			<button type="button" class="btn btn-default" onclick="fetch(6)">学术</button>
			<button type="button" class="btn btn-default" onclick="fetch(7)">其他</button>
		</div>
		<input type="hidden" id="cpage" value="0" />		
		<div id="nlist" style="display:none">
			
			
		</div>
		<div id="ncontent" style="display:none">
			<div class="text-center">
				<a href="javascript:hidecontent()" class="btn btn-sm btn-success">返回列表</a>
			</div>
			<div id="newscontent"></div>
			<div class="text-center">
				<a href="javascript:hidecontent()" class="btn btn-sm btn-success">返回列表</a>
			</div>
		</div>

	</div>
	<s:include value="/common/foot.jsp" />




</body>
</html>
