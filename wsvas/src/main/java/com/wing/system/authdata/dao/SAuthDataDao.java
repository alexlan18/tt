package com.wing.system.authdata.dao;

import com.wing.framework.mybatis.mapper.SqlMapper;
import com.wing.system.authdata.model.SAuthData;
import com.wing.system.authdata.model.SAuthDataParams;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * @author wing
 * @version 1.0
 *
 * the JAVA code is generate by middlegen
 *
 */
@Component("SAuthDataDao")
public interface SAuthDataDao extends SqlMapper {

    /**
     *
     * 查询数据
     *
    */
    public List querySAuthData(@Param("params") SAuthDataParams params);

    /**
     *
     * 根据角色获取已分配权限
     *
     * @param roleId
     * @return
     */
    public List queryAuthByRole(@Param("roleId") String roleId);

    /**
     *
     * 根据角色获取未已分配权限
     *
     * @param roleId
     * @return
     */
    public List queryNotAuthByRole(@Param("roleId") String roleId);


    /**
     *
     * 根据主键获取数据
     *
     */
    public SAuthData getSAuthDataById(Integer id);

    /**
     *
     * 插入数据
     *
     */
    public void insert(SAuthData sAuthData);

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
    public void update(SAuthData sAuthData);

    /**
     *
     * @return
     */
    public List<String> queryAuthDataList();
}
