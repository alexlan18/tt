package com.wing.system.authdata.service;

import com.wing.framework.base.BaseDao;
import com.wing.framework.common.Page;
import com.wing.framework.security.VasInvocationSecurityMetadataSourceService;
import com.wing.system.authdata.dao.SAuthDataDao;
import com.wing.system.authdata.model.SAuthData;
import com.wing.system.authdata.model.SAuthDataParams;
import com.wing.system.roles.dao.SRoleDao;
import com.wing.system.roles.model.SRole;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;



/**
 *
 * @author wing
 * @version 1.0
 *
 * the JAVA code is generate by middlegen
 *
 */
@Service("SAuthDataService")
public class SAuthDataService {

    public static final String BEAN_ID = "SAuthDataService";

    //注入Dao层
    @Resource(name = "SAuthDataDao")
    private SAuthDataDao sAuthDataDao;

    @Resource(name = "SRoleDao")
    private SRoleDao sRoleDao;

    //注入BaseDao
    @Resource(name = "BaseDao")
    private BaseDao baseDao;

    //授权资源加载服务
    @Resource(name = "vasSecurityMetadataSource")
    private VasInvocationSecurityMetadataSourceService vasSecurityMetadataSource;

    /**
     *
     * 查询数据
     *
     * @param sAuthDataParams
     * @param page
     *
     */
    @Transactional
    public List querySAuthDataList(SAuthDataParams sAuthDataParams, Page page){

        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put("params", sAuthDataParams);
        if(page.getSortname() != null && !page.getSortname().equals("")) {
            params.put("sortname", page.getSortname());
        }
        if(page.getSortorder() != null && !page.getSortorder().equals("")) {
            params.put("sortorder", page.getSortorder());
        }

        return baseDao.queryListByPage("querySAuthData", params, page);
    }

    /**
     *
     * 已分配权限
     *
     * @param id
     * @param page
     * @return
     */
    @Transactional
    public List querySAuthDataListByRole(Integer id, Page page){

        SRole role = sRoleDao.getSRoleById(id);

        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put("roleId", role.getRoleId());
        if(page.getSortname() != null && !page.getSortname().equals("")) {
            params.put("sortname", page.getSortname());
        }
        if(page.getSortorder() != null && !page.getSortorder().equals("")) {
            params.put("sortorder", page.getSortorder());
        }

        return baseDao.queryListByPage("queryAuthByRole", params, page);
    }

    /**
     *
     * 未分配权限
     *
     * @param id
     * @param page
     * @return
     */
    @Transactional
    public List queryNotSAuthDataListByRole(Integer id, Page page){

        SRole role = sRoleDao.getSRoleById(id);

        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put("roleId", role.getRoleId());
        if(page.getSortname() != null && !page.getSortname().equals("")) {
            params.put("sortname", page.getSortname());
        }
        if(page.getSortorder() != null && !page.getSortorder().equals("")) {
            params.put("sortorder", page.getSortorder());
        }

        return baseDao.queryListByPage("queryNotAuthByRole", params, page);
    }


    /**
     *
     * 插入数据
     * @param sAuthData
     *
     */
    @Transactional
    public void saveSAuthData(SAuthData sAuthData) {
        if(sAuthData.getId() != null) {
            SAuthData result = getSAuthData(sAuthData.getId());
            if(result != null) {
                sAuthDataDao.update(sAuthData);
            } else {
                sAuthDataDao.insert(sAuthData);
            }
        } else {
            sAuthDataDao.insert(sAuthData);
        }

        //加载资源授权信息
        vasSecurityMetadataSource.loadResourceDefine();
    }

    /**
     *
     * 删除数据
     * @param id
     *
     */
    @Transactional
    public void deleteSAuthData(Integer id) {

        sAuthDataDao.delete(id);
    }

    /**
     *
     * 删除数据
     * @param sAuthDataList
     *
     */
    @Transactional
    public void deleteSAuthDataBatch(List<String> sAuthDataList) {
        for(String id : sAuthDataList) {
            deleteSAuthData(Integer.valueOf(id));
        }
        //加载资源授权信息
        vasSecurityMetadataSource.loadResourceDefine();
    }

    /**
     *
     * 根据ID获取数据
     * @param id
     *
     */
    @Transactional
    public SAuthData getSAuthData(Integer id) {

        return sAuthDataDao.getSAuthDataById(id);
    }
}