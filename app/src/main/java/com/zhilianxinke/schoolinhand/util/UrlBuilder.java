package com.zhilianxinke.schoolinhand.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by hh on 2015-04-26.
 */
public class UrlBuilder {

    private static final String TAG = "UrlBuilder";
//    public static String serverUrl = "http://192.168.1.3:9080";

    public static String serverUrl = "http://121.42.146.235:9080";
    public static String serverName = "UUTong365";
//    public static String serverName = "UUTest";

    public static String baseUrl = serverUrl + "/" + serverName;

    public static final String Api_Connect = "/api/connect";
    public static final String Api_lastMyNews = "/api/lastMyNews";
    public static final String Api_lastMyNewsCount = "/api/lastMyNewsCount";
    public static final String Api_lastCorpNews = "/api/lastCorpNews";
    public static final String Api_lastCorpNewsCount = "/api/lastCorpNewsCount";
    public static final String Api_addNews = "/api/addNews";
    public static final String Api_myGroups = "/api/myGroups";



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
        System.out.print("---tupian111---");
        System.out.print(sb);
        return sb.toString();
    }

    public static String thumbnailImageUrl(String url){
        StringBuilder sb = new StringBuilder(serverUrl).append("/").append(serverName);
        sb.append("/upload/");
        sb.append(url.replace("0.png", ""));
        sb.append("thumbnail_0.png");
        System.out.print("---tupian111---");
        System.out.print(url.replace("0.png",""));
        System.out.print(sb);
        return sb.toString();
    }

    /**
     * 转换头像路径
     * @param path
     * @return
     */
    public static Uri portrait(String path){
        Uri uri = null;
        if(path != null){
            uri = Uri.parse(new StringBuilder(serverUrl).append("/").append(serverName).append("/").append("update").append("/").append(path).toString());
        }
        return uri;
    }

}
