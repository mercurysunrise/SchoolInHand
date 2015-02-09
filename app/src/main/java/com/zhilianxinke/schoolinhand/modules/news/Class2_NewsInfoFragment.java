package com.zhilianxinke.schoolinhand.modules.news;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zhilianxinke.schoolinhand.NewsInfoActivity;
import com.zhilianxinke.schoolinhand.R;
import com.zhilianxinke.schoolinhand.domain.App_NewsInfoModel;
import com.zhilianxinke.schoolinhand.modules.news.adapters.NewsAdapter;
import com.zhilianxinke.schoolinhand.util.JSONHelper;
import com.zhilianxinke.schoolinhand.util.StaticRes;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Class2_NewsInfoFragment extends Fragment {


    public Class2_NewsInfoFragment() {
        // Required empty public constructor
    }

    private View view;

    private static List<App_NewsInfoModel> _app_newsInfoModels;

    private PullToRefreshListView mPullRefreshListView;
    private NewsAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_class2__news_info, container, false);

//        mPullRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pull_to_refreshList4News);
//
//        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
//            @Override
//            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
//                String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
//                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
//
//                // Update the LastUpdatedLabel
//                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
//
//                // Do work to refresh the list here.
//                new GetDataTask().execute();
//            }
//        });
//        initView();

        return view;
    }

    public void initView() {

        List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();

        params.add(new BasicNameValuePair("pk", StaticRes.currCustom.getPk()));
        // 对参数编码
        final String param = URLEncodedUtils.format(params, "UTF-8");

        // baseUrl
        final String baseUrl = StaticRes.baseUrl + "/newsInfo/appNewsInfo";

        if (mAdapter == null) {
            final HttpClient httpClient = new DefaultHttpClient();

            new Thread() {
                public void run() {
                    try {
                        HttpGet getMethod = new HttpGet(baseUrl + "?" + param);
                        HttpResponse response = httpClient.execute(getMethod); // 发起GET请求

                        String result = EntityUtils.toString(response.getEntity(),"utf-8");
                        _app_newsInfoModels = (LinkedList<App_NewsInfoModel>) JSONHelper
                                .parseCollection(result, List.class,
                                        App_NewsInfoModel.class);

                        handler.post(runnable);
                    } catch (ClientProtocolException e) {
                        Log.e("", "ClientProtocolException", e);
                    } catch (IOException e) {
                        Log.e("", "IOException", e);
                    } catch (JSONException e) {
                        Log.e("", "JSONException", e);
                    }
                }
            }.start();
        }else {
//            refreshList();
        }

    }

    private Handler handler = new Handler();

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // 更新界面
            mAdapter = new NewsAdapter(getActivity(), R.layout.news_row, _app_newsInfoModels);
            ListView actualListView = mPullRefreshListView.getRefreshableView();
            actualListView.setAdapter(mAdapter);

            actualListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Intent intent = new Intent(getActivity(),
                            NewsInfoActivity.class);
                    App_NewsInfoModel app_NewsInfoModel = _app_newsInfoModels
                            .get(position);
                    intent.putExtra("app_NewsInfoModel", app_NewsInfoModel);
                    startActivity(intent);
                    Log.d("点击", "" + position);
                }
            });
        }


    };



    private class GetDataTask extends AsyncTask<Void, Void, List<App_NewsInfoModel>> {

        //后台处理部分
        @Override
        protected List<App_NewsInfoModel> doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
//            String str="Added after refresh...I add";

            List<App_NewsInfoModel> resultList = new ArrayList<App_NewsInfoModel>();

            List<BasicNameValuePair> httpParams = new LinkedList<BasicNameValuePair>();

            httpParams.add(new BasicNameValuePair("pk", StaticRes.currCustom.getPk()));
            // 对参数编码
            final String param = URLEncodedUtils.format(httpParams, "UTF-8");

            // baseUrl
            final String baseUrl = StaticRes.baseUrl + "/newsInfo/appNewsInfo";

            final HttpClient httpClient = new DefaultHttpClient();

            try {
                HttpGet getMethod = new HttpGet(baseUrl + "?" + param);
                HttpResponse response = httpClient.execute(getMethod); // 发起GET请求

                String result = EntityUtils.toString(response.getEntity(),
                        "utf-8");
                resultList = (List<App_NewsInfoModel>) JSONHelper
                        .parseCollection(result, List.class,
                                App_NewsInfoModel.class);
            } catch (ClientProtocolException e) {
                Log.e("", "ClientProtocolException", e);
            } catch (IOException e) {
                Log.e("", "IOException", e);
            } catch (JSONException e) {
                Log.e("", "JSONException", e);
            }

            return resultList;
        }

        //这里是对刷新的响应，可以利用addFirst（）和addLast()函数将新加的内容加到LISTView中
        //根据AsyncTask的原理，onPostExecute里的result的值就是doInBackground()的返回值
        @Override
        protected void onPostExecute(List<App_NewsInfoModel> result) {
            //在头部增加新添内容
//            for(App_NewsInfoModel item : result){
//                _app_newsInfoModels.addFirst(item);
//            }

            //通知程序数据集已经改变，如果不做通知，那么将不会刷新mListItems的集合
            mAdapter.notifyDataSetChanged();
            // Call onRefreshComplete when the list has been refreshed.
            mPullRefreshListView.onRefreshComplete();

            super.onPostExecute(result);
        }
    }

}
