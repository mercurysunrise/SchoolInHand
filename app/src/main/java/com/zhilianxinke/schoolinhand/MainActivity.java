package com.zhilianxinke.schoolinhand;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import com.zhilianxinke.schoolinhand.base.BaseFragmentActivity;
import com.zhilianxinke.schoolinhand.modules.news.AddNewsinfoActivity;
import com.zhilianxinke.schoolinhand.modules.news.NewsInfoFragment;
import com.zhilianxinke.schoolinhand.modules.stories.StoryFragment;
import com.zhilianxinke.schoolinhand.modules.users.UserFragment;
import com.zhilianxinke.schoolinhand.modules.vedios.VedioListFragment;
import com.zhilianxinke.schoolinhand.util.UpdateManager;

/**
 * 主界面
 * @author hh
 */
public class MainActivity extends BaseFragmentActivity implements OnTabChangeListener {

    private static final String TAG = "MainActivity";

    public static final String ACTION_DMEO_RECEIVE_MESSAGE = "action_demo_receive_message";

    private FragmentTabHost fgTabHost;

    private LayoutInflater layoutInflater;

    private Class fragmentArray[] = {NewsInfoFragment.class,VedioListFragment.class,StoryFragment.class,UserFragment.class};

    //定义数组来存放按钮图片
    private int mImageViewArray[] = {R.drawable.tab_news_btn,R.drawable.tab_vedio_btn,R.drawable.tab_story_btn,R.drawable.tab_user_btn};

    private String mTextviewArray[] = {"公告", "频道", "群组", "我的"};

//    private ActionBar mAction;

    private int numbermessage = 0;
    private ReceiveMessageBroadcastReciver mBroadcastReciver;

    private ImageView mSettingView;
    private PopupWindow mPopupWindow;


    private class ReceiveMessageBroadcastReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_DMEO_RECEIVE_MESSAGE)) {
                numbermessage = intent.getIntExtra("rongCloud", -1);
//                initData();
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //检查升级
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo == null || !networkInfo.isAvailable())
        {
            //当前有可用网络
            UpdateManager updateManager = new UpdateManager();
            updateManager.QueryApkVersion(this,false);
        }
        else
        {
            //当前无可用网络
        }

        initView();

    }

    private void initView(){
        //实例化布局对象
        layoutInflater = LayoutInflater.from(this);

//        mAction = (ActionBar) findViewById(R.id.action_main_bar);
//        mAction.getBackView().setVisibility(View.GONE);
//        mAction.getTitleTextView().setText(R.string.app_name);
//        mAction.getTitleTextView().setTextColor(Color.WHITE);
//        mAction.getTitleTextView().setTextSize(18);

//        View view = layoutInflater.inflate(Res.getInstance(this).layout("rc_action_bar_conversation_settings"), mAction, false);
//        view.setRight(8);
//        mSettingView = (ImageView) view.findViewById(R.id.rc_conversation_settings_image);

        mSettingView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (mPopupWindow != null) {

                    if (mPopupWindow.isShowing()) {
                        mPopupWindow.dismiss();
                    } else {
                        showPopupWindow(mSettingView);
                    }
                } else {
                    showPopupWindow(mSettingView);
                }

            }
        });

//        mAction.addView(mSettingView);

        //实例化TabHost对象，得到TabHost
        fgTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        fgTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        //得到fragment的个数
        int count = fragmentArray.length;

        for(int i = 0; i < count; i++){
            //为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = fgTabHost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));
            //将Tab按钮添加进Tab选项卡中
            fgTabHost.addTab(tabSpec, fragmentArray[i], null);
            //设置Tab按钮的背景
//            fgTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_tab_background);
        }
    }

    private void showPopupWindow(View v) {

        View view = LayoutInflater.from(this).inflate(R.layout.item_popupwindow, null);

        view.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (KeyEvent.KEYCODE_BACK == keyCode && event.getAction() == KeyEvent.ACTION_DOWN) {

                    if (mPopupWindow != null && mPopupWindow.isShowing()) {
                        mPopupWindow.dismiss();
                        return true;
                    }
                }

                return false;
            }
        });

        view.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (mPopupWindow != null && mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                    return true;
                }
                return false;
            }
        });

        view.setFocusable(true);
        view.setFocusableInTouchMode(true);

        TextView settingTextView = (TextView) view.findViewById(R.id.setting);
        settingTextView.setText("创建公告");
        TextView exitTextView = (TextView) view.findViewById(R.id.exit);

        settingTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                RongIM.getInstance().startConversationSetting(CustomServiceActivity.this, RongIMClient.ConversationType.CUSTOMER_SERVICE, getString(R.string.CUSTOMER_SERVICE));
                Intent intent = new Intent(MainActivity.this,AddNewsinfoActivity.class);
                startActivity(intent);

                mPopupWindow.dismiss();
            }
        });

        exitTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mPopupWindow.dismiss();
            }
        });

        mPopupWindow = new PopupWindow(view);
        mPopupWindow.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);

        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);

        int[] location = new int[2];
        v.getLocationOnScreen(location);

        mPopupWindow.showAsDropDown(v);

    }

    /**
     * 给Tab按钮设置图标和文字
     */
    private View getTabItemView(int index){
        View view = layoutInflater.inflate(R.layout.tab_item_view, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        imageView.setImageResource(mImageViewArray[index]);

        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(mTextviewArray[index]);
//        if(index == 3){
//            msgTV = (TextView) view.findViewById(R.id.message_count);
//        }
//        Button btnTab = (Button)view.findViewById(R.id.btnTab);
//        btnTab.setBackground(getResources().getDrawable(mImageViewArray[index]));
//        btnTab.setText(mTextviewArray[index]);
        return view;
    }


    @Override
    public void onTabChanged(String currentTabTag) {
        //添加tab点击后的操作
		/*int nowTabNum = mTabHost.getCurrentTab();//获取当前点击的是第几个tab
		int num = mTabHost.getCurrentTab();
		if(num == 0 || num == 2){//第一个和第三个tab不用登陆即可查看
			lastTabTag = currentTabTag;
		} else {
			if (!SharePreferenceUtils.getPrefBoolean(MainActivity.this, "isLogin", false)) {
				 Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                 startActivity(intent);
			} else {
				lastTabTag = currentTabTag;
			}
		}*/
    }

    @Override
    protected void onResume() {
		/*if (SharePreferenceUtils.getPrefBoolean(MainActivity.this, "isLogin", false)) {//如果登陆执行的操作

        } else {//如果没有登陆，进入前一个点击的页面
            mTabHost.setCurrentTabByTag(lastTag);
        }*/
        super.onResume();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_publishNewsInfo:
//                Intent intent = new Intent(this, AddNewsinfoActivity.class);
////                intent.setData(Uri.parse("https://github.com/DenisMondon/material-design-library"));
//                startActivity(intent);
//                Log.i(TAG,"创建公告");
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }


    /**
     * 外部启动MainActivity
     * @param context
     */
    public static void actionStart(Context context){
        Intent intent = new Intent(context,MainActivity.class);
        context.startActivity(intent);
    }


}