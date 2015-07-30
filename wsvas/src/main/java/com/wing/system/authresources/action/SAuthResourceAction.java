package com.wing.system.authresources.action;

import com.wing.framework.base.BaseAction;
import com.wing.framework.common.FlexGrid;
import com.wing.framework.common.Message;
import com.wing.framework.common.Page;
import com.wing.framework.common.constant.SysContant;
import com.wing.system.authresources.model.SAuthResource;
import com.wing.system.authresources.model.SAuthResourceParams;
import com.wing.system.authresources.service.SAuthResourceService;
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
@RequestMapping("/system/sAuthResource")
public class SAuthResourceAction extends BaseAction {

    //服务层
    @Resource(name = "SAuthResourceService")
    private SAuthResourceService sAuthResourceService;

    /**
     *
     * 跳转到列表页面
     *
     */
    @RequestMapping
    public String goSAuthResource() {
        return"/system/authresources/SAuthResourceList";
    }

    /**
     *
     * 加载数据
     *
     */
    @RequestMapping("/load")
    @ResponseBody
    public FlexGrid loadSAuthResource(HttpServletRequest request, SAuthResourceParams sAuthResourceParams, Page page) {

        try {

            List results = sAuthResourceService.querySAuthResourceList(sAuthResourceParams, page);
            return new FlexGrid(results, page.getCurrentPage(), page.getTotalSize());

        } catch(Exception e) {
            e.printStackTrace();
            logger.error("查询SAuthResource数据异常！");
        }

        return null;
    }

    /**
     *
     * 查询所有资源的信息，以及其权限分配情况
     *
     * @param authId
     * @param page
     * @return
     */
    @RequestMapping("/loadResources")
    @ResponseBody
    public FlexGrid loadResourcesAuth(Integer id, Page page) {

        try {

            List results = sAuthResourceService.queryResourceAuth(id, page);
            return new FlexGrid(results, page.getCurrentPage(), page.getTotalSize());

        } catch(Exception e) {
            e.printStackTrace();
            logger.error("查询SAuthResource数据异常！");
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
    public SAuthResource getSAuthResource(@PathVariable("id") Integer id) {
        return sAuthResourceService.getSAuthResource(id);
    }

    /**
     *
     * 根据IDs删除数据
     *
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Message deleteSAuthResource(String ids) {
        try {
            List<String> idList = new ArrayList<String>();
            if(ids.length() > 0) {
                idList = Arrays.asList(ids.split(","));
            }
            sAuthResourceService.deleteSAuthResourceBatch(idList);
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
     * @param allIds
     * @param checkeds
     * @return
     */
    @RequestMapping("/saveAuth")
    @ResponseBody
    public Message saveAuth(Integer id, String allIds, String checkeds) {
        try {
            sAuthResourceService.saveAuth(id, allIds, checkeds);
        } catch(Exception e) {
            e.printStackTrace();
            logger.error("数据" + checkeds + "授权异常！");
            return new Message(SysContant.ERROR, SysContant.SAVE_ERROR_MESSAGE);
        }
        return new Message(SysContant.SUCCESS, SysContant.SAVE_SUCCESS_MESSAGE);
    }

    /**
     *
     * 保存数据
     *
     */
    @RequestMapping("/save")
    @ResponseBody
    public Message saveSAuthResource(SAuthResource sAuthResource) {

        try {
            sAuthResourceService.saveSAuthResource(sAuthResource);
        } catch(Exception e) {
            e.printStackTrace();
            logger.error("数据SAuthResource保存异常！");
            return new Message(SysContant.ERROR, SysContant.SAVE_ERROR_MESSAGE);
        }
        return new Message(SysContant.SUCCESS, SysContant.SAVE_SUCCESS_MESSAGE);
    }
}
