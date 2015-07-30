package com.wing.framework.utils;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * String工具类
 * 
 * @author zhongzh
 */
public class StringUtil {

	/**
	 * list转为String数组
	 */
	public static String[] listToStrArray(List list) {

		if (list == null || list.size() == 0)
			return null;

		Iterator iterator = list.iterator();
		String[] strArray = new String[list.size()];
		int i = 0;

		while (iterator.hasNext()) {
			strArray[i] = String.valueOf(iterator.next());
			i++;
		}
		return strArray;
	}
	
	 /**
     * list 转为有分隔符的 string
     * @param element
     * @param separator
     * @return
     */
    public static String listToStr(List element, String separator) {

        StringBuffer returnstr = new StringBuffer("");

        if (element == null) return "";
        if (separator == null) separator = "";

        Iterator it = element.iterator();

        while (it.hasNext()) {
            returnstr.append(String.valueOf(it.next()));
            if (it.hasNext()) returnstr.append(separator);
        }

        return returnstr.toString();
    }

	/**
	 * 验证[某字符串]是否存在于指定符号分隔的字符串中
	 * 
	 * @param str： 【abc,123,www】
	 * @param substr： 【123】
	 * @param sepatator： 【,】
	 * @return
	 */
	public static boolean isExist(String str, String substr, String sepatator) {
		if (str == null || str.trim().equals(""))
			return false;
		if (substr == null || substr.trim().equals(""))
			return false;
		String[] strArr = str.split(sepatator);
		int size = strArr.length;
		for (int i = 0; i < size; i++) {
			if (strArr[i].equals(substr))
				return true;
		}
		return false;
	}
	
	/**
	 * 字符串转换，若是str为null或是"null"则返回""
	 * @param str
	 * @return
	 */
	public static String getString(String str) {
		if (StringUtils.isBlank(str) || str.toLowerCase().equals("null"))
			return "";
		else
			return str;
	}
	
	/**
	 * 字符串转换，若str为空字符串("   ")或str==null，则返回""
	 * @param str
	 * @return
	 */
	public static String blankToString(String str) {
		if(StringUtils.isBlank(str)) {
			return "";
		}else 
			return str.trim();
	}

    /**
     *
     * 过滤全角字符
     *
     * @param str
     * @return
     */
    public static String filterFullWidth(String str) {
        Pattern pattern = Pattern.compile("[\\uFF00-\\uFF0B]|[\\uFF0D-\\uFFFF]");
        Matcher re = pattern.matcher(str);
        str=re.replaceAll("");
        return str;
    }

    /**
     *
     * 过滤特殊字符
     *
     * @param str
     * @return
     */
    public  static String StringFilter(String str)  {

        if(str == null) {
            return null;
        }

        try {
            // 清除掉所有特殊字符
            String regEx = "[`~!@#$%^&*()+=|{}':;',//[//]<>/?~！#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(str);
            return m.replaceAll("").trim();
        } catch (Exception e) {
        }
        return str;

    }

    /**
     *
     * 判断是否存在特殊字符
     *
     * @param str
     * @return
     */
    public static boolean hasSpecialChar(String str) {
        if(str == null) {
            return false;
        }
        String regEx = "[`~!#$%^&*+=|{}':;',//[//]<>/?~！#￥%……&*——+|{}【】‘；：”“’。，、？]|[\\uFF00-\\uFF07]|[\\uFF0A-\\uFF0B]|[\\uFF0D-\\uFFFF]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }
	/**
	 * 
	 * 判断密码是否符合规则
	 * 
	 * @param passwrodToCheck
	 * @return
	 */
	public static boolean checkPassword(String passwrodToCheck){
		Pattern pattern = Pattern.compile("^[@A-Za-z0-9_]*$");
        Matcher re = pattern.matcher(passwrodToCheck);
		return re.find();	
	}
    public static void main(String[] args) {
		System.out.println((int)'（');
        System.out.println((int)'）');
	}

}
