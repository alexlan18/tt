package com.wing.system.rolemenus.dao;

import com.wing.framework.mybatis.mapper.SqlMapper;
import com.wing.system.rolemenus.model.SRoleMenus;
import com.wing.system.rolemenus.model.SRoleMenusParams;
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
@Component("SRoleMenusDao")
public interface SRoleMenusDao extends SqlMapper {

    /**
     *
     * 查询数据
     *
    */
    public List querySRoleMenus(@Param("params") SRoleMenusParams params);

    /**
     *
     * 根据主键获取数据
     *
     */
    public SRoleMenus getSRoleMenusById(Integer id);

    /**
     *
     * 插入数据
     *
     */
    public void insert(SRoleMenus sRoleMenus);

    /**
     *
     * 删除数据
     *
     */
    public void delete(Integer id);

    /**
     *
     * 根据角色删除角色菜单权限
     *
     * @param roleId
     */
    public void deleteByRole(String roleId);

    /**
     *
     * 更新数据
     *
     */
    public void update(SRoleMenus sRoleMenus);
}
