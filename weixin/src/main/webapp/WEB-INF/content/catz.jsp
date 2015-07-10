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
<title>复旦信息办--新闻公告</title>
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/bootstrap-theme.min.css" rel="stylesheet">
<link href="css/font-awesome.min.css" rel="stylesheet" >
<script src="js/jquery-1.11.1.min.js"></script>
<script src="js/bootstrap.min.js"></script>

<script type="text/javascript">
	$(function() {
		append();
	});
	function getParameterByName(name) {
		var match = RegExp('[?&]' + name + '=([^&]*)').exec(
				window.location.search);
		return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
	}
	function getListUrl(){
		var ptype=getParameterByName("type");
		
		if(ptype=="ca")
			return "calist.act";
		if(ptype=="lib")
			return "liblist.act";
		if(ptype=="dst")
			return "dstlist.act";
		if(ptype=="xxb")
			return "sdlist.act?domain="+encodeURIComponent("http://www.xxb.fudan.edu.cn")+"&listid=2240";
		if(ptype=="jwc")
			return "sdlist.act?domain="+encodeURIComponent("http://www.jwc.fudan.edu.cn")+"&listid=3167";
		if(ptype=="gs")
			return "sdlist.act?domain="+encodeURIComponent("http://www.gs.fudan.edu.cn")+"&listid=2892";
		if(ptype=="cwc")
			return "sdlist.act?domain="+encodeURIComponent("http://www.cwc.fudan.edu.cn")+"&listid=1227";
		if(ptype=="hr")
			return "sdlist.act?domain="+encodeURIComponent("http://www.hr.fudan.edu.cn")+"&listid=4805";
		if(ptype=="fao")
			return "sdlist.act?domain="+encodeURIComponent("http://www.fao.fudan.edu.cn")+"&listid=1700";
		if(ptype=="zcglc")
			return "sdlist.act?domain="+encodeURIComponent("http://www.zcglc.fudan.edu.cn")+"&listid=5010";
		if(ptype=="xyfw")
			return "sdlist.act?domain="+encodeURIComponent("http://www.xyfw.fudan.edu.cn")+"&listid=2027";
		if(ptype=="baoweichu")
			return "sdlist.act?domain="+encodeURIComponent("http://baoweichu.fudan.edu.cn")+"&listid=6707";
		if(ptype=="wkkyc")
			return "sdlist.act?domain="+encodeURIComponent("http://wkkyc.fudan.edu.cn")+"&listid=1237";
		if(ptype=="yxky")
			return "sdlist.act?domain="+encodeURIComponent("http://yxky.fudan.edu.cn")+"&listid=6236";
		if(ptype.indexOf("news-")==0)
			return "newslist.act?listid="+ptype.substr(5);
	}
	function getDetailUrl(){
		var ptype=getParameterByName("type");
		if(ptype.indexOf("news-")==0)
			return "newscontent.act";
		if(ptype=="ca")
			return "cacontent.act";
		if(ptype=="lib")
			return "libcontent.act";
		if(ptype=="dst")
			return "dstcontent.act";
		if(ptype=="xxb"||ptype=="jwc"||ptype=="gs"||ptype=="cwc"||ptype=="hr"||ptype=="fao"||ptype=="zcglc"||ptype=="xyfw")
			return "sdcontent.act?domain="+encodeURIComponent("http://www."+ptype+".fudan.edu.cn");
		if(ptype=="baoweichu"||ptype=="wkkyc"||ptype=="yxky")
			return "sdcontent.act?domain="+encodeURIComponent("http://"+ptype+".fudan.edu.cn");
		if(ptype.indexOf("news-")==0)
			return "newscontent.act";
	}
	
	function append() {
		$
				.getJSON(
						"crawler/"+getListUrl(),
						{
							"page" : $("#cpage").val()
						},
						function(resp) {
							var vlist = $("#newslist");
							var htm = "";
							for (var i = 0; i < resp.length; i++) {
								htm += "<li class=\"list-group-item\" onclick=\"showdetail('"	+ resp[i].id
										+ "')\">"
										+ resp[i].title+(resp[i].pubdate!=null?("<i> ("+resp[i].pubdate+")</i>"):"")
										+ "</li>";
							}
							vlist.html(vlist.html() + htm);
							$("#nlist").css("display", "block");
						});
		$("#cpage").val(new Number($("#cpage").val()) + 1);
		
	}

	function showdetail(newsid) {
		$("#nlist").css("display", "none");
		$("#ncontent").css("display", "block");
		$("#newscontent").html("正在加载...");
		$.getJSON("crawler/"+getDetailUrl(), {
			"newsid" : newsid
		}, function(resp) {			
			$("#newscontent")
					.html("<h4  class='text-center'>" + resp.title + "</h4>" +(resp.pubdate!=null?("<p class='text-center'>"+resp.pubdate+"</p>"):"")+ resp.content);

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
		<input type="hidden" id="cpage" value="1" />
		<div id="nlist" style="display:none">
			<ul class="list-group" id="newslist">			
			</ul>
			<div class="text-center">
				<a href="javascript:append()" class="btn btn-sm btn-success">加载更多....</a>
			</div>
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
