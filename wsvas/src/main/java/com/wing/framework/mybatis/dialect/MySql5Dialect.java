package com.wing.framework.mybatis.dialect;

/**
 * 
 * Mysql 方言
 * 
 * @author panwb
 *
 */
public class MySql5Dialect extends Dialect {

	/**
	 * 
	 * 获取分页SQL
	 * 
	 * @param sql
	 * @param offset
	 * @param limit
	 * @return
	 */
	public String getLimitString(String sql, int offset, int limit) {

		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(sql).append(" limit ").append(offset).append(" , ").append(limit);

		return stringBuffer.toString();
	}
}
