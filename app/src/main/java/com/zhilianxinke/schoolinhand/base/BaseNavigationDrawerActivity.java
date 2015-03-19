package com.zhilianxinke.schoolinhand.base;

import android.os.Bundle;
import android.util.Log;

import com.blunderer.materialdesignlibrary.handlers.NavigationDrawerBottomHandler;
import com.blunderer.materialdesignlibrary.handlers.NavigationDrawerTopHandler;
import com.zhilianxinke.schoolinhand.App;

/**
 * Created by hh on 2015-03-18.
 */
public abstract class BaseNavigationDrawerActivity extends com.blunderer.materialdesignlibrary.activities.NavigationDrawerActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("NavigationDrawer", getClass().getSimpleName());
        App.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.removeActivity(this);
    }
}
