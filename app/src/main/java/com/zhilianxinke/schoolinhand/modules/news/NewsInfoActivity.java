package com.zhilianxinke.schoolinhand.modules.news;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhilianxinke.schoolinhand.R;
import com.zhilianxinke.schoolinhand.base.BaseActivity;
import com.zhilianxinke.schoolinhand.domain.AppNews;
import com.zhilianxinke.schoolinhand.util.StaticRes;
import com.zhilianxinke.schoolinhand.util.UrlBuilder;

public class NewsInfoActivity extends BaseActivity implements android.view.View.OnClickListener {

    private static final String TAG = "NewsInfoActivity";
	private TextView tv_top_title;
	private TextView btn_title_share;
	private TextView btn_title_left;
	
	private TextView tv_news_title;
	private TextView tv_news_publicman;

	private TextView tv_news_publicTime;

	private TextView tv_news_content;

//	private ActionBar mAction;
	private LinearLayout ll_images;
	DisplayImageOptions options;        // DisplayImageOptions是用于设置图片显示的类

	private static AppNews appNews;

    @Override
    protected int setContentViewResId() {
        return R.layout.activity_newsinfo;
    }

    @Override
    protected void initView(){
//		mAction = (ActionBar) findViewById(R.id.abar_news_activity);

		tv_news_publicman = (TextView) findViewById(R.id.tv_news_publicman);
		tv_news_publicman.setText(appNews.getAuthorName());
		
		tv_news_publicTime = (TextView) findViewById(R.id.tv_news_publicTime);

		tv_news_content= (TextView)findViewById(R.id.tv_news_content);

		ll_images = (LinearLayout)findViewById(R.id.ll_images);

		// 使用DisplayImageOptions.Builder()创建DisplayImageOptions
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.image_bg)          // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.image_bg)  // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.image_bg)       // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory()                        // 设置下载的图片是否缓存在内存中
				.cacheOnDisc()                          // 设置下载的图片是否缓存在SD卡中
//				.displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片
				.build();                                   // 创建配置过得DisplayImageOption对象
	}

    @Override
    protected void initData() {
//		mAction.getLogoView().setVisibility(View.GONE);
//		mAction.getTitleTextView().setText(appNews.getTitle());
//		mAction.getTitleTextView().setTextColor(Color.WHITE);
//		mAction.getTitleTextView().setTextSize(18);

		if (appNews.getCover() != null && appNews.getCover().toString().length() > 10){
			tv_news_publicTime.setText(appNews.getCover().toString().substring(0,10));
		}
		String content = appNews.getContent();
		tv_news_content.setText(content);

		if (appNews.getImage() != null){
			String[] strUrls = appNews.getImage().split(";");
			for (String strUrl : strUrls){
				requestImage(UrlBuilder.buildImageUrl(strUrl));
			}
		}
    }

	public void requestImage(String url){
//		ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
//			@Override
//			public void onResponse(Bitmap response) {
//				ImageView imageView = new ImageView(NewsInfoActivity.this);
//				imageView.setImageBitmap(response);
//				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
//						LinearLayout.LayoutParams.MATCH_PARENT,
//						LinearLayout.LayoutParams.WRAP_CONTENT
//				);
//				imageView.setLayoutParams(layoutParams);
//				ll_images.addView(imageView);
//			}
//		}, 0, 0, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
//			@Override
//			public void onErrorResponse(VolleyError arg0) {
//				Log.e(TAG,arg0.getMessage());
//			}
//		});
//		requestQueue.add(imageRequest);
		ImageView imageView = new ImageView(this);
						LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT
				);
				imageView.setLayoutParams(layoutParams);
		ImageLoader.getInstance().displayImage(url,imageView);
		ll_images.addView(imageView);
	}

	public static void actionStart(Context context,AppNews item){
		Intent intent = new Intent(context,NewsInfoActivity.class);

		appNews = item;
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
