<%@ page 
language="java"
contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"
%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<table width="95%"  border="0" align="center" cellpadding="2" cellspacing="0">
      <tr>
        <td colspan="2">&nbsp;</td>
      </tr>
      <tr>
        <td colspan="2"><div align="center">
          <p><a href="<s:url namespace="/" value="/" />" ><img src="<s:url namespace="/" value="/images/error.png" />" width="97" height="97" border="0" title="<s:text name="common.navigate.goindex" />" /></a></p>
          <p><s:text name="common.page.privilege" /></p>
          <p>&nbsp;</p>
          <s:url id="loginUrl" action="logout" namespace="/" >
          	<s:param name="redirectURL" value="%{'/login.act'}" />
          </s:url>
          <s:a href="%{loginUrl}" ><s:text name="common.navigate.relogin" /></s:a> 
          
        </div></td>
        </tr>
    </table>
