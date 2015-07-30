package com.wing.system.login.model;

import com.wing.framework.common.Message;

/**
 *
 * 登录状态信息
 *
 * @author: panwb
 *
 * Date: 14-3-7
 * Time: 下午3:17
 */
public class LoginMessage extends Message {

    private String targetUrl;

    public LoginMessage(String code, String message, String targetUrl) {
        super(code, message);
        this.targetUrl = targetUrl;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }
}
