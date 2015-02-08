package com.zhilianxinke.schoolinhand.base;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.zhilianxinke.schoolinhand.util.ActivityCollector;

/**
 * Created by hh on 2015-02-08.
 */
public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("BaseActivity",getClass().getSimpleName());
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
