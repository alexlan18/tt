package com.wing.framework.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * 获取web上下文信息（request、response）
 *
 * @author: panwb
 *
 * Date: 14-2-28
 * Time: 下午2:00
 */
public class WebContext {

    private static ThreadLocal<HttpServletRequest> requestThreadLocal = new ThreadLocal<HttpServletRequest>();
    private static ThreadLocal<HttpServletResponse> responseThreadLocal = new ThreadLocal<HttpServletResponse>();

    static void unRegister() {
        requestThreadLocal.remove();
        responseThreadLocal.remove();
    }

    static void register(HttpServletRequest request, HttpServletResponse response) {
        requestThreadLocal.set(request);
        responseThreadLocal.set(response);
    }

    public static HttpServletRequest getRequest() {
        HttpServletRequest request = requestThreadLocal.get();
        if (request == null) {
            throw new RuntimeException("the error seems will not occur . HttpServletRequest null. ");
        }
        return requestThreadLocal.get();
    }

    public static HttpServletResponse getResponse() {
        HttpServletResponse response = responseThreadLocal.get();
        if (response == null) {
            throw new RuntimeException("the error seems will not occur . HttpServletResponse null. ");
        }
        return response;
    }
}
