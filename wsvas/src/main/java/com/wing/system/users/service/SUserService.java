package com.wing.system.users.service;

import com.wing.framework.base.BaseDao;
import com.wing.framework.common.Message;
import com.wing.framework.common.Page;
import com.wing.framework.common.constant.SysContant;
import com.wing.system.users.dao.SUserDao;
import com.wing.system.users.model.ModifyPassword;
import com.wing.system.users.model.SUser;
import com.wing.system.users.model.SUserParams;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;



/**
 *
 * @author wing
 * @version 1.0
 *
 * the JAVA code is generate by middlegen
 *
 */
@Service("SUserService")
public class SUserService {

    public static final String BEAN_ID = "SUserService";

    //注入Dao层
    @Resource(name = "SUserDao")
    private SUserDao sUserDao;

    //注入BaseDao
    @Resource(name = "BaseDao")
    private BaseDao baseDao;

    /**
     *
     * 查询数据
     *
     * @param sUserParams
     * @param page
     *
     */
    @Transactional
    public List querySUserList(SUserParams sUserParams, Page page){

        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put("params", sUserParams);
        if(page.getSortname() != null && !page.getSortname().equals("")) {
            params.put("sortname", page.getSortname());
        }
        if(page.getSortorder() != null && !page.getSortorder().equals("")) {
            params.put("sortorder", page.getSortorder());
        }

        return baseDao.queryListByPage("querySUser", params, page);
    }

    /**
     *
     * 插入数据
     * @param sUser
     *
     */
    @Transactional
    public void saveSUser(SUser sUser) {
        if(sUser.getId() != null) {
            SUser result = getSUser(sUser.getId());
            if(result != null) {
                sUserDao.update(sUser);
            } else {
                String password = DigestUtils.md5Hex(SysContant.INIT_PASSWORD);
                sUser.setPassword(password);
                sUserDao.insert(sUser);
            }
        } else {
            String password = DigestUtils.md5Hex(SysContant.INIT_PASSWORD);
            sUser.setPassword(password);
            sUserDao.insert(sUser);
        }
    }

    /**
     *
     * 保存用户密码
     *
     * @param sUser
     * @param modifyPassword
     */
    @Transactional
    public Message saveNewPassword(SUser sUser, ModifyPassword modifyPassword) {

        String password = DigestUtils.md5Hex(modifyPassword.getPassword());

        //判断旧密码是否正确
        if(!sUser.getPassword().equals(password)) {
            return new Message(SysContant.ERROR, SysContant.MODIFY_PASSWORD_OLD_PASSWORD_ERROR);
        }

        //判断两次密码是否一致
        if(!modifyPassword.getPassword1().equals(modifyPassword.getPassword2())) {
            return new Message(SysContant.ERROR, SysContant.MODIFY_PASSWORD_NEW_PASSWORD_ERROR);
        }

        //更新用户密码
        sUser.setPassword(DigestUtils.md5Hex(modifyPassword.getPassword1()));
        sUserDao.update(sUser);

        return new Message(SysContant.SUCCESS, SysContant.MODIFY_PASSWORD_SUCCESS);
    }

    /**
     *
     * 删除数据
     * @param id
     *
     */
    @Transactional
    public void deleteSUser(Integer id) {

        sUserDao.delete(id);
    }

    /**
     *
     * 删除数据
     * @param sUserList
     *
     */
    @Transactional
    public void deleteSUserBatch(List<String> sUserList) {
        for(String id : sUserList) {
            deleteSUser(Integer.valueOf(id));
        }
    }

    /**
     *
     * 根据ID获取数据
     * @param id
     *
     */
    @Transactional
    public SUser getSUser(Integer id) {

        return sUserDao.getSUserById(id);
    }
}