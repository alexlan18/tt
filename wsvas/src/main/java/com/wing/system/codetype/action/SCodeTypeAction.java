package com.wing.system.codetype.action;

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
import com.wing.system.codetype.model.SCodeTypeParams;
import com.wing.system.codetype.model.SCodeType;
import com.wing.system.codetype.service.SCodeTypeService;
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
@RequestMapping("/system/sCodeType")
public class SCodeTypeAction extends BaseAction {

    //服务层
    @Resource(name = "SCodeTypeService")
    private SCodeTypeService sCodeTypeService;

    /**
     *
     * 跳转到列表页面
     *
     */
    @RequestMapping
    public String goSCodeType() {
        return"/system/codetype/SCodeTypeList";
    }

    /**
     *
     * 加载数据
     *
     */
    @RequestMapping("/load")
    @ResponseBody
    public FlexGrid loadSCodeType(HttpServletRequest request, SCodeTypeParams sCodeTypeParams, Page page) {

        try {

            List results = sCodeTypeService.querySCodeTypeList(sCodeTypeParams, page);
            return new FlexGrid(results, page.getCurrentPage(), page.getTotalSize());

        } catch(Exception e) {
            e.printStackTrace();
            logger.error("查询SCodeType数据异常！");
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
    public SCodeType getSCodeType(@PathVariable("id") Integer id) {
        return sCodeTypeService.getSCodeType(id);
    }

    /**
     *
     * 根据IDs删除数据
     *
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Message deleteSCodeType(String ids) {
        try {
            List<String> idList = new ArrayList<String>();
            if(ids.length() > 0) {
                idList = Arrays.asList(ids.split(","));
            }
            sCodeTypeService.deleteSCodeTypeBatch(idList);
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
    public Message saveSCodeType(SCodeType sCodeType) {

        try {
            sCodeTypeService.saveSCodeType(sCodeType);
        } catch(Exception e) {
            e.printStackTrace();
            logger.error("数据SCodeType保存异常！");
            return new Message(SysContant.ERROR, SysContant.SAVE_ERROR_MESSAGE);
        }
        return new Message(SysContant.SUCCESS, SysContant.SAVE_SUCCESS_MESSAGE);
    }
}
