package com.zhilianxinke.schoolinhand.modules.chats;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zhilianxinke.schoolinhand.AppContext;
import com.zhilianxinke.schoolinhand.R;
import com.zhilianxinke.schoolinhand.adapters.DeFriendMultiChoiceAdapter;
import com.zhilianxinke.schoolinhand.rongyun.ui.LoadingDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

public class DeFriendMultiChoiceFragment extends DeFriendListFragment implements Handler.Callback {


    private static final int HANDLE_UPDATE_CONFIRM_BUTTON = 10001;

    private DeFriendMultiChoiceAdapter.MutilChoiceCallback mCallback;

    //    private TextView mConfirmTextView;
    private String mConfirmFromatString;
    private ArrayList<String> mMemberIds;
    private Conversation.ConversationType mConversationType;
    private String mDiscussionId;
    private Handler mHandle;
    private LoadingDialog mLoadingDialog = null;
    private Button selectButton;
    private LinearLayout mLinearFinish;
    String targetIds = null;
    String[] targets = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mMemberIds = new ArrayList<String>();


        if (getActivity().getIntent() == null || getActivity().getIntent().getData() == null || !getActivity().getIntent().getData().getScheme().equals("rong")) {
            mConversationType = Conversation.ConversationType.PRIVATE;
        } else {
            Uri uri = getActivity().getIntent().getData();
            mDiscussionId = uri.getQueryParameter("discussionId");
            targetIds = uri.getQueryParameter("userIds");
            String delimiter = uri.getQueryParameter("delimiter");
            if (TextUtils.isEmpty(delimiter)) {
                delimiter = ",";
            }

            mConversationType = Conversation.ConversationType
                    .valueOf(uri.getLastPathSegment().toUpperCase(Locale.getDefault()));

            if (TextUtils.isEmpty(targetIds)) {
            } else {
                String[] ids = targetIds.split(delimiter);

                for (String item : Arrays.asList(ids)) {
                    mMemberIds.add(item);
                }
            }
        }
        Log.e("", "----demoacitivity  targetId----:" + ",targetIds----" + targetIds + ",mConversationType--" + mConversationType + ",mDiscussionId---" + mDiscussionId);


        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setMultiChoice(true, new ArrayList<String>(mMemberIds));

        selectButton = (Button) view.findViewById(R.id.send_message_friend);
        ImageView tilefinish = (ImageView) view.findViewById(R.id.send_message_finish);
        mLinearFinish = (LinearLayout) view.findViewById(R.id.liner_click);
        mLinearFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPeopleComplete();
            }
        });
        if (targetIds != null) {
            targets = targetIds.split(",");
            selectButtonShowStyle(targets.length);
        } else {
            selectButtonShowStyle(0);
        }
        mHandle = new Handler(this);

        super.onViewCreated(view, savedInstanceState);

    }

    private void selectButtonShowStyle(int selectedCount) {
        if (selectedCount > 0) {
            selectButton.setEnabled(true);
            mConfirmFromatString = getResources().getString(R.string.friend_list_multi_choice_comfirt_btn);
            selectButton.setTextColor(getResources().getColor(R.color.rc_text_color_secondary_inverse));
            selectButton.setText(String.format(mConfirmFromatString, selectedCount));

        } else {
            selectButton.setEnabled(false);
            mConfirmFromatString = getResources().getString(R.string.friend_list_multi_choice_comfirt_btn);
            selectButton.setText(String.format(mConfirmFromatString, 0));
        }
    }

    private final void selectPeopleComplete() {
        if (mAdapter == null)
            return;
        ArrayList<UserInfo> userInfos = ((DeFriendMultiChoiceAdapter) mAdapter).getChoiceUserInfos();

        int selected = 0;
        if (mMemberIds != null) {
            selected = mMemberIds.size();
        }

        if (!outOfMaxPrompt(userInfos.size() + selected))
            return;

        List<String> ids = new ArrayList<String>();

        if (userInfos.size() == 0) {
            getActivity().finish();
            return;
        }

        if (mConversationType == Conversation.ConversationType.DISCUSSION || userInfos.size() + mMemberIds.size() > 1) {
            StringBuilder sb = new StringBuilder();

            String userid = AppContext.getAppUser().getId();
//            if (AppContext.getInstance() != null) {
                if (true) {

//                userid = AppContext.getSharedPreferences().getString("DEMO_USERID", null);

                UserInfo userInfo = AppContext.getUserInfoById(userid);
//            }
                for (UserInfo item : userInfos) {
                    ids.add(item.getUserId());

                    if (sb.length() <= 60) {
                        if (sb.length() > 0 && !TextUtils.isEmpty(item.getName()))
                            sb.append(",");
                        sb.append(item.getName());
                    }
                }
                if (sb.length() <= 60 && userInfo != null) {
                    sb.append(",");
                    sb.append(userInfo.getName());
                }
                if (mMemberIds.size() == 0) {
                    RongIM.getInstance().createDiscussionChat(getActivity(), ids, sb.toString());
                    getActivity().finish();
                } else {
                    mLoadingDialog = new LoadingDialog(this.getActivity());
//                mLoadingDialog.setText(Res.getInstance(getActivity()).string("rc_public_data_process"));
                    mLoadingDialog.show();

                    if (!TextUtils.isEmpty(mDiscussionId)) {

                        RongIM.getInstance().getRongClient().addMemberToDiscussion(mDiscussionId, ids, new RongIMClient.OperationCallback() {
                            @Override
                            public void onSuccess() {
                                if (mLoadingDialog != null) {
                                    mLoadingDialog.dismiss();
                                }

                                getActivity().finish();
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {
                                if (mLoadingDialog != null) {
                                    mLoadingDialog.dismiss();
                                }
                            }
                        });
                    } else {
                        ids.addAll(mMemberIds);
                        RongIM.getInstance().createDiscussionChat(getActivity(), ids, sb.toString());
                    }
                }
            }
        } else if (mConversationType == Conversation.ConversationType.PRIVATE) {
            RongIM.getInstance().startPrivateChat(getActivity(), userInfos.get(0).getUserId(), userInfos.get(0).getName());
            getActivity().finish();
            return;
        }

    }

    /**
     * 超出好友上线提示
     *
     * @param count
     * @return
     */
    private boolean outOfMaxPrompt(int count) {

//        int setMaxCount = getActivity().getResources().getInteger(Res.getInstance(getActivity()).integer("discussion_member_max_count"));
//        String countFormat = getActivity().getResources().getString(Res.getInstance(getActivity()).string("friend_multi_choice_people_max_prompt"));
//
//        if (count >= setMaxCount) {
//            RongToast.toast(this.getActivity(), String.format(countFormat, setMaxCount));
//            return false;
//        } else if (count >= RongConst.SYS.DISCUSSION_PEOPLE_MAX_COUNT) {
//            RongToast.toast(this.getActivity(), String.format(countFormat, RongConst.SYS.DISCUSSION_PEOPLE_MAX_COUNT));
//            return false;
//        }

        return true;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (mCallback == null) {

            mCallback = new DeFriendMultiChoiceAdapter.MutilChoiceCallback() {

                @Override
                public void callback(int count) {
                    boolean isShow = outOfMaxPrompt(count);

                    if (!isShow) {
                        return;
                    } else {

                        if (targetIds != null)
                            mHandle.obtainMessage(HANDLE_UPDATE_CONFIRM_BUTTON, count+targets.length - mMemberIds.size()).sendToTarget();
                        else
                            mHandle.obtainMessage(HANDLE_UPDATE_CONFIRM_BUTTON, count - mMemberIds.size()).sendToTarget();
                    }
                }
            };
        }

        DeFriendMultiChoiceAdapter adapter = (DeFriendMultiChoiceAdapter) mAdapter;
        adapter.setCallback((DeFriendMultiChoiceAdapter.MutilChoiceCallback) mCallback);

        super.onItemClick(parent, view, position, id);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == HANDLE_UPDATE_CONFIRM_BUTTON) {

            selectButtonShowStyle((Integer) msg.obj);
        }
        return false;
    }
}
