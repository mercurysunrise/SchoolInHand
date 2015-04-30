package com.zhilianxinke.schoolinhand.modules.customservices;

import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

import com.zhilianxinke.schoolinhand.R;
import com.zhilianxinke.schoolinhand.base.BaseFragmentActivity;

/**
*
*/
public class CustomServiceActivity extends BaseFragmentActivity {

//    private ActionBar mActionBar;
    private ImageView mSettingView;
    private PopupWindow mPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        String customerServiceId = "KEFU1426171503766";
        getIntent().setData(Uri.parse("rong://io.rong.imkit.demo").buildUpon().appendPath("conversation").appendPath("customer_service")
                .appendQueryParameter("targetId", getString(R.string.CUSTOMER_SERVICE)).appendQueryParameter("title", "客服").build());


        setContentView(R.layout.activity_custom_service);

//        mActionBar = (ActionBar) findViewById(R.id.rc_actionbar);
//        mActionBar.getTitleTextView().setText("客服");
//
//        mActionBar.setOnBackClick(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });


//        View view = LayoutInflater.from(this).inflate(Res.getInstance(this).layout("rc_action_bar_conversation_settings"), mActionBar, false);
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

//        mActionBar.addView(mSettingView);
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
        TextView exitTextView = (TextView) view.findViewById(R.id.exit);

        settingTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                RongIM.getInstance().startConversationSetting(CustomServiceActivity.this, Conversation.ConversationType.CUSTOMER_SERVICE, getString(R.string.CUSTOMER_SERVICE));
                mPopupWindow.dismiss();
            }
        });

        exitTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                /**
                 * 发送一个SuspendMessage消息就退出了当前客服
                 */
//                RongIM.getInstance().sendMessage(RongIMClient.ConversationType.CUSTOMER_SERVICE, getString(R.string.CUSTOMER_SERVICE), new SuspendMessage(), new RongIMClient.SendMessageCallback() {
//
//                    @Override
//                    public void onSuccess(int messageId) {
//                        CustomServiceActivity.this.finish();
//                    }
//
//                    @Override
//                    public void onError(int messageId, ErrorCode errorCode) {
//
//                    }
//
//                    @Override
//                    public void onProgress(int messageId, int percent) {
//
//                    }
//                });

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

}