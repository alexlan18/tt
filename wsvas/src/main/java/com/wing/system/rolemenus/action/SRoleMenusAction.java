package com.wing.system.rolemenus.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import javax.annotation.Resource;

import com.wing.framework.common.FlexGrid;
import com.wing.framework.common.Page;
import com.wing.framework.common.Message;
import com.wing.framework.common.constant.SysContant;
import com.wing.system.rolemenus.model.SRoleMenusParams;
import com.wing.system.rolemenus.model.SRoleMenus;
import com.wing.system.rolemenus.service.SRoleMenusService;
import com.wing.framework.base.BaseAction;



/**
 *
 * @author wing
 * @version 1.0
 *
 * the JAVA code is generate by middlegen
 *
 */
@Controller
@RequestMapping("/system/sRoleMenus")
public class SRoleMenusAction extends BaseAction {

    //服务层
    @Resource(name = "SRoleMenusService")
    private SRoleMenusService sRoleMenusService;

    /**
     *
     * 跳转到列表页面
     *
     */
    @RequestMapping
    public String goSRoleMenus() {
        return"/system/rolemenus/SRoleMenusList";
    }

    /**
     *
     * 加载数据
     *
     */
    @RequestMapping("/load")
    @ResponseBody
    public FlexGrid loadSRoleMenus(HttpServletRequest request, SRoleMenusParams sRoleMenusParams, Page page) {

        try {

            List results = sRoleMenusService.querySRoleMenusList(sRoleMenusParams, page);
            return new FlexGrid(results, page.getCurrentPage(), page.getTotalSize());

        } catch(Exception e) {
            e.printStackTrace();
            logger.error("查询SRoleMenus数据异常！");
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
    public SRoleMenus getSRoleMenus(@PathVariable("id") Integer id) {
        return sRoleMenusService.getSRoleMenus(id);
    }

    /**
     *
     * 根据IDs删除数据
     *
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Message deleteSRoleMenus(String ids) {
        try {
            List<String> idList = new ArrayList<String>();
            if(ids.length() > 0) {
                idList = Arrays.asList(ids.split(","));
            }
            sRoleMenusService.deleteSRoleMenusBatch(idList);
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
    public Message saveSRoleMenus(SRoleMenus sRoleMenus) {

        try {
            sRoleMenusService.saveSRoleMenus(sRoleMenus);
        } catch(Exception e) {
            e.printStackTrace();
            logger.error("数据SRoleMenus保存异常！");
            return new Message(SysContant.ERROR, SysContant.SAVE_ERROR_MESSAGE);
        }
        return new Message(SysContant.SUCCESS, SysContant.SAVE_SUCCESS_MESSAGE);
    }
}
