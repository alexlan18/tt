<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="/WEB-INF/tld/ws.tld" prefix="ws" %>

<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <%@include file="/include.jsp" %>
    <title></title>
</head>
<body>
<div class="pageHeader">
    <form id="queryForm" method="post">
        <div class="searchBar">
            <table class="searchContent">
                <tbody>
                <tr>
                    <td>
                        <label>登录名称：</label>
                        <input id="loginName" type="text" name="loginName">
                    </td>
                    <td>
                        <label>用户名称：</label>
                        <input id="userName" type="text" name="userName">
                    </td>
                    <td>
                        <label>是否可用：</label>
                        <select id="enabled" name="enabled" class="easyui-combobox"
                                data-options="editable:false,url:'<c:url value='/combo/100002'/>?showWaitChoose=true'"
                                style="width: 120px">
                        </select>
                    </td>
                    <td>
                        <label>部门：</label>
                        <input id="department" type="text" name="department">
                    </td>
                </tr>
                <tr>
                    <td>
                        <label>邮件：</label>
                        <input id="email" type="text" name="email">
                    </td>
                    <td>
                        <label>手机：</label>
                        <input id="telphone" type="text" name="telphone">
                    </td>
                    <td>
                        <label>归属地区：</label>
                        <input id="areaCode" type="text" name="areaCode">
                    </td>
                    <td>
                        <label>是否超级用户：</label>
                        <select id="isSys" name="isSys" class="easyui-combobox" data-options="editable:false,url:'<c:url value='/combo/100003'/>?showWaitChoose=true'"
                                style="width: 120px">
                        </select>
                    </td>
                </tr>
                <tr>
                    <td colspan="4" align="center">
                        <button type="button" class="btn btn-small btn-primary" style="margin-top: 15px;"
                                onclick="queryAction();">查询
                        </button>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </form>
</div>
<table class="grid"></table>
<jsp:include page="EditSUser.jsp"/>
<jsp:include page="assignRoles.jsp"/>
<div class="clear"></div>
</body>
</html>
<script type="text/javascript">
$(function () {
    //初始化表格
    $(".grid").flexigrid
    (
            {
                url: '<c:url value="/system/sUser/load" />',
                dataType: 'json',
                method: 'POST',
                colModel: [
                    {display: '<input type="checkbox" style="padding-bottom: 0; padding-top: 0;"/>', name: 'id', sortable: false, align: 'left', width: 15},
                    {display: '登录名称', name: 'login_name', sortable: true, align: 'left'},
                    {display: '用户名称', name: 'user_name', sortable: true, align: 'left'},
                    {display: '是否可用', name: 'enabled', sortable: true, align: 'left'},
                    {display: '部门', name: 'department', sortable: true, align: 'left'},
                    {display: '邮件', name: 'email', sortable: true, align: 'left'},
                    {display: '手机', name: 'telphone', sortable: true, align: 'left'},
                    {display: '归属地区', name: 'area_code', sortable: true, align: 'left'},
                    {display: '是否超级用户', name: 'is_sys', sortable: true, align: 'left'},
                    {display: '操作', name: 'operators', sortable: false, align: 'left'}
                ],
                buttons: [
                    {name: '新增', onpress: addAction, bimage: '<c:url value="/images/icons/add_16.png"/>'},
                    {name: '删除', onpress: deleteAction, bimage: '<c:url value="/images/icons/delete_16.png"/>'},
                    {name: '导出', onpress: exportAction, bimage: '<c:url value="/images/icons/export_16.png"/>'},
                    {separator: true}
                ],
                usepager: true,
                title: '',
                useRp: true,
                rp: 15,
                showTableToggleBtn: true,
                dblClickResize: true,
                width: 'auto',
                height: 'auto',
                datacol: {
                    'operators': function (colval, row) {
                        return "<a href='#' onclick=\"return editAction('" + row.id + "');\">编辑</a>&nbsp;" + "<a href='#' onclick=\"return assignRole('" + row.id + "');\">分配角色</a>";
                    }, 'id': function (colval, row) {
                        return "<input id='idValue' type='hidden' value='" + colval + "'/><input name='id' type='checkbox' value='" + colval + "'/>";
                    }
                },
                onError: function (XMLHttpRequest, textStatus, errorThrown) {
                    $.messager.alert("请求错误", "服务器请求异常，请联系管理员！", "error");
                }
            }
    );

    //绑定关闭窗口事件
    $("#editWindow").window({
        onClose: function () {
            $(".grid").flexReload();
        }
    });
});

//查询数据
function queryAction() {
    $(".grid").flexReload({
        page: 1,
        params: [
            {'name': 'id', 'value': $("#id").val()},
            {'name': 'loginName', 'value': $("#loginName").val()},
            {'name': 'password', 'value': $("#password").val()},
            {'name': 'userName', 'value': $("#userName").val()},
            {'name': 'enabled', 'value': $("#enabled").combobox('getValue')},
            {'name': 'department', 'value': $("#department").val()},
            {'name': 'email', 'value': $("#email").val()},
            {'name': 'telphone', 'value': $("#telphone").val()},
            {'name': 'areaCode', 'value': $("#areaCode").val()},
            {'name': 'isSys', 'value': $("#isSys").combobox('getValue')}
        ]
    });
}

//新增数据
function addAction() {
    //打开新增框
    $("#editWindow").window("open");
    $("#editForm").form("clear");
}

//编辑数据
function editAction(rowId) {
    var editFrom = $("#editForm");
    $("#editWindow").window("open");
    editFrom.form("clear");
    var id = $('#' + rowId).find(":checkbox").val();
    editFrom.form("load", "<c:url value='/system/sUser/'/>" + id);
}

//删除数据
function deleteAction() {

    var values = "";
    var i = 0;
    $(".grid").find(":checkbox").each(function () {
        if ($(this).prop("checked")) {
            values += $(this).val() + ",";
            i++;
        }
    });
    if (i == 0) {
        $.messager.alert("信息", "请选择您要删除的记录！", "info");
        return false;
    }

    $.messager.confirm("确认框", "是否确认删除选中的记录？", function (flag) {
        if (flag) {
            $.post("<c:url value='/system/sUser/delete'/>", {
                'ids': values
            }, function (data) {
                data = $.parseJSON(data);
                if (data.code == "0") {
                    $.messager.alert("信息", data.message, "info");
                }
                if (data.code == "-1") {
                    $.messager.alert("异常", data.message, "error");
                }
                $(".grid").flexReload();
            });
        }
    });
}

//导出数据
function exportAction() {

}

//保存数据
function save() {
    var editFrom = $("#editForm");
    editFrom.form({
        success: function (data) {
            data = $.parseJSON(data);
            if (data.code == "0") {
                $.messager.alert("信息", data.message, "info");
            }
            if (data.code == "-1") {
                $.messager.alert("异常", data.message, "error");
            }
        }
    });
    editFrom.form("submit");
}

//打开角色分配窗口
function assignRole(rowId) {

    var id = $('#' + rowId).find("#idValue").val();
    $("#roleUserId").val(id);

    $('#rolesWindow').window("open");
    $('#authoritys').flexigrid
    (
            {
                url: '<c:url value="/system/sUserRole/loadRolesByUser" />',
                params: [
                    {'name': 'id', 'value': id}
                ],
                dataType: 'json',
                method: 'POST',
                colModel: [
                    {display: '<input type="checkbox" style="padding-bottom: 0; padding-top: 0;"/>', name: 'id', sortable: false, align: 'left', width: 15},
                    {display: '角色ID', name: 'role_id', sortable: true, align: 'left'},
                    {display: '角色名称', name: 'role_name', sortable: true, align: 'left'},
                    {display: '是否可用', name: 'enabled', sortable: true, align: 'left'},
                    {display: '是否超级权限', name: 'is_sys', sortable: true, align: 'left'}
                ],
                usepager: true,
                title: '已分配角色',
                useRp: true,
                rp: 10,
                showTableToggleBtn: true,
                dblClickResize: true,
                width: 'auto',
                height: 'auto',
                datacol: {'id': function (colval, row) {
                    return "<input id='idValue' type='hidden' value='" + colval + "'/><input name='id' type='checkbox' value='" + colval + "'/>";
                }},
                onError: function (XMLHttpRequest, textStatus, errorThrown) {
                    $.messager.alert("请求错误", "服务器请求异常，请联系管理员！", "error");
                }
            }
    );

    $('#notAuthoritys').flexigrid
    (
            {
                url: '<c:url value="/system/sUserRole/loadNotRolesByUser" />',
                params: [
                    {'name': 'id', 'value': id}
                ],
                dataType: 'json',
                method: 'POST',
                colModel: [
                    {display: '<input type="checkbox" style="padding-bottom: 0; padding-top: 0;"/>', name: 'id', sortable: false, align: 'left', width: 15},
                    {display: '角色ID', name: 'role_id', sortable: true, align: 'left'},
                    {display: '角色名称', name: 'role_name', sortable: true, align: 'left'},
                    {display: '是否可用', name: 'enabled', sortable: true, align: 'left'},
                    {display: '是否超级权限', name: 'is_sys', sortable: true, align: 'left'}
                ],
                usepager: true,
                title: '未分配角色',
                useRp: true,
                rp: 10,
                showTableToggleBtn: true,
                dblClickResize: true,
                width: 'auto',
                height: 'auto',
                datacol: {'id': function (colval, rowId) {
                    return "<input id='idValue' type='hidden' value='" + colval + "'/><input name='id' type='checkbox' value='" + colval + "'/>";
                }},
                onError: function (XMLHttpRequest, textStatus, errorThrown) {
                    $.messager.alert("请求错误", "服务器请求异常，请联系管理员！", "error");
                }
            }
    );
    $('#authoritys').flexReload();
    $('#notAuthoritys').flexReload();
}

//删除角色
function deleteAuth() {
    var values = "";
    var i = 0;
    $("#authoritys").find(":checkbox").each(function () {
        if ($(this).prop("checked")) {
            values += $(this).val() + ",";
            i++;
        }
    });
    if (i == 0) {
        $.messager.alert("信息", "请选择您要删除的角色！", "info");
        return false;
    }

    $.messager.confirm("确认框", "是否确认删除选中的角色？", function (flag) {
        if (flag) {
            $.post("<c:url value='/system/sUserRole/deleteAuth'/>", {
                'ids': values,
                'id': $("#roleUserId").val()
            }, function (data) {
                data = $.parseJSON(data);
                if (data.code == "0") {
                    $.messager.alert("信息", data.message, "info");
                }
                if (data.code == "-1") {
                    $.messager.alert("异常", data.message, "error");
                }
                $('#authoritys').flexReload();
                $('#notAuthoritys').flexReload();
            });
        }
    });
    return true;
}

//分配角色
function auth() {

    var values = "";
    var i = 0;
    $("#notAuthoritys").find(":checkbox").each(function () {
        if ($(this).prop("checked")) {
            values += $(this).val() + ",";
            i++;
        }
    });
    if (i == 0) {
        $.messager.alert("信息", "请选择您要添加的权限！", "info");
        return false;
    }
    $.post("<c:url value='/system/sUserRole/saveAuth'/>", {
        'id': $("#roleUserId").val(),
        'ids': values
    }, function (data) {
        data = $.parseJSON(data);
        if (data.code == "0") {
            $.messager.alert("信息", data.message, "info");
        }
        if (data.code == "-1") {
            $.messager.alert("异常", data.message, "error");
        }
        $('#authoritys').flexReload();
        $('#notAuthoritys').flexReload();
    });
    return true;
}
</script>