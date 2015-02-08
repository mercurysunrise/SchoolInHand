package com.zhilianxinke.schoolinhand;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.zhilianxinke.schoolinhand.domain.App_DeviceInfoModel;
import com.zhilianxinke.schoolinhand.util.MyVideoView;
import com.zhilianxinke.schoolinhand.R;

public class MediaPlayerActivity extends Activity  implements OnCompletionListener, OnErrorListener {
//	implements OnPreparedListener
	static final String TAG = "MediaPlayerActivity";
	
	private MyVideoView _my_video;
	private MediaController _mController;
	private SurfaceHolder _sufaceHolder;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_media_player);
		
		_my_video = (MyVideoView)findViewById(R.id.my_videoView);
		
		_mController = new MediaController(this);
		_sufaceHolder = _my_video.getHolder();
		
//		_sufaceHolder.getSurface().
//		_vv_video.
		Display display = getWindowManager().getDefaultDisplay();  
		
		_my_video.setMediaController(_mController);
		_my_video.getHolder().setKeepScreenOn(true);
//		_vv_video.getHolder().setFixedSize(display.getWidth(), display.getHeight());
		Intent intent = getIntent();
		App_DeviceInfoModel app_DeviceInfoModel = (App_DeviceInfoModel) intent.getSerializableExtra("app_DeviceInfoModel");
		String url = app_DeviceInfoModel.getStreamUrl1();
		
		
		
		
		DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        MyVideoView.WIDTH=dm.widthPixels;
        MyVideoView.HEIGHT=dm.heightPixels;
        Log.e("widthPixels", "widthPixels"+dm.widthPixels);
        Log.e("heightPixels", "widthPixels"+dm.heightPixels);


        _my_video.setOnCompletionListener(this);
        _my_video.setOnErrorListener(this);

        _my_video.setVideoURI(Uri.parse(url));
		_my_video.requestFocus();
		
		//设置surfaceView的布局参数  
//		_vv_video.getHolder().setSizeFromLayout();
//		_vv_video.getHolder().setLayoutParams(new LinearLayout.LayoutParams(1280, 720));  
		_my_video.start();
		
	}

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