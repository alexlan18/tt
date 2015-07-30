package com.wing.system.resources.dao;

import java.util.List;

import com.wing.system.resources.model.SResource;
import com.wing.framework.mybatis.mapper.SqlMapper;
import org.springframework.stereotype.Component;
import org.apache.ibatis.annotations.Param;

import com.wing.system.resources.model.SResourceParams;

/**
 *
 * @author wing
 * @version 1.0
 *
 * the JAVA code is generate by middlegen
 *
 */
@Component("SResourceDao")
public interface SResourceDao extends SqlMapper {

    /**
     *
     * 查询数据
     *
    */
    public List querySResource(@Param("params") SResourceParams params);

    /**
     *
     * 根据主键获取数据
     *
     */
    public SResource getSResourceById(Integer id);

    /**
     *
     * 插入数据
     *
     */
    public void insert(SResource sResource);

    /**
     *
     * 删除数据
     *
     */
    public void delete(Integer id);

    /**
     *
     * 更新数据
     *
     */
    public void update(SResource sResource);
}
