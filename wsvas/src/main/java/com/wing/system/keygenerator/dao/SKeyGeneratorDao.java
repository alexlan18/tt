package com.wing.system.keygenerator.dao;

import com.wing.framework.mybatis.mapper.SqlMapper;
import com.wing.system.keygenerator.model.SKeyGenerator;
import com.wing.system.keygenerator.model.SKeyGeneratorParams;
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
@Component("SKeyGeneratorDao")
public interface SKeyGeneratorDao extends SqlMapper {

    /**
     *
     * 查询数据
     *
    */
    public List querySKeyGenerator(@Param("params") SKeyGeneratorParams params);

    /**
     *
     * 根据主键获取数据
     *
     */
    public SKeyGenerator getSKeyGeneratorById(Integer id);

    /**
     *
     * 根据不同的type，获取生成策略
     *
     * @param type
     * @return
     */
    public SKeyGenerator getSKeyGeneratorByType(String type);

    /**
     *
     * 插入数据
     *
     */
    public void insert(SKeyGenerator sKeyGenerator);

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
    public void update(SKeyGenerator sKeyGenerator);
}
