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
<body class="easyui-layout" style="width:100%;height:500px;">
<div region="west" style="width: 300px; border-top: 0px; border-left: 0px; border-bottom: 0px;">
    <div style="padding: 5px;">
        <input type="button" class="btn btn-small btn-primary" value="新增根节点" onclick="addRoot();"/>
        <input type="button" class="btn btn-small btn-primary" value="新增节点" onclick="addNode();"/>
        <input type="button" class="btn btn-small btn-primary" value="删除" onclick="deleteNode();"/>
    </div>
    <div id="menuTree">
    </div>
</div>
<div region="center" style="border: 0px;">
    <br/>
    <div id="editWindow">
        <form id="editForm" action="<c:url value='/system/sMenus/save'/>" method="post">
            <ul>
                <input type="hidden" name="id"/>
                <li>
                    <label style="">菜单ID<sup>*</sup>：</label>
                    <input id="menuId" type="text" name="menuId" class="easyui-validatebox" required="true"/>
                </li>
                <li>
                    <label style="">菜单名称：</label>
                    <input id="menuName" type="text" name="menuName"/>
                </li>
                <li>
                    <label style="">父菜单ID：</label>
                    <input id="parentMenuId" type="text" name="parentMenuId" value="ROOT_MENU" readonly="true"/>
                </li>
                <li>
                    <label style="">菜单地址：</label>
                    <input type="text" name="menuUrl"/>
                </li>
                <li>
                    <label style="">图标地址：</label>
                    <input type="text" name="iconUrl"/>
                </li>
                <li>
                    <label style="">排序号：</label>
                    <input type="text" name="sortNo"/>
                </li>
                <li>
                    <label style="">是否可见<sup style="color:red">*</sup>：</label>
                    <select name="isVisible" class="easyui-combobox" data-options="required:true,deltaX:25,editable:false" style="width: 120px">
                        <option value="1">是</option>
                        <option value="0">否</option>
                    </select>
                </li>
                <li>
                    <label style="">备注：</label>
                    <input type="text" name="remarks"/>
                </li>
                <li><label></label><input id="editSave" type="button" class="btn btn-small btn-primary" value="保存" onclick="save();"/></li>
            </ul>
        </form>
    </div>
</div>
</body>
</html>
<script type="text/javascript">

    var isAddRoot = false;

    $(function () {
        //加载菜单树
        $('#menuTree').tree({
            checkbox : false,
            cache : false,
            dnd : true,
            lines : true,
            url : "<c:url value='/system/sMenus/tree'/>",
            onClick : function(node) {//详细节点信息
                $(this).tree('toggle', node.target);
                $('#editForm').form("clear");
                $('#editForm').form("load", "<c:url value='/system/sMenus/'></c:url>" + node.attributes.id);
                $('#menuId').attr("readonly", true);
                $('#parentMenuId').attr("readonly", true);
            }
        });
    });

    //新增节点
    function addNode() {
        isAddRoot = false;
        //获取选择节点
        var selectedNode = getSelected();
        if(!selectedNode) {
            $.messager.alert("信息", "请先选择添加节点的父节点！", "info");
            return;
        }
        var editFrom = $("#editForm");
        editFrom.form("clear");
        $('#menuId').attr("readonly", false);
        $('#parentMenuId').val(selectedNode.id);
        $('#parentMenuId').attr("readonly", true);

    }

    //新增根节点
    function addRoot() {
        isAddRoot = true;
        var editFrom = $("#editForm");
        editFrom.form("clear");
        $('#menuId').attr("readonly", false);
        $('#parentMenuId').val("ROOT_MENU");
        $('#parentMenuId').attr("readonly", true);
    }

    //删除数据
    function deleteNode() {
        //获取选择节点
        var selectedNode = getSelected();
        if(!selectedNode) {
            $.messager.alert("信息", "请先选择节点！", "info");
            return;
        }

        $.messager.confirm("确认框", "是否确认删除该菜单？", function(flag) {
            if(flag) {
                $.ajax({
                    type : 'post',
                    url : "<c:url value='/system/sMenus/delete'/>",
                    cache : false,
                    data : {
                        'ids' : selectedNode.attributes.id
                    },
                    success : function(data) {
                        data = $.parseJSON(data);
                        if(data.code == "0") {
                            $.messager.alert("信息", data.message, "info");
                            $('#menuTree').tree('remove', selectedNode.target);
                            $("#editForm").form("clear");
                            $('#menuId').attr("readonly", false);
                        }
                        if(data.code == "-1") {
                            $.messager.alert("异常", data.message, "error");
                        }
                    }
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
                if(data.code == "0") {
                    $.messager.alert("信息", data.message, "info");
                    if(isAddRoot) {
                        reloadMenu(null, data.sMenus);
                    } else {
                        reloadMenu(getSelected(), data.sMenus);
                    }

                }
                if(data.code == "-1") {
                    $.messager.alert("异常", data.message, "error");
                }
            }
        });
        editFrom.form("submit");
    }

    //重新加载树
    function reloadMenu(node, menu) {
        //$('#menuTree').tree('reload');
        if(getSelected().id == menu.menuId) {
            $('#menuTree').tree('update',{
                target:node.target,
                id:menu.menuId,
                text:menu.menuName
            });
        } else {
            $('#menuTree').tree('append',{
                parent:node?node.target:null,
                data:[{
                    id:menu.menuId,
                    text:menu.menuName,
                    attributes:{
                        id:menu.id
                    }
                }]
            });
        }
    }

    function getSelected() {
        return $("#menuTree").tree("getSelected");
    }
</script>