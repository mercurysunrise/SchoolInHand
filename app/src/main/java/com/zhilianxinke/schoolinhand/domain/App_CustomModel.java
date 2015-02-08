package com.zhilianxinke.schoolinhand.domain;

import java.io.Serializable;
import java.util.Calendar;


public class App_CustomModel implements Serializable{

	private String pk;
	private int gender;
	private String strName;
	private String psd;
	private String childrenName;
	private String relation;
	private String phone1;
	private String phone2;
	private String schoolInfoPk;
	private String schoolName;
	private Calendar expirationTime;
	
	public String getPk() {
		return pk;
	}
	public void setPk(String pk) {
		this.pk = pk;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public String getStrName() {
		return strName;
	}
	public void setStrName(String strName) {
		this.strName = strName;
	}
	public String getPsd() {
		return psd;
	}
	public void setPsd(String psd) {
		this.psd = psd;
	}
	public String getChildrenName() {
		return childrenName;
	}
	public void setChildrenName(String childrenName) {
		this.childrenName = childrenName;
	}
	public String getRelation() {
		return relation;
	}
	public void setRelation(String relation) {
		this.relation = relation;
	}
	public String getPhone1() {
		return phone1;
	}
	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}
	public String getPhone2() {
		return phone2;
	}
	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}
	public String getSchoolInfoPk() {
		return schoolInfoPk;
	}
	public void setSchoolInfoPk(String schoolInfoPk) {
		this.schoolInfoPk = schoolInfoPk;
	}
	public Calendar getExpirationTime() {
		return expirationTime;
	}
	public void setExpirationTime(Calendar expirationTime) {
		this.expirationTime = expirationTime;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	
	
}
