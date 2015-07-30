package com.wing.framework.security;

import com.wing.system.users.dao.SUserDao;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * 记住我拦截器
 *
 * @author: panwb
 *
 * Date: 2014/7/15
 * Time: 14:17
 */
public class VasRememberMeAuthenticationFilter extends GenericFilterBean implements ApplicationEventPublisherAware {

    //~ Instance fields ================================================================================================

    private ApplicationEventPublisher eventPublisher;
    private AuthenticationSuccessHandler successHandler;
    private AuthenticationManager authenticationManager;
    private RememberMeServices rememberMeServices;

    private SUserDao userDao;

    /**
     * @deprecated Use constructor injection
     */
    @Deprecated
    public VasRememberMeAuthenticationFilter() {
    }

    public VasRememberMeAuthenticationFilter(AuthenticationManager authenticationManager,
                                          RememberMeServices rememberMeServices) {
        this.authenticationManager = authenticationManager;
        this.rememberMeServices = rememberMeServices;
    }

    //~ Methods ========================================================================================================

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(authenticationManager, "authenticationManager must be specified");
        Assert.notNull(rememberMeServices, "rememberMeServices must be specified");
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            Authentication rememberMeAuth = rememberMeServices.autoLogin(request, response);

            if (rememberMeAuth != null) {
                // Attempt authenticaton via AuthenticationManager
                try {
                    rememberMeAuth = authenticationManager.authenticate(rememberMeAuth);

                    // Store to SecurityContextHolder
                    SecurityContextHolder.getContext().setAuthentication(rememberMeAuth);

                    onSuccessfulAuthentication(request, response, rememberMeAuth);

                    if (logger.isDebugEnabled()) {
                        logger.debug("SecurityContextHolder populated with remember-me token: '"
                                + SecurityContextHolder.getContext().getAuthentication() + "'");
                    }

                    // Fire event
                    if (this.eventPublisher != null) {
                        eventPublisher.publishEvent(new InteractiveAuthenticationSuccessEvent(
                                SecurityContextHolder.getContext().getAuthentication(), this.getClass()));
                    }

                    if (successHandler != null) {
                        successHandler.onAuthenticationSuccess(request, response, rememberMeAuth);

                        return;
                    }

                } catch (AuthenticationException authenticationException) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("SecurityContextHolder not populated with remember-me token, as "
                                + "AuthenticationManager rejected Authentication returned by RememberMeServices: '"
                                + rememberMeAuth + "'; invalidating remember-me token", authenticationException);
                    }

                    rememberMeServices.loginFail(request, response);

                    onUnsuccessfulAuthentication(request, response, authenticationException);
                }

                //添加用户信息到Session（用户登陆成功）
                User user = (User)rememberMeAuth.getPrincipal();
                String loginName = user.getUsername();
                request.getSession().setAttribute("loginUser", userDao.getSUserByLoginName(loginName));
            }

            chain.doFilter(request, response);
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("SecurityContextHolder not populated with remember-me token, as it already contained: '"
                        + SecurityContextHolder.getContext().getAuthentication() + "'");
            }

            chain.doFilter(request, response);
        }
    }

    /**
     * Called if a remember-me token is presented and successfully authenticated by the {@code RememberMeServices}
     * {@code autoLogin} method and the {@code AuthenticationManager}.
     */
    protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              Authentication authResult) {
    }

    /**
     * Called if the {@code AuthenticationManager} rejects the authentication object returned from the
     * {@code RememberMeServices} {@code autoLogin} method. This method will not be called when no remember-me
     * token is present in the request and {@code autoLogin} reurns null.
     */
    protected void onUnsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                                AuthenticationException failed) {
    }

    public RememberMeServices getRememberMeServices() {
        return rememberMeServices;
    }

    public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    /**
     * @deprecated Use constructor injection
     */
    @Deprecated
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * @deprecated Use constructor injection
     */
    @Deprecated
    public void setRememberMeServices(RememberMeServices rememberMeServices) {
        this.rememberMeServices = rememberMeServices;
    }

    /**
     * Allows control over the destination a remembered user is sent to when they are successfully authenticated.
     * By default, the filter will just allow the current request to proceed, but if an
     * {@code AuthenticationSuccessHandler} is set, it will be invoked and the {@code doFilter()} method will return
     * immediately, thus allowing the application to redirect the user to a specific URL, regardless of whatthe original
     * request was for.
     *
     * @param successHandler the strategy to invoke immediately before returning from {@code doFilter()}.
     */
    public void setAuthenticationSuccessHandler(AuthenticationSuccessHandler successHandler) {
        Assert.notNull(successHandler, "successHandler cannot be null");
        this.successHandler = successHandler;
    }

    public void setUserDao(SUserDao userDao) {
        this.userDao = userDao;
    }
}
