 <%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
 <%@ taglib uri="/struts-tags" prefix="s"%>
 <div class="navbar navbar-default navbar-collapse"   >
				<div class="col-xs-2">
					<a href="javascript:history.back()" style="float: right"><i class="fa fa-arrow-circle-left fa-3x" ></i></a>
				</div>
				<div class="col-xs-8 text-center">
					<h4>
						<s:property value="#session.nickname" />
					</h4>
				</div>
				<div class="col-xs-2">
					<a href="index.act"><i class="fa fa-home fa-3x" ></i></a>
				</div>

			</div>