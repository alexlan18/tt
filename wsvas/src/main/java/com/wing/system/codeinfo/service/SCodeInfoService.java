package com.wing.system.codeinfo.service;

import com.wing.framework.base.BaseDao;
import com.wing.framework.cache.CodeCache;
import com.wing.framework.common.Page;
import com.wing.system.codeinfo.dao.SCodeInfoDao;
import com.wing.system.codeinfo.model.SCodeInfo;
import com.wing.system.codeinfo.model.SCodeInfoParams;
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
@Service("SCodeInfoService")
public class SCodeInfoService {

    public static final String BEAN_ID = "SCodeInfoService";

    //注入Dao层
    @Resource(name = "SCodeInfoDao")
    private SCodeInfoDao sCodeInfoDao;

    //注入BaseDao
    @Resource(name = "BaseDao")
    private BaseDao baseDao;

    /**
     *
     * 查询数据
     *
     * @param sCodeInfoParams
     * @param page
     *
     */
    @Transactional
    public List querySCodeInfoList(SCodeInfoParams sCodeInfoParams, Page page){

        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put("params", sCodeInfoParams);
        if(page.getSortname() != null && !page.getSortname().equals("")) {
            params.put("sortname", page.getSortname());
        }
        if(page.getSortorder() != null && !page.getSortorder().equals("")) {
            params.put("sortorder", page.getSortorder());
        }

        return baseDao.queryListByPage("querySCodeInfo", params, page);
    }

    /**
     *
     * 插入数据
     * @param sCodeInfo
     *
     */
    @Transactional
    public void saveSCodeInfo(SCodeInfo sCodeInfo) {
        if(sCodeInfo.getId() != null) {
            SCodeInfo result = getSCodeInfo(sCodeInfo.getId());
            if(result != null) {
                sCodeInfoDao.update(sCodeInfo);
            } else {
                sCodeInfoDao.insert(sCodeInfo);
            }
        } else {
            sCodeInfoDao.insert(sCodeInfo);
        }

        CodeCache.getInstance().loadData();
    }

    /**
     *
     * 删除数据
     * @param id
     *
     */
    @Transactional
    public void deleteSCodeInfo(Integer id) {

        sCodeInfoDao.delete(id);
    }

    /**
     *
     * 删除数据
     * @param sCodeInfoList
     *
     */
    @Transactional
    public void deleteSCodeInfoBatch(List<String> sCodeInfoList) {
        for(String id : sCodeInfoList) {
            deleteSCodeInfo(Integer.valueOf(id));
        }

        CodeCache.getInstance().loadData();
    }

    /**
     *
     * 根据ID获取数据
     * @param id
     *
     */
    @Transactional
    public SCodeInfo getSCodeInfo(Integer id) {

        return sCodeInfoDao.getSCodeInfoById(id);
    }
}