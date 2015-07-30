<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="Expires" CONTENT="0">
    <meta http-equiv="Cache-Control" CONTENT="no-cache">
    <meta http-equiv="Pragma" CONTENT="no-cache">
    <title>增值业务系统</title>

    <%@include file="/include.jsp" %>
</head>
<body>
<form id="loginForm" action="<c:url value='/j_spring_security_check'/>" method="post" style="text-align: center">
    <div class="login">
        <div class="login_top">
            <div class="login_top_left"><img src="<c:url value='/images/login_03.gif'/>"/></div>
            <div class="login_top_center"></div>
        </div>
        <div class="login_center">
            <div class="login_center_left"></div>

            <div class="login_center_middle">
                <div class="login_user">用 户
                    <input type="text" name="username"/>
                </div>
                <div class="login_password">密 码
                    <input type="password" name="password"/>
                </div>
                <div class="clear"></div>
                <div class="login_message"><input style="width: 20px; border: 0px" type="checkbox"
                                                  name="_spring_security_remember_me"/><span>&nbsp;记住密码</span></div>
                <div class="login_btn"><a id="loginBtn" href="#">登录</a><a id="cleanBtn" href="#">清空</a></div>
                <div class="clear"></div>
                <div class="login_message1"><span><a href="#">忘记密码？<a/></span></div>
            </div>
            <div class="login_center_right"></div>
        </div>
        <div class="login_down">
            <div class="login_down_left">
                <div class="login_inf">
                    <span class="inf_text">版本信息</span>
                    <span class="copyright">v1.0</span>
                </div>
            </div>
            <div class="login_down_center"></div>
        </div>
    </div>
</form>
</body>
</html>
<script>
    $(function () {

        //判断是否未登陆页
        if(top.location != this.location){
            top.location.replace('<c:url value="/login/loginPage"/>');
        }

        //绑定回车事件
        $(document).keydown(function (event) {
            var e = event || window.event || arguments.callee.caller.arguments[0];
            if (e && e.keyCode == 13) { // enter 键
                $("#loginBtn").click();
            }
        });

        //登陆框提交后，绑定成功事件
        $('#loginForm').form({
            success: function (data) {
                data = $.parseJSON(data);
                if (data.code == "-1") {
                    $.messager.alert("异常", data.message, "error");
                }
                if (data.code == "0") {
                    window.location.href = _ContextPath + data.targetUrl;
                }
            }
        });

        //绑定登陆按钮的点击事件
        $('#loginBtn').click(function () {
            $('#loginForm').form('submit');
            return false;
        });

        //绑定清除按钮的点击事件
        $('#cleanBtn').click(function () {
            $('#loginForm').form('clear');
            return false;
        });
    });
</script>
