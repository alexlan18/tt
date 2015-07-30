package com.wing.system.codetype.service;

import com.wing.framework.base.BaseDao;
import com.wing.framework.cache.CodeCache;
import com.wing.framework.common.Page;
import com.wing.framework.utils.PkUtil;
import com.wing.system.codetype.dao.SCodeTypeDao;
import com.wing.system.codetype.model.SCodeType;
import com.wing.system.codetype.model.SCodeTypeParams;
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
@Service("SCodeTypeService")
public class SCodeTypeService {

    public static final String BEAN_ID = "SCodeTypeService";

    //注入Dao层
    @Resource(name = "SCodeTypeDao")
    private SCodeTypeDao sCodeTypeDao;

    //注入BaseDao
    @Resource(name = "BaseDao")
    private BaseDao baseDao;

    /**
     *
     * 查询数据
     *
     * @param sCodeTypeParams
     * @param page
     *
     */
    @Transactional
    public List querySCodeTypeList(SCodeTypeParams sCodeTypeParams, Page page){

        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put("params", sCodeTypeParams);
        if(page.getSortname() != null && !page.getSortname().equals("")) {
            params.put("sortname", page.getSortname());
        }
        if(page.getSortorder() != null && !page.getSortorder().equals("")) {
            params.put("sortorder", page.getSortorder());
        }

        return baseDao.queryListByPage("querySCodeType", params, page);
    }

    /**
     *
     * 插入数据
     * @param sCodeType
     *
     */
    @Transactional
    public void saveSCodeType(SCodeType sCodeType) {

        if(sCodeType.getId() != null) {
            SCodeType result = getSCodeType(sCodeType.getId());
            if(result != null) {
                sCodeTypeDao.update(sCodeType);
            } else {
                String codeTypeId = PkUtil.getPrimaryKey("CODE_TYPE");
                sCodeType.setCodeTypeId(codeTypeId);
                sCodeTypeDao.insert(sCodeType);
            }
        } else {
            String codeTypeId = PkUtil.getPrimaryKey("CODE_TYPE");
            sCodeType.setCodeTypeId(codeTypeId);
            sCodeTypeDao.insert(sCodeType);
        }
    }

    /**
     *
     * 删除数据
     * @param id
     *
     */
    @Transactional
    public void deleteSCodeType(Integer id) {
        sCodeTypeDao.deleteCodeInfo(id);
        sCodeTypeDao.delete(id);
    }

    /**
     *
     * 删除数据
     * @param sCodeTypeList
     *
     */
    @Transactional
    public void deleteSCodeTypeBatch(List<String> sCodeTypeList) {
        for(String id : sCodeTypeList) {
            deleteSCodeType(Integer.valueOf(id));
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
    public SCodeType getSCodeType(Integer id) {

        return sCodeTypeDao.getSCodeTypeById(id);
    }
}