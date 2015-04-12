package com.zhilianxinke.schoolinhand.api;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.sea_monster.core.network.AbstractHttpRequest;
import com.sea_monster.core.network.ApiCallback;
import com.sea_monster.core.network.ApiReqeust;
import com.sea_monster.core.network.BaseApi;
import com.sea_monster.core.network.HttpHandler;
import com.zhilianxinke.schoolinhand.domain.App_CustomModel;
import com.zhilianxinke.schoolinhand.domain.User;
import com.zhilianxinke.schoolinhand.parser.GsonArrayParser;
import com.zhilianxinke.schoolinhand.parser.GsonParser;
import com.zhilianxinke.schoolinhand.util.StaticRes;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hh on 2015-04-02.
 */
public class AppApi extends BaseApi {

//    private  static String HOST = "http://119.254.110.79:8080/";

    //客服
//    private final static String DEMO_REG = "reg";
    private final static String APP_LOGIN = "/App/login";
    private final static String APP_FRIENDS = "friends";

    public AppApi(HttpHandler handler, Context context) {
        super(handler, context);
    }


    /**
     * 登录 demo server
     * @param email
     * @param password
     * @param deviceId
     * @param callback
     * @return
     */
    public AbstractHttpRequest<App_CustomModel> login(String email, String password, String deviceId, ApiCallback<App_CustomModel> callback) {

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("strName", email));
        nameValuePairs.add(new BasicNameValuePair("psd", password));
//        nameValuePairs.add(new BasicNameValuePair("deviceid", deviceId));

        String strUrl = StaticRes.baseUrl + APP_LOGIN;

        ApiReqeust<App_CustomModel> apiReqeust = new DefaultApiReqeust<App_CustomModel>(ApiReqeust.GET_METHOD, URI.create(strUrl), nameValuePairs, callback);
        AbstractHttpRequest<App_CustomModel> httpRequest = apiReqeust.obtainRequest(new GsonParser<App_CustomModel>(App_CustomModel.class), null, null);
        handler.executeRequest(httpRequest);

        return httpRequest;
    }

    public AbstractHttpRequest<ArrayList<User>> getFriends(String cookie, ApiCallback<ArrayList<User>> callback) {

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("cookie", cookie));

        ApiReqeust<ArrayList<User>> apiReqeust = new DefaultApiReqeust<ArrayList<User>>(ApiReqeust.POST_METHOD, URI.create( APP_FRIENDS), nameValuePairs, callback);
        AbstractHttpRequest<ArrayList<User>> httpRequest = apiReqeust.obtainRequest(new GsonArrayParser<User>(new TypeToken<ArrayList<User>>(){}), null, null);
        handler.executeRequest(httpRequest);

        return httpRequest;

    }


}
