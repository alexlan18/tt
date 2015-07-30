package com.wing.framework.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

/**
 *
 * 登录成功处理
 *
 * @author: panwb
 *
 * Date: 14-3-7
 * Time: 下午3:02
 */
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private String successUrl;

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        handle(request, response, authentication);
        clearAuthenticationAttributes(request);

    }

    /**
     *
     * 将最终结果，传递至login/success，统一处理
     *
     * @param request
     * @param response
     * @param authentication
     * @throws IOException
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                        Authentication authentication) throws IOException {

        String targetUrl = determineTargetUrl(request, response);
        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }
        String result = successUrl + "?targetUrl=" + URLEncoder.encode(targetUrl, "UTF8");
        redirectStrategy.sendRedirect(request, response, result);
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }
}
