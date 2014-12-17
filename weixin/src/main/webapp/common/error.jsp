<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title><s:text name="common.error.title" /></title>
<link href="<s:url namespace="/" value="/css/index.css" />"
	rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<s:url namespace="/" value="/js/pngfix.js" />"></script>

</head>

<body>
<table width="95%" border="0" align="center" cellpadding="2"
	cellspacing="0">
	<tr>
		<td colspan="2">&nbsp;</td>
	</tr>
	<tr>
		<td width="10%"><img src="<s:url namespace="/" value="/images/error2.png" />"
			border="0" /></td>
		<td width="90%"><br />
		<h3><s:text name="common.error.title" /></h3>
		</td>
	</tr>
	<tr>
		<td colspan="2">
		<br />
		<div class="info"><s:text name="common.error.intro" /></div>		
		<br />
		<br />
		<a href="/"> <s:text
			name="common.navigate.goindex" /></a> 		
			
		<br />
		<br />
		<hr />

		<h3><s:text name="common.error.message" /></h3>
		<div class="info"><s:actionerror />

		<p><s:property value="%{exception}" /></p>
		</div>
		<hr />

		<h3><s:text name="common.error.stack" /></h3>

		<pre class="info">
    <s:property value="%{exceptionStack}" />
</pre></td>
	</tr>
</table>

</body>
</html>
