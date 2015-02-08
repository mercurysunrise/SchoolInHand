package com.zhilianxinke.schoolinhand.modules.autoupdate;

import org.json.JSONObject;
/**
 * Created by hh on 2015-02-02.
 */
public interface IUpdateObject {
    int getVersionCode();//返回版本号
    String getFeatures();//返回更新描述
    String getApkUrl();//返回新apk包的下载地址
    int getApkSize();//返回新apk的大小

    void  parseJSONObject(JSONObject object);//解析JSON数据
    boolean isInitial();//是否有解析上述JSON
}
