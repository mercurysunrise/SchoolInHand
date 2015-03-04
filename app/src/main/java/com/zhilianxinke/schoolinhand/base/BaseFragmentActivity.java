package com.zhilianxinke.schoolinhand.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.zhilianxinke.schoolinhand.App;

/**
 * Created by hh on 2015-03-01.
 */
public class BaseFragmentActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("BaseFragmentActivity",getClass().getSimpleName());
        App.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.removeActivity(this);
    }

}
