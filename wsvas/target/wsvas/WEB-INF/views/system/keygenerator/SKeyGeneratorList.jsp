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
                        <label>前缀：</label>
                        <input id="prefix" type="text" name="prefix">
                    </td>
                    <td>
                        <label>后缀：</label>
                        <input id="suffix" type="text" name="suffix">
                    </td>
                    <td>
                        <label>主键日期：</label>
                        <input id="keyDate" type="text" class="Wdate" name="keyDate" onfocus="WdatePicker({isShowClear:false})">
                    </td>
                </tr>
                <tr>
                    <td>
                        <label>类型：</label>
                        <input id="type" type="text" name="type">
                    </td>
                </tr>
                <tr>
                    <td colspan="4" align="center">
                        <button type="button" class="btn btn-small btn-primary" style="margin-bottom: 3px;"
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
<jsp:include page="EditSKeyGenerator.jsp"/>
</body>
</html>
<script type="text/javascript">
    $(function () {
        //初始化表格
        $(".grid").flexigrid
        (
            {
                url:'<c:url value="/system/sKeyGenerator/load" />',
                dataType: 'json',
                method: 'POST',
                colModel:[
                {display: '<input type="checkbox" style="padding-bottom: 0; padding-top: 0;"/>', name : 'id', sortable : false, align: 'left', width : 15},
                {display: '前缀', name : 'prefix', sortable : true, align : 'left'},
                {display: '自增数', name : 'num', sortable : true, align : 'left'},
                {display: '后缀', name : 'suffix', sortable : true, align : 'left'},
                {display: '长度', name : 'length', sortable : true, align : 'left'},
                {display: '主键日期', name : 'key_date', sortable : true, align : 'left'},
                {display: '类型', name : 'type', sortable : true, align : 'left'},
                {display: '操作', name : 'operators', sortable : false, align: 'left'}
                ],
                buttons: [
                    {name: '新增', onpress: addAction, bimage:'<c:url value="/images/icons/add_16.png"/>'},
                    {name: '删除', onpress: deleteAction, bimage:'<c:url value="/images/icons/delete_16.png"/>'},
                    {name: '导出', onpress: exportAction, bimage:'<c:url value="/images/icons/export_16.png"/>'},
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
                    return "<a href='#' onclick=\"return editAction('" + row.id + "');\">编辑</a>";
                }, 'id': function (colval, row) {
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
                {'name':'prefix', 'value':$("#prefix").val()},
                {'name':'suffix', 'value':$("#suffix").val()},
                {'name':'keyDate', 'value':$("#keyDate").val()},
                {'name':'type', 'value':$("#type").val()}
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
        editFrom.form("load", "<c:url value='/system/sKeyGenerator/'/>" + id);
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
                $.post("<c:url value='/system/sKeyGenerator/delete'/>", {
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
</script>