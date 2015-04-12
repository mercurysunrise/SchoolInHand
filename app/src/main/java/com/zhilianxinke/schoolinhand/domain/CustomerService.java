package com.zhilianxinke.schoolinhand.domain;

import java.io.Serializable;

/**
 * Created by zhjchen on 14-8-5.
 */
public class CustomerService implements Serializable {

    private String userId;
    private String globalNick;
    private String globalIcon;

    public String getGlobalIcon() {
        return globalIcon;
    }

    public void setGlobalIcon(String globalIcon) {
        this.globalIcon = globalIcon;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGlobalNick() {
        return globalNick;
    }

    public void setGlobalNick(String globalNick) {
        this.globalNick = globalNick;
    }



}
