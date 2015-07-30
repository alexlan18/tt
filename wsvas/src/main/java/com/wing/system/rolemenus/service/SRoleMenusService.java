package com.wing.system.rolemenus.service;

import java.util.List;
import java.util.HashMap;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wing.framework.base.BaseDao;
import com.wing.framework.common.Page;
import com.wing.system.rolemenus.model.SRoleMenus;
import com.wing.system.rolemenus.dao.SRoleMenusDao;
import com.wing.system.rolemenus.model.SRoleMenusParams;

import javax.annotation.Resource;



/**
 *
 * @author wing
 * @version 1.0
 *
 * the JAVA code is generate by middlegen
 *
 */
@Service("SRoleMenusService")
public class SRoleMenusService {

    public static final String BEAN_ID = "SRoleMenusService";

    //注入Dao层
    @Resource(name = "SRoleMenusDao")
    private SRoleMenusDao sRoleMenusDao;

    //注入BaseDao
    @Resource(name = "BaseDao")
    private BaseDao baseDao;

    /**
     *
     * 查询数据
     *
     * @param sRoleMenusParams
     * @param page
     *
     */
    @Transactional
    public List querySRoleMenusList(SRoleMenusParams sRoleMenusParams, Page page){

        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put("params", sRoleMenusParams);
        if(page.getSortname() != null && !page.getSortname().equals("")) {
            params.put("sortname", page.getSortname());
        }
        if(page.getSortorder() != null && !page.getSortorder().equals("")) {
            params.put("sortorder", page.getSortorder());
        }

        return baseDao.queryListByPage("querySRoleMenus", params, page);
    }

    /**
     *
     * 插入数据
     * @param sRoleMenus
     *
     */
    @Transactional
    public void saveSRoleMenus(SRoleMenus sRoleMenus) {
        if(sRoleMenus.getId() != null) {
            SRoleMenus result = getSRoleMenus(sRoleMenus.getId());
            if(result != null) {
                sRoleMenusDao.update(sRoleMenus);
            } else {
                sRoleMenusDao.insert(sRoleMenus);
            }
        } else {
            sRoleMenusDao.insert(sRoleMenus);
        }
    }

    /**
     *
     * 删除数据
     * @param id
     *
     */
    @Transactional
    public void deleteSRoleMenus(Integer id) {

        sRoleMenusDao.delete(id);
    }

    /**
     *
     * 删除数据
     * @param sRoleMenusList
     *
     */
    @Transactional
    public void deleteSRoleMenusBatch(List<String> sRoleMenusList) {
        for(String id : sRoleMenusList) {
            deleteSRoleMenus(Integer.valueOf(id));
        }
    }

    /**
     *
     * 根据ID获取数据
     * @param id
     *
     */
    @Transactional
    public SRoleMenus getSRoleMenus(Integer id) {

        return sRoleMenusDao.getSRoleMenusById(id);
    }
}