package com.zhilianxinke.schoolinhand.modules.autoupdate;

import java.io.Serializable;

/**
 * Created by hh on 2015-03-05.
 */
public class ApkUpdateModel implements Serializable {
    private int versionCode;
    private String versionName;
    private String versionFeature;
    private String updateUrl;


    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionFeature() {
        return versionFeature;
    }

    public void setVersionFeature(String versionFeature) {
        this.versionFeature = versionFeature;
    }

    public String getUpdateUrl() {
        return updateUrl;
    }

    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }
}
