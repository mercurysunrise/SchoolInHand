package com.zhilianxinke.schoolinhand.modules.news;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhilianxinke.schoolinhand.App;
import com.zhilianxinke.schoolinhand.AppContext;
import com.zhilianxinke.schoolinhand.R;
import com.zhilianxinke.schoolinhand.base.BaseActivity;
import com.zhilianxinke.schoolinhand.domain.AppNews;
import com.zhilianxinke.schoolinhand.rongyun.ui.WinToast;
import com.zhilianxinke.schoolinhand.util.StaticRes;
import com.zhilianxinke.schoolinhand.util.UrlBuilder;

import java.util.LinkedList;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.UriFragment;
import io.rong.imlib.model.Conversation;

public class NewsInfoActivity extends BaseActivity  {

    private static final String TAG = "NewsInfoActivity";
	private TextView tv_top_title;
	private TextView btn_title_share;
	private TextView btn_title_left;
	
	private TextView tv_news_title;
	private TextView tv_news_publicman;

	private TextView tv_news_publicTime;

	private TextView tv_news_content;

	private LinearLayout ll_images;
	DisplayImageOptions options;        // DisplayImageOptions是用于设置图片显示的类

	private static AppNews appNews;
	private static Normal_NewsInfoFragment parent;
    @Override
    protected int setContentViewResId() {
        return R.layout.activity_newsinfo;
    }

    @Override
    protected void initView(){
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

		if (appNews.getCover() != null && appNews.getCover().toString().length() > 10){
			tv_news_publicTime.setText(appNews.getCover().toString().substring(0,10));
		}
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle(appNews.getTitle());

		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeAsUpIndicator(R.drawable.de_actionbar_back);

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

		ImageView imageView = new ImageView(this);
						LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT
				);
				imageView.setLayoutParams(layoutParams);
		ImageLoader.getInstance().displayImage(url,imageView);
		ll_images.addView(imageView);
	}

	public static void actionStart(Context context,AppNews item,Normal_NewsInfoFragment parentView){
		Intent intent = new Intent(context,NewsInfoActivity.class);

		appNews = item;
		parent = parentView;
		context.startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		if (AppContext.getAppUser().getIdentity().equals("家长")){
			inflater.inflate(R.menu.main_news, menu);
		}else{
			inflater.inflate(R.menu.main_news_parent, menu);
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.it_favorite:
				appNews.setFavorite(true);
				LinkedList<AppNews> favoriteList = App.current.newsData.get("收藏");
				boolean exist = false;
				for(AppNews appNews : favoriteList){
					if(appNews.getId().equals(appNews.getId())){
						exist = true;
					}
				}
				if(!exist){
					App.current.newsData.get("收藏").addFirst(appNews);
					App.cacheSave("收藏");
				}
				break;
//			case R.id.it_remove:

//				break;
			case android.R.id.home:
				finish();

				break;
		}

		return super.onOptionsItemSelected(item);
	}



}
