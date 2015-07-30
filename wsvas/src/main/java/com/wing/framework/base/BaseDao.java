package com.wing.framework.base;

import com.wing.framework.common.Page;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * Mybatis基类
 * 
 * @author panwb
 *
 */
public class BaseDao extends SqlSessionDaoSupport {

    /**
     *
     * 为FlexGrid提供分页查询
     *
     * @param mapper
     * @param params
     * @param page
     * @return
     */
    @SuppressWarnings({"rawtypes" })
    public List queryListByPage(String mapper, Map params, Page page) {


        //如果Page为空
        if(null == page) {
            page = new Page();
        }

        //判断传参对象是否为空
        if(null == params) {
            params = new HashMap();
        }

        //起始点
        int offset = page.getPageSize() * (page.getCurrentPage() - 1);

        //查询的记录数
        int limit = page.getPageSize();

        //设置page作为参数
        params.put("page", page);
        List list = getSqlSession().selectList(mapper, params, new RowBounds(offset, limit));
        return list;
    }
	
	/**
	 * 
	 * 不分页查询
	 * 
	 * @param mapper
	 * @param params
	 * @return
	 */
	@SuppressWarnings({"rawtypes" })
	public List queryList(String mapper, Map params) {
		
		//判断传参对象是否为空
		if(null == params) {
			params = new HashMap();
		}
		
		return getSqlSession().selectList(mapper, params);
	}
	
	/**
	 * 获取map<key,Object>
	 * @param mapper	sql语言id
	 * @param params	sql参数
	 * @param keyPro	作为key的属性
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Map queryMap(String mapper,Map params,String keyPro) {
		//判断传参对象是否为空
		if(null == params) {
			params = new HashMap();
		}
		return getSqlSession().selectMap(mapper, params, keyPro);
	}
	
	
}
