<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div id="editWindow" class="easyui-window" title="编辑信息" data-options="modal:true,closed:true,iconCls:'icon-save'"
     style="width:480px;height:520px;">
    <form id="editForm" action="<c:url value='/system/sUser/save'/>" method="post">
        <ul>
            <input type="hidden" name="id"/>
            <input type="hidden" name="password"/>
            <li>
                <label style="">登录名称<sup style="color:red">*</sup>：</label>
                <input type="text" name="loginName" class="easyui-validatebox" required="true"/>
            </li>
            <li>
                <label style="">用户名称：</label>
                <input type="text" name="userName"/>
            </li>
            <li>
                <label style="">是否可用<sup style="color:red">*</sup>：</label>
                <select name="enabled" class="easyui-combobox" data-options="required:true,deltaX:25,editable:false,url:'<c:url value='/combo/100002'/>'" style="width: 120px">
                </select>
            </li>
            <li>
                <label style="">部门：</label>
                <input type="text" name="department"/>
            </li>
            <li>
                <label style="">邮件：</label>
                <input type="text" name="email" class="easyui-validatebox" data-options="validType:'email'" />
            </li>
            <li>
                <label style="">手机：</label>
                <input type="text" name="telphone"/>
            </li>
            <li>
                <label style="">归属地区：</label>
                <input type="text" name="areaCode"/>
            </li>
            <li>
                <label style="">是否超级用户<sup style="color:red">*</sup>：</label>
                <select name="isSys" class="easyui-combobox" data-options="deltaX:25,editable:false,url:'<c:url value='/combo/100003'/>'" style="width: 120px">
                </select>
            </li>
            <li><label></label><input id="editSave" type="button" class="btn btn-small btn-primary" value="保存" onclick="save();"/></li>
        </ul>
    </form>
</div>