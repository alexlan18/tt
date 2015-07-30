<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
#set($i = 0)
#set($endIndex = $plugin.middlegen.appname.lastIndexOf("."))
#set($jspIndex = $plugin.middlegen.appname.lastIndexOf(".")+1)
#set($jsppath = $plugin.middlegen.appname.substring($jspIndex))
#set($tmpStr = $plugin.middlegen.appname.substring(0,$endIndex))
#set($startIndex = $tmpStr.lastIndexOf(".")+1)
#set($module = $tmpStr.substring($startIndex,$endIndex))
#set($point = "$.")

<div id="editWindow" class="easyui-window" title="编辑信息" data-options="modal:true,closed:true,iconCls:'icon-save'"
     style="width:480px;">
    <form id="editForm" action="<c:url value='/${module}/${table.destinationClassName.substring(0, 1).toLowerCase()}${table.destinationClassName.substring(1)}/save'/>" method="post">
        <ul>
#foreach($column in $table.columns)
            <li>
                <label style="">${column.remarks}：</label>
                <input type="text" name="${column.variableName}"/>
            </li>
#end
            <li><label></label><input id="editSave" type="button" class="btn btn-small btn-primary" value="保存" onclick="save();"/></li>
        </ul>
    </form>
</div>