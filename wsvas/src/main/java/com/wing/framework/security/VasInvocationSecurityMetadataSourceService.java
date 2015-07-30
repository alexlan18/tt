package com.wing.framework.security;

import com.wing.system.authdata.dao.SAuthDataDao;
import com.wing.system.authresources.dao.SAuthResourceDao;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import java.util.*;

/**
 *
 * 提取权限和资源的关系，并保存在关系映射中
 *
 * @author: panwb
 *
 * Date: 14-3-6
 * Time: 下午2:44
 */
@Service("vasSecurityMetadataSource")
public class VasInvocationSecurityMetadataSourceService implements FilterInvocationSecurityMetadataSource {

    //资源权限映射
    private static Map<String, Collection<ConfigAttribute>> resourceMap = null;

    //url匹配
    private AntPathMatcher urlMatcher = new AntPathMatcher();

    //注入权限表信息
    private SAuthDataDao sAuthDataDao;

    //注入权限表信息
    private SAuthResourceDao sAuthResourceDao;

    public VasInvocationSecurityMetadataSourceService(SAuthDataDao sAuthDataDao, SAuthResourceDao sAuthResourceDao) {
        this.sAuthDataDao = sAuthDataDao;
        this.sAuthResourceDao = sAuthResourceDao;
        //加载权限、资源信息
        loadResourceDefine();
    }

    public void loadResourceDefine() {

        //权限信息
        List<String> authDatas = sAuthDataDao.queryAuthDataList();

        /*
        * 应当是资源为key， 权限为value。 资源通常为url， 权限就是那些以ROLE_为前缀的角色。 一个资源可以由多个权限来访问。
        * sparta
        */
        resourceMap = new HashMap<String, Collection<ConfigAttribute>>();

        for (String authId : authDatas) {

            //权限
            ConfigAttribute ca = new SecurityConfig(authId);

            //查询对应的资源
            List<String> resources = sAuthResourceDao.queryResourcesByAuth(authId);

            //遍历对应的所有的资源
            for (String url : resources) {

                //判断资源文件和权限的对应关系，如果已经存在相关的资源url，则要通过该url为key提取出权限集合，将权限增加到权限集合中。
                if (resourceMap.containsKey(url)) {
                    Collection<ConfigAttribute> value = resourceMap.get(url);
                    value.add(ca);
                    resourceMap.put(url, value);
                } else {
                    Collection<ConfigAttribute> auths = new ArrayList<ConfigAttribute>();
                    auths.add(ca);
                    resourceMap.put(url, auths);
                }
            }
        }
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        Set<ConfigAttribute> allAttributes = new HashSet<ConfigAttribute>();
        for (Map.Entry<String, Collection<ConfigAttribute>> entry : resourceMap.entrySet()) {
            allAttributes.addAll(entry.getValue());
        }
        return allAttributes;
    }

    // 根据URL，找到相关的权限配置。
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object)
            throws IllegalArgumentException {

        // object 是一个URL，被用户请求的url。
        String url = ((FilterInvocation) object).getRequestUrl();

        int firstQuestionMarkIndex = url.indexOf("?");

        if (firstQuestionMarkIndex != -1) {
            url = url.substring(0, firstQuestionMarkIndex);
        }

        Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();

        for (String resURL : resourceMap.keySet()) {
            if (urlMatcher.match(resURL, url)) {
                configAttributes.addAll(resourceMap.get(resURL));
            }
        }
        if(configAttributes.size()==0){
            configAttributes.add(new SecurityConfig("ROLE_NO_USER"));
        }
        return configAttributes;
    }

    @Override
    public boolean supports(Class<?> arg0) {
        return true;
    }

    public void setsAuthResourceDao(SAuthResourceDao sAuthResourceDao) {
        this.sAuthResourceDao = sAuthResourceDao;
    }

    public void setsAuthDataDao(SAuthDataDao sAuthDataDao) {
        this.sAuthDataDao = sAuthDataDao;
    }
}
