package com.zhilianxinke.schoolinhand.base;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.zhilianxinke.schoolinhand.App;
import com.zhilianxinke.schoolinhand.AppContext;

import android.support.v4.app.FragmentActivity;
import android.view.View;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by hh on 2015-02-08.
 */
public abstract class BaseActivity extends FragmentActivity {

    protected final String TAG = getClass().getSimpleName();
    //当前应用程序token
//    public static String userToken = null;

    protected RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("BaseActivity", TAG);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);// 使得音量键控制媒体声音

        setContentView(setContentViewResId());

        initView();
        initData();

        requestQueue = Volley.newRequestQueue(this);
        App.addActivity(this);
    }

    protected <T extends View> T getViewById(int id) {
        return (T) findViewById(id);
    }

    protected abstract int setContentViewResId();

    protected abstract void initView();

    protected abstract void initData();


    public void onResume() {
        super.onResume();
//        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
//        MobclickAgent.onPause(this);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

//        if (RongIM.getInstance() == null) {
//            if (savedInstanceState.containsKey("RONG_TOKEN")) {
//                userToken = AppContext.getInstance().getSharedPreferences().getString("LOGIN_TOKEN", null);
//                reconnect(userToken);
//            }
//        }
    }


    @Override
    protected void onSaveInstanceState(Bundle bundle) {

//        userToken = AppContext.getInstance().getSharedPreferences().getString("LOGIN_TOKEN", null);
//        bundle.putString("RONG_TOKEN", userToken);
//        super.onSaveInstanceState(bundle);
    }

    private void reconnect(String token) {
        try {
            RongIM.connect(token, new RongIMClient.ConnectCallback() {
                @Override
                public void onSuccess(String userId) {
                    Log.e("ddd","----------- BAse onSuccess:");
                }

                @Override
                public void onError(final ErrorCode errorCode) {
                    Log.e("ddd","----------- BAse errorCode:");
                }
            });
        } catch (Exception e) {
            Log.e("Error",e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.removeActivity(this);
    }
}
