package com.wing.system.roleauths.service;

import com.wing.framework.base.BaseDao;
import com.wing.framework.common.Page;
import com.wing.system.authdata.dao.SAuthDataDao;
import com.wing.system.authdata.model.SAuthData;
import com.wing.system.roleauths.dao.SRoleAuthDao;
import com.wing.system.roleauths.model.SRoleAuth;
import com.wing.system.roleauths.model.SRoleAuthParams;
import com.wing.system.roles.dao.SRoleDao;
import com.wing.system.roles.model.SRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Service("SRoleAuthService")
public class SRoleAuthService {

    public static final String BEAN_ID = "SRoleAuthService";

    //日志对象
    private Logger logger = LoggerFactory.getLogger(SRoleAuthService.class);

    //注入Dao层
    @Resource(name = "SRoleAuthDao")
    private SRoleAuthDao sRoleAuthDao;

    @Resource(name = "SAuthDataDao")
    private SAuthDataDao sAuthDataDao;

    @Resource(name = "SRoleDao")
    private SRoleDao sRoleDao;

    //注入BaseDao
    @Resource(name = "BaseDao")
    private BaseDao baseDao;

    /**
     *
     * 查询数据
     *
     * @param sRoleAuthParams
     * @param page
     *
     */
    @Transactional
    public List querySRoleAuthList(SRoleAuthParams sRoleAuthParams, Page page){

        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put("params", sRoleAuthParams);
        if(page.getSortname() != null && !page.getSortname().equals("")) {
            params.put("sortname", page.getSortname());
        }
        if(page.getSortorder() != null && !page.getSortorder().equals("")) {
            params.put("sortorder", page.getSortorder());
        }

        return baseDao.queryListByPage("querySRoleAuth", params, page);
    }

    /**
     *
     * 插入数据
     * @param sRoleAuth
     *
     */
    @Transactional
    public void saveSRoleAuth(SRoleAuth sRoleAuth) {
        if(sRoleAuth.getId() != null) {
            SRoleAuth result = getSRoleAuth(sRoleAuth.getId());
            if(result != null) {
                sRoleAuthDao.update(sRoleAuth);
            } else {
                sRoleAuthDao.insert(sRoleAuth);
            }
        } else {
            sRoleAuthDao.insert(sRoleAuth);
        }
    }

    /**
     *
     * 授权
     *
     * @param id
     * @param ids
     */
    public void auth(Integer id, String ids) {

        String[] authIds = ids.split(",");
        SRole role = sRoleDao.getSRoleById(id);

        for(String authId : authIds) {

            SAuthData authData = sAuthDataDao.getSAuthDataById(Integer.valueOf(authId));
            SRoleAuth roleAuth = new SRoleAuth();
            roleAuth.setRoleId(role.getRoleId());
            roleAuth.setAuthId(authData.getAuthId());

            sRoleAuthDao.insert(roleAuth);
        }
    }

    /**
     *
     * 删除数据
     * @param id
     *
     */
    @Transactional
    public void deleteSRoleAuth(Integer id) {

        sRoleAuthDao.delete(id);
    }

    /**
     *
     * 删除数据
     * @param sRoleAuthList
     *
     */
    @Transactional
    public void deleteSRoleAuthBatch(List<String> sRoleAuthList) {
        for(String id : sRoleAuthList) {
            deleteSRoleAuth(Integer.valueOf(id));
        }
    }

    /**
     *
     * 删除授权信息
     *
     * @param authIdList
     */
    @Transactional
    public void deleteSRoleAuthBatchByAuthId(Integer roleId, List<String> authIdList) throws Exception {
        SRole role = sRoleDao.getSRoleById(roleId);

        if(role == null) {
            logger.info("删除授权信息时，查询不到角色信息，角色ID=" + roleId);
            throw new Exception("删除授权信息时，查询不到角色信息！");
        }

        for(String id : authIdList) {
            SAuthData authData = sAuthDataDao.getSAuthDataById(Integer.valueOf(id));
            sRoleAuthDao.deleteByRoleAuth(role.getRoleId(), authData.getAuthId());
        }
    }

    /**
     *
     * 根据ID获取数据
     * @param id
     *
     */
    @Transactional
    public SRoleAuth getSRoleAuth(Integer id) {

        return sRoleAuthDao.getSRoleAuthById(id);
    }
}