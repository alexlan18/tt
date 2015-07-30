<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div id="resourcesWindow" class="easyui-window" title="编辑信息" data-options="modal:true,closed:true,iconCls:'icon-save'"
     style="width:550px;height:500px;">

    <table id="authoritys"></table>
    <br/>
    <div class="text-center"><input type="button" class="btn btn-primary btn-small" value="删除" onclick="deleteAuth();"/></div>

    <input type="hidden" name="id" id="resourceRoleId" />
    <hr/>

    <table id="notAuthoritys"></table>
    <br/>
    <div class="text-center"><input type="button" class="btn btn-primary btn-small" value="添加到已分配" onclick="auth();"/></div>
    <br/>
</div>