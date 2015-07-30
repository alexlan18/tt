package com.wing.system.menus.dao;

import com.wing.framework.mybatis.mapper.SqlMapper;
import com.wing.system.menus.model.SMenus;
import com.wing.system.menus.model.SMenusParams;
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
@Component("SMenusDao")
public interface SMenusDao extends SqlMapper {

    /**
     *
     * 查询数据
     *
    */
    public List querySMenus(@Param("params") SMenusParams params);

    /**
     *
     * 根据父节点ID查询菜单列表
     *
     * @param parentMenuId
     * @return
     */
    public List<SMenus> queryMenusByParent(@Param("parentMenuId") String parentMenuId);

    /**
     *
     * 根据父节点ID查询菜单列表
     *
     * @param parentMenuId
     * @return
     */
    public List<SMenus> queryMenusByParentUserId(@Param("parentMenuId") String parentMenuId, @Param("userId") String userId);

    /**
     *
     * 根据角色ID获取菜单列表
     *
     * @param roleId
     * @return
     */
    public List<String> querySMenusByRole(@Param("roleId") String roleId);

    /**
     *
     * 根据主键获取数据
     *
     */
    public SMenus getSMenusById(Integer id);

    /**
     *
     * 插入数据
     *
     */
    public void insert(SMenus sMenus);

    /**
     *
     * 删除数据
     *
     */
    public void delete(Integer id);

    /**
     *
     * 根据parentMenuId，删除菜单信息
     *
     * @param menuId
     */
    public void deleteByParentMenuId(String parentMenuId);

    /**
     *
     * 更新数据
     *
     */
    public void update(SMenus sMenus);
}
