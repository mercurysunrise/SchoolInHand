package com.zhilianxinke.schoolinhand;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.alibaba.fastjson.JSON;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.zhilianxinke.schoolinhand.base.BaseActivity;
import com.zhilianxinke.schoolinhand.common.FastJsonRequest;
import com.zhilianxinke.schoolinhand.domain.AppUser;
import com.zhilianxinke.schoolinhand.domain.SdkHttpResult;
import com.zhilianxinke.schoolinhand.domain.User;
import com.zhilianxinke.schoolinhand.rongyun.RongCloudEvent;
import com.zhilianxinke.schoolinhand.rongyun.ui.WinToast;
import com.zhilianxinke.schoolinhand.util.UrlBuilder;

import java.util.ArrayList;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**登陆界面activity*/
public class LoginActivity extends BaseActivity implements OnClickListener {

	private Button btn_login;
	private EditText et_LoginName;
	private EditText et_Psd;

    private String mDeviceId;

    private static final int HANDLER_LOGIN_SUCCESS = 1;
    private static final int HANDLER_LOGIN_FAILURE = 2;
    private static final int REQUEST_CODE_REGISTER = 2001;

//    private AbstractHttpRequest<ArrayList<User>> getFriendsHttpRequest;

    @Override
    protected int setContentViewResId() {
        return R.layout.login;
    }

    @Override
	protected void initView(){
		btn_login = (Button) findViewById(R.id.btn_login);
		et_LoginName = (EditText) findViewById(R.id.et_LoginName);
		et_Psd = (EditText) findViewById(R.id.et_Psd);
	}

    @Override
    protected void initData(){
        btn_login.setOnClickListener(this);
    }

    @Override
	public void onClick(View v) {
        String username = et_LoginName.getEditableText().toString();
        String password = et_Psd.getEditableText().toString();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            WinToast.toast(LoginActivity.this, "请填写用户名称或密码");
            return;
        }

        if(AppContext.getInstance()!=null){
            String url = UrlBuilder.baseUrl + "/user/login"+"?id="+username+"&psd="+password;

            FastJsonRequest<SdkHttpResult> fastJson=new FastJsonRequest<SdkHttpResult>(url, SdkHttpResult.class,
                    new Response.Listener<SdkHttpResult>() {
                        @Override
                        public void onResponse(SdkHttpResult sdkHttpResult) {
                            if (sdkHttpResult.getCode() == 200){
                                AppUser appUser = JSON.parseObject(sdkHttpResult.getResult(),AppUser.class);
                                connectRongService(appUser);
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
	}


    /**
     * 连接融云认证服务
     * @param appUser
     */
    public void connectRongService(final AppUser appUser){
        try {
            RongIM.connect(appUser.getRongToken(), new RongIMClient.ConnectCallback() {
                @Override
                public void onSuccess(String userId) {
                    Log.d("LoginActivity", "--------- onSuccess userId----------:" + userId);

                    RongCloudEvent.getInstance().setOtherListener();

                    AppContext.getInstance().setAppUser(appUser);

                    MainActivity.actionStart(LoginActivity.this);
                    finish();
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                    Log.d(TAG, "---------onError ----------:" + errorCode);
                    LoginActivity.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            WinToast.toast(LoginActivity.this, R.string.connect_fail);
                        }
                    });
                }
            });
        } catch (Exception e) {
            Log.e(TAG,"",e);
        }
    }

//    public void onCallApiSuccess(AbstractHttpRequest request, Object obj) {
//
//        //获取好友列表接口  返回好友数据  (注：非融云SDK接口，是demo接口)
//        if (getFriendsHttpRequest == request) {
//
////            @SuppressWarnings("unchecked")
////            final ArrayList<RongIMClient.UserInfo> friends = (ArrayList<RongIMClient.UserInfo>) getFriends((ArrayList<User>) obj);
////            if(AppContext.getInstance()!=null)
////                AppContext.getInstance().setFriends(friends);
//        }
//
//    }


}
