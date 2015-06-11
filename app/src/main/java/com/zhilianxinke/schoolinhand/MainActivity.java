package com.zhilianxinke.schoolinhand;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.*;
import android.os.Process;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.zhilianxinke.schoolinhand.base.BaseActivity;
import com.zhilianxinke.schoolinhand.base.BaseFragmentActivity;
import com.zhilianxinke.schoolinhand.modules.chats.DeFriendListActivity;
import com.zhilianxinke.schoolinhand.modules.groups.GroupFragment;
import com.zhilianxinke.schoolinhand.modules.news.AddNewsinfoActivity;
import com.zhilianxinke.schoolinhand.modules.news.NewsInfoFragment;
import com.zhilianxinke.schoolinhand.modules.stories.StoryFragment;
import com.zhilianxinke.schoolinhand.modules.users.UserFragment;
import com.zhilianxinke.schoolinhand.modules.vedios.VedioListFragment;
import com.zhilianxinke.schoolinhand.util.UpdateManager;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

/**
 * 主界面
 * @author hh
 */
public class MainActivity extends BaseActivity implements OnTabChangeListener,View.OnClickListener,ActionBar.OnMenuVisibilityListener {

    private static final String TAG = "MainActivity";

    public static final String ACTION_DMEO_RECEIVE_MESSAGE = "action_demo_receive_message";

    private FragmentTabHost fgTabHost;
    private Menu mMenu;

    private LayoutInflater mInflater;

    private Class fragmentArray[] = {NewsInfoFragment.class,VedioListFragment.class,GroupFragment.class,UserFragment.class};

    //定义数组来存放按钮图片
    private int mImageViewArray[] = {R.drawable.tab_news_btn,R.drawable.tab_vedio_btn,R.drawable.tab_story_btn,R.drawable.tab_user_btn};

    private String mTextviewArray[] = {"公告", "频道", "群组", "我的"};

    private int numbermessage = 0;
    private ReceiveMessageBroadcastReciver mBroadcastReciver;

    private ImageView mSettingView;
    private PopupWindow mPopupWindow;

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onMenuVisibilityChanged(boolean b) {

    }


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
    protected int setContentViewResId() {
        return R.layout.activity_main;
    }

    protected void initView(){
        //检查升级
        getSupportActionBar().setTitle(R.string.app_name);
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo == null || networkInfo.isAvailable())
        {
            //当前有可用网络
            UpdateManager updateManager = new UpdateManager();
            updateManager.QueryApkVersion(this,false);
        }
        else
        {
            //当前无可用网络
        }
//        //实例化布局对象
//        layoutInflater = LayoutInflater.from(this);
        // 获取布局填充器
        mInflater = (LayoutInflater) this
                .getSystemService(LAYOUT_INFLATER_SERVICE);

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

    @Override
    protected void initData() {

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
        View view = mInflater.inflate(R.layout.tab_item_view, null);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        this.mMenu = menu;
        if (AppContext.getAppUser().getIdentity().equals("家长")){
            inflater.inflate(R.menu.de_main_menu_parent, menu);
        }else{
            inflater.inflate(R.menu.de_main_menu, menu);
        }

//        if (hasNewFriends) {
//            mMenu.getItem(0).setIcon(getResources().getDrawable(R.drawable.de_ic_add_hasmessage));
//            mMenu.getItem(0).getSubMenu().getItem(2).setIcon(getResources().getDrawable(R.drawable.de_btn_main_contacts_select));
//        } else {
//            mMenu.getItem(0).setIcon(getResources().getDrawable(R.drawable.de_ic_add));
//            mMenu.getItem(0).getSubMenu().getItem(2).setIcon(getResources().getDrawable(R.drawable.de_btn_main_contacts));
//        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.it_add_news://发起公告
                Intent intent = new Intent(this,AddNewsinfoActivity.class);
                startActivity(intent);
                break;
//            case R.id.it_contacts://选择群组
//                startActivity(new Intent(MainActivity.this, DeAdressListActivity.class));
//                break;
            case R.id.it_add_chat://创建聊天
                startActivity(new Intent(this, DeFriendListActivity.class));
                break;
//            case R.id.set_item1://我的账号
//                startActivity(new Intent(MainActivity.this, MyAccountActivity.class));
//                break;
//            case R.id.set_item2://新消息提醒
//                startActivity(new Intent(MainActivity.this, NewMessageRemindActivity.class));
//                break;
//            case R.id.set_item3://隐私
//                startActivity(new Intent(MainActivity.this, PrivacyActivity.class));
//                break;
//            case R.id.set_item4://关于融云
//                startActivity(new Intent(MainActivity.this, AboutRongCloudActivity.class));
//                break;
//            case R.id.set_item5://退出
//                if (RongIM.getInstance() != null) RongIM.getInstance().disconnect(false);
//                android.os.Process.killProcess(Process.myPid());
//                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 外部启动MainActivity
     * @param context
     */
    public static void actionStart(Context context){
        Intent intent = new Intent(context,MainActivity.class);
        context.startActivity(intent);
    }


}