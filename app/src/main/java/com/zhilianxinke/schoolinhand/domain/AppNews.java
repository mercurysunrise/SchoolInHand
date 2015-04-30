package com.zhilianxinke.schoolinhand.domain;

import java.io.Serializable;
import java.util.Calendar;

public class AppNews implements Serializable{

	private static final long serialVersionUID = 1L;

	private String id;
	private String schoolId;
	private String schoolName;
	private String classId;
	private String className;
	private String authorId;
	private String authorName;
 
	private String title;
	
	private int optIn;
	private String url;
	private String cover;
	private int ownershipType;

	private String image;
	private String content;
	private boolean read;
	private boolean favorite;

	public AppNews() {
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getAuthorId() {
		return authorId;
	}

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getOptIn() {
		return optIn;
	}

	public void setOptIn(int optIn) {
		this.optIn = optIn;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public int getOwnershipType() {
		return ownershipType;
	}

	public void setOwnershipType(int ownershipType) {
		this.ownershipType = ownershipType;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isFavorite() {
		return favorite;
	}

	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}
}
