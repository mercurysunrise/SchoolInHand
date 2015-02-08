package com.zhilianxinke.schoolinhand.modules.vedios;


import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.zhilianxinke.schoolinhand.MediaPlayerActivity;
import com.zhilianxinke.schoolinhand.R;
import com.zhilianxinke.schoolinhand.RollViewPager;
import com.zhilianxinke.schoolinhand.domain.App_DeviceInfoModel;
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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class VedioListFragment extends Fragment implements MediaPlayer.OnErrorListener,MediaPlayer.OnSeekCompleteListener{


    public VedioListFragment() {
        // Required empty public constructor
    }

    private static String TAG = "VedioListFragment";
    private View view;

    private VideoView vedioView;
    private static List<App_DeviceInfoModel> _app_deviceInfoModels;
    private static SimpleAdapter _adapter;
    private ListView lv_newsInfo;
    private ArrayList<String> uriList;
    private ImageView img_Vedio;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_vedio_list, container, false);
        initView();
        return view;
    }


    public void initView() {
        vedioView = (VideoView)view.findViewById(R.id.videoView);
        lv_newsInfo = (ListView) view.findViewById(R.id.lv_vediolist);
        img_Vedio = (ImageView) view.findViewById(R.id.img_Vedio);


        List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();

        params.add(new BasicNameValuePair("pk", StaticRes.currCustom.getPk()));
        // 对参数编码
        final String param = URLEncodedUtils.format(params, "UTF-8");

        // baseUrl
        final String baseUrl = StaticRes.baseUrl + "/deviceInfo/appDeviceInfo";

        if (_adapter == null) {
            final HttpClient httpClient = new DefaultHttpClient();

            new Thread() {
                public void run() {
                    try {
                        HttpGet getMethod = new HttpGet(baseUrl + "?" + param);
                        HttpResponse response = httpClient.execute(getMethod); // 发起GET请求

                        String result = EntityUtils.toString(response.getEntity(),
                                "utf-8");
                        _app_deviceInfoModels = (List<App_DeviceInfoModel>) JSONHelper
                                .parseCollection(result, List.class,
                                        App_DeviceInfoModel.class);

                        handler.post(runnable);
                    } catch (ClientProtocolException e) {
                        Log.e(TAG, "ClientProtocolException", e);
                    } catch (IOException e) {
                        Log.e(TAG, "IOException", e);
                    } catch (JSONException e) {
                        Log.e(TAG, "JSONException", e);
                    }
                }
            }.start();
        }else {
            refreshList();
        }
        vedioView.setOnErrorListener(this);
    }

    private Handler handler = new Handler();

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // 更新界面
            ArrayList<HashMap<String, String>> modelList = new ArrayList<HashMap<String, String>>();
            for (App_DeviceInfoModel app_deviceInfoModel : _app_deviceInfoModels) {
                // 新建一个 HashMap
                HashMap<String, String> map = new HashMap<String, String>();

                // 每个子节点添加到HashMap关键= >值
                map.put("title", app_deviceInfoModel.getAddress());
                map.put("artist", app_deviceInfoModel.getStrName());

                // HashList添加到数组列表
                modelList.add(map);
            }
            _adapter = new SimpleAdapter(view.getContext(),
                    modelList, R.layout.newslist_row, new String[] { "title",
                    "artist" }, new int[] { R.id.title,
                    R.id.artist });
            refreshList();
        }


    };

    private void refreshList() {
        lv_newsInfo.setAdapter(_adapter);

        lv_newsInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
//                Intent intent = new Intent(getActivity(),
//                        MediaPlayerActivity.class);
                App_DeviceInfoModel app_DeviceInfoModel = _app_deviceInfoModels
                        .get(position);
//                intent.putExtra("app_DeviceInfoModel", app_DeviceInfoModel);
//                startActivity(intent);
                vedioView.setVideoURI(Uri.parse(app_DeviceInfoModel.getStreamUrl1()));
                vedioView.requestFocus();
                img_Vedio.setVisibility(View.VISIBLE);
                vedioView.start();
                img_Vedio.setVisibility(View.GONE);
                Log.d("点击", "" + position);
            }
        });
    }


    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.v("this is error", "onError method is called!!");

        if (what == MediaPlayer.MEDIA_ERROR_SERVER_DIED) {
            Log.v("this is error", "Media Error,Server Died" + extra);
        } else if (what == MediaPlayer.MEDIA_ERROR_UNKNOWN) {
            Log.v("this is error", "Media Error,Error Unknown" + extra);
        }
        Toast.makeText(getActivity(), "视频播放错误，请重新选择！",
                Toast.LENGTH_LONG).show();
        return false;
    }


    @Override
    public void onSeekComplete(MediaPlayer mediaPlayer) {
        img_Vedio.setVisibility(View.GONE);
    }
}
