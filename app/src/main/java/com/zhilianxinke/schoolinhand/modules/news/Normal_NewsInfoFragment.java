package com.zhilianxinke.schoolinhand.modules.news;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.zhilianxinke.schoolinhand.AppContext;
import com.zhilianxinke.schoolinhand.R;
import com.zhilianxinke.schoolinhand.common.FastJsonRequest;
import com.zhilianxinke.schoolinhand.domain.AppNews;
import com.zhilianxinke.schoolinhand.domain.AppUser;
import com.zhilianxinke.schoolinhand.domain.SdkHttpResult;
import com.zhilianxinke.schoolinhand.modules.news.adapters.NewsAdapter;
import com.zhilianxinke.schoolinhand.rongyun.ui.WinToast;
import com.zhilianxinke.schoolinhand.util.CacheUtils;
import com.zhilianxinke.schoolinhand.util.JSONHelper;
import com.zhilianxinke.schoolinhand.util.StaticRes;
import com.zhilianxinke.schoolinhand.util.UrlBuilder;

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
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by hh on 2015-02-09.
 */
public class Normal_NewsInfoFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,AdapterView.OnItemClickListener{

    private static String TAG = "Normal_NewsInfoFragment";

    private SwipeRefreshLayout _mSwipeRefreshLayout;
    private ListView _lv_list;

    private View _inflatedView;

    private String tag;
    private String dataTag;

    public RequestQueue requestQueue;

    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private LinkedList<AppNews> _dataList;
    private NewsAdapter _newsAdapter;

    public Normal_NewsInfoFragment(){

    }

    public void setTitle(String strTitle){
        this.tag = strTitle;
        this.dataTag = AppContext.getAppUser().getId() + tag + "Data";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView+" + tag);
        _inflatedView = inflater.inflate(R.layout.fragment_class2__news_info, container, false);

        _mSwipeRefreshLayout = (SwipeRefreshLayout) _inflatedView.findViewById(R.id.swipe_refresh);
        _mSwipeRefreshLayout.setColorSchemeResources(getPullToRefreshColorResources());
        _mSwipeRefreshLayout.setOnRefreshListener(this);

        _lv_list = (ListView) _inflatedView.findViewById(R.id.lv_list);

        if (_dataList == null && CacheUtils.isExistDataCache(getActivity(),dataTag)){
            LinkedList<AppNews> list = (LinkedList<AppNews>)CacheUtils.readObject(getActivity(),dataTag);
            _dataList = list;
        }
        if (_dataList == null){
            _dataList = new LinkedList<AppNews>();
        }

        requestQueue = Volley.newRequestQueue(getActivity());

        _newsAdapter = new NewsAdapter(getActivity(), R.layout.news_row, _dataList);
        _lv_list.setAdapter(_newsAdapter);
        _lv_list.setOnItemClickListener(this);
        return _inflatedView;
    }

    protected int[] getPullToRefreshColorResources() {
        return new int[]{R.color.color_primary};
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * 获取上次更新时间戳
     * @return
     */
    public String getLastGetDataDate(){
        if (_dataList != null && _dataList.size() > 0){
            String cover = _dataList.get(0).getCover().toString();
            return cover;
        }
        return "2000-01-01 00:00:00";
    }

    @Override
    public void onRefresh() {
        _mSwipeRefreshLayout.setRefreshing(false);

        AppUser appUser = AppContext.getAppUser();

        Map<String,String> params = new HashMap<>(2);
        params.put("id",appUser.getId());
        params.put("dt", getLastGetDataDate());
        if("最新".equals(tag) || "推荐".equals(tag)){
            String baseUrl = "最新".equals(tag) ? "/news/lastMyNews":"/news/lastCorpNews";
            String url = UrlBuilder.build(baseUrl,params);

            FastJsonRequest<SdkHttpResult> fastJson=new FastJsonRequest<SdkHttpResult>(url, SdkHttpResult.class,
                    new Response.Listener<SdkHttpResult>() {
                        @Override
                        public void onResponse(SdkHttpResult sdkHttpResult) {

                            if (sdkHttpResult != null && sdkHttpResult.getCode() == 200){
                                String result = sdkHttpResult.getResult();
                                List<AppNews> list = JSON.parseArray(result,AppNews.class);
                                for(AppNews appNews : list){
                                    _dataList.addFirst(appNews);
                                }
                                CacheUtils.saveObject(getActivity(),_dataList,dataTag);
                                _newsAdapter.notifyDataSetChanged();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyError ve = error;
                    if (ve != null ){
                        Log.e(TAG,error.getMessage());
                        WinToast.toast(getActivity(),error.getMessage());
                    }
                }
            });
            requestQueue.add(fastJson);
        }else{

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AppNews appNews = _dataList.get(position);
        appNews.setReaded(true);
        NewsAdapter.setReadState(view);

        NewsInfoActivity.actionStart(getActivity(),appNews);
        Log.d(TAG, "点击" + position);
    }


}
