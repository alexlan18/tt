package com.wing.framework.common;

import com.wing.framework.common.constant.SysContant;

/**
 *
 * 返回信息
 *
 * @author: panwb
 *
 * Date: 14-3-3
 * Time: 下午1:57
 */
public class Message {

    private String code;

    private String message;

    public Message() {
        this.code = SysContant.SUCCESS;
        this.message = SysContant.SUCCESS_MESSAGE;
    }

    public Message(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
