package com.zhilianxinke.schoolinhand;

import java.io.IOException;
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
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.zhilianxinke.schoolinhand.base.BaseActivity;
import com.zhilianxinke.schoolinhand.domain.App_CustomModel;
import com.zhilianxinke.schoolinhand.util.JSONHelper;
import com.zhilianxinke.schoolinhand.util.StaticRes;

/**登陆界面activity*/
public class LoginActivity extends BaseActivity implements OnClickListener{

//	private Button btn_login_regist;//注册按钮
	private Button btn_login;
	private EditText et_LoginName;
	private EditText et_Psd;
    private CheckBox cboIsRememberPsd;
	

	public static final int MENU_PWD_BACK = 1;
	public static final int MENU_HELP = 2;
	public static final int MENU_EXIT = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);


		initView();
        if (PreferenceManager.getDefaultSharedPreferences(this).contains("strName")){
            et_LoginName.setText(PreferenceManager.getDefaultSharedPreferences(this).getString("strName",""));
            et_Psd.setText(PreferenceManager.getDefaultSharedPreferences(this).getString("psd",""));
            excuteLogin();
        }
	}
	
	private void initView(){
//		btn_login_regist = (Button) findViewById(R.id.btn_login_regist);
//		btn_login_regist.setOnClickListener(this);
		
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_login.setOnClickListener(this);
		
		et_LoginName = (EditText) findViewById(R.id.et_LoginName);
		et_Psd = (EditText) findViewById(R.id.et_Psd);

        cboIsRememberPsd = (CheckBox)findViewById(R.id.cboIsRememberPsd);
	}
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
//		case R.id.btn_login_regist:
//			Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
//			startActivity(intent);
//			break;
		case R.id.btn_login:
			excuteLogin();
			break;
		}
	}
	
	private void excuteLogin(){
		//客户端检查
		//新线程发送到服务端
		new Thread(runnable).start();
	}
	
	private Handler handler = new Handler(){
	    @Override
	    public void handleMessage(Message msg) {
	        super.handleMessage(msg);
	        Bundle data = msg.getData();
	        String val = data.getString("value");
	    }
	    Throwable a = new Throwable();
	};
	 
	Runnable runnable = new Runnable(){
	    @Override
	    public void run() {
	        // TODO: http request.
	        Message msg = new Message();
	        Bundle data = new Bundle();
	        data.putString("value",excuteRequest());
	        msg.setData(data);
	        handler.sendMessage(msg);
	    }
	};
	
	public String excuteRequest() {
		//先将参数放入List，再对参数进行URL编码
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		
		params.add(new BasicNameValuePair("strName", et_LoginName.getText().toString()));
		params.add(new BasicNameValuePair("psd", et_Psd.getText().toString()));

		//对参数编码
		String param = URLEncodedUtils.format(params, "UTF-8");
		
		String baseUrl = StaticRes.baseUrl + "/customInfo/appLogin";
		//将URL与参数拼接
		HttpGet getMethod = new HttpGet(baseUrl + "?" + param);
					
		HttpClient httpClient = new DefaultHttpClient();

		try {
		    HttpResponse response = httpClient.execute(getMethod); //发起GET请求

		    String result = EntityUtils.toString(response.getEntity(), "utf-8");
		    StaticRes.currCustom = JSONHelper.parseObject(result, App_CustomModel.class);
		    
		    if (StaticRes.currCustom != null) {
                if (cboIsRememberPsd.isChecked()){
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
                    editor.putString("strName", et_LoginName.getText().toString());
                    editor.putString("psd",et_Psd.getText().toString());
                    editor.commit();
                }
		    	startActivity(new Intent(LoginActivity.this,MainActivity.class));
			}else {
//				Toast.makeText(getApplicationContext(), "登录信息错误，请重新输入", Toast.LENGTH_SHORT).show();
			}
		    

		} catch (ClientProtocolException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		} catch (IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return baseUrl;
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {//创建系统功能菜单
		// TODO Auto-generated method stub
		menu.add(0, MENU_PWD_BACK, 1, "密码找回").setIcon(R.drawable.menu_findkey);
		menu.add(0,MENU_HELP,2,"帮助").setIcon(R.drawable.menu_setting);
		menu.add(0, MENU_EXIT, 3, "退出").setIcon(R.drawable.menu_exit);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case MENU_PWD_BACK:
			break;
		case MENU_HELP:
			break;
		case MENU_EXIT:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	

}
