package com.rwb.entity;

import java.util.Date;

/**
 * 
* @ClassName: Info
* @Description: 来访者信息
* @author ruwenbo
* @date 2017年10月21日 下午9:09:37
*
 */
public class Info {

	private int iid;
	
	private String requestUrl;
	
	private String contextPath;
	
	private String remoteAddr;
	
	private Date accessTime;

	public int getIid() {
		return iid;
	}

	public void setIid(int iid) {
		this.iid = iid;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public String getRemoteAddr() {
		return remoteAddr;
	}

	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}

	public Date getAccessTime() {
		return accessTime;
	}

	public void setAccessTime(Date accessTime) {
		this.accessTime = accessTime;
	}

	@Override
	public String toString() {
		return "Info [iid=" + iid + ", requestUrl=" + requestUrl
				+ ", contextPath=" + contextPath + ", remoteAddr=" + remoteAddr
				+ ", accessTime=" + accessTime + "]";
	}
	
	
}
