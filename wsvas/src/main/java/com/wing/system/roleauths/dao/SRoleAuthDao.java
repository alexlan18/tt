package com.wing.system.roleauths.dao;

import com.wing.framework.mybatis.mapper.SqlMapper;
import com.wing.system.roleauths.model.SRoleAuth;
import com.wing.system.roleauths.model.SRoleAuthParams;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author wing
 * @version 1.0
 *          <p/>
 *          the JAVA code is generate by middlegen
 */
@Component("SRoleAuthDao")
public interface SRoleAuthDao extends SqlMapper {

    /**
     * 查询数据
     */
    public List querySRoleAuth(@Param("params") SRoleAuthParams params);

    /**
     * 根据主键获取数据
     */
    public SRoleAuth getSRoleAuthById(Integer id);

    /**
     * 插入数据
     */
    public void insert(SRoleAuth sRoleAuth);

    /**
     * 删除数据
     */
    public void delete(Integer id);

    /**
     * 根据授权ID删除角色权限关系
     *
     * @param authId
     */
    public void deleteByRoleAuth(@Param("roleId") String roleId, @Param("authId") String authId);

    /**
     * 更新数据
     */
    public void update(SRoleAuth sRoleAuth);
}
