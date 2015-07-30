<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div id="rolesWindow" class="easyui-window" title="角色信息" data-options="modal:true,closed:true,iconCls:'icon-save'"
     style="width:550px;height:500px;">

    <table id="authoritys"></table>
    <br/>
    <div class="text-center"><input type="button" class="btn btn-primary btn-small" value="删除" onclick="deleteAuth();"/></div>

    <input type="hidden" name="id" id="roleUserId" />
    <hr/>

    <table id="notAuthoritys"></table>
    <br/>
    <div class="text-center"><input type="button" class="btn btn-primary btn-small" value="添加到已分配" onclick="auth();"/></div>
    <br/>
</div>