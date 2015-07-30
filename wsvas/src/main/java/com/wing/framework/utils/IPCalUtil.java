package com.wing.framework.utils;

import org.apache.commons.net.util.SubnetUtils;
import org.apache.commons.net.util.SubnetUtils.SubnetInfo;

/**
 * ip工具类
 * @author zhongzh
 *
 */
public class IPCalUtil {
	public static final long[] ipWeights={1,256,256*256,256*256*256};
	
	/**
	 * 获取ip的整数值表示，值等同数据库中的int_aton(ip)
	 * @param ip
	 * @return
	 */
	public static long calIp(String ip){
		String[] ipSplits=ip.split("\\.");
		long value=Long.valueOf(ipSplits[3]);
		for(int i=2;i>=0;i--){
			value+=Long.valueOf(ipSplits[i])*ipWeights[3-i];
		}
		return value;
	}
	
	//暂时用calIp  calIp2为改造的方法
	public static long calIp2(String ip){
		String[] ipSplits=ip.split("\\.");
		long value=0;
		for(int i=0;i<=3;i++){
			value+=Long.valueOf(ipSplits[3-i])*ipWeights[i];
		}
		return value;
	}
	
	
	//zhongzh add start
	
	//依据ip/network（掩码位数或掩码），求出ip对应的起始地址和结束地址
	public static String[] getStartAndEndIp(String ipmask) throws Exception {
		SubnetUtils subnetUtils = null;
		String[] split = ipmask.split("/");
		String ip = split[0];
		String mask = split[1];
		if(mask.contains(".")) {
			subnetUtils = new SubnetUtils(ip, mask);
		}else {
			subnetUtils = new SubnetUtils(ipmask);
		}
		SubnetInfo info = subnetUtils.getInfo();
		String lowAddress = info.getLowAddress();
		String highAddress = info.getHighAddress();
		String[] ips = new String[]{lowAddress,highAddress};
		return ips;
	} 
	
	
	//去除ip的端口号
	public static String removeIpPort(String ip) {
		if(ip.contains(":")) {
			String[] split = ip.split(":");
			ip = split[0];
		}
		return ip;
	}
	//zhongzh add end
	
	public static void main(String[] args) {
//		System.out.println(IPCalUtil.calIp("120.198.232.145"));
//		System.out.println(IPCalUtil.calIp2("120.198.232.145"));
		try {
			String[] ips = getStartAndEndIp("10.220.0.0/15");
			System.out.println("ip size:"+ips.length);
			for (String string : ips) {
				System.out.println(string);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
