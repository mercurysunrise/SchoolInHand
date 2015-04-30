package com.zhilianxinke.schoolinhand.api;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhilianxinke.schoolinhand.domain.AppUser;
import com.zhilianxinke.schoolinhand.domain.User;
import com.zhilianxinke.schoolinhand.parser.GsonArrayParser;
import com.zhilianxinke.schoolinhand.parser.GsonParser;
import com.zhilianxinke.schoolinhand.util.StaticRes;
import com.zhilianxinke.schoolinhand.util.UrlBuilder;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import me.add1.network.AbstractHttpRequest;
import me.add1.network.ApiCallback;
import me.add1.network.ApiReqeust;
import me.add1.network.BaseApi;
import me.add1.network.HttpHandler;
import me.add1.network.NetworkManager;

/**
 * Created by hh on 2015-04-02.
 */
public class AppApi extends BaseApi {

//    private  static String HOST = "http://119.254.110.79:8080/";

    //客服
//    private final static String DEMO_REG = "reg";
    private final static String APP_LOGIN = "/App/login";
    private final static String APP_FRIENDS = "friends";

    public AppApi(NetworkManager manager, Context context) {
        super(manager, context);
    }

//    public AppApi(HttpHandler handler, Context context) {
//        super(handler, context);
//    }




//    public AbstractHttpRequest<ArrayList<User>> getFriends(String cookie, ApiCallback<ArrayList<User>> callback) {
//
//        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//        nameValuePairs.add(new BasicNameValuePair("cookie", cookie));
//
//        ApiReqeust<ArrayList<User>> apiReqeust = new DefaultApiReqeust<ArrayList<User>>(ApiReqeust.POST_METHOD, URI.create( APP_FRIENDS), nameValuePairs, callback);
//        AbstractHttpRequest<ArrayList<User>> httpRequest = apiReqeust.obtainRequest(new GsonArrayParser<User>(new TypeToken<ArrayList<User>>(){}), null, null);
//        handler.executeRequest(httpRequest);
//
//        return httpRequest;
//
//    }


}
