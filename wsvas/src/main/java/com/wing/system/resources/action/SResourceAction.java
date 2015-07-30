package com.wing.system.resources.action;

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
import com.wing.system.resources.model.SResourceParams;
import com.wing.system.resources.model.SResource;
import com.wing.system.resources.service.SResourceService;
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
@RequestMapping("/system/sResource")
public class SResourceAction extends BaseAction {

    //服务层
    @Resource(name = "SResourceService")
    private SResourceService sResourceService;

    /**
     *
     * 跳转到列表页面
     *
     */
    @RequestMapping
    public String goSResource() {
        return"/system/resources/SResourceList";
    }

    /**
     *
     * 加载数据
     *
     */
    @RequestMapping("/load")
    @ResponseBody
    public FlexGrid loadSResource(HttpServletRequest request, SResourceParams sResourceParams, Page page) {

        try {

            List results = sResourceService.querySResourceList(sResourceParams, page);
            return new FlexGrid(results, page.getCurrentPage(), page.getTotalSize());

        } catch(Exception e) {
            e.printStackTrace();
            logger.error("查询SResource数据异常！");
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
    public SResource getSResource(@PathVariable("id") Integer id) {
        return sResourceService.getSResource(id);
    }

    /**
     *
     * 根据IDs删除数据
     *
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Message deleteSResource(String ids) {
        try {
            List<String> idList = new ArrayList<String>();
            if(ids.length() > 0) {
                idList = Arrays.asList(ids.split(","));
            }
            sResourceService.deleteSResourceBatch(idList);
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
    public Message saveSResource(SResource sResource) {

        try {
            sResourceService.saveSResource(sResource);
        } catch(Exception e) {
            e.printStackTrace();
            logger.error("数据SResource保存异常！");
            return new Message(SysContant.ERROR, SysContant.SAVE_ERROR_MESSAGE);
        }
        return new Message(SysContant.SUCCESS, SysContant.SAVE_SUCCESS_MESSAGE);
    }
}
