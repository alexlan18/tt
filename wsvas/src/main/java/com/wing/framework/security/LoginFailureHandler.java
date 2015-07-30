package com.wing.framework.security;

import com.wing.framework.common.constant.SysContant;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * 登录异常处理
 *
 * @author: panwb
 *
 * Date: 14-3-7
 * Time: 下午2:24
 */
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private String defaultFailureUrl;
    private boolean forwardToDestination = false;
    private boolean allowSessionCreation = true;
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    //登录异常处理类
    public LoginFailureHandler() {
    }

    /**
     *
     * 构造函数（异常处理URL）
     *
     * @param defaultFailureUrl
     */
    public LoginFailureHandler(String defaultFailureUrl) {
        setDefaultFailureUrl(defaultFailureUrl);
    }

    /**
     *
     * 异常处理
     *
     * @param request
     * @param response
     * @param exception
     * @throws IOException
     * @throws ServletException
     */
    @SuppressWarnings("unchecked")
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        if (defaultFailureUrl == null) {
            logger.debug("No failure URL set, sending 401 Unauthorized error");

            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication Failed: " + exception.getMessage());
        } else {
            String errorCode = exception.getMessage();
            String userId = obtainUsername(request);

            if(!errorCode.equals(SysContant.LOGIN_ERROR_USERNAME) && !errorCode.equals(SysContant.LOGIN_ERROR_PASSWORD)) {
                if(exception instanceof BadCredentialsException) {
                    errorCode = SysContant.LOGIN_ERROR_NO_AUTHENTICATION;
                }
            }

            if (forwardToDestination) {
                logger.debug("Forwarding to " + defaultFailureUrl);
                request.getRequestDispatcher(defaultFailureUrl + "?errorCode=" + errorCode).forward(request, response);
            } else {
                logger.debug("Redirecting to " + defaultFailureUrl);
                redirectStrategy.sendRedirect(request, response, defaultFailureUrl);
            }
        }
    }

    /**
     * The URL which will be used as the failure destination.
     *
     * @param defaultFailureUrl the failure URL, for example "/loginFailed.jsp".
     */
    public void setDefaultFailureUrl(String defaultFailureUrl) {

        Assert.isTrue(UrlUtils.isValidRedirectUrl(defaultFailureUrl),
                "'" + defaultFailureUrl + "' is not a valid redirect URL");
        this.defaultFailureUrl = defaultFailureUrl;
    }

    /**
     * If set to <tt>true</tt>, performs a forward to the failure destination URL instead of a redirect. Defaults to
     * <tt>false</tt>.
     */
    public void setUseForward(boolean forwardToDestination) {
        this.forwardToDestination = forwardToDestination;
    }

    /**
     * Allows overriding of the behaviour when redirecting to a target URL.
     */
    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }

    protected RedirectStrategy getRedirectStrategy() {
        return redirectStrategy;
    }

    protected boolean isAllowSessionCreation() {
        return allowSessionCreation;
    }

    public void setAllowSessionCreation(boolean allowSessionCreation) {
        this.allowSessionCreation = allowSessionCreation;
    }

    private String obtainUsername(HttpServletRequest request) {
        Object obj = request.getParameter("userId");
        return null == obj ? "" : obj.toString();
    }

}
