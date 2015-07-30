package com.wing.system.codetype.dao;

import com.wing.framework.mybatis.mapper.SqlMapper;
import com.wing.system.codetype.model.SCodeType;
import com.wing.system.codetype.model.SCodeTypeParams;
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
@Component("SCodeTypeDao")
public interface SCodeTypeDao extends SqlMapper {

    /**
     *
     * 查询数据
     *
    */
    public List querySCodeType(@Param("params") SCodeTypeParams params);

    /**
     *
     * 查询出所有的代码类型
     *
     * @return
     */
    public List queryAllCodeType();

    /**
     *
     * 根据主键获取数据
     *
     */
    public SCodeType getSCodeTypeById(Integer id);

    /**
     *
     * 插入数据
     *
     */
    public void insert(SCodeType sCodeType);

    /**
     *
     * 删除数据
     *
     */
    public void delete(Integer id);

    /**
     *
     * 根据代码类型ID删除代码信息
     *
     * @param id
     */
    public void deleteCodeInfo(Integer id);

    /**
     *
     * 更新数据
     *
     */
    public void update(SCodeType sCodeType);
}
