package com.zhilianxinke.schoolinhand.base;

import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.zhilianxinke.schoolinhand.App;
import com.zhilianxinke.schoolinhand.R;
import android.view.View;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by hh on 2015-02-08.
 */
public abstract class BaseActivity extends ActionBarActivity {

    protected final String TAG = getClass().getSimpleName();

    public RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("BaseActivity", TAG);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);// 使得音量键控制媒体声音

        getSupportActionBar().setLogo(R.drawable.app_bar_logo);
        setContentView(setContentViewResId());

        requestQueue = Volley.newRequestQueue(this);
        App.addActivity(this);
    }

    protected <T extends View> T getViewById(int id) {
        return (T) findViewById(id);
    }

    protected abstract int setContentViewResId();

    protected abstract void initView();

    protected abstract void initData();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.removeActivity(this);
    }
}
