<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div id="editWindow" class="easyui-window" title="编辑信息" data-options="modal:true,closed:true,iconCls:'icon-save'"
     style="width:480px;height:520px;">
    <form id="editForm" action="<c:url value='/system/sCodeInfo/save'/>" method="post">
        <ul>
            <li>
                <label style="">主键：</label>
                <input type="text" name="id"/>
            </li>
            <li>
                <label style="">代码类型ID：</label>
                <input type="text" name="codeTypeId"/>
            </li>
            <li>
                <label style="">代码值：</label>
                <input type="text" name="codeValue"/>
            </li>
            <li>
                <label style="">代码名称：</label>
                <input type="text" name="codeName"/>
            </li>
            <li>
                <label style="">排序号：</label>
                <input type="text" name="sortNo"/>
            </li>
            <li>
                <label style="">备注：</label>
                <input type="text" name="remark"/>
            </li>
            <li><label></label><input id="editSave" type="button" class="btn btn-small btn-primary" value="保存" onclick="save();"/></li>
        </ul>
    </form>
</div>