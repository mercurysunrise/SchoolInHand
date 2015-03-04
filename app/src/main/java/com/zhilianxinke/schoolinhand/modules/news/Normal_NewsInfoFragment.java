package com.zhilianxinke.schoolinhand.modules.news;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.zhilianxinke.schoolinhand.NewsInfoActivity;
import com.zhilianxinke.schoolinhand.R;
import com.zhilianxinke.schoolinhand.base.BaseListViewFragment;
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
public class Normal_NewsInfoFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private static String TAG = "Normal_NewsInfoFragment";

    private SwipeRefreshLayout _mSwipeRefreshLayout;
    private ListView _lv_list;

    private View _inflatedView;

    private String tag;
    private int rowContainerId;
    private LinkedList<App_NewsInfoModel> _dataList;
    private NewsAdapter _newsAdapter;

    public Normal_NewsInfoFragment(){

    }

    public void setTitle(String strTitle){
        this.tag = strTitle;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView+" + tag);
        _inflatedView = inflater.inflate(R.layout.fragment_class2__news_info, container, false);

        _mSwipeRefreshLayout = (SwipeRefreshLayout) _inflatedView.findViewById(R.id.swipe_refresh);
        _mSwipeRefreshLayout.setColorSchemeResources(getPullToRefreshColorResources());
        _mSwipeRefreshLayout.setOnRefreshListener(this);

        _lv_list = (ListView) _inflatedView.findViewById(R.id.lv_list);
        _dataList = new LinkedList<App_NewsInfoModel>();
//        mPullRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent = new Intent(getActivity(),NewsInfoActivity.class);
//                App_NewsInfoModel app_NewsInfoModel = _app_newsInfoModels.get(i);
//                intent.putExtra("app_NewsInfoModel", app_NewsInfoModel);
//                startActivity(intent);
//                Log.d("点击", "" + i);
//            }
//        });

        for (int i = 0; i < 10; i++) {
            App_NewsInfoModel newsInfoModel = new App_NewsInfoModel();
            newsInfoModel.setTitle(tag + "测试公告标题" + i);
            newsInfoModel.setPublicUserName(tag + "user" + i);
            newsInfoModel.setStrPublicTime("2015-02-09 00:00:00");
            _dataList.add(newsInfoModel);
        }


        _newsAdapter = new NewsAdapter(getActivity(), R.layout.news_row, _dataList);
        _lv_list.setAdapter(_newsAdapter);
//        ListView actualListView = mPullRefreshListView.getRefreshableView();
//        actualListView.setAdapter(mAdapter);
        return _inflatedView;
    }

    protected int[] getPullToRefreshColorResources() {
        return new int[]{R.color.color_primary};
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            public void run() {

                _mSwipeRefreshLayout.setRefreshing(false);
                App_NewsInfoModel a = new App_NewsInfoModel();
                a.setTitle("新的12123");

                _dataList.add(0,a);
                _newsAdapter.notifyDataSetChanged();
            }
        }, 500);
    }

    private class QueryNewsAsyncTask extends AsyncTask<Void, Void, List<App_NewsInfoModel>> {
        @Override
        protected List<App_NewsInfoModel> doInBackground(Void... voids) {

            Log.i("TAG", tag + "Fragment 发起查询请求");
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
                Log.i("TAG", tag + "Fragment 获得查询结果=" + app_newsInfoModels.size());
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
            for (App_NewsInfoModel item : result) {
                if (item.getNewsTypeName().contains(tag)) {
                    Log.i(TAG, "包含" + tag + "消息");
                    _dataList.addFirst(item);
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
            _newsAdapter.notifyDataSetChanged();
            // Call onRefreshComplete when the list has been refreshed.
//            _lv_list.onref
//            mPullRefreshListView.onRefreshComplete();
            Log.i(TAG, "更新列表数据" + tag);
            super.onPostExecute(result);
        }
    }


}
