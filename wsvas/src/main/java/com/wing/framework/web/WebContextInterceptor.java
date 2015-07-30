package com.wing.framework.web;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * 拦截器，设置上下文信息
 *
 * @author: panwb
 *
 * Date: 14-2-28
 * Time: 下午2:01
 */
public class WebContextInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        WebContext.register(request, response);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        WebContext.unRegister();
        super.afterCompletion(request, response, handler, ex);
    }

}
