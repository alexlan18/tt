package com.wing.framework.utils;

/**
 * url描述信息
 * @author zhongzh
 *
 */
public class UrlSplitInfo {
	private String protocol;
	private String hostAddr;
	private String uri;
	private String ext;
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getHostAddr() {
		return hostAddr;
	}
	public void setHostAddr(String hostAddr) {
		this.hostAddr = hostAddr;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getExt() {
		return ext;
	}
	public void setExt(String ext) {
		this.ext = ext;
	}
	
}
