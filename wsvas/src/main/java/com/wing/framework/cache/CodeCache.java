package com.wing.framework.cache;

import com.wing.framework.utils.SpringUtils;
import com.wing.system.codeinfo.dao.SCodeInfoDao;
import com.wing.system.codeinfo.model.SCodeInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 代码数据缓存
 *
 * @author: panwb
 * <p/>
 * Date: 2014/6/26
 * Time: 14:46
 */
public class CodeCache implements ICache {

    //代码缓存Map
    private static Map<Object, List> codeMap = new HashMap<Object, List>();

    //代码缓存对象
    private static CodeCache codeCache = new CodeCache();

    //获取dao
    private SCodeInfoDao sCodeInfoDao = SpringUtils.getBean("SCodeInfoDao");

    //获取对象实例
    public static CodeCache getInstance() {
        return codeCache;
    }

    /**
     * 加载数据
     */
    public void loadData() {

        //清除缓存
        codeMap.clear();

        //获取代码信息，并加载
        List<SCodeInfo> codeInfoList = sCodeInfoDao.queryAllCodeInfo();
        for (SCodeInfo codeInfo : codeInfoList) {
            List codeInfos = codeMap.get(codeInfo.getCodeTypeId());
            if (codeInfos == null) {
                codeInfos = new ArrayList();
                codeMap.put(codeInfo.getCodeTypeId(), codeInfos);
            }
            codeInfos.add(codeInfo);
        }
    }

    /**
     * 获取数据
     *
     * @param key
     * @return
     */
    public List getValue(String key) {
        List codeInfos = codeMap.get(key);
        if(codeInfos == null) {
            codeInfos = new ArrayList();
        }
        return codeInfos;
    }

    /**
     * 销毁缓存
     */
    @Override
    public void destroy() {
        codeMap = null;
    }
}
