package com.zhilianxinke.schoolinhand;


import android.app.Activity;
import android.app.Application;

import com.zhilianxinke.schoolinhand.base.DefaultExceptionHandler;
import com.zhilianxinke.schoolinhand.message.GroupInvitationNotification;
import com.zhilianxinke.schoolinhand.rongyun.RongCloudEvent;
import com.zhilianxinke.schoolinhand.util.ImageCacheUtil;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.AnnotationNotFoundException;


public class App extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
        ImageCacheUtil.init(this);
        // 初始化。
        RongIM.init(this);

        RongCloudEvent.init(this);

        AppContext.init(this);
            //注册自定义消息类型
            RongIM.registerMessageType(GroupInvitationNotification.class);


        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));
    }

    private static List<Activity> activities = new ArrayList<Activity>();

    public static void addActivity(Activity activity){
        activities.add(activity);
    }

    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }

    /**
     * 结束当前应用所有Activity
     */
    public static void finishAllActivities(){
        for (Activity activity : activities){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
        System.exit(0);
    }



}

