<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<form>
<!--table>
<s:iterator value="list" id="id" status="lstat">
   <tr>
        <td><s:property value="openid"/></td><td><s:property value="nickname"/> </td><td><input type = "button" value="发送"></td>
   </tr> 
</s:iterator>
</table-->
</form>



<form name = "sendmessage" action="sendmessage.act" method="post">
<select name="openid">
<s:iterator value="list" id="id" status="lstat">
<option value = <s:property value="openid"/>>
<s:property value="nickname"/>
</option>
</s:iterator>
</select>
<br>
<textarea name="content" cols="40" rows="10" id="textmessage"></textarea><br>
<s:hidden/>
<input type = "submit" value = "发送" />
</form>



</body>
</html>