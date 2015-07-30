package com.wing.system.roles.dao;

import java.util.List;

import com.wing.system.roles.model.SRole;
import com.wing.framework.mybatis.mapper.SqlMapper;
import org.springframework.stereotype.Component;
import org.apache.ibatis.annotations.Param;

import com.wing.system.roles.model.SRoleParams;

/**
 *
 * @author wing
 * @version 1.0
 *
 * the JAVA code is generate by middlegen
 *
 */
@Component("SRoleDao")
public interface SRoleDao extends SqlMapper {

    /**
     *
     * 查询数据
     *
    */
    public List querySRole(@Param("params") SRoleParams params);

    /**
     *
     * 根据主键获取数据
     *
     */
    public SRole getSRoleById(Integer id);

    /**
     *
     * 插入数据
     *
     */
    public void insert(SRole sRole);

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
    public void update(SRole sRole);
}
