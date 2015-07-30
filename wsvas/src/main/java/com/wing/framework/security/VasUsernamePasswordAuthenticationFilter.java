package com.wing.framework.security;

import com.wing.framework.common.constant.SysContant;
import com.wing.system.users.dao.SUserDao;
import com.wing.system.users.model.SUser;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * 用户验证
 *
 * @author: panwb
 *
 * Date: 14-3-7
 * Time: 下午2:10
 */
public class VasUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    @Resource(name = "SUserDao")
    private SUserDao userDao;

    private RememberMeServices rememberMeServices;

    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        String username = obtainUsername(request);
        String password = obtainPassword(request);

        String encodePassword = DigestUtils.md5Hex(password);

        if (username == null) {
            username = "";
        }

        if (password == null) {
            password = "";
        }

        username = username.trim();

        //用户信息
        SUser user = userDao.getSUserByLoginName(username);

        if(user == null) {
            throw new AuthenticationServiceException(SysContant.LOGIN_ERROR_USERNAME);
        }
        if(!user.getPassword().equals(encodePassword)) {
            throw new AuthenticationServiceException(SysContant.LOGIN_ERROR_PASSWORD);
        }

        HttpSession session = request.getSession();
        session.setAttribute("loginUser", user);

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);

    }

    /**
     *
     * 获取密码
     *
     * @param request
     * @return
     */
    protected String obtainPassword(HttpServletRequest request) {
        return request.getParameter(PASSWORD);
    }

    /**
     *
     * 获取用户名
     *
     * @param request
     * @return
     */
    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter(USERNAME);
    }
}
