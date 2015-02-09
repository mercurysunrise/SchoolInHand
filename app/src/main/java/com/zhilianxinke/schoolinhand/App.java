package com.zhilianxinke.schoolinhand;


import android.app.Activity;
import android.app.Application;
import com.zhilianxinke.schoolinhand.util.ImageCacheUtil;

import java.util.ArrayList;
import java.util.List;


public class App extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
        ImageCacheUtil.init(this);
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

