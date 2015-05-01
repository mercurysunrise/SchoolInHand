package com.zhilianxinke.schoolinhand.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by hh on 2015-04-26.
 */
public class UrlBuilder {

    private static final String TAG = "UrlBuilder";
//    public static String serverUrl = "http://192.168.1.105:9080";

    //    public static String serverName = "DMService";
    public static String serverUrl = "http://121.42.146.235:9080";
//    public static String serverName = "DeviceManagement";
    public static String serverName = "UUTong365";

    public static String baseUrl = serverUrl + "/" + serverName;
//    public static String updateJson = serverUrl + "/appUpdate/schoolInHand.json";
//    public static String updateUrl = serverUrl + "/appUpdate/app-debug.apk";

    public final static String URL_TESTHLS = "http://115.28.171.84/hls/dh0B319/playlist.m3u8";

    /**
     * 构建请求url
     * @param route
     * @param params
     * @return
     */
    public static String build(String route,Map<String,String> params){
        StringBuilder sb = new StringBuilder(serverUrl).append("/").append(serverName);
        sb.append(route);
        if (params != null && params.size() > 0){
            int index = 0;
            for (String key : params.keySet()) {
                String mark = index++==0?"?":"&";
                try {
                    String value = URLEncoder.encode(params.get(key), "UTF-8");
                    sb.append(mark).append(key).append("=").append(value);
                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG,e.getMessage());
                }
            }
        }
        return sb.toString();
    }

    public static String buildImageUrl(String url){
        StringBuilder sb = new StringBuilder(serverUrl).append("/").append(serverName);
        sb.append("/upload/");
        sb.append(url);

        return sb.toString();
    }

}
