package com.wing.framework.base;

import com.wing.framework.web.WebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

/**
 *
 * 封装异常统一处理
 *
 * @author: panwb
 *
 * Date: 14-2-27
 * Time: 下午5:00
 */
public abstract class BaseAction {

    //日志对象
    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     *
     * 处理404异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = {NoSuchRequestHandlingMethodException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    protected String handler404(Exception ex) {
        logger.error("{},404找不到请求", WebContext.getRequest().getRequestURL().toString(), ex);
        WebContext.getRequest().setAttribute("message",
                WebContext.getRequest().getRequestURL().toString() + "request not found");
        return "exception/errorCode";
    }

    /**
     *
     * 处理500异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = { ConversionNotSupportedException.class, HttpMessageNotWritableException.class })
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    protected String handler500(Exception ex) {
        logger.error("{} : 500出错", WebContext.getRequest().getRequestURL(), ex);
        WebContext.getRequest().setAttribute("message", "sorry, something mistake");
        return "exception/errorCode";
    }

    /**
     *
     * 处理400异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = { HttpMessageNotReadableException.class, MissingServletRequestParameterException.class,
            TypeMismatchException.class })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected String handle400(Exception ex) {
        logger.error("{} : 400出错", WebContext.getRequest().getRequestURL(), ex);
        WebContext.getRequest().setAttribute("message", "sorry, something mistake");
        return "exception/errorCode";
    }
}
