package com.wing.system.authresources.dao;

import com.wing.framework.mybatis.mapper.SqlMapper;
import com.wing.system.authresources.model.SAuthResource;
import com.wing.system.authresources.model.SAuthResourceParams;
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
@Component("SAuthResourceDao")
public interface SAuthResourceDao extends SqlMapper {

    /**
     *
     * 查询数据
     *
    */
    public List querySAuthResource(@Param("params") SAuthResourceParams params);

    /**
     *
     * 查询所有的资源信息，以及其是否授权的信息
     *
     * @param authId
     * @return
     */
    public List queryAllResourcesByAuth(@Param("authId") String authId);

    /**
     *
     * 根据主键获取数据
     *
     */
    public SAuthResource getSAuthResourceById(Integer id);

    /**
     *
     * 插入数据
     *
     */
    public void insert(SAuthResource sAuthResource);

    /**
     *
     * 删除数据
     *
     */
    public void delete(Integer id);

    /**
     *
     * 根据权限ID、资源ID删除授权关系
     *
     * @param authId
     * @param resourceId
     */
    public void deleteAuth(@Param("authId")String authId, @Param("resourceId")String resourceId);

    /**
     *
     * 更新数据
     *
     */
    public void update(SAuthResource sAuthResource);

    /**
     *
     * 根据权限获取资源信息
     *
     * @param authId
     * @return
     */
    public List<String> queryResourcesByAuth(String authId);
}
