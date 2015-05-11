package com.zhilianxinke.schoolinhand;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import com.zhilianxinke.schoolinhand.R;
import com.zhilianxinke.schoolinhand.base.BaseActivity;
import com.zhilianxinke.schoolinhand.rongyun.RongCloudEvent;
import com.zhilianxinke.schoolinhand.rongyun.ui.WinToast;
import com.zhilianxinke.schoolinhand.util.UpdateManager;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

/**欢迎动画activity*/
public class WelcomeActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        App.addActivity(this);

        //系统会为需要启动的activity寻找与当前activity不同的task;
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //创建一个新的线程来显示欢迎动画，指定时间后结束，跳转至指定界面
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    Thread.sleep(1500);
                    //获取应用的上下文，生命周期是整个应用，应用结束才会结束
                    if (AppContext.getAppUser() != null){
                        RongIM.connect(AppContext.getAppUser().getRongToken(), new RongIMClient.ConnectCallback() {
                            @Override
                            public void onSuccess(String userId) {
                                Log.d("LoginActivity", "--------- onSuccess userId----------:" + userId);

                                RongCloudEvent.getInstance().setOtherListener();

                                RongIM.getInstance().setUserInfoAttachedState(true);
                                RongIM.getInstance().setCurrentUserInfo(new UserInfo(userId, null, null));

                                MainActivity.actionStart(WelcomeActivity.this);
                                finish();
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {

                                Log.d("WelcomeActivity", "---------onError ----------:" + errorCode);
                                WinToast.toast(WelcomeActivity.this, R.string.connect_fail);
                            }
                        });

                    }else{
                        final Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    finish();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }).start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.removeActivity(this);
    }

}