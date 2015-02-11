package com.zhilianxinke.schoolinhand.modules.vedios;


import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.VideoView;

import com.zhilianxinke.schoolinhand.R;
import com.zhilianxinke.schoolinhand.domain.App_DeviceInfoModel;
import com.zhilianxinke.schoolinhand.util.JSONHelper;
import com.zhilianxinke.schoolinhand.util.MyVideoView;
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
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class VedioListFragment extends Fragment implements MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener, Button.OnClickListener {


    public VedioListFragment() {
        // Required empty public constructor
    }

    private static String TAG = "VedioListFragment";
    private View view;

    private MyVideoView vv_ViedoView;
    private static List<App_DeviceInfoModel> _app_deviceInfoModels;
    private ArrayList<String> uriList;
    private ImageView img_AD;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_vedio_list, container, false);

        vv_ViedoView = (MyVideoView) view.findViewById(R.id.vv_ViedoView);
        img_AD = (ImageView) view.findViewById(R.id.img_AD);

        view.findViewById(R.id.btnPlay0).setOnClickListener(this);
        view.findViewById(R.id.btnPlay1).setOnClickListener(this);
        view.findViewById(R.id.btnPlay2).setOnClickListener(this);
        view.findViewById(R.id.btnPlay3).setOnClickListener(this);
        view.findViewById(R.id.btnPlay4).setOnClickListener(this);
        view.findViewById(R.id.btnPlay5).setOnClickListener(this);
        view.findViewById(R.id.btnPlay6).setOnClickListener(this);
        view.findViewById(R.id.img_full_screen).setOnClickListener(this);
        vv_ViedoView.setOnErrorListener(this);

        new VedioListAsyncTask().execute();
        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.img_full_screen) {
            //全屏显示
            Intent intent = new Intent(getActivity(),MediaPlayerActivity.class);
            intent.putExtra("app_DeviceInfoModel", _app_deviceInfoModels.get(0));
            startActivity(intent);
        } else {
            //加载视频及广告
            playVedio(null);
        }
    }

    private void playVedio(String url){
        if (vv_ViedoView.getVisibility() == View.GONE){
            vv_ViedoView.setVisibility(View.VISIBLE);
        }
        if (vv_ViedoView.isPlaying()){
            vv_ViedoView.stopPlayback();
        }
        vv_ViedoView.setVideoURI(Uri.parse(StaticRes.URL_TESTHLS));
        vv_ViedoView.requestFocus();
        img_AD.setVisibility(View.VISIBLE);

        vv_ViedoView.start();
        Timer timer = new Timer(true);
        TimerTask timeTask = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 0;
                closeAdHandler.sendMessage(message);
            }
        };
        timer.schedule(timeTask, 1500);
    }

    private Handler closeAdHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            img_AD.setVisibility(View.GONE);
            super.handleMessage(msg);
        }
    };

    private void refreshList() {
//        lv_newsInfo.setAdapter(_adapter);
//
//        lv_newsInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
////                Intent intent = new Intent(getActivity(),
////                        MediaPlayerActivity.class);
//                App_DeviceInfoModel app_DeviceInfoModel = _app_deviceInfoModels
//                        .get(position);
////                intent.putExtra("app_DeviceInfoModel", app_DeviceInfoModel);
////                startActivity(intent);
//                vv_ViedoView.setVideoURI(Uri.parse(app_DeviceInfoModel.getStreamUrl1()));
//                vv_ViedoView.requestFocus();
//                img_AD.setVisibility(View.VISIBLE);
//                vv_ViedoView.start();
//                img_AD.setVisibility(View.GONE);
//                Log.d("点击", "" + position);
//            }
//        });
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
    public void onDestroy() {
        vv_ViedoView.destroyDrawingCache();
        super.onDestroy();
    }

    @Override
    public void onSeekComplete(MediaPlayer mediaPlayer) {
        img_AD.setVisibility(View.GONE);
    }

    private class VedioListAsyncTask extends AsyncTask<Void, Void, List<App_DeviceInfoModel>> {

        @Override
        protected void onPreExecute() {
            view.findViewById(R.id.pb_btns).setVisibility(View.VISIBLE);
            view.findViewById(R.id.ll_btns).setVisibility(View.GONE);
        }

        @Override
        protected List<App_DeviceInfoModel> doInBackground(Void... voids) {
            List<App_DeviceInfoModel> app_deviceInfoModels = new ArrayList<App_DeviceInfoModel>();
            List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();

            params.add(new BasicNameValuePair("pk", StaticRes.currCustom.getPk()));
            // 对参数编码
            final String param = URLEncodedUtils.format(params, "UTF-8");

            // baseUrl
            final String baseUrl = StaticRes.baseUrl + "/deviceInfo/appDeviceInfo";

            final HttpClient httpClient = new DefaultHttpClient();
            HttpGet getMethod = new HttpGet(baseUrl + "?" + param);
            try {
                HttpResponse response = httpClient.execute(getMethod); // 发起GET请求

                String result = EntityUtils.toString(response.getEntity(),
                        "utf-8");
                app_deviceInfoModels = (List<App_DeviceInfoModel>) JSONHelper
                        .parseCollection(result, List.class,
                                App_DeviceInfoModel.class);

            } catch (ClientProtocolException e) {
                Log.e(TAG, "ClientProtocolException", e);
            } catch (IOException e) {
                Log.e(TAG, "IOException", e);
            } catch (JSONException e) {
                Log.e(TAG, "JSONException", e);
            }

            return app_deviceInfoModels;
        }

        @Override
        protected void onPostExecute(List<App_DeviceInfoModel> app_deviceInfoModels) {
            _app_deviceInfoModels = app_deviceInfoModels;

            view.findViewById(R.id.pb_btns).setVisibility(View.GONE);
            view.findViewById(R.id.ll_btns).setVisibility(View.VISIBLE);
            // 更新界面
//            ArrayList<HashMap<String, String>> modelList = new ArrayList<HashMap<String, String>>();
//            for (App_DeviceInfoModel app_deviceInfoModel : _app_deviceInfoModels) {
//                // 新建一个 HashMap
//                HashMap<String, String> map = new HashMap<String, String>();
//
//                // 每个子节点添加到HashMap关键= >值
//                map.put("title", app_deviceInfoModel.getAddress());
//                map.put("artist", app_deviceInfoModel.getStrName());
//
//                // HashList添加到数组列表
//                modelList.add(map);
//            }
//            _adapter = new SimpleAdapter(view.getContext(),
//                    modelList, R.layout.newslist_row, new String[] { "title",
//                    "artist" }, new int[] { R.id.title,
//                    R.id.artist });
//            refreshList();
        }
    }
}


