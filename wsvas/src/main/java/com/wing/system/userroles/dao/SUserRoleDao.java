package com.wing.system.userroles.dao;

import com.wing.framework.mybatis.mapper.SqlMapper;
import com.wing.system.userroles.model.SUserRole;
import com.wing.system.userroles.model.SUserRoleParams;
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
@Component("SUserRoleDao")
public interface SUserRoleDao extends SqlMapper {

    /**
     *
     * 查询数据
     *
    */
    public List querySUserRole(@Param("params") SUserRoleParams params);

    /**
     *
     * 根据用户的ID获取已分配角色信息
     *
     * @param id
     * @return
     */
    public List queryRolesByUser(@Param("id") Integer id);

    /**
     *
     * 根据用户的ID获取未分配的角色信息
     *
     * @param id
     * @return
     */
    public List queryNotRolesByUser(@Param("id") Integer id);

    /**
     *
     * 根据主键获取数据
     *
     */
    public SUserRole getSUserRoleById(Integer id);

    /**
     *
     * 插入数据
     *
     */
    public void insert(SUserRole sUserRole);

    /**
     *
     * 删除数据
     *
     */
    public void delete(Integer id);

    /**
     *
     * 删除用户角色关系
     *
     * @param userId
     * @param roleId
     */
    public void deleteByUserRole(@Param("userId") String userId, @Param("roleId") String roleId);

    /**
     *
     * 更新数据
     *
     */
    public void update(SUserRole sUserRole);

    /**
     *
     * 根据登录名获取权限信息
     *
     * @param loginName
     * @return
     */
    public List<String> queryAuthByLoginName(@Param("loginName")String loginName);
}
