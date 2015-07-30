package com.wing.system.keygenerator.service;

import java.util.List;
import java.util.HashMap;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wing.framework.base.BaseDao;
import com.wing.framework.common.Page;
import com.wing.system.keygenerator.model.SKeyGenerator;
import com.wing.system.keygenerator.dao.SKeyGeneratorDao;
import com.wing.system.keygenerator.model.SKeyGeneratorParams;

import javax.annotation.Resource;



/**
 *
 * @author wing
 * @version 1.0
 *
 * the JAVA code is generate by middlegen
 *
 */
@Service("SKeyGeneratorService")
public class SKeyGeneratorService {

    public static final String BEAN_ID = "SKeyGeneratorService";

    //注入Dao层
    @Resource(name = "SKeyGeneratorDao")
    private SKeyGeneratorDao sKeyGeneratorDao;

    //注入BaseDao
    @Resource(name = "BaseDao")
    private BaseDao baseDao;

    /**
     *
     * 查询数据
     *
     * @param sKeyGeneratorParams
     * @param page
     *
     */
    @Transactional
    public List querySKeyGeneratorList(SKeyGeneratorParams sKeyGeneratorParams, Page page){

        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put("params", sKeyGeneratorParams);
        if(page.getSortname() != null && !page.getSortname().equals("")) {
            params.put("sortname", page.getSortname());
        }
        if(page.getSortorder() != null && !page.getSortorder().equals("")) {
            params.put("sortorder", page.getSortorder());
        }

        return baseDao.queryListByPage("querySKeyGenerator", params, page);
    }

    /**
     *
     * 插入数据
     * @param sKeyGenerator
     *
     */
    @Transactional
    public void saveSKeyGenerator(SKeyGenerator sKeyGenerator) {
        if(sKeyGenerator.getId() != null) {
            SKeyGenerator result = getSKeyGenerator(sKeyGenerator.getId());
            if(result != null) {
                sKeyGeneratorDao.update(sKeyGenerator);
            } else {
                sKeyGeneratorDao.insert(sKeyGenerator);
            }
        } else {
            sKeyGeneratorDao.insert(sKeyGenerator);
        }
    }

    /**
     *
     * 删除数据
     * @param id
     *
     */
    @Transactional
    public void deleteSKeyGenerator(Integer id) {

        sKeyGeneratorDao.delete(id);
    }

    /**
     *
     * 删除数据
     * @param sKeyGeneratorList
     *
     */
    @Transactional
    public void deleteSKeyGeneratorBatch(List<String> sKeyGeneratorList) {
        for(String id : sKeyGeneratorList) {
            deleteSKeyGenerator(Integer.valueOf(id));
        }
    }

    /**
     *
     * 根据ID获取数据
     * @param id
     *
     */
    @Transactional
    public SKeyGenerator getSKeyGenerator(Integer id) {

        return sKeyGeneratorDao.getSKeyGeneratorById(id);
    }
}