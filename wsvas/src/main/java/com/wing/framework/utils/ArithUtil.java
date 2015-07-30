package com.wing.framework.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 数字计算工具类。
 * @author zhongzh
 */
public class ArithUtil {
	
	// 默认除法运算精度
	private static final int DEF_DIV_SCALE = 10; 
	// 数字转字符串时的整数、小数最大位数
	public static final int MAX_LENGTH_INTEGER = 12;
	public static final int MAX_LENGTH_FRACTION = 12;

	// 这个类不能实例化
	private ArithUtil() {
	}

	/**
	 * 功 能：判断d1是否不大于d2  
	 * 返回值： false:大于;true:不大于
	 */
	public static boolean isDoubleNotGreater(double d1, double d2) {
		boolean bn = false;
		BigDecimal b1 = new BigDecimal(Double.toString(d1));
		BigDecimal b2 = new BigDecimal(Double.toString(d2));

		if (b1.compareTo(b2) <= 0)
			bn = true;
		else
			bn = false;

		return bn;
	}

	/**
	 * 功 能：判断d1是否不小于d2  
	 * 返回值： false:小于 true:不小于
	 */
	public static boolean isDoubleNotLess(double d1, double d2) {
		boolean bn = false;
		BigDecimal b1 = new BigDecimal(Double.toString(d1));
		BigDecimal b2 = new BigDecimal(Double.toString(d2));

		if (b1.compareTo(b2) >= 0)
			bn = true;
		else
			bn = false;

		return bn;
	}

	/**
	 *功 能：比较两个double型数据是否相等
	 * 返回值： false:不等，true:相等
	 */
	public static boolean isDoubleEqual(double d1, double d2) {
		boolean bn = false;
		BigDecimal b1 = new BigDecimal(Double.toString(d1));
		BigDecimal b2 = new BigDecimal(Double.toString(d2));

		if (b1.compareTo(b2) == 0)
			bn = true;
		else
			bn = false;

		return bn;
	}

	/**
	 * 功 能：提供精确的加法运算 
	 */
	public static double add(double d1, double d2) {
		BigDecimal b1 = new BigDecimal(Double.toString(d1));
		BigDecimal b2 = new BigDecimal(Double.toString(d2));
		return b1.add(b2).doubleValue();
	}

	/**
	 * 功 能：提供精确的减法运算 
	 */
	public static double sub(double d1, double d2) {
		BigDecimal b1 = new BigDecimal(Double.toString(d1));
		BigDecimal b2 = new BigDecimal(Double.toString(d2));
		return b1.subtract(b2).doubleValue();
	}

	/**
	 * 功 能：提供精确的乘法运算
	 */
	public static double mul(double d1, double d2) {
		BigDecimal b1 = new BigDecimal(Double.toString(d1));
		BigDecimal b2 = new BigDecimal(Double.toString(d2));
		return b1.multiply(b2).doubleValue();
	}

	/**
	 * 功 能：提供（相对）精确的除法运算。当发生除不尽的情况时， 由scale参数指定精度，以后的数字四舍五入。 
	 * d1： 被除数	d2： 除数	scale ：表示表示需要精确到小数点以后几位
	 */
	public static double div(double d1, double d2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(d1));
		BigDecimal b2 = new BigDecimal(Double.toString(d2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 *功 能：提供（相对）精确的除法运算，当发生除不尽的情况时， 精确到小数点以后10位， 以后的数字四舍五入
	 *d1：被除数 d2：除数
	 */
	public static double div(double d1, double d2) {
		return div(d1, d2, DEF_DIV_SCALE);
	}

	/**
	 *功 能：提供精确的小数位四舍五入处理 
	 *入口参数：d 需要四舍五入的数字 scale 小数点后保留几位
	 */
	public static double round(double d, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Double.toString(d));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 产生金额格式化中使用的规则串Pattern
	 * @param subStr:子串，通常是'#','0'
	 * @param n:子串重复次数
	 * @return 金额格式化的规则Pattern
	 */
	public static String genPattern(String subStr, int n) {
		StringBuffer temp = new StringBuffer();
		while (n > 0) {
			if (subStr.trim().equals("#") && (n == 1)) {
				temp.append("0");
			} else {
				temp.append(subStr);
			}
			n--;
		}
		return temp.toString();
	}

	/**
	 * @param number:待格式化的数
	 * @param intPart:格式化后的整数位数
	 * @param decPart:格式化后的小数位数
	 * @return 格式化后的字符串
	 */
	public static String formatDouble(double number, int intPart, int decPart) {
		return new DecimalFormat(genPattern("#", intPart) + "." + genPattern("0", decPart)).format(number);
	}

	public static String formatDecimal(BigDecimal number, int intPart, int decPart) {
		return new DecimalFormat(genPattern("#", intPart) + "." + genPattern("0", decPart)).format(number);
	}
	
	/**
	 * 整数位和小数位默认为12，格式化后小数位末尾的0去掉
	 */
	public static String formatDecimal(BigDecimal number) {
		String tmp = "";

		tmp = new DecimalFormat(genPattern("#", ArithUtil.MAX_LENGTH_INTEGER) + "." + genPattern("0", ArithUtil.MAX_LENGTH_FRACTION)).format(number).trim();

		int i = tmp.length() - 1;
		for (; i > 0; i--) {
			if (tmp.charAt(i) != '0')
				break;
		}
		return tmp.substring(0, i + 1);
	}

	public static void main(String[] args) {
		
		BigDecimal bd1 = new BigDecimal("123456.987655");
		String b = formatDecimal(bd1, 15, 10);
		System.out.println(b);
		String a = formatDecimal(bd1);
		System.out.println(a);
	}
}
