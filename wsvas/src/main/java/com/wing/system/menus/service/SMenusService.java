package com.wing.system.menus.service;

import com.wing.framework.base.BaseDao;
import com.wing.framework.common.Page;
import com.wing.framework.common.Tree;
import com.wing.framework.common.constant.SysContant;
import com.wing.system.menus.dao.SMenusDao;
import com.wing.system.menus.model.MenuView;
import com.wing.system.menus.model.SMenus;
import com.wing.system.menus.model.SMenusParams;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * @author wing
 * @version 1.0
 *
 * the JAVA code is generate by middlegen
 *
 */
@Service("SMenusService")
public class SMenusService {

    public static final String BEAN_ID = "SMenusService";

    //注入Dao层
    @Resource(name = "SMenusDao")
    private SMenusDao sMenusDao;

    //注入BaseDao
    @Resource(name = "BaseDao")
    private BaseDao baseDao;

    /**
     *
     * 查询数据
     *
     * @param sMenusParams
     * @param page
     *
     */
    @Transactional
    public List querySMenusList(SMenusParams sMenusParams, Page page){

        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put("params", sMenusParams);
        if(page.getSortname() != null && !page.getSortname().equals("")) {
            params.put("sortname", page.getSortname());
        }
        if(page.getSortorder() != null && !page.getSortorder().equals("")) {
            params.put("sortorder", page.getSortorder());
        }

        return baseDao.queryListByPage("querySMenus", params, page);
    }

    /**
     *
     * 根据用户权限查询菜单数据
     *
     * @return
     */
    public List<MenuView> queryMenusByUser(String userId) {

        List<MenuView> result = new ArrayList<MenuView>();

        List<SMenus> rootMenus = sMenusDao.queryMenusByParentUserId(SysContant.ROOT_PARENT_MENU_ID, userId);
        for(SMenus rootMenu : rootMenus) {
            MenuView menuView = new MenuView();
            menuView.setTitle(rootMenu.getMenuName());
            menuView.setContent(sMenusDao.queryMenusByParentUserId(rootMenu.getMenuId(), userId));

            result.add(menuView);
        }

        return result;
    }

    /**
     *
     * 根据用户权限查询菜单数据
     *
     * @return
     */
    public List<MenuView> queryMenusSuper() {

        List<MenuView> result = new ArrayList<MenuView>();

        List<SMenus> rootMenus = sMenusDao.queryMenusByParent(SysContant.ROOT_PARENT_MENU_ID);
        for(SMenus rootMenu : rootMenus) {
            MenuView menuView = new MenuView();
            menuView.setTitle(rootMenu.getMenuName());
            menuView.setContent(sMenusDao.queryMenusByParent(rootMenu.getMenuId()));

            result.add(menuView);
        }

        return result;
    }

    /**
     *
     * 插入数据
     * @param sMenus
     *
     */
    @Transactional
    public void saveSMenus(SMenus sMenus) {
        if(sMenus.getId() != null) {
            SMenus result = getSMenus(sMenus.getId());
            if(result != null) {
                sMenusDao.update(sMenus);
            } else {
                sMenusDao.insert(sMenus);
            }
        } else {
            sMenusDao.insert(sMenus);
        }
    }

    /**
     *
     * 删除数据
     * @param id
     *
     */
    @Transactional
    public void deleteSMenus(Integer id) {
        //要删除的菜单信息
        SMenus menu = sMenusDao.getSMenusById(id);
        if(menu != null) {
            sMenusDao.deleteByParentMenuId(menu.getMenuId());
        }
        sMenusDao.delete(id);
    }

    /**
     *
     * 删除数据
     * @param sMenusList
     *
     */
    @Transactional
    public void deleteSMenusBatch(List<String> sMenusList) {
        for(String id : sMenusList) {
            deleteSMenus(Integer.valueOf(id));
        }
    }

    /**
     *
     * 根据ID获取数据
     * @param id
     *
     */
    @Transactional
    public SMenus getSMenus(Integer id) {

        return sMenusDao.getSMenusById(id);
    }

    /**
     *
     * 获取菜单数据树
     *
     * @return
     */
    public List<Tree> getMenuTree() {
        //根节点
        Tree root = new Tree();
        root.setId(SysContant.ROOT_PARENT_MENU_ID);
        queryMenusTree(root, 0);
        return root.getChildren();
    }

    /**
     *
     * 根据角色ID获取菜单树
     *
     * @param roleId
     * @return
     */
    public List<Tree> getMenuTreeByRoleId(String roleId) {

        //根节点
        Tree root = new Tree();
        root.setId(SysContant.ROOT_PARENT_MENU_ID);

        List<String> authMenus = sMenusDao.querySMenusByRole(roleId);

        queryMenusTreeByRole(root, 0, authMenus);
        return root.getChildren();

    }

    /**
     *
     * 查询菜单树
     *
     * @param tree
     * @param level
     */
    public void queryMenusTreeByRole(Tree tree, Integer level, List<String> authMenus) {

        //子树
        List<Tree> treeList = new ArrayList<Tree>();

        //查询子菜单
        List<SMenus> list = sMenusDao.queryMenusByParent(tree.getId());

        //遍历所有的子菜单数据
        for (SMenus menu : list) {

            Tree subTreeNode = new Tree();

            subTreeNode.setId(menu.getMenuId());
            subTreeNode.setText(menu.getMenuName());
            subTreeNode.setState("open");
            Map<Object, Object> attributes = new HashMap<Object, Object>();
            attributes.put("level", level);
            attributes.put("id", menu.getId());
            subTreeNode.setAttributes(attributes);

            if(authMenus.contains(menu.getMenuId())) {
                subTreeNode.setChecked("true");
            }
            treeList.add(subTreeNode);
            queryMenusTreeByRole(subTreeNode, level+1, authMenus);
        }
        tree.setChildren(treeList);
    }

    /**
     *
     * 查询菜单树
     *
     * @param tree
     * @param level
     */
    public void queryMenusTree(Tree tree, Integer level) {

        //子树
        List<Tree> treeList = new ArrayList<Tree>();

        //查询子菜单
        List<SMenus> list = sMenusDao.queryMenusByParent(tree.getId());

        //遍历所有的子菜单数据
        for (SMenus menu : list) {

            Tree subTreeNode = new Tree();

            subTreeNode.setId(menu.getMenuId());
            subTreeNode.setText(menu.getMenuName());
            subTreeNode.setState("closed");

            Map<Object, Object> attributes = new HashMap<Object, Object>();
            attributes.put("level", level);
            attributes.put("id", menu.getId());
            subTreeNode.setAttributes(attributes);

            treeList.add(subTreeNode);
            queryMenusTree(subTreeNode, level+1);
        }
        //判断是否存在子节点
        if(treeList.size() == 0) {
            tree.setState("open");
        }
        tree.setChildren(treeList);
    }
}