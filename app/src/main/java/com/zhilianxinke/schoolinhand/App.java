package com.zhilianxinke.schoolinhand;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.zhilianxinke.schoolinhand.base.DefaultExceptionHandler;
import com.zhilianxinke.schoolinhand.domain.AppNews;
import com.zhilianxinke.schoolinhand.message.GroupInvitationNotification;
import com.zhilianxinke.schoolinhand.rongyun.RongCloudEvent;
import com.zhilianxinke.schoolinhand.rongyun.message.DeAgreedFriendRequestMessage;
import com.zhilianxinke.schoolinhand.rongyun.message.DeContactNotificationMessageProvider;
import com.zhilianxinke.schoolinhand.util.CacheUtils;
import com.zhilianxinke.schoolinhand.util.ImageCacheUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.AnnotationNotFoundException;


public class App extends Application {

    public static Map<String,LinkedList<AppNews>> newsData = new HashMap<>(3);

    public static App current;
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


                //注册消息类型的时候判断当前的进程是否在主进程
                if ("com.zhilianxinke.schoolinhand".equals(getCurProcessName(getApplicationContext()))) {
                    try {
                        //注册自定义消息,注册完消息后可以收到自定义消息
                        RongIM.registerMessageType(DeAgreedFriendRequestMessage.class);
                        //注册消息模板，注册完消息模板可以在会话列表上展示
                RongIM.registerMessageTemplate(new DeContactNotificationMessageProvider());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        current = this;
        initNewsData();
    }

    public static void initNewsData(){
        String[] newsTag = new String[]{"最新","推荐","收藏"};
        for(String tag : newsTag){
            LinkedList<AppNews> list = new LinkedList<>();
            if (AppContext.getAppUser() != null){
                String dataTag= AppContext.getAppUser().getId() + tag + "Data";
                if(CacheUtils.isExistDataCache(current, dataTag)){
                    list = (LinkedList<AppNews>)CacheUtils.readObject(current,dataTag);
                }
            }
            newsData.put(tag,list);
        }
    }

    public static void cacheSave(String tag){
        String dataTag= AppContext.getAppUser().getId() + tag + "Data";
        CacheUtils.saveObject(current,newsData.get(tag),dataTag);
    }

    /**
     * 获得当前进程号
     *
     * @param context
     * @return
     */
    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    private static List<Activity> activities = new ArrayList<Activity>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    /**
     * 结束当前应用所有Activity
     */
    public static void finishAllActivities() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        System.exit(0);
    }


}

