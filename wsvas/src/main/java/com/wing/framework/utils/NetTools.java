package com.wing.framework.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetTools {

	/**
	 * 
	 * 根据url获取IP地址
	 * 
	 * @param url
	 * @return
	 */
	static public String getIpAddrByUrl(String url) {
		
		try {
			//获取域名+IP地址
			String parseUrl = parseUrl(url);
			if(UrlUtils.isIp(parseUrl)) {
				return parseUrl;
			}
			if(UrlUtils.isIpOrIpPort(parseUrl)) {
				return parseUrl.split(":")[0];
			}
			InetAddress serverIpAddress = InetAddress.getByName(parseUrl);
			
			//取IP地址
			String domainIp = serverIpAddress.toString();
			return domainIp.split("/")[1];
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		};
	    
		return null;
	}
	
	/**
	 * 
	 * 解析url，获取域名
	 * 
	 * @param url
	 * @return
	 */
	static public String parseUrl(String url) {
		UrlSplitInfo urlSplitInfo = UrlUtils.getSplitInfo(url);
		return urlSplitInfo.getHostAddr();
	}
	
	public static void main(String[] args) {
		System.out.println(NetTools.getIpAddrByUrl("http://www.baidu.com"));
	}
}
