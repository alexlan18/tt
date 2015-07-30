package com.wing.system.authresources.service;

import com.wing.framework.base.BaseDao;
import com.wing.framework.common.Page;
import com.wing.framework.security.VasInvocationSecurityMetadataSourceService;
import com.wing.system.authdata.dao.SAuthDataDao;
import com.wing.system.authdata.model.SAuthData;
import com.wing.system.authresources.dao.SAuthResourceDao;
import com.wing.system.authresources.model.SAuthResource;
import com.wing.system.authresources.model.SAuthResourceParams;
import com.wing.system.resources.dao.SResourceDao;
import com.wing.system.resources.model.SResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;


/**
 * @author wing
 * @version 1.0
 *          <p/>
 *          the JAVA code is generate by middlegen
 */
@Service("SAuthResourceService")
public class SAuthResourceService {

    public static final String BEAN_ID = "SAuthResourceService";

    //注入Dao层
    @Resource(name = "SAuthResourceDao")
    private SAuthResourceDao sAuthResourceDao;

    @Resource(name = "SAuthDataDao")
    private SAuthDataDao sAuthDataDao;

    @Resource(name = "SResourceDao")
    private SResourceDao sResourceDao;

    //授权资源加载服务
    @Resource(name = "vasSecurityMetadataSource")
    private VasInvocationSecurityMetadataSourceService vasSecurityMetadataSource;

    //注入BaseDao
    @Resource(name = "BaseDao")
    private BaseDao baseDao;

    /**
     * 查询数据
     *
     * @param sAuthResourceParams
     * @param page
     */
    @Transactional
    public List querySAuthResourceList(SAuthResourceParams sAuthResourceParams, Page page) {

        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put("params", sAuthResourceParams);
        if (page.getSortname() != null && !page.getSortname().equals("")) {
            params.put("sortname", page.getSortname());
        }
        if (page.getSortorder() != null && !page.getSortorder().equals("")) {
            params.put("sortorder", page.getSortorder());
        }

        return baseDao.queryListByPage("querySAuthResource", params, page);
    }

    /**
     * 查询所有的资源信息
     *
     * @param id
     * @param page
     * @return
     */
    public List queryResourceAuth(Integer id, Page page) {

        SAuthData sAuthData = sAuthDataDao.getSAuthDataById(id);

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("authId", sAuthData.getAuthId());
        return baseDao.queryListByPage("queryAllResourcesByAuth", params, page);

    }


    /**
     * 插入数据
     *
     * @param sAuthResource
     */
    @Transactional
    public void saveSAuthResource(SAuthResource sAuthResource) {
        if (sAuthResource.getId() != null) {
            SAuthResource result = getSAuthResource(sAuthResource.getId());
            if (result != null) {
                sAuthResourceDao.update(sAuthResource);
            } else {
                sAuthResourceDao.insert(sAuthResource);
            }
        } else {
            sAuthResourceDao.insert(sAuthResource);
        }
        vasSecurityMetadataSource.loadResourceDefine();
    }

    /**
     * 删除数据
     *
     * @param id
     */
    @Transactional
    public void deleteSAuthResource(Integer id) {

        sAuthResourceDao.delete(id);
        vasSecurityMetadataSource.loadResourceDefine();
    }

    /**
     * 删除数据
     *
     * @param sAuthResourceList
     */
    @Transactional
    public void deleteSAuthResourceBatch(List<String> sAuthResourceList) {
        for (String id : sAuthResourceList) {
            deleteSAuthResource(Integer.valueOf(id));
        }
        vasSecurityMetadataSource.loadResourceDefine();
    }

    /**
     * 保存权限
     *
     * @param id
     * @param allIds
     * @param checkeds
     */
    @Transactional
    public void saveAuth(Integer id, String allIds, String checkeds) {

        SAuthData authData = sAuthDataDao.getSAuthDataById(id);
        String authId = authData.getAuthId();
        String[] allResources = allIds.split(",");
        String[] authResources = checkeds.split(",");

        for (String resId : allResources) {
            if (null != resId && !resId.equals("")) {
                SResource resource = sResourceDao.getSResourceById(Integer.valueOf(resId));
                String resourceId = resource.getResourceId();
                sAuthResourceDao.deleteAuth(authId, resourceId);
            }
        }

        for (String checkedId : authResources) {
            if (null != checkedId && !checkedId.equals("")) {
                SResource resource = sResourceDao.getSResourceById(Integer.valueOf(checkedId));
                String resourceId = resource.getResourceId();

                SAuthResource authResource = new SAuthResource();
                authResource.setAuthId(authId);
                authResource.setResourceId(resourceId);

                sAuthResourceDao.insert(authResource);
            }
        }

        vasSecurityMetadataSource.loadResourceDefine();
    }

    /**
     * 根据ID获取数据
     *
     * @param id
     */
    @Transactional
    public SAuthResource getSAuthResource(Integer id) {

        return sAuthResourceDao.getSAuthResourceById(id);
    }
}