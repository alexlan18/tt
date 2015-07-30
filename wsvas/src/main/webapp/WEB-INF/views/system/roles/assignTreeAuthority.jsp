<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div id="treeWindow" class="easyui-window" title="菜单权限分配" data-options="modal:true,closed:true,iconCls:'icon-save'"
     style="width:450px;height:500px;">
    <div id="menuTree" style="height: 400px"></div>
    <br/>
    <input type="hidden" name="id" id="authRoleId" />
    <div class="text-center"><input id="menuTreeSave" type="button" class="btn btn-small btn-primary" value="保存" onclick="saveTreeAuth();"/></div>
</div>