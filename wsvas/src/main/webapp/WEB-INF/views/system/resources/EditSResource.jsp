<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div id="editWindow" class="easyui-window" title="编辑信息" data-options="modal:true,closed:true,iconCls:'icon-save'"
     style="width:480px;height:350px;">
    <form id="editForm" action="<c:url value='/system/sResource/save'/>" method="post">
        <ul>
            <input type="hidden" name="id"/>
            <input type="hidden" name="resourceId"/>
            <li>
                <label style="">资源名称：</label>
                <input type="text" name="resourceName"/>
            </li>
            <li>
                <label style="">资源类型：</label>
                <input type="text" name="resourceType"/>
            </li>
            <li>
                <label style="">资源优先权：</label>
                <input type="text" name="resourcePriority"/>
            </li>
            <li>
                <label style="">资源链接：</label>
                <input type="text" name="resourceUrl"/>
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