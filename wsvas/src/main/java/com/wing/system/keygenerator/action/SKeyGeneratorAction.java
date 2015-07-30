package com.wing.system.keygenerator.action;

import com.wing.framework.base.BaseAction;
import com.wing.framework.common.FlexGrid;
import com.wing.framework.common.Message;
import com.wing.framework.common.Page;
import com.wing.framework.common.constant.SysContant;
import com.wing.system.keygenerator.model.SKeyGenerator;
import com.wing.system.keygenerator.model.SKeyGeneratorParams;
import com.wing.system.keygenerator.service.SKeyGeneratorService;
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
@RequestMapping("/system/sKeyGenerator")
public class SKeyGeneratorAction extends BaseAction {

    //服务层
    @Resource(name = "SKeyGeneratorService")
    private SKeyGeneratorService sKeyGeneratorService;

    /**
     *
     * 跳转到列表页面
     *
     */
    @RequestMapping
    public String goSKeyGenerator() {
        return"/system/keygenerator/SKeyGeneratorList";
    }

    /**
     *
     * 加载数据
     *
     */
    @RequestMapping("/load")
    @ResponseBody
    public FlexGrid loadSKeyGenerator(HttpServletRequest request, SKeyGeneratorParams sKeyGeneratorParams, Page page) {

        try {

            List results = sKeyGeneratorService.querySKeyGeneratorList(sKeyGeneratorParams, page);
            return new FlexGrid(results, page.getCurrentPage(), page.getTotalSize());

        } catch(Exception e) {
            e.printStackTrace();
            logger.error("查询SKeyGenerator数据异常！");
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
    public SKeyGenerator getSKeyGenerator(@PathVariable("id") Integer id) {
        return sKeyGeneratorService.getSKeyGenerator(id);
    }

    /**
     *
     * 根据IDs删除数据
     *
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Message deleteSKeyGenerator(String ids) {
        try {
            List<String> idList = new ArrayList<String>();
            if(ids.length() > 0) {
                idList = Arrays.asList(ids.split(","));
            }
            sKeyGeneratorService.deleteSKeyGeneratorBatch(idList);
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
    public Message saveSKeyGenerator(SKeyGenerator sKeyGenerator) {

        try {
            sKeyGeneratorService.saveSKeyGenerator(sKeyGenerator);
        } catch(Exception e) {
            e.printStackTrace();
            logger.error("数据SKeyGenerator保存异常！");
            return new Message(SysContant.ERROR, SysContant.SAVE_ERROR_MESSAGE);
        }
        return new Message(SysContant.SUCCESS, SysContant.SAVE_SUCCESS_MESSAGE);
    }
}
