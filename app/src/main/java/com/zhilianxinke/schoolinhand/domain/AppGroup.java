package com.zhilianxinke.schoolinhand.domain;

import java.io.Serializable;

/**
 * Created by hh on 2015-05-08.
 */
public class AppGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String pid;
    private String hint;
    private String cover;

    public AppGroup() {
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

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }



}
