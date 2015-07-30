package com.wing.system.roleauths.action;

import com.wing.framework.base.BaseAction;
import com.wing.framework.common.FlexGrid;
import com.wing.framework.common.Message;
import com.wing.framework.common.Page;
import com.wing.framework.common.constant.SysContant;
import com.wing.system.roleauths.model.SRoleAuth;
import com.wing.system.roleauths.model.SRoleAuthParams;
import com.wing.system.roleauths.service.SRoleAuthService;
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
@RequestMapping("/system/sRoleAuth")
public class SRoleAuthAction extends BaseAction {

    //服务层
    @Resource(name = "SRoleAuthService")
    private SRoleAuthService sRoleAuthService;

    /**
     *
     * 跳转到列表页面
     *
     */
    @RequestMapping
    public String goSRoleAuth() {
        return"/system/roleauths/SRoleAuthList";
    }

    /**
     *
     * 加载数据
     *
     */
    @RequestMapping("/load")
    @ResponseBody
    public FlexGrid loadSRoleAuth(HttpServletRequest request, SRoleAuthParams sRoleAuthParams, Page page) {

        try {

            List results = sRoleAuthService.querySRoleAuthList(sRoleAuthParams, page);
            return new FlexGrid(results, page.getCurrentPage(), page.getTotalSize());

        } catch(Exception e) {
            e.printStackTrace();
            logger.error("查询SRoleAuth数据异常！");
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
    public SRoleAuth getSRoleAuth(@PathVariable("id") Integer id) {
        return sRoleAuthService.getSRoleAuth(id);
    }

    /**
     *
     * 根据IDs删除数据
     *
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Message deleteSRoleAuth(String ids) {
        try {
            List<String> idList = new ArrayList<String>();
            if(ids.length() > 0) {
                idList = Arrays.asList(ids.split(","));
            }
            sRoleAuthService.deleteSRoleAuthBatch(idList);
        } catch(Exception e) {
           e.printStackTrace();
           logger.error("数据" + ids + "删除异常！");
            return new Message(SysContant.ERROR, SysContant.DELETE_ERROR_MESSAGE);
        }
        return new Message(SysContant.SUCCESS, SysContant.DELETE_SUCCESS_MESSAGE);
    }

    /**
     *
     * 删除授权信息
     *
     * @param ids
     * @return
     */
    @RequestMapping("/deleteAuth")
    @ResponseBody
    public Message deleteSRoleAuthByAuthId(Integer id, String ids) {
        try {
            List<String> idList = new ArrayList<String>();
            if(ids.length() > 0) {
                idList = Arrays.asList(ids.split(","));
            }
            sRoleAuthService.deleteSRoleAuthBatchByAuthId(id, idList);
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
    public Message saveSRoleAuth(SRoleAuth sRoleAuth) {

        try {
            sRoleAuthService.saveSRoleAuth(sRoleAuth);
        } catch(Exception e) {
            e.printStackTrace();
            logger.error("数据SRoleAuth保存异常！");
            return new Message(SysContant.ERROR, SysContant.SAVE_ERROR_MESSAGE);
        }
        return new Message(SysContant.SUCCESS, SysContant.SAVE_SUCCESS_MESSAGE);
    }

    /**
     *
     * (授权)
     *
     * @param id
     * @param ids
     * @return
     */
    @RequestMapping("/saveAuth")
    @ResponseBody
    public Message addSRoleAuth(Integer id, String ids) {

        try {
            sRoleAuthService.auth(id, ids);
        } catch(Exception e) {
            e.printStackTrace();
            logger.error("数据SRoleAuth保存异常！");
            return new Message(SysContant.ERROR, SysContant.ERROR_MESSAGE);
        }
        return new Message(SysContant.SUCCESS, SysContant.SUCCESS_MESSAGE);

    }
}
