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
                        <label>代码类型ID：</label>
                        <input id="codeTypeId" type="text" name="codeTypeId">
                    </td>
                    <td>
                        <label>代码类型名称：</label>
                        <input id="codeTypeName" type="text" name="codeTypeName">
                    </td>
                    <td>
                        <label>排序号：</label>
                        <input id="sortNo" type="text" name="sortNo">
                    </td>
                </tr>
                <tr>
                    <td colspan="3" align="center">
                        <button type="button" class="btn btn-small btn-primary" style="margin-bottom: 15px;"
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
<jsp:include page="EditSCodeType.jsp"/>
</body>
</html>
<script type="text/javascript">
$(function () {
    //初始化表格
    $(".grid").flexigrid
    (
            {
                url: '<c:url value="/system/sCodeType/load" />',
                dataType: 'json',
                method: 'POST',
                colModel: [
                    {display: '<input type="checkbox" style="padding-bottom: 0; padding-top: 0;"/>', name: 'id', sortable: false, align: 'left', width: 15},
                    {display: '代码类型ID', name: 'code_type_id', sortable: true, align: 'left'},
                    {display: '代码类型名称', name: 'code_type_name', sortable: true, align: 'left'},
                    {display: '排序号', name: 'sort_no', sortable: true, align: 'left'},
                    {display: '备注', name: 'remark', sortable: true, align: 'left'},
                    {display: '操作', name: 'operators', sortable: false, align: 'left'}
                ],
                buttons: [
                    {name: '新增', onpress: addAction, bimage: '<c:url value="/images/icons/add_16.png"/>'},
                    {name: '删除', onpress: deleteAction, bimage: '<c:url value="/images/icons/delete_16.png"/>'},
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
                datacol: {'operators': function (colval, row) {
                    return "<a href='#' onclick=\"return editAction('" + row.id + "');\">编辑</a>&nbsp;" + "<a href='#' onclick=\"return editCodeInfo('" + row.id + "');\">编辑代码</a>";
                }, 'id': function (colval, row) {
                    return "<input id='idValue' type='hidden' value='" + colval + "'/><input name='id' type='checkbox' value='" + colval + "'/>";
                }, 'code_type_id': function (colval, row) {
                    return "<span id='code_type_id_" + row.id + "'>" + colval + "</span>";
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
    $("#editCodeInfoWindow").window({
        onClose: function () {
            $("#codeInfoGrid").flexReload();
        }
    });

});

//查询数据
function queryAction() {
    $(".grid").flexReload({
        page: 1,
        params: [
            {'name': 'id', 'value': $("#id").val()},
            {'name': 'codeTypeId', 'value': $("#codeTypeId").val()},
            {'name': 'codeTypeName', 'value': $("#codeTypeName").val()},
            {'name': 'sortNo', 'value': $("#sortNo").val()},
            {'name': 'remark', 'value': $("#remark").val()}
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
    editFrom.form("load", "<c:url value='/system/sCodeType/'/>" + id);
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
            $.post("<c:url value='/system/sCodeType/delete'/>", {
                'ids': values
            }, function (data) {
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

//打开代码信息编辑
function editCodeInfo(rowId) {
    $("#codeInfoWindow").window("open");
    $("#editCodeTypeId").val($("#code_type_id_" + rowId).html());
    $("#codeInfoGrid").flexigrid
    (
            {
                id: "CodeInfo",
                url: '<c:url value="/system/sCodeInfo/load" />',
                params: [
                    {'name': 'codeTypeId', 'value': $("#code_type_id_" + rowId).html()},
                ],
                dataType: 'json',
                method: 'POST',
                colModel: [
                    {display: '<input type="checkbox" style="padding-bottom: 0; padding-top: 0;"/>', name: 'id', sortable: false, align: 'left', width: 15},
                    {display: '代码类型ID', name: 'code_type_id', sortable: true, align: 'left'},
                    {display: '代码值', name: 'code_value', sortable: true, align: 'left'},
                    {display: '代码名称', name: 'code_name', sortable: true, align: 'left'},
                    {display: '排序号', name: 'sort_no', sortable: true, align: 'left'},
                    {display: '备注', name: 'remark', sortable: true, align: 'left'},
                    {display: '操作', name: 'operators', sortable: false, align: 'left'}
                ],
                buttons: [
                    {name: '新增', onpress: addCodeInoAction, bimage: '<c:url value="/images/icons/add_16.png"/>'},
                    {name: '删除', onpress: deleteCodeInfoAction, bimage: '<c:url value="/images/icons/delete_16.png"/>'},
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
                datacol: {'operators': function (colval, row) {
                    return "<a href='#' onclick=\"return editCodeInfoAction('" + row.id + "');\">编辑</a>";
                }, 'id': function (colval, row) {
                    return "<input id='idValue' type='hidden' value='" + colval + "'/><input name='id' type='checkbox' value='" + colval + "'/>";
                }},
                onError: function (XMLHttpRequest, textStatus, errorThrown) {
                    $.messager.alert("请求错误", "服务器请求异常，请联系管理员！", "error");
                }
            }
    );
    $("#codeInfoGrid").flexReload({
        params: [
            {'name': 'codeTypeId', 'value': $("#code_type_id_" + rowId).html()}
        ]
    });
}

//编辑代码信息
function editCodeInfoAction(rowId) {
    var editFrom = $("#editCodeInfoForm");
    $("#editCodeInfoWindow").window("open");
    editFrom.form("clear");
    var id = $('#' + rowId).find(":checkbox").val();
    editFrom.form("load", "<c:url value='/system/sCodeInfo/'/>" + id);
}

//新增代码信息数据
function addCodeInoAction() {
    //打开新增框
    $("#editCodeInfoWindow").window("open");
    $("#editCodeInfoForm").form("clear");
    $("#hiddenCodeTypeId").val($("#editCodeTypeId").val());
}

//删除代码信息数据
function deleteCodeInfoAction() {

    var values = "";
    var i = 0;
    $("#codeInfoGrid").find(":checkbox").each(function () {
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
            $.post("<c:url value='/system/sCodeInfo/delete'/>", {
                'ids': values
            }, function (data) {
                if (data.code == "0") {
                    $.messager.alert("信息", data.message, "info");
                }
                if (data.code == "-1") {
                    $.messager.alert("异常", data.message, "error");
                }
                $("#codeInfoGrid").flexReload();
            });
        }
    });
}

//保存代码数据
function saveCodeInfo() {
    var editFrom = $("#editCodeInfoForm");
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
</script>