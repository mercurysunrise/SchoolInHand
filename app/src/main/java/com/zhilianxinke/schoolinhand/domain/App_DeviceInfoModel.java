package com.zhilianxinke.schoolinhand.domain;

import java.io.Serializable;

public class App_DeviceInfoModel implements Serializable {

	private String pk;
	private String strName;
	private String mac;
	private String schoolName;
	private String address;
	private int publicLevel;
	private String streamUrl1;
	private String streamUrl2;
	
	public String getPk() {
		return pk;
	}
	public void setPk(String pk) {
		this.pk = pk;
	}
	public String getStrName() {
		return strName;
	}
	public void setStrName(String strName) {
		this.strName = strName;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getPublicLevel() {
		return publicLevel;
	}
	public void setPublicLevel(int publicLevel) {
		this.publicLevel = publicLevel;
	}
	public String getStreamUrl1() {
		return streamUrl1;
	}
	public void setStreamUrl1(String streamUrl1) {
		this.streamUrl1 = streamUrl1;
	}
	public String getStreamUrl2() {
		return streamUrl2;
	}
	public void setStreamUrl2(String streamUrl2) {
		this.streamUrl2 = streamUrl2;
	}
	
	
}
