<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="top">
    <a class="logo" href="#">标志</a>
    <ul class="nav">
        <li>|</li>
        <li><a href="http://www.chinanetcenter.com" target="_blank">Wing Tech</a></li>
        <li><a href="#" onclick="return modifyUserPassword();">密码修改</a></li>
        <li><a href="<c:url value='/logout'/>">退出</a></li>
    </ul>
</div>
<div class="clearfix"></div>
<div id="editWindow" class="easyui-window" title="用户密码修改" data-options="modal:true,closed:true,iconCls:'icon-save'"
     style="width:450px;height:300px;">
    <form id="editForm" action="<c:url value='/system/sUser/modifyPassword'/>" method="post">
        <ul>
            <input type="hidden" name="id"/>
            <li>
                <label style="">旧密码<sup style="color:red">*</sup>：</label>
                <input type="password" name="password" class="easyui-validatebox" data-options="required:true"/>
            </li>
            <li>
                <label style="">新密码<sup style="color:red">*</sup>：</label>
                <input type="password" name="password1" class="easyui-validatebox" data-options="required:true"/>
            </li>
            <li>
                <label style="">密码确认<sup style="color:red">*</sup>：</label>
                <input type="password" name="password2" class="easyui-validatebox" data-options="required:true"/>
            </li>
            </li>
            <li><label></label><input id="editSave" type="button" class="btn btn-small btn-primary" value="保存" onclick="savePassword();"/></li>
        </ul>
    </form>
</div>

<script type="text/javascript">

    //绑定密码修改成功事件
    $(function() {
        $("#editForm").form({
            success : function(data) {
                data = $.parseJSON(data);
                if(data.code == "0") {
                    $.messager.alert("信息", data.message, "info");
                }
                if(data.code == "-1") {
                    $.messager.alert("异常", data.message, "error");
                }
            }
        });
    });

    //弹出密码修改对话框
    function modifyUserPassword() {
        $("#editWindow").window("open");
    }

    //保存密码
    function savePassword() {
        $("#editForm").form("submit");
    }
</script>