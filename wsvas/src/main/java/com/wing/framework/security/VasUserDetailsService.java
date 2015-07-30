package com.wing.framework.security;

import com.wing.system.userroles.dao.SUserRoleDao;
import com.wing.system.users.dao.SUserDao;
import com.wing.system.users.model.SUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * 获取用户的权限
 *
 * @author: panwb
 *
 * Date: 14-3-6
 * Time: 下午4:09
 */
public class VasUserDetailsService implements UserDetailsService {

    //用户角色
    @Resource(name = "SUserRoleDao")
    private SUserRoleDao sUserRoleDao;

    //角色权限
    @Resource(name = "SUserDao")
    private SUserDao sUserDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Set<GrantedAuthority> authorities = loadUserAuthoritiesByName(username);
        SUser user = sUserDao.getSUserByLoginName(username);
        return new User(username, user.getPassword(), authorities);
    }

    /**
     *
     * 通过用户登录名加载用户权限
     *
     * @param username
     * @return
     */
    public Set<GrantedAuthority> loadUserAuthoritiesByName(String username) {

        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        List<String> authList = sUserRoleDao.queryAuthByLoginName(username);
        for(String auth : authList) {
            authorities.add(new SimpleGrantedAuthority(auth));
        }
        return authorities;
    }
}

