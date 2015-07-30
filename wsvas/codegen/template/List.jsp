<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="/WEB-INF/tld/ws.tld" prefix="ws" %>

#set($i = 0)
#set($endIndex = $plugin.middlegen.appname.lastIndexOf("."))
#set($jspIndex = $plugin.middlegen.appname.lastIndexOf(".")+1)
#set($jsppath = $plugin.middlegen.appname.substring($jspIndex))
#set($tmpStr = $plugin.middlegen.appname.substring(0,$endIndex))
#set($startIndex = $tmpStr.lastIndexOf(".")+1)
#set($module = $tmpStr.substring($startIndex,$endIndex))
#set($point = "$.")

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
#foreach($column in $table.columns)
                    <td>
                        <label>${column.remarks}：</label>
                        <input id="${column.variableName}" type="text" name="${column.variableName}">
                    </td>
#set($i = $i + 1)
#if($i == 3)
                </tr>
                <tr>
#set($i = 0)
#end
#end
                </tr>
                <tr>
                    <td colspan="4" align="center">
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
<jsp:include page="Edit${table.destinationClassName}.jsp"/>
</body>
</html>
<script type="text/javascript">
    $(function () {
        //初始化表格
        $(".grid").flexigrid
        (
            {
                url:'<c:url value="/${module}/${table.destinationClassName.substring(0, 1).toLowerCase()}${table.destinationClassName.substring(1)}/load" />',
                dataType: 'json',
                method: 'POST',
                colModel:[
#foreach($column in $table.columns)
#if($column.pk)
                {display: '<input type="checkbox" style="padding-bottom: 0; padding-top: 0;"/>', name : '${column.SqlName.toLowerCase()}', sortable : false, align: 'left', width : 15},
#end
#if(!$column.pk)
                {display: '${column.remarks}', name : '${column.SqlName.toLowerCase()}', sortable : true, align : 'left'},
#end
#end
                {display: '操作', name : 'operators', sortable : false, align: 'left'}
                ],
                buttons: [
                    {name: '新增', onpress: addAction, bimage:'<c:url value="/images/icons/add_16.png"/>'},
                    {name: '删除', onpress: deleteAction, bimage:'<c:url value="/images/icons/delete_16.png"/>'},
                    {name: '导出', onpress: exportAction, bimage:'<c:url value="/images/icons/export_16.png"/>'},
                    {separator: true}
                ],
                usepager: true,
                title: '${table.remarks}',
                useRp: true,
                rp: 15,
                showTableToggleBtn: true,
                dblClickResize: true,
                width: 'auto',
                height:  'auto',
                datacol: {'operators': function(colval, row) {
                    return "<a href='#' onclick=\"return editAction('" + row.id + "');\">编辑</a>";
                }, '${table.PkColumn.SqlName.toLowerCase()}': function (colval, row) {
                    return "<input id='idValue' type='hidden' value='" + colval + "'/><input name='${table.PkColumn.SqlName.toLowerCase()}' type='checkbox' value='" + colval + "'/>";
                }},
                onError: function(XMLHttpRequest, textStatus, errorThrown) {
                    ${point}messager.alert("请求错误", "服务器请求异常，请联系管理员！", "error");
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

#set($i = 0)
    //查询数据
    function queryAction() {
        $(".grid").flexReload({
            page : 1,
            params : [
#foreach($column in $table.columns)
#set($i = $i + 1)
#if($i == $table.columns.size())
                {'name':'${column.variableName}', 'value':$("#${column.variableName}").val()}
#end
#if($i != $table.columns.size())
                {'name':'${column.variableName}', 'value':$("#${column.variableName}").val()},
#end
#end
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
        editFrom.form("load", "<c:url value='/${module}/${table.destinationClassName.substring(0, 1).toLowerCase()}${table.destinationClassName.substring(1)}/'/>" + id);
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
            ${point}messager.alert("信息", "请选择您要删除的记录！", "info");
            return false;
        }

        ${point}messager.confirm("确认框", "是否确认删除选中的记录？", function(flag) {
            if(flag) {
                ${point}post("<c:url value='/${module}/${table.destinationClassName.substring(0, 1).toLowerCase()}${table.destinationClassName.substring(1)}/delete'/>", {
                    'ids' : values
                }, function(data) {
                    if(data.code == "0") {
                        ${point}messager.alert("信息", data.message, "info");
                    }
                    if(data.code == "-1") {
                        ${point}messager.alert("异常", data.message, "error");
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
                data = ${point}parseJSON(data);
                if(data.code == "0") {
                    ${point}messager.alert("信息", data.message, "info");
                }
                if(data.code == "-1") {
                    ${point}messager.alert("异常", data.message, "error");
                }
            }
        });
        editFrom.form("submit");
    }
</script>