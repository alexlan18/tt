package com.wing.system.users.dao;

import com.wing.framework.mybatis.mapper.SqlMapper;
import com.wing.system.users.model.SUser;
import com.wing.system.users.model.SUserParams;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * @author wing
 * @version 1.0
 *
 * the JAVA code is generate by middlegen
 *
 */
@Component("SUserDao")
public interface SUserDao extends SqlMapper {

    /**
     *
     * 查询数据
     *
    */
    public List querySUser(@Param("params") SUserParams params);

    /**
     *
     * 根据主键获取数据
     *
     */
    public SUser getSUserById(Integer id);

    /**
     *
     * 插入数据
     *
     */
    public void insert(SUser sUser);

    /**
     *
     * 删除数据
     *
     */
    public void delete(Integer id);

    /**
     *
     * 更新数据
     *
     */
    public void update(SUser sUser);

    /**
     *
     * 根据登录名获取用户
     *
     * @param loginName
     * @return
     */
    public SUser getSUserByLoginName(@Param("loginName")String loginName);
}
