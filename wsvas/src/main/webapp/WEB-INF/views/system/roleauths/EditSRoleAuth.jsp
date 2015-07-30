<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div id="editWindow" class="easyui-window" title="编辑信息" data-options="modal:true,closed:true,iconCls:'icon-save'"
     style="width:480px;height:520px;">
    <form id="editForm" action="<c:url value='/system/sRoleAuth/save'/>" method="post">
        <ul>
            <li>
                <label style="">主键：</label>
                <input type="text" name="id"/>
            </li>
            <li>
                <label style="">角色ID：</label>
                <input type="text" name="roleId"/>
            </li>
            <li>
                <label style="">权限ID：</label>
                <input type="text" name="authId"/>
            </li>
            <li><label></label><input id="editSave" type="button" class="btn btn-small btn-primary" value="保存" onclick="save();"/></li>
        </ul>
    </form>
</div>