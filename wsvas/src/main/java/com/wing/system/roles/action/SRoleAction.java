package com.wing.system.roles.action;

import com.wing.framework.base.BaseAction;
import com.wing.framework.common.FlexGrid;
import com.wing.framework.common.Message;
import com.wing.framework.common.Page;
import com.wing.framework.common.constant.SysContant;
import com.wing.system.roles.model.SRole;
import com.wing.system.roles.model.SRoleParams;
import com.wing.system.roles.service.SRoleService;
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
@RequestMapping("/system/sRole")
public class SRoleAction extends BaseAction {

    //服务层
    @Resource(name = "SRoleService")
    private SRoleService sRoleService;

    /**
     *
     * 跳转到列表页面
     *
     */
    @RequestMapping
    public String goSRole() {
        return"/system/roles/SRoleList";
    }

    /**
     *
     * 加载数据
     *
     */
    @RequestMapping("/load")
    @ResponseBody
    public FlexGrid loadSRole(HttpServletRequest request, SRoleParams sRoleParams, Page page) {

        try {

            List results = sRoleService.querySRoleList(sRoleParams, page);
            return new FlexGrid(results, page.getCurrentPage(), page.getTotalSize());

        } catch(Exception e) {
            e.printStackTrace();
            logger.error("查询SRole数据异常！");
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
    public SRole getSRole(@PathVariable("id") Integer id) {
        return sRoleService.getSRole(id);
    }

    /**
     *
     * 根据IDs删除数据
     *
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Message deleteSRole(String ids) {
        try {
            List<String> idList = new ArrayList<String>();
            if(ids.length() > 0) {
                idList = Arrays.asList(ids.split(","));
            }
            sRoleService.deleteSRoleBatch(idList);
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
    public Message saveSRole(SRole sRole) {

        try {
            sRoleService.saveSRole(sRole);
        } catch(Exception e) {
            e.printStackTrace();
            logger.error("数据SRole保存异常！");
            return new Message(SysContant.ERROR, SysContant.SAVE_ERROR_MESSAGE);
        }
        return new Message(SysContant.SUCCESS, SysContant.SAVE_SUCCESS_MESSAGE);
    }

    /**
     *
     * 保存菜单树权限
     *
     * @param id
     * @param menuIds
     * @return
     */
    @RequestMapping("/saveMenuTreeAuth")
    @ResponseBody
    public Message saveMenuTreeAuth(Integer id, String menuIds) {
        try {

            SRole role = sRoleService.getSRole(id);
            String[] menuIdList = menuIds.split(",");

            sRoleService.saveRoleMenus(role, menuIdList);
        } catch(Exception e) {
            e.printStackTrace();
            logger.error("数据SRole保存异常！");
            return new Message(SysContant.ERROR, SysContant.SAVE_ERROR_MESSAGE);
        }
        return new Message(SysContant.SUCCESS, SysContant.SAVE_SUCCESS_MESSAGE);
    }
}
