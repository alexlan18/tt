package com.wing.system.roles.service;

import com.wing.framework.base.BaseDao;
import com.wing.framework.common.Page;
import com.wing.system.rolemenus.dao.SRoleMenusDao;
import com.wing.system.rolemenus.model.SRoleMenus;
import com.wing.system.roles.dao.SRoleDao;
import com.wing.system.roles.model.SRole;
import com.wing.system.roles.model.SRoleParams;
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
@Service("SRoleService")
public class SRoleService {

    public static final String BEAN_ID = "SRoleService";

    //注入Dao层
    @Resource(name = "SRoleDao")
    private SRoleDao sRoleDao;

    //注入BaseDao
    @Resource(name = "BaseDao")
    private BaseDao baseDao;

    @Resource(name = "SRoleMenusDao")
    private SRoleMenusDao sRoleMenusDao;

    /**
     *
     * 查询数据
     *
     * @param sRoleParams
     * @param page
     *
     */
    @Transactional
    public List querySRoleList(SRoleParams sRoleParams, Page page){

        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put("params", sRoleParams);
        if(page.getSortname() != null && !page.getSortname().equals("")) {
            params.put("sortname", page.getSortname());
        }
        if(page.getSortorder() != null && !page.getSortorder().equals("")) {
            params.put("sortorder", page.getSortorder());
        }

        return baseDao.queryListByPage("querySRole", params, page);
    }

    /**
     *
     * 插入数据
     * @param sRole
     *
     */
    @Transactional
    public void saveSRole(SRole sRole) {
        if(sRole.getId() != null) {
            SRole result = getSRole(sRole.getId());
            if(result != null) {
                sRoleDao.update(sRole);
            } else {
                sRoleDao.insert(sRole);
            }
        } else {
            sRoleDao.insert(sRole);
        }
    }

    /**
     *
     * 删除数据
     * @param id
     *
     */
    @Transactional
    public void deleteSRole(Integer id) {

        sRoleDao.delete(id);
    }

    /**
     *
     * 删除数据
     * @param sRoleList
     *
     */
    @Transactional
    public void deleteSRoleBatch(List<String> sRoleList) {
        for(String id : sRoleList) {
            deleteSRole(Integer.valueOf(id));
        }
    }

    /**
     *
     * 根据ID获取数据
     * @param id
     *
     */
    @Transactional
    public SRole getSRole(Integer id) {

        return sRoleDao.getSRoleById(id);
    }

    /**
     *
     * 保存角色菜单权限
     *
     * @param role
     * @param menuIdList
     */
    public void saveRoleMenus(SRole role, String[] menuIdList) {

        sRoleMenusDao.deleteByRole(role.getRoleId());

        for(String menuId : menuIdList) {

            SRoleMenus roleMenus = new SRoleMenus();
            roleMenus.setMenuId(menuId);
            roleMenus.setRoleId(role.getRoleId());
            sRoleMenusDao.insert(roleMenus);
        }
    }
}