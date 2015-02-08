package com.zhilianxinke.schoolinhand;

import java.io.Serializable;

//import cn.sharesdk.framework.ShareSDK;
import com.zhilianxinke.schoolinhand.domain.App_NewsInfoModel;
import com.zhilianxinke.schoolinhand.util.ClassPathResource;
import com.zhilianxinke.schoolinhand.util.StaticRes;
import com.zhilianxinke.schoolinhand.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NewsInfoActivity extends Activity implements android.view.View.OnClickListener {

	private TextView tv_top_title;
	private TextView btn_title_share;
	private TextView btn_title_left;
	
	private TextView tv_news_title;
	private TextView tv_news_publicman;
	private WebView wv_news_content;
	private TextView tv_news_publicTime;
	private String strUrl;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_newsinfo);
		
		initView();
	}
	
	private void initView(){
		tv_top_title = (TextView) findViewById(R.id.tv_top_title);
		tv_top_title.setText("公告详细");
		
		btn_title_share = (Button) findViewById(R.id.btn_title_share);
//		btn_title_right.setVisibility(View.GONE);
		btn_title_share.setOnClickListener(this);
		
		btn_title_left = (Button) findViewById(R.id.btn_title_left);
		btn_title_left.setOnClickListener(this);
		
		Intent intent = getIntent();

		App_NewsInfoModel app_NewsInfoModel = (App_NewsInfoModel)intent.getSerializableExtra("app_NewsInfoModel");
		tv_news_title = (TextView) findViewById(R.id.tv_news_title);
		tv_news_title.setText(app_NewsInfoModel.getTitle());
		
		tv_news_publicman = (TextView) findViewById(R.id.tv_news_publicman);
		tv_news_publicman.setText("发布人:"+app_NewsInfoModel.getPublicUserName());
		
		tv_news_publicTime = (TextView) findViewById(R.id.tv_news_publicTime);
		tv_news_publicTime.setText(app_NewsInfoModel.getStrPublicTime());
		
		wv_news_content = (WebView) findViewById(R.id.wv_news_content);
//		http://127.0.0.1/DMService/newsInfo/detail.html?pk=c06a0c04-703a-4e31-afb5-17d927317016
		strUrl = StaticRes.baseUrl + "/newsInfo/detail.html?pk="+app_NewsInfoModel.getPk();
		wv_news_content.loadUrl(strUrl);
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
