package com.wing.framework.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式匹配工具
 * @author zhongzh
 *	Date:2013-5-9
 * 	Time:上午11:24:51
 */
public class RegexUtils {
	
	//匹配单个email
	public static boolean matchEmail(String src) {
		String regex = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
		Matcher matcher = Pattern.compile(regex).matcher(src);
		return matcher.find();
	}
	
	
	public static void main(String[] args) {
		String src = "zzz@163.com";
		boolean matchEmail = matchEmail(src);
		System.out.println(matchEmail);
	}
}
