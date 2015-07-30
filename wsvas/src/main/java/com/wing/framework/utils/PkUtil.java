package com.wing.framework.utils;

import com.wing.system.keygenerator.dao.SKeyGeneratorDao;
import com.wing.system.keygenerator.model.SKeyGenerator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 产生唯一Key
 *
 * @author panwb
 */
public class PkUtil {

    /**
     * 获取32位唯一的uuid
     *
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     *
     * 根据不同的生成策略，生成主键
     *
     * @param type
     * @return
     */
    public static synchronized String getPrimaryKey(String type) {

        //获取主键策略
        SKeyGeneratorDao sKeyGeneratorDao = SpringUtils.getBean("SKeyGeneratorDao");
        SKeyGenerator sKeyGenerator = sKeyGeneratorDao.getSKeyGeneratorByType(type);
        String prefix = sKeyGenerator.getPrefix();
        String suffix = sKeyGenerator.getSuffix();
        String num = String.valueOf(sKeyGenerator.getNum());
        Integer length = sKeyGenerator.getLength();

        StringBuffer result = new StringBuffer();
        result.append(prefix);

        //补0
        for(int i = 0; i < length - suffix.length() - prefix.length() - num.length(); i++) {
            result.append("0");
        }
        result.append(num);
        result.append(suffix);

        sKeyGenerator.setNum(sKeyGenerator.getNum() + 1);
        sKeyGeneratorDao.update(sKeyGenerator);

        return result.toString();
    }

    /**
     *
     * 获取批量主键
     *
     * @param type
     * @param count
     * @return
     */
    public static synchronized String[] getPrimaryKeyBatch(String type, Integer count) {

        //获取主键策略
        SKeyGeneratorDao sKeyGeneratorDao = SpringUtils.getBean("SKeyGeneratorDao");
        SKeyGenerator sKeyGenerator = sKeyGeneratorDao.getSKeyGeneratorByType(type);
        String prefix = sKeyGenerator.getPrefix();
        String suffix = sKeyGenerator.getSuffix();
        Integer length = sKeyGenerator.getLength();

        String[] result = new String[count];


        for(int i = 0; i < count; i++) {

            StringBuffer key = new StringBuffer();
            key.append(prefix);
            String num = String.valueOf(sKeyGenerator.getNum());
            //补0
            for(int j = 0; j < length - suffix.length() - prefix.length() - num.length(); j++) {
                key.append("0");
            }
            key.append(num);
            key.append(suffix);
            sKeyGenerator.setNum(sKeyGenerator.getNum() + 1);

            result[i] = key.toString();
        }
        sKeyGeneratorDao.update(sKeyGenerator);
        return result;
    }

    /**
     *
     * 根据不同的生成策略，生成主键（按日期生成），批量
     *
     * @param type
     * @return
     */
    public static String[] getDatePrimaryKeyBatch(String type, Integer count) {

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

        Date now = new Date();
        //获取主键策略
        SKeyGeneratorDao sKeyGeneratorDao = SpringUtils.getBean("SKeyGeneratorDao");
        SKeyGenerator sKeyGenerator = sKeyGeneratorDao.getSKeyGeneratorByType(type);
        String prefix = sKeyGenerator.getPrefix();
        String suffix = sKeyGenerator.getSuffix();
        Integer length = sKeyGenerator.getLength();

        //结果集
        String[] results = new String[count];

        for(int i = 0; i < count; i++) {

            StringBuffer result = new StringBuffer();
            Date keyDate = sKeyGenerator.getKeyDate();

            if(dateFormat.format(now).equals(dateFormat.format(keyDate))) {

                String num = String.valueOf(sKeyGenerator.getNum());

                result.append(prefix);
                result.append(dateFormat.format(keyDate));

                //补0
                for(int j = 0; j < length - suffix.length() - prefix.length() - num.length() - 8; j++) {
                    result.append("0");
                }
                result.append(num);
                result.append(suffix);

                results[i] = result.toString();
                sKeyGenerator.setNum(sKeyGenerator.getNum() + 1);

            } else {
                sKeyGenerator.setKeyDate(now);
                sKeyGenerator.setNum(1);

                String num = String.valueOf(sKeyGenerator.getNum());

                result.append(prefix);
                result.append(dateFormat.format(keyDate));

                //补0
                for(int j = 0; j < length - suffix.length() - prefix.length() - num.length() - 8; j++) {
                    result.append("0");
                }
                result.append(num);
                result.append(suffix);

                results[i] = result.toString();
                sKeyGenerator.setNum(sKeyGenerator.getNum() + 1);
            }
        }

        sKeyGeneratorDao.update(sKeyGenerator);
        return results;
    }

    /**
     *
     * 根据不同的类型，生成主键（日期）
     *
     * @param type
     * @return
     */
    public static String getDatePrimaryKey(String type) {

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

        Date now = new Date();
        //获取主键策略
        SKeyGeneratorDao sKeyGeneratorDao = SpringUtils.getBean("SKeyGeneratorDao");
        SKeyGenerator sKeyGenerator = sKeyGeneratorDao.getSKeyGeneratorByType(type);
        String prefix = sKeyGenerator.getPrefix();
        String suffix = sKeyGenerator.getSuffix();
        Integer length = sKeyGenerator.getLength();
        Date keyDate = sKeyGenerator.getKeyDate();

        StringBuffer result = new StringBuffer();

        if(dateFormat.format(now).equals(dateFormat.format(keyDate))) {

            String num = String.valueOf(sKeyGenerator.getNum());

            result.append(prefix);
            result.append(dateFormat.format(keyDate));

            //补0
            for(int i = 0; i < length - suffix.length() - prefix.length() - num.length() - 8; i++) {
                result.append("0");
            }
            result.append(num);
            result.append(suffix);
        } else {
            sKeyGenerator.setKeyDate(now);
            sKeyGenerator.setNum(1);

            String num = String.valueOf(sKeyGenerator.getNum());

            result.append(prefix);
            result.append(dateFormat.format(keyDate));

            //补0
            for(int i = 0; i < length - suffix.length() - prefix.length() - num.length() - 8; i++) {
                result.append("0");
            }
            result.append(suffix);
        }
        sKeyGenerator.setNum(sKeyGenerator.getNum() + 1);
        sKeyGeneratorDao.update(sKeyGenerator);

        return result.toString();

    }

    public static void main(String[] args) {
        //System.out.println(PkUtil.getPrimaryKeyBatch("R", 10).toString());
        for(String key : PkUtil.getDatePrimaryKeyBatch("R", 10)) {
            System.out.println(key);
        }

    }
}
