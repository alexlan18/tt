package com.wing.system.login.action;

import com.alibaba.fastjson.JSON;
import com.wing.framework.common.Message;
import com.wing.framework.common.constant.SysContant;
import com.wing.system.login.model.LoginMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 *
 * 登录
 *
 * @author: panwb
 *
 * Date: 14-3-7
 * Time: 下午2:37
 */
@Controller
@RequestMapping("/login")
public class LoginAction {


    /**
     *
     * 跳转到登陆页面
     *
     * @param request
     * @param response
     */
    @RequestMapping("/loginPage")
    public String loginPage(HttpServletRequest request, HttpServletResponse response) {

        //判断是否为ajax请求
        if (request.getHeader("x-requested-with") != null && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
            Message message = new Message("sessionTimeout", "登陆超时请重新登陆！");
            try {
                response.setCharacterEncoding("UTF8");
                response.setContentType("text/plain");
                PrintWriter printWriter = response.getWriter();
                printWriter.print(JSON.toJSONString(message));
                printWriter.flush();
                printWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "/login";
    }

    /**
     *
     * 返回检验信息
     *
     * @param errorCode
     * @return
     */
    @RequestMapping("/error")
    @ResponseBody
    public LoginMessage error(String errorCode) {

        if(errorCode.equals(SysContant.LOGIN_ERROR_USERNAME)) {
            return new LoginMessage(SysContant.ERROR, SysContant.LOGIN_ERROR_USERNAME_MESSAGE, "");
        }
        if(errorCode.equals(SysContant.LOGIN_ERROR_PASSWORD)) {
            return new LoginMessage(SysContant.ERROR, SysContant.LOGIN_ERROR_PASSWORD_MESSAGE, "");
        }
        if(errorCode.equals(SysContant.LOGIN_ERROR_NO_AUTHENTICATION)) {
            return new LoginMessage(SysContant.ERROR, SysContant.LOGIN_ERROR_NO_AUTHENTICATION_MESSAGE, "");
        }
        return new LoginMessage(SysContant.ERROR, "登录异常，请联系管理员！", "");
    }

    /**
     *
     * 登录成功，返回目标URL
     *
     * @param targetUrl
     * @return
     */
    @RequestMapping(value = "/success")
    @ResponseBody
    public LoginMessage success(String targetUrl) {
        try {
            return new LoginMessage(SysContant.SUCCESS, "", URLDecoder.decode(targetUrl, "UTF8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new LoginMessage(SysContant.ERROR, "登录异常，请联系管理员！", "");
    }
}
