<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
<title>复旦大学信息办</title>
<link href="css/bootstrap.min.css" rel="stylesheet">
 <link href="css/bootstrap-theme.min.css" rel="stylesheet">
 <link href="css/font-awesome.min.css" rel="stylesheet" >
<script src="js/jquery-1.11.1.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<style>
.fa-stack-5x{position:absolute;left:0;width:100%;text-align:center;font-size:5em}
.fa-stack-lg-5x{width: 5em;
  height: 5em;
  line-height: 5em;}
  i.fa-stack-2x {line-height: inherit;}
</style>
</head>

<body>

	<div class="container-fluid">
	<div class="navbar navbar-default navbar-collapse"   ><div class="col-xs-2">
					</div> <div class="col-xs-8 text-center"  ><h4><s:if test="#session.openid!=null"><s:property value="#session.nickname" /></s:if><s:else>请点击右侧图标登录</s:else></h4></div>
					<div class="col-xs-2"><s:if test="#session.openid!=null"><a href="logout.act" style="float:right"><i class="fa fa-sign-out fa-3x"></i></a></s:if>
					<s:else><a href="wxlogin.act" style="float:right"><i class="fa fa-sign-in fa-3x"></i></a></s:else>
					</div>

</div>

				<div class="panel panel-info">
					<!-- Default panel contents -->
				
					<div class="panel-body">
					<div class="col-xs-6 col-sm-4 col-md-3 text-center">
					<a href="booklist.act">
					<span class="fa-stack fa-stack-lg-5x">
  <i class="fa fa-circle fa-stack-5x" ></i>
  <i class="fa fa-bell-o fa-stack-2x  fa-inverse" ></i>
</span><br />订阅中心</a>
					</div>
					<div class="col-xs-6 col-sm-4 col-md-3 text-center">
					<a href="http://baishitong.fudan.edu.cn/wiki/%E5%A4%8D%E6%97%A6%E4%BF%A1%E6%81%AF%E5%8A%9E%E5%BE%AE%E4%BF%A1%E5%85%AC%E4%BC%97%E5%8F%B7">
	<span class="fa-stack fa-stack-lg-5x">
  <i class="fa fa-circle fa-stack-5x" ></i>
  <i class="fa fa-info fa-stack-2x  fa-inverse" ></i>
</span><br />使用帮助</a>
					</div>
					<div class="col-xs-6 col-sm-4 col-md-3 text-center">
					<a href="binds.act">
						<span class="fa-stack fa-stack-lg-5x">
  <i class="fa fa-circle fa-stack-5x" ></i>
  <i class="fa fa-user fa-stack-2x  fa-inverse" ></i>
</span><br />绑定账号查看</a>
					</div>
					<div class="col-xs-6 col-sm-4 col-md-3 text-center">
					<a href="ecard.act">	<span class="fa-stack fa-stack-lg-5x">
  <i class="fa fa-circle fa-stack-5x" ></i>
  <i class="fa fa-credit-card fa-stack-2x  fa-inverse" ></i>
</span><br />一卡通</a>
					</div>
					<div class="col-xs-6 col-sm-4 col-md-3 text-center">
					<a href="lesson.act">	<span class="fa-stack fa-stack-lg-5x">
  <i class="fa fa-circle fa-stack-5x" ></i>
  <i class="fa fa-th-list fa-stack-2x  fa-inverse" ></i>
</span><br />课程</a>
					</div>
					<div class="col-xs-6 col-sm-4 col-md-3 text-center">
					<a href="score.act">	<span class="fa-stack fa-stack-lg-5x">
  <i class="fa fa-circle fa-stack-5x" ></i>
  <i class="fa fa-line-chart fa-stack-2x  fa-inverse" ></i>
</span><br />成绩</a>
					</div>
					<div class="col-xs-6 col-sm-4 col-md-3 text-center">
					<a href="phoneyp.act">	<span class="fa-stack fa-stack-lg-5x">
  <i class="fa fa-circle fa-stack-5x" ></i>
  <i class="fa fa-phone fa-stack-2x  fa-inverse" ></i>
</span><br />电话黄页</a>					
					</div>
					
					<div class="col-xs-6 col-sm-4 col-md-3 text-center">
					<a href="classroom.act">	<span class="fa-stack fa-stack-lg-5x">
  <i class="fa fa-circle fa-stack-5x" ></i>
  <i class="fa fa-desktop fa-stack-2x  fa-inverse" ></i>
</span><br />空闲教室</a>
					</div>
					<div class="col-xs-6 col-sm-4 col-md-3 text-center">
					<a href="tzgg.act">	<span class="fa-stack fa-stack-lg-5x">
  <i class="fa fa-circle fa-stack-5x" ></i>
  <i class="fa fa-bullhorn fa-stack-2x  fa-inverse" ></i>
</span><br />通知公告</a>
					</div>
					<div class="col-xs-6 col-sm-4 col-md-3 text-center">
					<a href="news.act">	<span class="fa-stack fa-stack-lg-5x">
  <i class="fa fa-circle fa-stack-5x" ></i>
  <i class="fa fa-rss fa-stack-2x  fa-inverse" ></i>
</span><br />复旦新闻</a>
					</div>
					<div class="col-xs-6 col-sm-4 col-md-3 text-center">
					<a href="event.act">	<span class="fa-stack fa-stack-lg-5x">
  <i class="fa fa-circle fa-stack-5x" ></i>
  <i class="fa fa-calendar fa-stack-2x  fa-inverse" ></i>
</span><br />校园文化日历</a>
					</div>
					<div class="col-xs-6 col-sm-4 col-md-3 text-center">
					<a href="electric.act">	<span class="fa-stack fa-stack-lg-5x">
  <i class="fa fa-circle fa-stack-5x" ></i>
  <i class="fa fa-bolt fa-stack-2x  fa-inverse" ></i>
</span><br />电费信息</a>
					</div>

				</div>

		

		</div>
		<s:include value="/common/foot.jsp" />
</body>
</html>
