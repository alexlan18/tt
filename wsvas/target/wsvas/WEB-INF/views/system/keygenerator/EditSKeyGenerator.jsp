<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div id="editWindow" class="easyui-window" title="编辑信息" data-options="modal:true,closed:true,iconCls:'icon-save'"
     style="width:480px;height:520px;">
    <form id="editForm" action="<c:url value='/system/sKeyGenerator/save'/>" method="post">
        <ul>
            <input type="hidden" name="id"/>
            <li>
                <label style="">前缀：</label>
                <input type="text" name="prefix"/>
            </li>
            <li>
                <label style="">自增数：</label>
                <input type="text" name="num"/>
            </li>
            <li>
                <label style="">后缀：</label>
                <input type="text" name="suffix"/>
            </li>
            <li>
                <label style="">长度：</label>
                <input type="text" name="length"/>
            </li>
            <li>
                <label style="">主键日期：</label>
                <input type="text" class="Wdate" name="keyDate" onfocus="WdatePicker({isShowClear:false})"/>
            </li>
            <li>
                <label style="">类型：</label>
                <input type="text" name="type"/>
            </li>
            <li><label></label><input id="editSave" type="button" class="btn btn-small btn-primary" value="保存" onclick="save();"/></li>
        </ul>
    </form>
</div>