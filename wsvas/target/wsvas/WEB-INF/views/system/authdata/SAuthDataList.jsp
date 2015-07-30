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
                        <label>权限ID：</label>
                        <input id="authId" type="text" name="authId">
                    </td>
                    <td>
                        <label>权限名称：</label>
                        <input id="authName" type="text" name="authName">
                    </td>
                    <td>
                        <label>是否可用：</label>
                        <select id="enabled" name="enabled" class="easyui-combobox" data-options="editable:false,url:'<c:url value='/combo/100002'/>?showWaitChoose=true'" style="width: 120px">
                        </select>
                    </td>
                    <td>
                        <label>是否超级权限：</label>
                        <select id="isSys" name="isSys" class="easyui-combobox" data-options="editable:false,url:'<c:url value='/combo/100003'/>?showWaitChoose=true'" style="width: 120px">
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
<jsp:include page="EditSAuthData.jsp"/>
<jsp:include page="assignResources.jsp"/>
</body>
</html>
<script type="text/javascript">
    $(function () {
                //初始化表格
        $(".grid").flexigrid
        (
            {
                url:'<c:url value="/system/sAuthData/load" />',
                dataType: 'json',
                method: 'POST',
                colModel:[
                {display: '<input type="checkbox" style="padding-bottom: 0; padding-top: 0;"/>', name : 'id', sortable : false, align: 'left', width : 15},
                {display: '权限ID', name : 'auth_id', sortable : true, align : 'left'},
                {display: '权限名称', name : 'auth_name', sortable : true, align : 'left'},
                {display: '是否可用', name : 'enabled', sortable : true, align : 'left'},
                {display: '是否超级权限', name : 'is_sys', sortable : true, align : 'left'},
                {display: '操作', name : 'operators', sortable : true, align: 'left'}
                ],
                buttons: [
                    {name: '新增', onpress: addAction,bimage:'<c:url value="/images/icons/add_16.png"/>'},
                    {name: '删除', onpress: deleteAction,bimage:'<c:url value="/images/icons/delete_16.png"/>'},
                    {separator: true}
                ],
                usepager: true,
                title: '',
                useRp: true,
                rp: 15,
                showTableToggleBtn: true,
                dblClickResize: true,
                width: 'auto',
                height:  'auto',
                datacol: {'operators': function(colval, row) {
                    return "<a href='#' onclick=\"return editAction('" + row.id + "');\">编辑</a>&nbsp;<a href='#' onclick=\"return assignResources('" + row.id + "');\">分配资源</a>";
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
                {'name':'id', 'value':$("#id").val()},
                {'name':'authId', 'value':$("#authId").val()},
                {'name':'authName', 'value':$("#authName").val()},
                {'name':'enabled', 'value':$("#enabled").val()},
                {'name':'isSys', 'value':$("#isSys").val()}
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
        editFrom.form("load", "<c:url value='/system/sAuthData/'/>" + id);
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
                $.post("<c:url value='/system/sAuthData/delete'/>", {
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

    //导出数据
    function exportAction() {

    }

    //保存数据
    function save() {
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
        editFrom.form("submit");
    }

    //分配资源
    function assignResources(rowId) {

        var resourcesWin = $("#resourcesWindow");
        resourcesWin.window("open");
        var id = $('#' + rowId).find("#idValue").val();
        $('#assignAuthId').val(id);

        $('#resources').flexigrid(
                {
                    url:'<c:url value="/system/sAuthResource/loadResources" />',
                    dataType: 'json',
                    method: 'POST',
                    params : [
                        {'name':'id', 'value':$("#assignAuthId").val()}
                    ],
                    colModel:[
                        {display: '<input type="checkbox" style="padding-bottom: 0; padding-top: 0;"/>', name : 'id', sortable : false, align: 'left', width : 15},
                        {display: '资源ID', name : 'resource_id', sortable : true, align : 'left'},
                        {display: '资源名称', name : 'resource_name', sortable : true, align : 'left'},
                        {display: '资源类型', name : 'resource_type', sortable : true, align : 'left'},
                        {display: '资源链接', name : 'resource_url', sortable : true, align : 'left'},
                        {display: '是否分配', name : 'auth_flag', sortable : true, align : 'left', hide : true}
                    ],
                    usepager: true,
                    title: '资源列表',
                    useRp: true,
                    rp: 15,
                    showTableToggleBtn: true,
                    dblClickResize: true,
                    width: 'auto',
                    height:  'auto',
                    datacol: {'operators': function(colval, rowId) {
                        return "<a href='#' onclick='return editAction(" + rowId + ");'>编辑</a>";
                    }, 'id': function (colval, rowId) {
                        return "<input id='idValue' type='hidden' value='" + colval + "'/><input name='id' type='checkbox' value='" + colval + "'/>";
                    }, 'auth_flag' : function(colval, rowId) {
                        return "<input id='authFlag' name='authFlag' type='hidden' value='" + colval + "'/>";
                    }},
                    onError: function(XMLHttpRequest, textStatus, errorThrown) {
                        $.messager.alert("请求错误", "服务器请求异常，请联系管理员！", "error");
                    },
                    onSuccess: function(table) {
                        $(table.bDiv).find("tr").each(function() {
                            if($(this).find("#authFlag").val() == 'true') {
                                $(this).find(":checkbox").prop("checked", true);
                            }
                        });
                    }
                }
        );
        $('#resources').flexReload({
            params : [
                {'name':'id', 'value':$("#assignAuthId").val()}
            ]
        });
    }

    //保存权限
    function auth() {

        var checkeds = "";
        var allIds = "";
        var i = 0;
        $("#resources").find(":checkbox").each(function () {
            allIds += $(this).val() + ",";
            if ($(this).prop("checked")) {
                checkeds += $(this).val() + ",";
                i++;
            }
        });

        $.post("<c:url value='/system/sAuthResource/saveAuth'/>", {
            'allIds' : allIds,
            'checkeds' : checkeds,
            'id' : $("#assignAuthId").val()
        }, function(data) {
            data = $.parseJSON(data);
            if(data.code == "0") {
                $.messager.alert("信息", data.message, "info");
            }
            if(data.code == "-1") {
                $.messager.alert("异常", data.message, "error");
            }
        });

    }

</script>