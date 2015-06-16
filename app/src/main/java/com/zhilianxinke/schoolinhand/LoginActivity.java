package com.zhilianxinke.schoolinhand;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.zhilianxinke.schoolinhand.base.BaseActivity;
import com.zhilianxinke.schoolinhand.common.FastJsonRequest;
import com.zhilianxinke.schoolinhand.domain.AppGroup;
import com.zhilianxinke.schoolinhand.domain.AppUser;
import com.zhilianxinke.schoolinhand.domain.SdkHttpResult;
import com.zhilianxinke.schoolinhand.domain.User;
import com.zhilianxinke.schoolinhand.rongyun.RongCloudEvent;
import com.zhilianxinke.schoolinhand.rongyun.ui.DeEditTextHolder;
import com.zhilianxinke.schoolinhand.rongyun.ui.LoadingDialog;
import com.zhilianxinke.schoolinhand.rongyun.ui.WinToast;
import com.zhilianxinke.schoolinhand.util.DateUtils;
import com.zhilianxinke.schoolinhand.util.StaticRes;
import com.zhilianxinke.schoolinhand.util.UrlBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;

/**登陆界面activity*/
public class LoginActivity extends BaseActivity implements View.OnClickListener,Handler.Callback,DeEditTextHolder.OnEditTextFocusChangeListener {

	private Button btn_login;
	private EditText et_LoginName;
	private EditText et_Psd;

    private String deviceInfo;
    private Handler mHandler;
    /**
     * 输入用户名删除按钮
     */
    private FrameLayout mFrUserNameDelete;
    /**
     * 输入密码删除按钮
     */
    private FrameLayout mFrPasswordDelete;
    /**
     * logo
     */
    private ImageView mLoginImg;
    /**
     * 软键盘的控制
     */
    private InputMethodManager mSoftManager;
    /**
     * 是否展示title
     */
    private RelativeLayout mIsShowTitle;
    /**
     * 左侧title
     */
    private TextView mLeftTitle;
    /**
     * 右侧title
     */
    private TextView mRightTitle;

    private static final int REQUEST_CODE_REGISTER = 200;
    public static final String INTENT_IMAIL = "intent_email";
    public static final String INTENT_PASSWORD = "intent_password";
    private static final int HANDLER_LOGIN_SUCCESS = 1;
    private static final int HANDLER_LOGIN_FAILURE = 2;
    private static final int HANDLER_LOGIN_HAS_FOCUS = 3;
    private static final int HANDLER_LOGIN_HAS_NO_FOCUS = 4;


    private LoadingDialog mDialog;

    private ImageView mImgBackgroud;
    DeEditTextHolder mEditUserNameEt;
    DeEditTextHolder mEditPassWordEt;

    @Override
    protected int setContentViewResId() {
        return R.layout.login;
    }

    @Override
	protected void initView(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();//隐藏ActionBar
		btn_login = (Button) findViewById(R.id.btn_login);
		et_LoginName = (EditText) findViewById(R.id.et_LoginName);
		et_Psd = (EditText) findViewById(R.id.et_Psd);

        mLoginImg = (ImageView) findViewById(R.id.de_login_logo);
        mSoftManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        mImgBackgroud = (ImageView) findViewById(R.id.de_img_backgroud);
        mFrUserNameDelete = (FrameLayout) findViewById(R.id.fr_username_delete);
        mFrPasswordDelete = (FrameLayout) findViewById(R.id.fr_pass_delete);
        mIsShowTitle = (RelativeLayout) findViewById(R.id.de_merge_rel);
        mLeftTitle = (TextView) findViewById(R.id.de_left);
        mRightTitle = (TextView) findViewById(R.id.de_right);

	}

    @Override
    protected void initData(){

        btn_login.setOnClickListener(this);

        mLeftTitle.setOnClickListener(this);
        mRightTitle.setOnClickListener(this);
        mHandler = new Handler(this);
        mDialog = new LoadingDialog(this);

        mEditUserNameEt = new DeEditTextHolder(et_LoginName, mFrUserNameDelete, null);
        mEditPassWordEt = new DeEditTextHolder(et_Psd, mFrPasswordDelete, null);

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.translate_anim);
                mImgBackgroud.startAnimation(animation);
            }
        });

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                et_LoginName.setOnClickListener(LoginActivity.this);
                et_Psd.setOnClickListener(LoginActivity.this);
                mEditPassWordEt.setmOnEditTextFocusChangeListener(LoginActivity.this);
                mEditUserNameEt.setmOnEditTextFocusChangeListener(LoginActivity.this);
            }
        }, 200);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                mSoftManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                Message mess = Message.obtain();
                mess.what = HANDLER_LOGIN_HAS_NO_FOCUS;
                mHandler.sendMessage(mess);
            }
        }
        return super.onTouchEvent(event);
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        event.getKeyCode();
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
            case KeyEvent.KEYCODE_ESCAPE:
                Message mess = Message.obtain();
                mess.what = HANDLER_LOGIN_HAS_NO_FOCUS;
                mHandler.sendMessage(mess);
                break;
        }
        return super.dispatchKeyEvent(event);
    }


    protected void onPause() {
        super.onPause();
        if (mSoftManager == null) {
            mSoftManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        if (getCurrentFocus() != null) {
            mSoftManager.hideSoftInputFromWindow(getCurrentFocus()
                    .getWindowToken(), 0);// 隐藏软键盘
        }
    }

    @Override
	public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login://登录
                String username = et_LoginName.getEditableText().toString();
                String password = et_Psd.getEditableText().toString();
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    WinToast.toast(LoginActivity.this, "请填写用户名称或密码");
                    return;
                }
                Map<String, String> map = new HashMap(3);
                map.put("id", username);
                map.put("psd", password);
                map.put("device", getLogonDevice());
                String strUrl = UrlBuilder.build(UrlBuilder.Api_Connect, map);
                final String[] jsonResult = {""};
                FastJsonRequest<SdkHttpResult> fastJson = new FastJsonRequest<SdkHttpResult>(strUrl, SdkHttpResult.class,
                        new Response.Listener<SdkHttpResult>() {
                            @Override
                            public void onResponse(SdkHttpResult sdkHttpResult) {
                                if (sdkHttpResult.getCode() == 200) {
                                    jsonResult[0] = sdkHttpResult.getResult();
                                    AppUser appUser = JSON.parseObject(jsonResult[0], AppUser.class);
                                    connectRongService(appUser);
                                } else {
                                    WinToast.toast(LoginActivity.this, R.string.login_pass_error);
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyError e = error;
                        Log.e(TAG, error.getLocalizedMessage());
                        WinToast.toast(LoginActivity.this, "JSON解析错误[" + jsonResult[0] + "]");
                    }
                });
                requestQueue.add(fastJson);

                if (mDialog != null && !mDialog.isShowing()) {
                    mDialog.show();
                }

            case R.id.et_Psd:
                Message mess = Message.obtain();
                mess.what = HANDLER_LOGIN_HAS_FOCUS;
                mHandler.sendMessage(mess);
                break;
        }
	}

    public String getLogonDevice(){
        if (deviceInfo == null){
            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            deviceInfo = tm.getDeviceId() + ";" + tm.getDeviceSoftwareVersion() + ";"+ tm.getLine1Number() + ";" + DateUtils.df.format(Calendar.getInstance().getTime()).toString();
        }
        return deviceInfo;
    }


    /**
     * 连接融云认证服务
     * @param appUser
     */
    public void connectRongService(final AppUser appUser){
        try {
            /**
             * IMKit SDK调用第二步
             *
             * 建立与服务器的连接
             *
             * 详见API
             * http://docs.rongcloud.cn/api/android/imkit/index.html
             */
        final String rongToken = appUser.getRongToken();
            RongIM.connect(rongToken, new RongIMClient.ConnectCallback() {
                @Override
                public void onSuccess(String userId) {
                    Log.d("LoginActivity", "--------- onSuccess userId----------:" + userId);

                    RongCloudEvent.getInstance().setOtherListener();

                    AppContext.setAppUser(appUser);

                    RongIM.getInstance().setUserInfoAttachedState(true);
                    RongIM.getInstance().setCurrentUserInfo(new UserInfo(userId, null, null));

                    syncFriends();
                    syncGroups();
                    mHandler.obtainMessage(HANDLER_LOGIN_SUCCESS).sendToTarget();
                    MainActivity.actionStart(LoginActivity.this);
                    finish();
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Log.d(TAG, "---------onError ----------:" + errorCode);
                    LoginActivity.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if (mDialog != null)
                                mDialog.dismiss();
                            WinToast.toast(LoginActivity.this, R.string.connect_fail);
                        }
                    });
                }
            });
        } catch (Exception e) {
            if (mDialog != null)
                mDialog.dismiss();
        }
    }

    /**
     * 登出
     * @param context
     */
    public static void logout(Context context){
        AppContext.deleteCache();
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
        ((Activity)context).finish();
    }

    public void syncFriends(){
        Map<String,String> map = new HashMap(3);
        map.put("id", AppContext.getAppUser().getId());
        String strUrl = UrlBuilder.build("/api/myFriends",map);
        FastJsonRequest<SdkHttpResult> fastJson=new FastJsonRequest<SdkHttpResult>(strUrl, SdkHttpResult.class,
                new Response.Listener<SdkHttpResult>() {
                    @Override
                    public void onResponse(SdkHttpResult sdkHttpResult) {
                        if (sdkHttpResult.getCode() == 200){
                            List<AppUser> appUsers = JSON.parseArray(sdkHttpResult.getResult(), AppUser.class);
                            ArrayList<UserInfo> friendreslut = new ArrayList<UserInfo>();
                            for (AppUser appUser : appUsers){
                                UserInfo info = new UserInfo(appUser.getId(), appUser.getName(), appUser.getPortrait() == null ? null : Uri.parse(appUser.getPortrait()));
                                friendreslut.add(info);
                            }
                            AppContext.setUserInfos(friendreslut);
                        }else{
                            WinToast.toast(LoginActivity.this,R.string.login_pass_error);
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG,error.getMessage());
                WinToast.toast(LoginActivity.this,error.getMessage());
            }
        });

        requestQueue.add(fastJson);
    }

    public void syncGroups(){
        Map<String,String> map = new HashMap(3);
        map.put("id", AppContext.getAppUser().getId());
        String strUrl = UrlBuilder.build(UrlBuilder.Api_myGroups,map);
        FastJsonRequest<SdkHttpResult> fastJson=new FastJsonRequest<SdkHttpResult>(strUrl, SdkHttpResult.class,
                new Response.Listener<SdkHttpResult>() {
                    @Override
                    public void onResponse(SdkHttpResult sdkHttpResult) {
                        if (sdkHttpResult.getCode() == 200){
                            ArrayList<AppGroup> appGroups = (ArrayList<AppGroup>)JSON.parseArray(sdkHttpResult.getResult(),AppGroup.class);

                            AppContext.setAppGroupList(appGroups);
                        }else{
                            WinToast.toast(LoginActivity.this,R.string.login_pass_error);
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG,error.getMessage());
                WinToast.toast(LoginActivity.this,error.getMessage());
            }
        });

        requestQueue.add(fastJson);
    }

    @Override
    public void onEditTextFocusChange(View v, boolean hasFocus) {
        Message mess = Message.obtain();
        switch (v.getId()) {
            case R.id.et_LoginName:
            case R.id.et_Psd:
                if (hasFocus) {
                    mess.what = HANDLER_LOGIN_HAS_FOCUS;
                }
                mHandler.sendMessage(mess);
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }


    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == HANDLER_LOGIN_FAILURE) {

            if (mDialog != null)
                mDialog.dismiss();
            WinToast.toast(LoginActivity.this, R.string.login_failure);


        } else if (msg.what == HANDLER_LOGIN_SUCCESS) {
            if (mDialog != null)
                mDialog.dismiss();
            WinToast.toast(LoginActivity.this, R.string.login_success);

            startActivity(new Intent(this, MainActivity.class));
            finish();

        } else if (msg.what == HANDLER_LOGIN_HAS_FOCUS) {
            mLoginImg.setVisibility(View.GONE);
//            mRegister.setVisibility(View.GONE);
//            mFogotPassWord.setVisibility(View.GONE);
            mIsShowTitle.setVisibility(View.VISIBLE);
//            mLeftTitle.setText(R.string.app_sign_up);
//            mRightTitle.setText(R.string.app_fogot_password);

        } else if (msg.what == HANDLER_LOGIN_HAS_NO_FOCUS) {
            mLoginImg.setVisibility(View.VISIBLE);
//            mRegister.setVisibility(View.VISIBLE);
//            mFogotPassWord.setVisibility(View.VISIBLE);
            mIsShowTitle.setVisibility(View.GONE);
        }
        return false;
    }
}
