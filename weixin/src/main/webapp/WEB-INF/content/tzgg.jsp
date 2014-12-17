<%@ page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
<title>复旦信息办--通知公告</title>
<link href="css/bootstrap.min.css" rel="stylesheet">
 <link href="css/bootstrap-theme.min.css" rel="stylesheet">
<script src="js/jquery-1.11.1.min.js"></script>
<script src="js/bootstrap.min.js"></script>

</head>

<body>

        <div class="container-fluid">
<s:include value="/common/wxnav.jsp" />

                                        <s:actionerror />
<ul class="list-group">
<li class="list-group-item" onclick="location='catz.act?type=ca'" >校主页通知公告</li>
<li class="list-group-item" onclick="location='catz.act?type=jwc'" >教务处（本科生院）通知公告</li>
<li class="list-group-item" onclick="location='catz.act?type=gs'" >研究生院通知公告</li>
<li class="list-group-item" onclick="location='catz.act?type=cwc'" >财务处公告栏</li>
<li class="list-group-item" onclick="location='catz.act?type=baoweichu'" >保卫处通知公告</li>
<li class="list-group-item" onclick="location='catz.act?type=hr'" >人事处新闻通知</li>
<li class="list-group-item" onclick="location='catz.act?type=dst'" >科技处通知公告</li>
<li class="list-group-item" onclick="location='catz.act?type=wkkyc'" >文科科研处通知公告</li>
<li class="list-group-item" onclick="location='catz.act?type=yxky'" >医学科研管理办公室通知通告</li>
<li class="list-group-item" onclick="location='catz.act?type=fao'" >外事处暨港澳台事务办公室公告</li>
<li class="list-group-item" onclick="location='catz.act?type=zcglc'" >资产管理处公告</li>
<li class="list-group-item" onclick="location='catz.act?type=xyfw'" >总务处服务公告</li>
<li class="list-group-item" onclick="location='catz.act?type=lib'" >图书馆最新消息</li>
</ul>
                                        </div>

                            
                      	<s:include value="/common/foot.jsp" />


               
               
</body>
</html>
