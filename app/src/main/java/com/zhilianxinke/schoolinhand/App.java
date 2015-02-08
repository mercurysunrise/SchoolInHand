package com.zhilianxinke.schoolinhand;


import android.app.Application;
import com.zhilianxinke.schoolinhand.util.ImageCacheUtil;


public class App extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
        ImageCacheUtil.init(this);
	}
}

