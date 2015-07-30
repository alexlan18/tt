package com.wing.system.userroles.service;

import com.wing.framework.base.BaseDao;
import com.wing.framework.common.Page;
import com.wing.framework.security.VasUserDetailsService;
import com.wing.system.roles.dao.SRoleDao;
import com.wing.system.roles.model.SRole;
import com.wing.system.userroles.dao.SUserRoleDao;
import com.wing.system.userroles.model.SUserRole;
import com.wing.system.userroles.model.SUserRoleParams;
import com.wing.system.users.dao.SUserDao;
import com.wing.system.users.model.SUser;
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
@Service("SUserRoleService")
public class SUserRoleService {

    public static final String BEAN_ID = "SUserRoleService";

    //日志对象
    private Logger logger = LoggerFactory.getLogger(SUserRoleService.class);

    //注入Dao层
    @Resource(name = "SUserRoleDao")
    private SUserRoleDao sUserRoleDao;

    //注入BaseDao
    @Resource(name = "BaseDao")
    private BaseDao baseDao;

    @Resource(name ="SUserDao")
    private SUserDao sUserDao;

    @Resource(name ="SRoleDao")
    private SRoleDao sRoleDao;

    //用户角色分配服务
    @Resource(name = "vasUserDetailsManager")
    private VasUserDetailsService vasUserDetailsService;

    /**
     *
     * 查询数据
     *
     * @param sUserRoleParams
     * @param page
     *
     */
    @Transactional
    public List querySUserRoleList(SUserRoleParams sUserRoleParams, Page page){

        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put("params", sUserRoleParams);
        if(page.getSortname() != null && !page.getSortname().equals("")) {
            params.put("sortname", page.getSortname());
        }
        if(page.getSortorder() != null && !page.getSortorder().equals("")) {
            params.put("sortorder", page.getSortorder());
        }

        return baseDao.queryListByPage("querySUserRole", params, page);
    }

    /**
     *
     * 获取已分配的角色
     *
     * @param id
     * @param page
     * @return
     */
    public List queryRolesByUser(Integer id, Page page) {

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        return baseDao.queryListByPage("queryRolesByUser", params, page);

    }

    /**
     *
     * 获取未分配的角色
     *
     * @param id
     * @param page
     * @return
     */
    public List queryNotRolesByUser(Integer id, Page page) {

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        return baseDao.queryListByPage("queryNotRolesByUser", params, page);

    }

    /**
     *
     * 插入数据
     * @param sUserRole
     *
     */
    @Transactional
    public void saveSUserRole(SUserRole sUserRole) {
        if(sUserRole.getId() != null) {
            SUserRole result = getSUserRole(sUserRole.getId());
            if(result != null) {
                sUserRoleDao.update(sUserRole);
            } else {
                sUserRoleDao.insert(sUserRole);
            }
        } else {
            sUserRoleDao.insert(sUserRole);
        }
    }

    /**
     *
     * 删除数据
     * @param id
     *
     */
    @Transactional
    public void deleteSUserRole(Integer id) {

        sUserRoleDao.delete(id);
    }

    /**
     *
     * 保存用户角色信息
     *
     * @param id
     * @param idList
     */
    public void saveAuthBatch(String id, List<String> idList) throws Exception{

        //用户信息
        SUser user = sUserDao.getSUserById(Integer.valueOf(id));
        if(user == null) {
            logger.info("删除用户权限时，找不到该用户，用户ID=" + id);
            throw new Exception("找不到该用户！");
        }

        //保存角色关系信息
        for(String roleId : idList) {
            SRole role = sRoleDao.getSRoleById(Integer.valueOf(roleId));
            if(role == null) {
                logger.info("保存用户权限时，找不到该角色，不做处理，角色ID=" + roleId);
                continue;
            }

            SUserRole userRole = new SUserRole();

            userRole.setRoleId(role.getRoleId());
            userRole.setUserId(user.getLoginName());

            sUserRoleDao.insert(userRole);
        }

        //根据用户登陆名，加载用户权限
        vasUserDetailsService.loadUserByUsername(user.getLoginName());
    }

    /**
     *
     * 删除用户的角色
     *
     * @param id
     * @param idList
     * @throws Exception
     */
    @Transactional
    public void deleteAuthBatch(String id, List<String> idList) throws Exception {

        //用户信息
        SUser user = sUserDao.getSUserById(Integer.valueOf(id));
        if(user == null) {
            logger.info("删除用户权限时，找不到该用户，用户ID=" + id);
            throw new Exception("找不到该用户！");
        }

        //删除角色关系信息
        for(String roleId : idList) {
            SRole role = sRoleDao.getSRoleById(Integer.valueOf(roleId));
            if(role == null) {
                logger.info("删除用户权限时，找不到该角色，不做处理，角色ID=" + roleId);
            }
            sUserRoleDao.deleteByUserRole(user.getLoginName(), role.getRoleId());
        }
        //根据用户登陆名，加载用户权限
        vasUserDetailsService.loadUserByUsername(user.getLoginName());
    }

    /**
     *
     * 删除数据
     * @param sUserRoleList
     *
     */
    @Transactional
    public void deleteSUserRoleBatch(List<String> sUserRoleList) {
        for(String id : sUserRoleList) {
            deleteSUserRole(Integer.valueOf(id));
        }
    }

    /**
     *
     * 根据ID获取数据
     * @param id
     *
     */
    @Transactional
    public SUserRole getSUserRole(Integer id) {

        return sUserRoleDao.getSUserRoleById(id);
    }
}