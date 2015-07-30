package com.wing.system.authdata.action;

import com.wing.framework.base.BaseAction;
import com.wing.framework.common.FlexGrid;
import com.wing.framework.common.Message;
import com.wing.framework.common.Page;
import com.wing.framework.common.constant.SysContant;
import com.wing.system.authdata.model.SAuthData;
import com.wing.system.authdata.model.SAuthDataParams;
import com.wing.system.authdata.service.SAuthDataService;
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
@RequestMapping("/system/sAuthData")
public class SAuthDataAction extends BaseAction {

    //服务层
    @Resource(name = "SAuthDataService")
    private SAuthDataService sAuthDataService;

    /**
     *
     * 跳转到列表页面
     *
     */
    @RequestMapping
    public String goSAuthData() {
        return"/system/authdata/SAuthDataList";
    }

    /**
     *
     * 加载数据
     *
     */
    @RequestMapping("/load")
    @ResponseBody
    public FlexGrid loadSAuthData(HttpServletRequest request, SAuthDataParams sAuthDataParams, Page page) {

        try {

            List results = sAuthDataService.querySAuthDataList(sAuthDataParams, page);
            return new FlexGrid(results, page.getCurrentPage(), page.getTotalSize());

        } catch(Exception e) {
            e.printStackTrace();
            logger.error("查询SAuthData数据异常！");
        }

        return null;
    }

    /**
     *
     * 加载数据已分配权限
     *
     */
    @RequestMapping("/loadByRole")
    @ResponseBody
    public FlexGrid loadSAuthDataByRole(HttpServletRequest request, Integer id, Page page) {

        try {

            List results = sAuthDataService.querySAuthDataListByRole(id, page);
            return new FlexGrid(results, page.getCurrentPage(), page.getTotalSize());

        } catch(Exception e) {
            e.printStackTrace();
            logger.error("查询SAuthData已分配权限数据异常！");
        }

        return null;
    }

    /**
     *
     * 加载数据未分配权限
     *
     */
    @RequestMapping("/loadNotByRole")
    @ResponseBody
    public FlexGrid loadNotSAuthDataByRole(HttpServletRequest request, Integer id, Page page) {

        try {

            List results = sAuthDataService.queryNotSAuthDataListByRole(id, page);
            return new FlexGrid(results, page.getCurrentPage(), page.getTotalSize());

        } catch(Exception e) {
            e.printStackTrace();
            logger.error("查询SAuthData未分配权限数据异常！");
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
    public SAuthData getSAuthData(@PathVariable("id") Integer id) {
        return sAuthDataService.getSAuthData(id);
    }

    /**
     *
     * 根据IDs删除数据
     *
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Message deleteSAuthData(String ids) {
        try {
            List<String> idList = new ArrayList<String>();
            if(ids.length() > 0) {
                idList = Arrays.asList(ids.split(","));
            }
            sAuthDataService.deleteSAuthDataBatch(idList);
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
    public Message saveSAuthData(SAuthData sAuthData) {

        try {
            sAuthDataService.saveSAuthData(sAuthData);
        } catch(Exception e) {
            e.printStackTrace();
            logger.error("数据SAuthData保存异常！");
            return new Message(SysContant.ERROR, SysContant.SAVE_ERROR_MESSAGE);
        }
        return new Message(SysContant.SUCCESS, SysContant.SAVE_SUCCESS_MESSAGE);
    }
}
