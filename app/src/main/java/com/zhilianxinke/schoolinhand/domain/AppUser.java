package com.zhilianxinke.schoolinhand.domain;

import java.io.Serializable;
import java.util.Calendar;

/**
 * 移动端用户对象
 * @author hh
 *
 */
public class AppUser implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String id;
	private String name;
	private String nickName;
	private int gender;
	private int enumState;
	private String portrait;
	private String phone1;
	private String phone2;
	private String email;
	private Calendar registerTime;
	private String address;
	private String identity;
	private String telephone;
	private String rongToken;
	
	public AppUser() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public int getEnumState() {
		return enumState;
	}
	public void setEnumState(int enumState) {
		this.enumState = enumState;
	}
	public String getPortrait() {
		return portrait;
	}
	public void setPortrait(String portrait) {
		this.portrait = portrait;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Calendar getRegisterTime() {
		return registerTime;
	}
	public void setRegisterTime(Calendar registerTime) {
		this.registerTime = registerTime;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getIdentity() {
		return identity;
	}
	public void setIdentity(String identity) {
		this.identity = identity;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getRongToken() {
		return rongToken;
	}
	public void setRongToken(String rongToken) {
		this.rongToken = rongToken;
	}
	
	
}
