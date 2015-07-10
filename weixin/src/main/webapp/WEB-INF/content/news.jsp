<%@ page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
<title>复旦信息办--复旦新闻</title>
<link href="css/bootstrap.min.css" rel="stylesheet">
 <link href="css/bootstrap-theme.min.css" rel="stylesheet">
 <link href="css/font-awesome.min.css" rel="stylesheet" >
<script src="js/jquery-1.11.1.min.js"></script>
<script src="js/bootstrap.min.js"></script>

</head>

<body>

        <div class="container-fluid">
<s:include value="/common/wxnav.jsp" />

                                        <s:actionerror />
<ul class="list-group">
<li class="list-group-item" onclick="location='catz.act?type=news-xxyw'" >学校要闻</li>
<li class="list-group-item" onclick="location='catz.act?type=news-ldjh'" >领导讲话</li>
<li class="list-group-item" onclick="location='catz.act?type=news-ztbd'" >专题报道</li>
<li class="list-group-item" onclick="location='catz.act?type=news-zhxw'" >综合新闻</li>
<li class="list-group-item" onclick="location='catz.act?type=news-yxdt'" >院系动态</li>
<li class="list-group-item" onclick="location='catz.act?type=news-gjsw'" >国际事务</li>
<li class="list-group-item" onclick="location='catz.act?type=news-xydt'" >校友动态</li>
<li class="list-group-item" onclick="location='catz.act?type=news-zsjy'" >招生就业</li>
<li class="list-group-item" onclick="location='catz.act?type=news-fdrw'" >复旦人物</li>
<li class="list-group-item" onclick="location='catz.act?type=news-zjsd'" >专家视点</li>
<li class="list-group-item" onclick="location='catz.act?type=news-fdjt'" >复旦讲堂</li>
<li class="list-group-item" onclick="location='catz.act?type=news-xysh'" >校园生活</li>
<li class="list-group-item" onclick="location='catz.act?type=news-xstx'" >校史通讯</li>
<li class="list-group-item" onclick="location='catz.act?type=news-fdsw'" >复旦书屋</li>
<li class="list-group-item" onclick="location='catz.act?type=news-xhbh'" >香辉笔会</li>
<li class="list-group-item" onclick="location='catz.act?type=news-mtsj'" >媒体视角</li>
</ul>
                                        </div>

                            
                      	<s:include value="/common/foot.jsp" />


               
               
</body>
</html>
