<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div id="editWindow" class="easyui-window" title="编辑信息" data-options="modal:true,closed:true,iconCls:'icon-save'"
     style="width:450px;height:300px;">
    <form id="editForm" action="<c:url value='/system/sRole/save'/>" method="post">
        <ul>
            <input type="hidden" name="id"/>
            <li>
                <label style="">角色ID<sup style="color:red">*</sup>：</label>
                <input type="text" name="roleId" class="easyui-validatebox" data-options="required:true"/>
            </li>
            <li>
                <label style="">角色名称<sup style="color:red">*</sup>：</label>
                <input type="text" name="roleName" class="easyui-validatebox" data-options="required:true"/>
            </li>
            <li>
                <label style="">是否可用<sup style="color:red">*</sup>：</label>
                <select name="enabled" class="easyui-combobox" data-options="required:true,deltaX:25,editable:false,url:'<c:url value='/combo/100002'/>'" style="width: 120px">
                </select>
            </li>
            <li>
                <label style="">是否超级权限<sup style="color:red">*</sup>：</label>
                <select name="isSys" class="easyui-combobox" data-options="required:true,deltaX:25,editable:false,url:'<c:url value='/combo/100003'/>'" style="width: 120px">
                </select>
            </li>
            <li><label></label><input id="editSave" type="button" class="btn btn-small btn-primary" value="保存" onclick="save(this);"/></li>
        </ul>
    </form>
</div>