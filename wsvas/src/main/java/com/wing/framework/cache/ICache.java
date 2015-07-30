package com.wing.framework.cache;

/**
 *
 * 缓存接口
 *
 * @author: panwb
 *
 * Date: 2014/6/26
 * Time: 15:00
 */
public interface ICache {

    /**
     *
     * 加载数据
     *
     */
    public void loadData();

    /**
     *
     * 根据key获取对应的value
     *
     * @param key
     * @return
     */
    public Object getValue(String key);

    /**
     *
     * 销毁缓存
     *
     */
    public void destroy();
}
