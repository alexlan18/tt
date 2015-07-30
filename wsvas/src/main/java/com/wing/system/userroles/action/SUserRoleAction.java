package com.wing.system.userroles.action;

import com.wing.framework.base.BaseAction;
import com.wing.framework.common.FlexGrid;
import com.wing.framework.common.Message;
import com.wing.framework.common.Page;
import com.wing.framework.common.constant.SysContant;
import com.wing.system.userroles.model.SUserRole;
import com.wing.system.userroles.model.SUserRoleParams;
import com.wing.system.userroles.service.SUserRoleService;
import org.springframework.stereotype.Controller;
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
@RequestMapping("/system/sUserRole")
public class SUserRoleAction extends BaseAction {

    //服务层
    @Resource(name = "SUserRoleService")
    private SUserRoleService sUserRoleService;

    /**
     *
     * 跳转到列表页面
     *
     */
    @RequestMapping
    public String goSUserRole() {
        return"/system/userroles/SUserRoleList";
    }

    /**
     *
     * 加载数据
     *
     */
    @RequestMapping("/load")
    @ResponseBody
    public FlexGrid loadSUserRole(HttpServletRequest request, SUserRoleParams sUserRoleParams, Page page) {

        try {

            List results = sUserRoleService.querySUserRoleList(sUserRoleParams, page);
            return new FlexGrid(results, page.getCurrentPage(), page.getTotalSize());

        } catch(Exception e) {
            e.printStackTrace();
            logger.error("查询SUserRole数据异常！");
        }

        return null;
    }

    /**
     *
     * 加载已分配角色
     *
     * @param id
     * @param page
     * @return
     */
    @RequestMapping("/loadRolesByUser")
    @ResponseBody
    public FlexGrid loadRolesByUser(String id, Page page) {

        try {

            List results = sUserRoleService.queryRolesByUser(Integer.valueOf(id), page);
            return new FlexGrid(results, page.getCurrentPage(), page.getTotalSize());

        } catch(Exception e) {
            e.printStackTrace();
            logger.error("查询已分配角色信息数据异常！");
        }

        return null;

    }

    /**
     *
     * 加载未分配角色
     *
     * @param id
     * @param page
     * @return
     */
    @RequestMapping("/loadNotRolesByUser")
    @ResponseBody
    public FlexGrid loadNotRolesByUser(String id, Page page) {

        try {

            List results = sUserRoleService.queryNotRolesByUser(Integer.valueOf(id), page);
            return new FlexGrid(results, page.getCurrentPage(), page.getTotalSize());

        } catch(Exception e) {
            e.printStackTrace();
            logger.error("查询未分配角色信息数据异常！");
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
    public SUserRole getSUserRole(@PathVariable("id") Integer id) {
        return sUserRoleService.getSUserRole(id);
    }

    /**
     *
     * 根据IDs删除数据
     *
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Message deleteSUserRole(String ids) {
        try {
            List<String> idList = new ArrayList<String>();
            if(ids.length() > 0) {
                idList = Arrays.asList(ids.split(","));
            }
            sUserRoleService.deleteSUserRoleBatch(idList);
        } catch(Exception e) {
           e.printStackTrace();
           logger.error("数据" + ids + "删除异常！");
            return new Message(SysContant.ERROR, SysContant.DELETE_ERROR_MESSAGE);
        }
        return new Message(SysContant.SUCCESS, SysContant.DELETE_SUCCESS_MESSAGE);
    }

    /**
     *
     * 删除权限
     *
     * @param id
     * @param ids
     * @return
     */
    @RequestMapping("/deleteAuth")
    @ResponseBody
    public Message deleteAuth(String id, String ids) {

        try {
            List<String> idList = new ArrayList<String>();
            if(ids.length() > 0) {
                idList = Arrays.asList(ids.split(","));
            }
            sUserRoleService.deleteAuthBatch(id, idList);
        } catch(Exception e) {
            e.printStackTrace();
            logger.error("数据" + ids + "删除异常！");
            return new Message(SysContant.ERROR, SysContant.ERROR_MESSAGE);
        }
        return new Message(SysContant.SUCCESS, SysContant.SUCCESS_MESSAGE);

    }

    /**
     *
     * 给用户添加权限
     *
     * @param id
     * @param ids
     * @return
     */
    @RequestMapping("/saveAuth")
    @ResponseBody
    public Message saveAuth(String id, String ids) {

        try {
            List<String> idList = new ArrayList<String>();
            if(ids.length() > 0) {
                idList = Arrays.asList(ids.split(","));
            }
            sUserRoleService.saveAuthBatch(id, idList);
        } catch(Exception e) {
            e.printStackTrace();
            logger.error("数据" + ids + "删除异常！");
            return new Message(SysContant.ERROR, SysContant.ERROR_MESSAGE);
        }
        return new Message(SysContant.SUCCESS, SysContant.SUCCESS_MESSAGE);

    }

    /**
     *
     * 保存数据
     *
     */
    @RequestMapping("/save")
    @ResponseBody
    public Message saveSUserRole(SUserRole sUserRole) {

        try {
            sUserRoleService.saveSUserRole(sUserRole);
        } catch(Exception e) {
            e.printStackTrace();
            logger.error("数据SUserRole保存异常！");
            return new Message(SysContant.ERROR, SysContant.SAVE_ERROR_MESSAGE);
        }
        return new Message(SysContant.SUCCESS, SysContant.SAVE_SUCCESS_MESSAGE);
    }
}
