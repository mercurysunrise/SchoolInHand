package com.zhilianxinke.schoolinhand.modules.news;

//import cn.sharesdk.framework.ShareSDK;
import com.zhilianxinke.schoolinhand.base.BaseActivity;
import com.zhilianxinke.schoolinhand.domain.App_NewsInfoModel;
import com.zhilianxinke.schoolinhand.modules.news.adapters.NewsAdapter;
import com.zhilianxinke.schoolinhand.util.StaticRes;
import com.zhilianxinke.schoolinhand.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
		import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import io.rong.imkit.view.ActionBar;

public class NewsInfoActivity extends BaseActivity implements android.view.View.OnClickListener {

    private static final String TAG = "NewsInfoActivity";
	private TextView tv_top_title;
	private TextView btn_title_share;
	private TextView btn_title_left;
	
	private TextView tv_news_title;
	private TextView tv_news_publicman;
	private WebView wv_news_content;
	private TextView tv_news_publicTime;
	private String strUrl;

	private ActionBar mAction;

	private static App_NewsInfoModel app_NewsInfoModel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		setContentView(R.layout.activity_newsinfo);

	}

    @Override
    protected int setContentViewResId() {
        return R.layout.activity_newsinfo;
    }

    @Override
    protected void initView(){
//		tv_top_title = (TextView) findViewById(R.id.tv_top_title);
//		tv_top_title.setText("公告详细");
		
//		btn_title_share = (Button) findViewById(R.id.btn_title_share);
////		btn_title_right.setVisibility(View.GONE);
//		btn_title_share.setOnClickListener(this);
//
//		btn_title_left = (Button) findViewById(R.id.btn_title_left);
//		btn_title_left.setOnClickListener(this);

		mAction = (ActionBar) findViewById(R.id.action_title_bar);



//		App_NewsInfoModel app_NewsInfoModel = (App_NewsInfoModel)intent.getSerializableExtra("app_NewsInfoModel");
//		tv_news_title = (TextView) findViewById(R.id.tv_news_title);
//		tv_news_title.setText(app_NewsInfoModel.getTitle());
		
		tv_news_publicman = (TextView) findViewById(R.id.tv_news_publicman);
		tv_news_publicman.setText(app_NewsInfoModel.getPublicUserName());
		
		tv_news_publicTime = (TextView) findViewById(R.id.tv_news_publicTime);


		wv_news_content = (WebView) findViewById(R.id.wv_news_content);

	}

    @Override
    protected void initData() {
		mAction.getLogoView().setVisibility(View.GONE);
		mAction.getTitleTextView().setText(app_NewsInfoModel.getTitle());
		mAction.getTitleTextView().setTextColor(Color.WHITE);
		mAction.getTitleTextView().setTextSize(18);

		strUrl = StaticRes.baseUrl + "/newsInfo/detail.html?pk="+app_NewsInfoModel.getPk();

		if (app_NewsInfoModel.getTitle().contains("测试公告标题")){
			strUrl = StaticRes.baseUrl + "/newsInfo/detail.html?pk=049aa25f-3581-4095-a4df-a83cfe3ac833";
		}

		if (app_NewsInfoModel.getStrPublicTime() != null && app_NewsInfoModel.getStrPublicTime().length() > 10){
			tv_news_publicTime.setText(app_NewsInfoModel.getStrPublicTime().substring(0,10));
		}

		wv_news_content.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);  //设置 缓存模式
		// 开启 DOM storage API 功能
		wv_news_content.getSettings().setDomStorageEnabled(true);
		//开启 database storage API 功能
		wv_news_content.getSettings().setDatabaseEnabled(true);
		String cacheDirPath = getFilesDir().getAbsolutePath()+"WebCache";

		Log.i(TAG, "cacheDirPath=" + cacheDirPath);
		//设置数据库缓存路径
		wv_news_content.getSettings().setDatabasePath(cacheDirPath);
		//设置  Application Caches 缓存目录
		wv_news_content.getSettings().setAppCachePath(cacheDirPath);
		//开启 Application Caches 功能
		wv_news_content.getSettings().setAppCacheEnabled(true);

		wv_news_content.loadUrl(strUrl);
    }

	public static void actionStart(Context context,App_NewsInfoModel item){
		Intent intent = new Intent(context,NewsInfoActivity.class);
//		App_NewsInfoModel app_NewsInfoModel = _dataList.get(position);
//		app_NewsInfoModel.setReaded(true);
//		NewsAdapter.setReadState(view);
//        NewsAdapter newsAdapter = (NewsAdapter)parent;
//		intent.putExtra("app_NewsInfoModel", app_NewsInfoModel);
		app_NewsInfoModel = item;
		context.startActivity(intent);

	}

    @Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		
		case R.id.btn_title_left:
			NewsInfoActivity.this.finish();
			break;
		case R.id.btn_title_share:
			showShare();
			break;
		
		}
	}
	
	private void showShare() {
//		 ShareSDK.initSDK(this);
//		 OnekeyShare oks = new OnekeyShare();
//		 //关闭sso授权
//		 oks.disableSSOWhenAuthorize();
//
//		// 分享时Notification的图标和文字
//		 oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
//		 // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
//		 oks.setTitle(tv_news_title.getText().toString());
//		 // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
//		 oks.setTitleUrl(strUrl);
//		 // text是分享文本，所有平台都需要这个字段
//		 oks.setText(strUrl);
//		 // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//		 oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
//		 // url仅在微信（包括好友和朋友圈）中使用
//		 oks.setUrl(strUrl);
//		 // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//		 oks.setComment("赞一个");
//		 // site是分享此内容的网站名称，仅在QQ空间使用
//		 oks.setSite(getString(R.string.app_name));
//		 // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//		 oks.setSiteUrl(strUrl);
//
//		// 启动分享GUI
//		 oks.show(this);
		 }
	
}
