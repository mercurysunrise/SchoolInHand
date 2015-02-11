package com.zhilianxinke.schoolinhand.modules.news;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
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
import java.util.LinkedList;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * Created by hh on 2015-02-09.
 */
public class Normal_NewsInfoFragment extends Fragment {

    private static String TAG = "Normal_NewsInfoFragment";

    private String title;
    public void setTitle(String title){
        this.title= title;
    }
    private static LinkedList<App_NewsInfoModel> _app_newsInfoModels;
    private View view;
    private PullToRefreshListView mPullRefreshListView;
    private NewsAdapter mAdapter;

    public Normal_NewsInfoFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG,"onCreateView+"+title);
        view = inflater.inflate(R.layout.fragment_class2__news_info, container, false);

        mPullRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pull_to_refreshList4News);
        _app_newsInfoModels = new LinkedList<App_NewsInfoModel>();

        //获取缓存数据
//        if (savedInstanceState != null && savedInstanceState.containsKey(title)){
//            String strContent = savedInstanceState.getString(title);
//            try {
//                List<App_NewsInfoModel> app_newsInfoModels = (List<App_NewsInfoModel>) JSONHelper
//                        .parseCollection(strContent, List.class,
//                                App_NewsInfoModel.class);
//                if (app_newsInfoModels != null){
//                    _app_newsInfoModels.addAll(app_newsInfoModels);
//                }
//            } catch (JSONException e) {
//                Log.e(TAG,"获取缓存数据反序列化异常",e);
//            }
//        }
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                // Do work to refresh the list here.
                new QueryNewsAsyncTask().execute();
            }
        });

        mAdapter = new NewsAdapter(getActivity(), R.layout.news_row, _app_newsInfoModels);
        mPullRefreshListView.setAdapter(mAdapter);
//        ListView actualListView = mPullRefreshListView.getRefreshableView();
//        actualListView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private class QueryNewsAsyncTask extends AsyncTask<Void, Void, List<App_NewsInfoModel>> {
        @Override
        protected List<App_NewsInfoModel> doInBackground(Void... voids) {

            Log.i("TAG",title+"Fragment 发起查询请求");
            List<App_NewsInfoModel> app_newsInfoModels = new ArrayList<App_NewsInfoModel>();

            List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();

            params.add(new BasicNameValuePair("pk", StaticRes.currCustom.getPk()));
            // 对参数编码
            final String param = URLEncodedUtils.format(params, "UTF-8");

            // baseUrl
            final String baseUrl = StaticRes.baseUrl + "/newsInfo/appNewsInfo";

            final HttpClient httpClient = new DefaultHttpClient();
            try {
                HttpGet getMethod = new HttpGet(baseUrl + "?" + param);
                HttpResponse response = httpClient.execute(getMethod); // 发起GET请求

                String result = EntityUtils.toString(response.getEntity(),
                        "utf-8");
                app_newsInfoModels = (List<App_NewsInfoModel>) JSONHelper
                        .parseCollection(result, List.class,
                                App_NewsInfoModel.class);
                Log.i("TAG",title+"Fragment 获得查询结果="+app_newsInfoModels.size());
            } catch (ClientProtocolException e) {
                Log.e(TAG, "ClientProtocolException", e);
            } catch (IOException e) {
                Log.e(TAG, "IOException", e);
            } catch (JSONException e) {
                Log.e(TAG, "JSONException", e);
            }

            return app_newsInfoModels;
        }

        //这里是对刷新的响应，可以利用addFirst（）和addLast()函数将新加的内容加到LISTView中
        //根据AsyncTask的原理，onPostExecute里的result的值就是doInBackground()的返回值
        @Override
        protected void onPostExecute(List<App_NewsInfoModel> result) {
            //在头部增加新添内容
            for(App_NewsInfoModel item : result){
                if (item.getNewsTypeName().contains(title)){
                    Log.i(TAG,"包含"+title+"消息");
                    _app_newsInfoModels.addFirst(item);
                }
            }
//            for (int i=0;i<5;i++){
//                App_NewsInfoModel newsInfoModel = new App_NewsInfoModel();
//                newsInfoModel.setTitle(title + "公告标题"+i);
//                newsInfoModel.setPublicUserName(title+"user"+i);
//                newsInfoModel.setStrPublicTime("2015-02-09 00:00:00");
//                _app_newsInfoModels.add(newsInfoModel);
//            }

            //通知程序数据集已经改变，如果不做通知，那么将不会刷新mListItems的集合
            mAdapter.notifyDataSetChanged();
            // Call onRefreshComplete when the list has been refreshed.
            mPullRefreshListView.onRefreshComplete();
            Log.i(TAG,"更新列表数据"+title);
            super.onPostExecute(result);
        }
    }


}
