package com.zhilianxinke.schoolinhand.modules.vedios;


import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.zhilianxinke.schoolinhand.AppContext;
import com.zhilianxinke.schoolinhand.R;
import com.zhilianxinke.schoolinhand.RollViewPager;
import com.zhilianxinke.schoolinhand.domain.AppAsset;
import com.zhilianxinke.schoolinhand.domain.AppNews;
import com.zhilianxinke.schoolinhand.domain.SdkHttpResult;
import com.zhilianxinke.schoolinhand.util.JSONHelper;
import com.zhilianxinke.schoolinhand.util.MyVideoView;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
    private static List<AppAsset> _app_deviceInfoModels;
    private ArrayList<String> uriList;
    private ImageView img_AD;

    private TextView title;
    private RollViewPager rollViewPager;
    private int[] imageIDs;
    private String[] titles;
    private ArrayList<View> dots;

//    private List<View> buttons= new ArrayList<View>();
    private RelativeLayout relativeLayout;

//    private Map<View,String> buttonUrlMap = new HashMap<View,String>(4);
    private Map<View,AppAsset> buttonUrlMap = new HashMap<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_vedio_list, container, false);

        vv_ViedoView = (MyVideoView) view.findViewById(R.id.vv_ViedoView);
        img_AD = (ImageView) view.findViewById(R.id.img_AD);

//        buttonUrlMap.clear();
//        buttonUrlMap.put(view.findViewById(R.id.btnPlay0), "http://121.42.146.235/hls/DHE153E/playlist.m3u8");
//        buttonUrlMap.put(view.findViewById(R.id.btnPlay1), "http://121.42.146.235/hls/DHC2655/playlist.m3u8");
//        buttonUrlMap.put(view.findViewById(R.id.btnPlay2), "http://121.42.146.235/hls/DHCB26F/playlist.m3u8");
//        buttonUrlMap.put(view.findViewById(R.id.btnPlay3), "http://121.42.146.235/hls/DH0B319/playlist.m3u8");

        view.findViewById(R.id.img_full_screen).setOnClickListener(this);
        vv_ViedoView.setOnErrorListener(this);

        relativeLayout = (RelativeLayout) view.findViewById(R.id.rlImages);
        title = (TextView) view.findViewById(R.id.title);
        rollViewPager = (RollViewPager) view.findViewById(R.id.viewpager);

        imageIDs = new int[] { R.drawable.a, R.drawable.b, R.drawable.c,
                R.drawable.d, R.drawable.e };

        // 图片名称
        titles = new String[] { "校园图片1", "校园图片2", "校园图片3", "校园图片4",
                "校园图片5" };

        // 用来显示的点
        dots = new ArrayList<View>();
        dots.add(view.findViewById(R.id.dot_0));
        dots.add(view.findViewById(R.id.dot_1));
        dots.add(view.findViewById(R.id.dot_2));
        dots.add(view.findViewById(R.id.dot_3));
        dots.add(view.findViewById(R.id.dot_4));

        // 构造网络图片
        uriList = new ArrayList<String>();

        uriList.add("assest://image/a.jpg");
        uriList.add("assest://image/b.jpg");
        uriList.add("assest://image/c.jpg");
        uriList.add("assest://image/d.jpg");
        uriList.add("assest://image/e.jpg");

        rollViewPager.setResImageIds(imageIDs);//设置res的图片id

        rollViewPager.setDot(dots, R.drawable.dot_focus, R.drawable.dot_normal);

        rollViewPager.setTitle(title, titles);//不需要显示标题，可以不设置
        rollViewPager.startRoll();//不调用的话不滚动

        new VedioListAsyncTask().execute();
        return view;
    }

    @Override
    public void onClick(View view) {
        if (relativeLayout.getVisibility() != View.GONE){
            relativeLayout.setVisibility(View.GONE);
        }
        if (view.getId() == R.id.img_full_screen) {
            //全屏显示
            Intent intent = new Intent(getActivity(),MediaPlayerActivity.class);
            AppAsset appAsset = buttonUrlMap.get(view);
            intent.putExtra("appAsset",appAsset);
            startActivity(intent);
        } else {
            //加载视频及广告
            Drawable drawable = view.getResources().getDrawable(R.drawable.webcam);
            Drawable drawable_select = view.getResources().getDrawable(R.drawable.webcam_select);
            for(View item : buttonUrlMap.keySet()){
                Drawable background = item==view?drawable_select:drawable;
                item.setBackground(background);
            }
            playVedio(view);
        }
    }

    private void playVedio(View view){
        if (vv_ViedoView.getVisibility() == View.GONE){
            vv_ViedoView.setVisibility(View.VISIBLE);
        }
        if (vv_ViedoView.isPlaying()){
            vv_ViedoView.stopPlayback();
        }
        AppAsset appAsset = buttonUrlMap.get(view);
        vv_ViedoView.setVideoURI(Uri.parse(appAsset.getUrl()));
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

    private class VedioListAsyncTask extends AsyncTask<Void, Void, List<AppAsset>> {

        @Override
        protected void onPreExecute() {
            view.findViewById(R.id.pb_btns).setVisibility(View.VISIBLE);
            view.findViewById(R.id.ll_btns).setVisibility(View.GONE);
        }

        @Override
        protected List<AppAsset> doInBackground(Void... voids) {
            List<AppAsset> appAssets = new ArrayList<AppAsset>();
            List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();

            params.add(new BasicNameValuePair("id", AppContext.getInstance().getAppUser().getId()));
            // 对参数编码
            final String param = URLEncodedUtils.format(params, "UTF-8");

            // baseUrl
            final String baseUrl = UrlBuilder.baseUrl + "/asset/myAssets";

            final HttpClient httpClient = new DefaultHttpClient();
            HttpGet getMethod = new HttpGet(baseUrl + "?" + param);
            try {
                HttpResponse response = httpClient.execute(getMethod); // 发起GET请求

                String result = EntityUtils.toString(response.getEntity(),
                        "utf-8");
                SdkHttpResult sdkHttpResult = JSON.parseObject(result, SdkHttpResult.class);
                if (sdkHttpResult != null && sdkHttpResult.getCode() == 200){
                    appAssets = JSON.parseArray(sdkHttpResult.getResult(),AppAsset.class);
                }
            } catch (ClientProtocolException e) {
                Log.e(TAG, "ClientProtocolException", e);
            } catch (IOException e) {
                Log.e(TAG, "IOException", e);
            }

            return appAssets;
        }

        @Override
        protected void onPostExecute(List<AppAsset> app_deviceInfoModels) {
            _app_deviceInfoModels = app_deviceInfoModels;

            buttonUrlMap.put(view.findViewById(R.id.btnPlay0), _app_deviceInfoModels.get(0));
            buttonUrlMap.put(view.findViewById(R.id.btnPlay1), _app_deviceInfoModels.get(1));
            buttonUrlMap.put(view.findViewById(R.id.btnPlay2), _app_deviceInfoModels.get(2));
            buttonUrlMap.put(view.findViewById(R.id.btnPlay3), _app_deviceInfoModels.get(3));

            for (View view : buttonUrlMap.keySet()){
                view.setOnClickListener(VedioListFragment.this);
            }

            view.findViewById(R.id.pb_btns).setVisibility(View.GONE);
            view.findViewById(R.id.ll_btns).setVisibility(View.VISIBLE);
        }
    }
}


