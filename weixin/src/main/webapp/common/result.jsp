<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<s:actionerror /><s:actionmessage />
<s:if test="%{info}">
	<s:property value="%{info}" />
</s:if>
<s:else>
	<s:text name="common.page.ok" />
</s:else>