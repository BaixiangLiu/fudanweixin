<%@ page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
<title>复旦信息办--UIS绑定列表</title>
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

                                        <table class="table table-striped table-condensed">
                                        <thead><tr><th>学/工号</th><th>微信昵称</th><th>性别</th><th>所在地</th></tr></thead>
                                        <s:set id="sa" value="#{0:'未知',1:'男',2:'女'}" />
                                        <s:iterator value="list">
                                        <tr><td><s:property value="uisid" /></td><td><s:property value="nickname" /></td><td><s:property value="#sa[sex]"/></td>
                                        <td><s:property value="country+' ' +province+' '+city" /></td>
                                        </tr>
                                        </s:iterator>
                                        </table>
                                        <div class="alert alert-danger">如果发现列表中有不是您所有的微信号请速与信息办联系处理，电话:65643207</div>
                                        <p align="center">
                                        <a class="btn btn-success btn-sm"  href="bindin.act">增加绑定账号</a>
                                        </p>
                                        </div>

                            
                      	<s:include value="/common/foot.jsp" />


               
               
</body>
</html>
