package com.wing.framework.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * url工具类
 * @author zhongzh
 *
 */
public class UrlUtils {
	public static Pattern ipPattern = Pattern
	.compile("^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$");
	public static Pattern ipOrIpPortPattern = Pattern
	.compile("^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})(:\\d{1,5}){0,1}$");
	
	//判断是否为ip，不含端口
	public static boolean isIp(String url){
		Matcher m = ipPattern.matcher(url);
		if (m.matches()) {
			int gcount = m.groupCount();
			for (int i = 1; i <= gcount; i++) {
				int digit = Integer.parseInt(m.group(i));
				if (digit < 0 || digit > 255) {
					return false;
				}
			}
		} else {
			return false;
		}
		return true;
	}
	//判断是否为ip：port
	public static boolean isIpOrIpPort(String url){
		Matcher m = ipOrIpPortPattern.matcher(url);
		return m.find();
	}
	
	//依据url获取UrlSplitInfo对象
	public static UrlSplitInfo getSplitInfo(String url){
		UrlSplitInfo splitInfo=new UrlSplitInfo();
		int protocolIndex=url.indexOf("://");
		if(protocolIndex!=-1){
			splitInfo.setProtocol(url.substring(0, protocolIndex));
		}
		int hostBeginIndex;
		int hostEndIndex=0;
		if(protocolIndex==-1){
			hostBeginIndex=0;
			hostEndIndex=url.indexOf("/");
		}
		else{
			hostBeginIndex=protocolIndex+3;
			hostEndIndex=url.indexOf("/",hostBeginIndex);
		}
		if(hostEndIndex==-1){
			splitInfo.setHostAddr(url.substring(hostBeginIndex));
			splitInfo.setUri("/");
		}
		else{
			splitInfo.setHostAddr(url.substring(hostBeginIndex,hostEndIndex));
			splitInfo.setUri(url.substring(hostEndIndex));
		}
		
		int endIndex=url.indexOf("?");
		
		if(endIndex!=-1){
			url=url.substring(0,endIndex);
		}
		int lastPointIndex=url.lastIndexOf(".");
		int checkIndex=url.lastIndexOf("/");
		if(lastPointIndex!=-1&&lastPointIndex>checkIndex){
			String ext=url.substring(lastPointIndex+1);
			splitInfo.setExt(ext);
		}
		return splitInfo;
	}
	public static void main(String[] args) {
//		String ip = "183.209.62.189            112.3.247.15";
//		boolean isIp = isIp(ip);
//		System.out.println(isIp);
		
	}
}
