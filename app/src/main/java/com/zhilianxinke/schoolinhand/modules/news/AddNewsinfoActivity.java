package com.zhilianxinke.schoolinhand.modules.news;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.zhilianxinke.schoolinhand.R;
import com.zhilianxinke.schoolinhand.base.BaseActivity;
import com.zhilianxinke.schoolinhand.domain.App_NewsInfoModel;
import com.zhilianxinke.schoolinhand.util.CacheUtils;
import com.zhilianxinke.schoolinhand.util.JSONHelper;
import com.zhilianxinke.schoolinhand.util.StaticRes;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class AddNewsinfoActivity  extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "AddNewsinfoActivity";

    private EditText etNewsTitle;
    private Spinner spNewsType;
    private EditText tmlNewsContent;
    private Button btnAddNewsSubmit;

    App_NewsInfoModel app_NewsInfoModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int setContentViewResId() {
        return R.layout.activity_add_newsinfo;
    }

    @Override
    protected void initView() {
        etNewsTitle = (EditText)findViewById(R.id.etNewsTitle);
        spNewsType = (Spinner)findViewById(R.id.spNewsType);
        tmlNewsContent = (EditText)findViewById(R.id.tmlNewsContent);
        btnAddNewsSubmit = (Button)findViewById(R.id.btnAddNewsSubmit);

        btnAddNewsSubmit.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }


    @Override
    public void onClick(View v) {
        Object obj = spNewsType.getSelectedItem();
        int newsType = "班级".equals(obj)?4:1;
        newsType = "系统".equals(obj)?2:1;
        newsType = "校园".equals(obj)?8:1;


        String title = etNewsTitle.getText().toString();

        String content = tmlNewsContent.getText().toString();

        app_NewsInfoModel = new App_NewsInfoModel();
        app_NewsInfoModel.setTitle(title);
        app_NewsInfoModel.setContent(content);

        new AddNewsAsyncTask().execute();
    }


    private class AddNewsAsyncTask extends AsyncTask<Void, Void, App_NewsInfoModel> {
        @Override
        protected App_NewsInfoModel doInBackground(Void... voids) {

            List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();

//            params.add(new BasicNameValuePair("userId", "111"));
            params.add(new BasicNameValuePair("newsInfo", JSONHelper.toJSON(app_NewsInfoModel)));
            // 对参数编码
            final String param = URLEncodedUtils.format(params, "UTF-8");

            // baseUrl
            final String baseUrl = StaticRes.baseUrl + "/newsInfo/publisByApp";

            final HttpClient httpClient = new DefaultHttpClient();
            try {
                HttpGet postMethod = new HttpGet(baseUrl + "?" + param);

                HttpResponse response = httpClient.execute(postMethod); // 发起GET请求

                String result = EntityUtils.toString(response.getEntity(),
                        "utf-8");
                return  JSONHelper.parseObject(result, App_NewsInfoModel.class);
            } catch (ClientProtocolException e) {
                Log.e(TAG, "ClientProtocolException", e);
            } catch (IOException e) {
                Log.e(TAG, "IOException", e);
            } catch (JSONException e) {
                Log.e(TAG, "JSONException", e);
            }
            return null;
        }

        //这里是对刷新的响应，可以利用addFirst（）和addLast()函数将新加的内容加到LISTView中
        //根据AsyncTask的原理，onPostExecute里的result的值就是doInBackground()的返回值
        @Override
        protected void onPostExecute(App_NewsInfoModel result) {
            //在头部增加新添内容
            app_NewsInfoModel = result;
            super.onPostExecute(result);
            if (app_NewsInfoModel != null){
                AddNewsinfoActivity.this.finish();
            }
        }
    }

}
