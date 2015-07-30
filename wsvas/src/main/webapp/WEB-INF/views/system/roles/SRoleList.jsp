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
                        <label>角色ID：</label>
                        <input id="roleId" type="text" name="roleId">
                    </td>
                    <td>
                        <label>角色名称：</label>
                        <input id="roleName" type="text" name="roleName">
                    </td>
                    <td>
                        <label>是否可用：</label>
                        <select id="enabled" name="enabled" class="easyui-combobox" style="width: 120px" data-options="editable:false,url:'<c:url value='/combo/100002'/>?showWaitChoose=true'">
                        </select>
                    </td>
                    <td>
                        <label>是否超级权限：</label>
                        <select id="isSys" name="isSys" class="easyui-combobox" style="width: 120px" data-options="editable:false,url:'<c:url value='/combo/100003'/>?showWaitChoose=true'">
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
<jsp:include page="EditSRole.jsp"/>
<jsp:include page="assignTreeAuthority.jsp"/>
<jsp:include page="assignResourceAuthority.jsp"/>
</body>
</html>
<script type="text/javascript">
    $(function () {
                //初始化表格
        $(".grid").flexigrid
        (
            {
                url:'<c:url value="/system/sRole/load" />',
                dataType: 'json',
                method: 'POST',
                colModel:[
                {display: '<input type="checkbox" style="padding-bottom: 0; padding-top: 0;"/>', name : 'id', sortable : false, align: 'left', width : 15},
                {display: '角色ID', name : 'role_id', sortable : true, align : 'left'},
                {display: '角色名称', name : 'role_name', sortable : true, align : 'left'},
                {display: '是否可用', name : 'enabled', sortable : true, align : 'left'},
                {display: '是否超级权限', name : 'is_sys', sortable : true, align : 'left'},
                {display: '操作', name : 'operators', sortable : false, align: 'left', width : 174}
                ],
                buttons: [
                    {name: '新增', onpress: addAction, bimage:'<c:url value="/images/icons/add_16.png"/>'},
                    {name: '删除', onpress: deleteAction, bimage:'<c:url value="/images/icons/delete_16.png"/>'},
                    {separator: true}
                ],
                usepager: true,
                title: '角色列表',
                useRp: true,
                rp: 15,
                showTableToggleBtn: true,
                dblClickResize: true,
                width: 'auto',
                height:  'auto',
                datacol: {'operators': function(colval, row) {
                    return "<a href='#' onclick=\"return editAction('" + row.id + "');\">编辑</a>&nbsp;<a href='#' onclick=\"return assignResourceAuth('" + row.id + "');\">分配资源权限</a>&nbsp;<a href='#' onclick=\"return assignTreeAuth('" + row.id + "');\">分配菜单权限</a>";
                }, 'id': function (colval, rowId) {
                    return "<input id='idValue' type='hidden' value='" + colval + "'/><input name='id' type='checkbox' value='" + colval + "'/>";
                }},
                onError: function(XMLHttpRequest, textStatus, errorThrown) {
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
            page : 1,
            params : [
                {'name':'roleId', 'value':$("#roleId").val()},
                {'name':'roleName', 'value':$("#roleName").val()},
                {'name':'enabled', 'value':$("#enabled").combobox("getValue")},
                {'name':'isSys', 'value':$("#isSys").combobox("getValue")}
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
        var id = $('#' + rowId).find("#idValue").val();
        editFrom.form("load", "<c:url value='/system/sRole/'/>" + id);
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
        if(i == 0) {
            $.messager.alert("信息", "请选择您要删除的记录！", "info");
            return false;
        }

        $.messager.confirm("确认框", "是否确认删除选中的记录？", function(flag) {
            if(flag) {
                $.post("<c:url value='/system/sRole/delete'/>", {
                    'ids' : values
                }, function(data) {
                    if(data.code == "0") {
                        $.messager.alert("信息", data.message, "info");
                    }
                    if(data.code == "-1") {
                        $.messager.alert("异常", data.message, "error");
                    }
                    $(".grid").flexReload();
                });
            }
        });
    }

    //保存数据
    function save(button) {
        var editFrom = $("#editForm");
        editFrom.form({
            success: function (data) {
                data = $.parseJSON(data);
                if(data.code == "0") {
                    $.messager.alert("信息", data.message, "info");
                }
                if(data.code == "-1") {
                    $.messager.alert("异常", data.message, "error");
                }
            }
        });
        editFrom.form("submit", {"button" : button});
    }

    //分配菜单树权限
    function assignTreeAuth(rowId) {

        var id = $('#' + rowId).find("#idValue").val();
        $("#authRoleId").val(id);
        $("#treeWindow").window("open");
        $("#menuTree").tree({
            checkbox : true,
            cache : false,
            dnd : true,
            lines : true,
            url : "<c:url value='/system/sMenus/roleTree/'/>" + id
        });
    }

    //保存分配的菜单树权限
    function saveTreeAuth() {

        var nodes = $('#menuTree').tree('getChecked', ['checked','indeterminate']);
        var values = "";
        $(nodes).each(function() {
            values += this.id + ",";
        });

        $.post("<c:url value='/system/sRole/saveMenuTreeAuth'/>",
                {id : $("#authRoleId").val(), menuIds : values}, function(data) {
                    data = $.parseJSON(data);
                    if(data.code == "0") {
                        $.messager.alert("信息", data.message, "info");
                    }
                    if(data.code == "-1") {
                        $.messager.alert("异常", data.message, "error");
                    }
        });
    }

    //分配资源权限
    function assignResourceAuth(rowId) {

        var id = $('#' + rowId).find("#idValue").val();
        $("#resourceRoleId").val(id);

        $('#resourcesWindow').window("open");
        $('#authoritys').flexigrid
        (
                {
                    url:'<c:url value="/system/sAuthData/loadByRole" />',
                    params : [{'name':'id', 'value':id}],
                    dataType: 'json',
                    method: 'POST',
                    colModel:[
                        {display: '<input type="checkbox" style="padding-bottom: 0; padding-top: 0;"/>', name : 'id', sortable : false, align: 'left', width : 15},
                        {display: '权限ID', name : 'auth_id', sortable : true, align : 'left'},
                        {display: '权限名称', name : 'auth_name', sortable : true, align : 'left'},
                        {display: '是否可用', name : 'enabled', sortable : true, align : 'left'},
                        {display: '是否超级权限', name : 'is_sys', sortable : true, align : 'left'}
                    ],
                    usepager: true,
                    title: '已分配权限',
                    useRp: true,
                    rp: 10,
                    showTableToggleBtn: true,
                    dblClickResize: true,
                    width: 'auto',
                    height:  'auto',
                    datacol:{'id': function (colval, rowId) {
                        return "<input id='idValue' type='hidden' value='" + colval + "'/><input name='id' type='checkbox' value='" + colval + "'/>";
                    }},
                    onError: function(XMLHttpRequest, textStatus, errorThrown) {
                        $.messager.alert("请求错误", "服务器请求异常，请联系管理员！", "error");
                    }
                }
        );

        $('#notAuthoritys').flexigrid
        (
                {
                    url:'<c:url value="/system/sAuthData/loadNotByRole" />',
                    params : [{'name':'id', 'value':id}],
                    dataType: 'json',
                    method: 'POST',
                    colModel:[
                        {display: '<input type="checkbox" style="padding-bottom: 0; padding-top: 0;"/>', name : 'id', sortable : false, align: 'left', width : 15},
                        {display: '权限ID', name : 'auth_id', sortable : true, align : 'left'},
                        {display: '权限名称', name : 'auth_name', sortable : true, align : 'left'},
                        {display: '是否可用', name : 'enabled', sortable : true, align : 'left'},
                        {display: '是否超级权限', name : 'is_sys', sortable : true, align : 'left'}
                    ],
                    usepager: true,
                    title: '未分配权限',
                    useRp: true,
                    rp: 10,
                    showTableToggleBtn: true,
                    dblClickResize: true,
                    width: 'auto',
                    height:  'auto',
                    datacol: {'id': function (colval, rowId) {
                        return "<input id='idValue' type='hidden' value='" + colval + "'/><input name='id' type='checkbox' value='" + colval + "'/>";
                    }},
                    onError: function(XMLHttpRequest, textStatus, errorThrown) {
                        $.messager.alert("请求错误", "服务器请求异常，请联系管理员！", "error");
                    }
                }
        );
        $('#authoritys').flexReload();
        $('#notAuthoritys').flexReload();
    }

    //删除授权
    function deleteAuth() {
        var values = "";
        var i = 0;
        $("#authoritys").find(":checkbox").each(function () {
            if ($(this).prop("checked")) {
                values += $(this).val() + ",";
                i++;
            }
        });
        if(i == 0) {
            $.messager.alert("信息", "请选择您要删除的权限！", "info");
            return false;
        }

        $.messager.confirm("确认框", "是否确认删除选中的权限？", function(flag) {
            if(flag) {
                $.post("<c:url value='/system/sRoleAuth/deleteAuth'/>", {
                    'id' : $("#resourceRoleId").val(),
                    'ids' : values
                }, function(data) {
                    data = $.parseJSON(data);
                    if(data.code == "0") {
                        $.messager.alert("信息", data.message, "info");
                    }
                    if(data.code == "-1") {
                        $.messager.alert("异常", data.message, "error");
                    }
                    $('#authoritys').flexReload();
                    $('#notAuthoritys').flexReload();
                });
            }
        });
        return true;
    }

    function auth() {
        var values = "";
        var i = 0;
        $("#notAuthoritys").find(":checkbox").each(function () {
            if ($(this).prop("checked")) {
                values += $(this).val() + ",";
                i++;
            }
        });
        if(i == 0) {
            $.messager.alert("信息", "请选择您要添加的权限！", "info");
            return false;
        }
        $.post("<c:url value='/system/sRoleAuth/saveAuth'/>", {
            'id' : $("#resourceRoleId").val(),
            'ids' : values
        }, function(data) {
            data = $.parseJSON(data);
            if(data.code == "0") {
                $.messager.alert("信息", data.message, "info");
            }
            if(data.code == "-1") {
                $.messager.alert("异常", data.message, "error");
            }
            $('#authoritys').flexReload();
            $('#notAuthoritys').flexReload();
        });
        return true;
    }

</script>