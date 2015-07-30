package com.wing.system.resources.service;

import com.wing.framework.base.BaseDao;
import com.wing.framework.common.Page;
import com.wing.framework.common.constant.SysContant;
import com.wing.framework.utils.PkUtil;
import com.wing.system.resources.dao.SResourceDao;
import com.wing.system.resources.model.SResource;
import com.wing.system.resources.model.SResourceParams;
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
@Service("SResourceService")
public class SResourceService {

    public static final String BEAN_ID = "SResourceService";

    //注入Dao层
    @Resource(name = "SResourceDao")
    private SResourceDao sResourceDao;

    //注入BaseDao
    @Resource(name = "BaseDao")
    private BaseDao baseDao;

    /**
     *
     * 查询数据
     *
     * @param sResourceParams
     * @param page
     *
     */
    @Transactional
    public List querySResourceList(SResourceParams sResourceParams, Page page){

        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put("params", sResourceParams);
        if(page.getSortname() != null && !page.getSortname().equals("")) {
            params.put("sortname", page.getSortname());
        }
        if(page.getSortorder() != null && !page.getSortorder().equals("")) {
            params.put("sortorder", page.getSortorder());
        }

        return baseDao.queryListByPage("querySResource", params, page);
    }

    /**
     *
     * 插入数据
     * @param sResource
     *
     */
    @Transactional
    public void saveSResource(SResource sResource) {
        if(sResource.getId() != null) {
            SResource result = getSResource(sResource.getId());
            if(result != null) {
                sResourceDao.update(sResource);
            } else {
                String resourceId = PkUtil.getPrimaryKey(SysContant.R);
                sResource.setResourceId(resourceId);
                sResourceDao.insert(sResource);
            }
        } else {
            String resourceId = PkUtil.getPrimaryKey(SysContant.R);
            sResource.setResourceId(resourceId);
            sResourceDao.insert(sResource);
        }
    }

    /**
     *
     * 删除数据
     * @param id
     *
     */
    @Transactional
    public void deleteSResource(Integer id) {

        sResourceDao.delete(id);
    }

    /**
     *
     * 删除数据
     * @param sResourceList
     *
     */
    @Transactional
    public void deleteSResourceBatch(List<String> sResourceList) {
        for(String id : sResourceList) {
            deleteSResource(Integer.valueOf(id));
        }
    }

    /**
     *
     * 根据ID获取数据
     * @param id
     *
     */
    @Transactional
    public SResource getSResource(Integer id) {

        return sResourceDao.getSResourceById(id);
    }
}