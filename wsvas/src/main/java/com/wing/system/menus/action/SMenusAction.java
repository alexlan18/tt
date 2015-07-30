package com.wing.system.menus.action;

import com.wing.framework.base.BaseAction;
import com.wing.framework.common.FlexGrid;
import com.wing.framework.common.Message;
import com.wing.framework.common.Page;
import com.wing.framework.common.Tree;
import com.wing.framework.common.constant.SysContant;
import com.wing.system.menus.model.MenuMessage;
import com.wing.system.menus.model.MenuView;
import com.wing.system.menus.model.SMenus;
import com.wing.system.menus.model.SMenusParams;
import com.wing.system.menus.service.SMenusService;
import com.wing.system.roles.model.SRole;
import com.wing.system.roles.service.SRoleService;
import com.wing.system.users.model.SUser;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



/**
 *
 * @author wing
 * @version 1.0
 *
 * the JAVA code is generate by middlegen
 *
 */
@Controller
@RequestMapping("/system/sMenus")
public class SMenusAction extends BaseAction {

    //服务层
    @Resource(name = "SMenusService")
    private SMenusService sMenusService;

    @Resource(name = "SRoleService")
    private SRoleService sRoleService;

    /**
     *
     * 跳转到列表页面
     *
     */
    @RequestMapping
    public String goSMenus() {
        return"/system/menus/SMenusList";
    }

    /**
     *
     * 加载数据
     *
     */
    @RequestMapping("/load")
    @ResponseBody
    public FlexGrid loadSMenus(HttpServletRequest request, SMenusParams sMenusParams, Page page) {

        try {

            List results = sMenusService.querySMenusList(sMenusParams, page);
            return new FlexGrid(results, page.getCurrentPage(), page.getTotalSize());

        } catch(Exception e) {
            e.printStackTrace();
            logger.error("查询SMenus数据异常！");
        }

        return null;
    }

    /**
     *
     * 根据ID获取数据
     *
     */
    @RequestMapping("/{id}")
    @ResponseBody
    public SMenus getSMenus(@PathVariable("id") Integer id) {
        return sMenusService.getSMenus(id);
    }

    /**
     *
     * 根据IDs删除数据
     *
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Message deleteSMenus(String ids) {
        try {
            List<String> idList = new ArrayList<String>();
            if(ids.length() > 0) {
                idList = Arrays.asList(ids.split(","));
            }
            sMenusService.deleteSMenusBatch(idList);
        } catch(Exception e) {
           e.printStackTrace();
           logger.error("数据" + ids + "删除异常！");
            return new Message(SysContant.ERROR, SysContant.DELETE_ERROR_MESSAGE);
        }
        return new Message(SysContant.SUCCESS, SysContant.DELETE_SUCCESS_MESSAGE);
    }

    /**
     *
     * 保存数据
     *
     */
    @RequestMapping("/save")
    @ResponseBody
    public Message saveSMenus(@Validated SMenus sMenus, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            List<FieldError> errorList = bindingResult.getFieldErrors();
            return new Message(SysContant.ERROR, errorList.get(0).getDefaultMessage());
        }

        try {
            sMenusService.saveSMenus(sMenus);
        } catch(Exception e) {
            e.printStackTrace();
            logger.error("数据SMenus保存异常！");
            return new MenuMessage(SysContant.ERROR, SysContant.SAVE_ERROR_MESSAGE, sMenus);
        }
        return new MenuMessage(SysContant.SUCCESS, SysContant.SAVE_SUCCESS_MESSAGE, sMenus);
    }

    /**
     *
     * 查询菜单列表数据
     *
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public List<MenuView> queryMenusByUser(HttpServletRequest request) {

        SUser user = (SUser)request.getSession().getAttribute("loginUser");
        if(user == null) {
            return new ArrayList();
        }

        //如果用户为超级用户则拥有所有的菜单
        if(user.getIsSys().equals(SysContant.IS_SYS_FLAG)) {
            return sMenusService.queryMenusSuper();
        }
        return sMenusService.queryMenusByUser(user.getLoginName());
    }

    /**
     *
     * 查询菜单树
     *
     * @return
     */
    @RequestMapping("/tree")
    @ResponseBody
    public List<Tree> getMenuTree() {

        return sMenusService.getMenuTree();
    }

    /**
     *
     * 根据角色ID获取菜单树
     *
     * @param id
     * @return
     */
    @RequestMapping("/roleTree/{id}")
    @ResponseBody
    public List<Tree> getMenuTreeByRole(@PathVariable("id") Integer id) {
        SRole role = sRoleService.getSRole(id);
        return sMenusService.getMenuTreeByRoleId(role.getRoleId());
    }
}
