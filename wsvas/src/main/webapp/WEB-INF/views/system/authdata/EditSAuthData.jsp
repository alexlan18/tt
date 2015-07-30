<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div id="editWindow" class="easyui-window" title="编辑信息" data-options="modal:true,closed:true,iconCls:'icon-save'"
     style="width:480px;height:300px;">
    <form id="editForm" action="<c:url value='/system/sAuthData/save'/>" method="post">
        <ul>
            <input type="hidden" name="id"/>
            <li>
                <label style="">权限ID：</label>
                <input type="text" name="authId"/>
            </li>
            <li>
                <label style="">权限名称：</label>
                <input type="text" name="authName"/>
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
            <li><label></label><input id="editSave" type="button" class="btn btn-small btn-primary" value="保存" onclick="save();"/></li>
        </ul>
    </form>
</div>