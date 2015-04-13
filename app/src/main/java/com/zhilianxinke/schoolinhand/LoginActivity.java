package com.zhilianxinke.schoolinhand;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.sea_monster.core.exception.BaseException;
import com.sea_monster.core.exception.InternalException;
import com.sea_monster.core.network.AbstractHttpRequest;
import com.zhilianxinke.schoolinhand.base.BaseActivity;
import com.zhilianxinke.schoolinhand.base.BaseApiActivity;
import com.zhilianxinke.schoolinhand.domain.App_CustomModel;
import com.zhilianxinke.schoolinhand.domain.User;
import com.zhilianxinke.schoolinhand.rongyun.RongCloudEvent;
import com.zhilianxinke.schoolinhand.rongyun.ui.LoadingDialog;
import com.zhilianxinke.schoolinhand.rongyun.ui.WinToast;
import com.zhilianxinke.schoolinhand.util.JSONHelper;
import com.zhilianxinke.schoolinhand.util.StaticRes;
import com.zhilianxinke.schoolinhand.util.UpdateManager;

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

    private AbstractHttpRequest<ArrayList<User>> getFriendsHttpRequest;

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
            String url = StaticRes.baseUrl + "/App/login"+"?strName="+username+"&psd="+password;

            JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET,url,null,new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        App_CustomModel app_CustomModel = JSONHelper.parseObject(response,App_CustomModel.class);

                        connectRongService(app_CustomModel);
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                        WinToast.toast(LoginActivity.this, e.getMessage());
                    }
                }
            },new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG,error.getMessage());
                    WinToast.toast(LoginActivity.this,R.string.login_pass_error);
                }
            });
            requestQueue.add(jr);
        }
	}


    /**
     * 连接融云认证服务
     * @param app_CustomModel
     */
    public void connectRongService(final App_CustomModel app_CustomModel){
        try {
            RongIM.connect(app_CustomModel.getUserToken(), new RongIMClient.ConnectCallback() {
                @Override
                public void onSuccess(String userId) {
                    Log.d("LoginActivity", "--------- onSuccess userId----------:" + userId);

                    RongCloudEvent.getInstance().setOtherListener();

                    AppContext.getInstance().setCurrUser(app_CustomModel);

                    MainActivity.actionStart(LoginActivity.this);
                    finish();
                }

                @Override
                public void onError(ErrorCode errorCode) {

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

    public void onCallApiSuccess(AbstractHttpRequest request, Object obj) {

        //获取好友列表接口  返回好友数据  (注：非融云SDK接口，是demo接口)
        if (getFriendsHttpRequest == request) {

//            @SuppressWarnings("unchecked")
//            final ArrayList<RongIMClient.UserInfo> friends = (ArrayList<RongIMClient.UserInfo>) getFriends((ArrayList<User>) obj);
//            if(AppContext.getInstance()!=null)
//                AppContext.getInstance().setFriends(friends);
        }

    }


}
