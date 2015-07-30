package com.wing.system.codeinfo.dao;

import com.wing.framework.mybatis.mapper.SqlMapper;
import com.wing.system.codeinfo.model.SCodeInfo;
import com.wing.system.codeinfo.model.SCodeInfoParams;
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
@Component("SCodeInfoDao")
public interface SCodeInfoDao extends SqlMapper {

    /**
     *
     * 查询数据
     *
    */
    public List querySCodeInfo(@Param("params") SCodeInfoParams params);

    /**
     *
     * 获取所有的代码信息
     *
     * @param codeTypeId
     * @return
     */
    public List queryAllCodeInfo();

    /**
     *
     * 根据主键获取数据
     *
     */
    public SCodeInfo getSCodeInfoById(Integer id);

    /**
     *
     * 插入数据
     *
     */
    public void insert(SCodeInfo sCodeInfo);

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
    public void update(SCodeInfo sCodeInfo);
}
