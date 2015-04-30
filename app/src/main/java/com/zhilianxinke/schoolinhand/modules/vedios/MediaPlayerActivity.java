package com.zhilianxinke.schoolinhand.modules.vedios;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;

import com.zhilianxinke.schoolinhand.R;
import com.zhilianxinke.schoolinhand.domain.AppAsset;
import com.zhilianxinke.schoolinhand.util.MyVideoView;
import com.zhilianxinke.schoolinhand.util.StaticRes;
import com.zhilianxinke.schoolinhand.util.UrlBuilder;

import java.util.Timer;
import java.util.TimerTask;

public class MediaPlayerActivity extends Activity  implements OnCompletionListener, OnErrorListener {
//	implements OnPreparedListener
	static final String TAG = "MediaPlayerActivity";
	
	private MyVideoView _my_video;
	private MediaController _mController;
	private ImageView img_FS_AD;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_media_player);
		
		_my_video = (MyVideoView)findViewById(R.id.my_videoView);
		_mController = new MediaController(this,false);

		_my_video.setMediaController(_mController);
		_my_video.getHolder().setKeepScreenOn(true);

        img_FS_AD = (ImageView)findViewById(R.id.img_FS_AD);

        _mController.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Prev
                playVedio(UrlBuilder.URL_TESTHLS,true);
            }
        },new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Next
                playVedio(UrlBuilder.URL_TESTHLS,true);
            }
        });
        _my_video.setOnCompletionListener(this);
        _my_video.setOnErrorListener(this);

        Intent intent = getIntent();
        AppAsset appAsset = (AppAsset) intent.getSerializableExtra("appAsset");
        String url = appAsset.getUrl();
        playVedio(url,false);
	}

    private void playVedio(String url,boolean isShowAd){
        _my_video.setVideoURI(Uri.parse(url));
        _my_video.requestFocus();
        _my_video.start();

        if (isShowAd){
            img_FS_AD.setVisibility(View.VISIBLE);
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
    }

    private Handler closeAdHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            img_FS_AD.setVisibility(View.GONE);
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
        Toast.makeText(MediaPlayerActivity.this, "视频播放错误，请重新选择！",
                Toast.LENGTH_LONG).show();
        return false;
	}

	@Override
	public void onCompletion(MediaPlayer arg0) {
		finish();
	}
	
}