<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div id="editWindow" class="easyui-window" title="编辑信息" data-options="modal:true,closed:true,iconCls:'icon-save'"
     style="width:450px;">
    <form id="editForm" action="<c:url value='/system/sCodeType/save'/>" method="post">
        <ul>
            <input type="hidden" name="id"/>
            <input type="hidden" name="codeTypeId"/>
            <li>
                <label style="">代码类型名称<sup style="color:red">*</sup>：</label>
                <input type="text" name="codeTypeName" class="easyui-validatebox" required="true"/>
            </li>
            <li>
                <label style="">排序号：</label>
                <input type="text" name="sortNo" class="easyui-numberbox"/>
            </li>
            <li>
                <label style="">备注：</label>
                <input type="text" name="remark"/>
            </li>
            <li><label></label><input id="editSave" type="button" class="btn btn-small btn-primary" value="保存" onclick="save();"/></li>
        </ul>
    </form>
</div>

<div id="editCodeInfoWindow" class="easyui-window editWindow" title="编辑代码信息" data-options="modal:true,closed:true,iconCls:'icon-save'"
     style="width:450px;">
    <input id="editCodeTypeId" type="hidden"/>
    <form id="editCodeInfoForm" action="<c:url value='/system/sCodeInfo/save'/>" method="post">
        <ul>
            <input type="hidden" name="id"/>
            <input id="hiddenCodeTypeId" name="codeTypeId" type="hidden"/>
            <li>
                <label style="">代码值<sup style="color:red">*</sup>：</label>
                <input type="text" name="codeValue" class="easyui-validatebox" required="true"/>
            </li>
            <li>
                <label style="">代码名称<sup style="color:red">*</sup>：</label>
                <input type="text" name="codeName" class="easyui-validatebox" required="true"/>
            </li>
            <li>
                <label style="">排序号<sup style="color:red">*</sup>：</label>
                <input type="text" name="sortNo" class="easyui-numberbox" required="true"/>F
            </li>
            <li>
                <label style="">备注：</label>
                <input type="text" name="remark"/>
            </li>
            <li><label></label><input id="editCodeInfoSave" type="button" class="btn btn-small btn-primary" value="保存" onclick="saveCodeInfo();"/></li>
        </ul>
    </form>
</div>

<div id="codeInfoWindow" class="easyui-window" title="代码信息" data-options="modal:true,closed:true,iconCls:'icon-save'"
     style="width:600px;height: 400px">
    <table id="codeInfoGrid"></table>
</div>