<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html class="no-js">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="Expires" CONTENT="0">
    <meta http-equiv="Cache-Control" CONTENT="no-cache">
    <meta http-equiv="Pragma" CONTENT="no-cache">
    <%@include file="include.jsp" %>
    <title>Wing Tech</title>
</head>
<body id="index_layout" class="easyui-layout"  fit="true">
<div region="north" style="width: 100%; height: 55px;">
    <jsp:include page="/top.jsp"/>
</div>
<div region="south" style="width:100%; height: 50px; border:0px;">
    <div class="footer">Copyright © 2014 <a href="http://www.chinanetcenter.com">Wing tech</a> </div>
</div>
<div region="center" style="height: 100%; padding: 0px;border-top: 0px; border-left: 0px; border-bottom: 0px;" href="content.jsp" />
</body>
