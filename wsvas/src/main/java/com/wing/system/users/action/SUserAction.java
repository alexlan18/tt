package com.wing.system.users.action;

import com.wing.framework.base.BaseAction;
import com.wing.framework.common.FlexGrid;
import com.wing.framework.common.Message;
import com.wing.framework.common.Page;
import com.wing.framework.common.constant.SysContant;
import com.wing.system.users.model.ModifyPassword;
import com.wing.system.users.model.SUser;
import com.wing.system.users.model.SUserParams;
import com.wing.system.users.service.SUserService;
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
@RequestMapping("/system/sUser")
public class SUserAction extends BaseAction {

    //服务层
    @Resource(name = "SUserService")
    private SUserService sUserService;

    /**
     *
     * 跳转到列表页面
     *
     */
    @RequestMapping
    public String goSUser() {
        return"/system/users/SUserList";
    }

    /**
     *
     * 加载数据
     *
     */
    @RequestMapping("/load")
    @ResponseBody
    public FlexGrid loadSUser(HttpServletRequest request, SUserParams sUserParams, Page page) {

        try {

            List results = sUserService.querySUserList(sUserParams, page);
            return new FlexGrid(results, page.getCurrentPage(), page.getTotalSize());

        } catch(Exception e) {
            e.printStackTrace();
            logger.error("查询SUser数据异常！");
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
    public SUser getSUser(@PathVariable("id") Integer id) {
        return sUserService.getSUser(id);
    }

    /**
     *
     * 根据IDs删除数据
     *
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Message deleteSUser(String ids) {
        try {
            List<String> idList = new ArrayList<String>();
            if(ids.length() > 0) {
                idList = Arrays.asList(ids.split(","));
            }
            sUserService.deleteSUserBatch(idList);
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
    public Message saveSUser(SUser sUser) {

        try {
            sUserService.saveSUser(sUser);
        } catch(Exception e) {
            e.printStackTrace();
            logger.error("数据SUser保存异常！");
            return new Message(SysContant.ERROR, SysContant.SAVE_ERROR_MESSAGE);
        }
        return new Message(SysContant.SUCCESS, SysContant.SAVE_SUCCESS_MESSAGE);
    }

    /**
     *
     * 密码修改
     *
     * @param modifyPassword
     * @return
     */
    @RequestMapping("/modifyPassword")
    @ResponseBody
    public Message modifyPassword(ModifyPassword modifyPassword, HttpServletRequest request) {

        //获取登陆的用户信息
        SUser user = (SUser)request.getSession().getAttribute("loginUser");

        //判断是否存在用户登陆信息
        if(user == null) {
            return new Message(SysContant.ERROR, "登陆超时，修改失败！");
        }

        return sUserService.saveNewPassword(user, modifyPassword);
    }
}
