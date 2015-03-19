package com.zhilianxinke.schoolinhand;


import android.app.Activity;
import android.app.Application;
import android.util.Log;

import com.zhilianxinke.schoolinhand.util.ImageCacheUtil;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;


public class App extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
        ImageCacheUtil.init(this);
        // 初始化。
        RongIM.init(this);

        // 从您的应用服务器请求，以获取 Token。在本示例中我们直接在下面 hardcode 给 token 赋值。
        // String token = getTokenFromAppServer();

        // 此处直接 hardcode 给 token 赋值，请替换为您自己的 Token。
        String token = "BigGgjNAWjWsM5jz7I7eiWdOpn/59z4L0dPvIU0YjucoQ+tuy94Eqt2Kf9AkIi6cS76R0Tfr0EiWpuZx4Jreew==";

        // 连接融云服务器。
        try {
            RongIM.connect(token, new RongIMClient.ConnectCallback() {

                @Override
                public void onSuccess(String s) {
                    // 此处处理连接成功。
                    Log.d("Connect:", "Login successfully.");
                }

                @Override
                public void onError(ErrorCode errorCode) {
                    // 此处处理连接错误。
                    Log.d("Connect:", "Login failed.");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<Activity> activities = new ArrayList<Activity>();

    public static void addActivity(Activity activity){
        activities.add(activity);
    }

    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }

    public static void finishAll(){
        for (Activity activity : activities){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}

