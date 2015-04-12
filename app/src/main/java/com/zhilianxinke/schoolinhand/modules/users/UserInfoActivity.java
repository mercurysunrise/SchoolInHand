package com.zhilianxinke.schoolinhand.modules.users;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.zhilianxinke.schoolinhand.AppContext;
import com.zhilianxinke.schoolinhand.LoginActivity;
import com.zhilianxinke.schoolinhand.R;
import com.zhilianxinke.schoolinhand.base.BaseActivity;
import com.zhilianxinke.schoolinhand.rongyun.ui.WinToast;

import io.rong.imkit.RongIM;
import io.rong.imkit.utils.Util;
import io.rong.imkit.view.ActionBar;
import io.rong.imlib.RongIMClient;

public class UserInfoActivity extends BaseActivity implements OnClickListener {
	private String TAG = "UserInfoActivity";
	ActionBar mActionBar;
	private Button mAddBlack;
	private TextView mUserInfo;
	String title = null;
	String userId = null;
	private Button mRemoveBlack;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    mAddBlack.setVisibility(View.GONE);
                    mRemoveBlack.setVisibility(View.VISIBLE);
                    mUserInfo.setText(title+"已被加入黑名单，无法收到此用户消息" );
                    break;
                case 2:
                    mAddBlack.setVisibility(View.VISIBLE);
                    mRemoveBlack.setVisibility(View.GONE);
                    mUserInfo.setText(title );
                    break;

            }
        }
    };
	@Override
	protected int setContentViewResId() {
		return R.layout.user_info;
	}

	@Override
	protected void initView() {

        mActionBar = (ActionBar) findViewById(android.R.id.custom);
        mUserInfo = (TextView) findViewById(R.id.user_name_tv);
        mAddBlack = (Button) findViewById(R.id.add_black);
        mRemoveBlack = (Button) findViewById(R.id.remove_black);
        mActionBar.setOnBackClick(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

            if (getIntent().getExtras() != null) {
                title = getIntent().getExtras().getString("user_name");
                userId = getIntent().getExtras().getString("user_id");
                mActionBar.getTitleTextView().setText(title + "用户详情");
            } else {
                mActionBar.getTitleTextView().setText("用户详情");
            }
    }

	@Override
	protected void initData() {
		mAddBlack.setOnClickListener(this);
		mRemoveBlack.setOnClickListener(this);
            if (RongIM.getInstance() != null && Util.getNetWorkType(this) != -1) {
                if (userId.equals(AppContext.getInstance().getCurrUser().getPk())) {
//                    if (userId.equals(LoginActivity.mUserID)) {
                    mUserInfo.setVisibility(View.GONE);
                } else {
                    RongIM.getInstance().getBlacklistStatus(userId,
                            new RongIM.GetUserBlacklistCallback() {

                                @Override
                                public void onError(ErrorCode errorCode) {
                                    Log.e(TAG,
                                            "-------getBlacklistStatus onError---------:"
                                                    + errorCode.getMessage() + ",:" + errorCode.getValue());
                                    mUserInfo.setText(errorCode + "");
                                }

                                @Override
                                public void onSuccess(RongIMClient.BlacklistStatus status) {
                                    Log.e(TAG,
                                            "-------getBlacklistStatus onSuccess---------:"
                                                    + status);
                                    if (status == RongIMClient.BlacklistStatus.NOT_EXIT_BLACK_LIST) {
                                        mUserInfo.setText(title);

                                        mAddBlack.setVisibility(View.VISIBLE);
                                    } else if (status == RongIMClient.BlacklistStatus.EXIT_BLACK_LIST) {
                                        mUserInfo.setText(title + "已被加入黑名单，无法收到此用户消息");
                                        mRemoveBlack.setVisibility(View.VISIBLE);
                                    }

                                }

                            });
                }
            }else{
                WinToast.toast(UserInfoActivity.this, R.string.network_not);
            }
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_black:
			if (RongIM.getInstance() != null && Util.getNetWorkType(this) != -1 ) {
				RongIM.getInstance().addToBlacklist(userId,
						new RongIM.OperationCallback() {

							@Override
							public void onError(ErrorCode errorCode) {
								Log.e(TAG,
										"-------addToBlacklist onError----------:"
												+ errorCode.getMessage());	
								Log.e(TAG,
														"-------addToBlacklist onError---------:"
																+ errorCode.getValue());
							}

							@Override
							public void onSuccess() {
								Log.e(TAG, "-------添加到黑名单--------:");

                               Message msg = Message.obtain();
                                msg.what = 1;
                                mHandler.sendMessage(msg);

                            }

						});
			}else{
                WinToast.toast(this,R.string.network_not);
            }
			break;
		case R.id.remove_black:
			if (RongIM.getInstance() != null  && Util.getNetWorkType(this) != -1) {
				RongIM.getInstance().removeFromBlacklist(userId,
						new RongIM.OperationCallback() {

							@Override
							public void onError(ErrorCode errorCode) {
								Log.e(TAG,
										"------- removeFromBlacklist onError-------:"
												+ errorCode);
							}

							@Override
							public void onSuccess() {
								Log.e(TAG, "-------被移除黑名单-------:");
                                Message msg = Message.obtain();
                                msg.what = 2;
                                mHandler.sendMessage(msg);
							}

						});
			}else{
                WinToast.toast(this,R.string.network_not);
            }
			break;
		default:
			break;
		}

	}

}
