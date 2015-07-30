package com.wing.framework.mybatis.dialect;

/**
 * 
 * Mybatis 方言
 * 
 * @author panwb
 *
 */
public class Dialect {

	/**
	 * 
	 * 方言类型
	 * 
	 * @author panwb
	 *
	 */
	public enum Type {
		MYSQL,ORACLE
	}

	//类型
	public static Type Type;
	
	public String getLimitString(String sql, int offset, int limit) {
		return sql;
	}
	
	public String getCountString(String sql) {
		return "select count(*) from (" + sql + ") a";
	}
}
