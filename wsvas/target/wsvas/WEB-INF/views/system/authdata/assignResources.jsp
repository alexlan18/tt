<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div id="resourcesWindow" class="easyui-window" title="分配资源" data-options="modal:true,closed:true,iconCls:'icon-save'"
     style="width:550px;height:500px;">
    <input type="hidden" name="authId" id="assignAuthId" />
    <table id="resources"></table>
    <br/>
    <div class="text-center"><input type="button" class="btn btn-primary btn-small" value="保存" onclick="auth();"/></div>
</div>