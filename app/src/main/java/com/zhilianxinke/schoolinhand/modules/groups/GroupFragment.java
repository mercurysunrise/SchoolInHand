package com.zhilianxinke.schoolinhand.modules.groups;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zhilianxinke.schoolinhand.AppContext;
import com.zhilianxinke.schoolinhand.R;
import com.zhilianxinke.schoolinhand.adapters.DeGroupListAdapter;
import com.zhilianxinke.schoolinhand.domain.rong.ApiResult;
import com.zhilianxinke.schoolinhand.domain.rong.Groups;
import com.zhilianxinke.schoolinhand.domain.rong.Status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import me.add1.network.AbstractHttpRequest;

/**
 * Created by hh on 2015-05-09.
 */
public class GroupFragment extends Fragment implements AdapterView.OnItemClickListener {

    private static final int RESULTCODE = 100;
    private ListView mGroupListView;
    private DeGroupListAdapter mDemoGroupListAdapter;
    private List<ApiResult> mResultList;
    private AbstractHttpRequest<Groups> mGetAllGroupsRequest;
    private AbstractHttpRequest<Status> mUserRequest;
    private HashMap<String, Group> mGroupMap;
    private ApiResult result;
    private Handler mHandler;

    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.de_fr_group_list, null);

        mGroupListView = (ListView) view.findViewById(R.id.de_group_list);
        mGroupListView.setItemsCanFocus(false);

        initData();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mGroupListView.setOnItemClickListener(this);
        mHandler = new Handler();
        super.onViewCreated(view, savedInstanceState);
    }

    private void initData() {
        mResultList = new ArrayList<>();

            mGroupMap = AppContext.getGroupMap();
        if (mGroupMap != null) {
            for (Group group : mGroupMap.values()) {
                ApiResult apiResult = new ApiResult();
                apiResult.setId(group.getId());
                apiResult.setName(group.getName());
                apiResult.setMax_number("200");
                apiResult.setIntroduce("普通");
                apiResult.setNumber("20");
//            apiResult.set
                mResultList.add(apiResult);
            }
        }
//            mGetAllGroupsRequest = AppContext.getDemoApi().getAllGroups(this);

        mDemoGroupListAdapter = new DeGroupListAdapter(getActivity(), mResultList, mGroupMap);
        mGroupListView.setAdapter(mDemoGroupListAdapter);

        mDemoGroupListAdapter.setOnItemButtonClick(new DeGroupListAdapter.OnItemButtonClick() {
            @Override
            public boolean onButtonClick(int position, View view) {

                result = mDemoGroupListAdapter.getItem(position);

                if (result == null)
                    return false;

//                if (mGroupMap.containsKey(result.getId())) {
                RongIM.getInstance().startGroupChat(getActivity(), result.getId(), result.getName());
//                } else {
//
//                    if (DemoContext.getInstance() != null) {
//                        mUserRequest = DemoContext.getInstance().getDemoApi().joinGroup(result.getId(), DeGroupListFragment.this);
//                    }
//
//                }
                return true;
            }
        });

        mDemoGroupListAdapter.notifyDataSetChanged();
    }

//    @Override
    public void onCallApiSuccess(final AbstractHttpRequest request, Object obj) {
//        Log.e(TAG, "----------- 获取群组列表onCallApiSuccess ----");
//        if (mGetAllGroupsRequest == request) {

//            if (obj instanceof Groups) {
//                final Groups groups = (Groups) obj;
//
//                if (groups.getCode() == 200) {
//                    for (int i = 0; i < groups.getResult().size(); i++) {
//                        mResultList.add(groups.getResult().get(i));
//                    }
//                    mDemoGroupListAdapter = new DeGroupListAdapter(getActivity(), mResultList, mGroupMap);
//                    mGroupListView.setAdapter(mDemoGroupListAdapter);
//
//                    mDemoGroupListAdapter.setOnItemButtonClick(new DeGroupListAdapter.OnItemButtonClick() {
//                        @Override
//                        public boolean onButtonClick(int position, View view) {
//
//                            result = mDemoGroupListAdapter.getItem(position);
//
//                            if (result == null)
//                                return false;
//
//                            if (mGroupMap.containsKey(result.getId())) {
//                                RongIM.getInstance().startGroupChat(getActivity(), result.getId(), result.getName());
//                            } else {
//
//                                if (DemoContext.getInstance() != null) {
//                                    mUserRequest = DemoContext.getInstance().getDemoApi().joinGroup(result.getId(), DeGroupListFragment.this);
//                                }
//
//                            }
//                            return true;
//                        }
//                    });
//
//                    mDemoGroupListAdapter.notifyDataSetChanged();
//
//                } else {
//                    WinToast.toast(getActivity(), groups.getCode());
//
//                }
//            }
//        } else if (mUserRequest == request) {
//            WinToast.toast(getActivity(), "加入群组成功");
//
//            if (result != null) {
//                updateAdapter();
//                setGroupMap(result, 1);
//
//                RongIM.getInstance().getRongClient().joinGroup(result.getId(), result.getName(), new RongIMClient.OperationCallback() {
//                    @Override
//                    public void onSuccess() {
//                        Log.e("", "04088==========================================-");
//                        RongIM.getInstance().startGroupChat(getActivity(), result.getId(), result.getName());
//                    }
//
//                    @Override
//                    public void onError(RongIMClient.ErrorCode errorCode) {
//                    }
//                });
//
//
//            }
//
//        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (resultCode) {
//            case DeConstants.GROUP_JOIN_REQUESTCODE:
//            case DeConstants.GROUP_QUIT_REQUESTCODE:
////                updateAdapter();
//                initData();
//                break;
//        }


    }

    private void updateAdapter() {
        Log.e("", "------updateAdapter------");
        if (mDemoGroupListAdapter != null) {
//            Intent intent = getActivity().getIntent();
//            Bundle bundle = intent.getExtras();
//            mGroupMap = (HashMap<String, Group>) bundle.get("result");

            mDemoGroupListAdapter = new DeGroupListAdapter(getActivity(), mResultList, mGroupMap);
            mGroupListView.setAdapter(mDemoGroupListAdapter);

            mDemoGroupListAdapter.setOnItemButtonClick(new DeGroupListAdapter.OnItemButtonClick() {
                @Override
                public boolean onButtonClick(int position, View view) {

                    result = mDemoGroupListAdapter.getItem(position);

                    if (result == null)
                        return true;

                    if (mGroupMap.containsKey(result.getId())) {
                        RongIM.getInstance().startGroupChat(getActivity(), result.getId(), result.getName());
//                        RongIM.getInstance().startGroupChat(DeGroupDetailActivity.this, mApiResult.getId(), mApiResult.getName());
                    } else {

//                        if (DemoContext.getInstance() != null) {
//                            mUserRequest = DemoContext.getInstance().getDemoApi().joinGroup(result.getId(), DeGroupListFragment.this);
//                        }

                    }
                    return true;
                }
            });
        } else {
            mDemoGroupListAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (mResultList != null && position != -1 && position < mResultList.size()) {

//            Uri uri = Uri.parse("demo://" + getActivity().getApplicationInfo().packageName).buildUpon().appendPath("conversationSetting")
//                    .appendPath(String.valueOf(Conversation.ConversationType.GROUP)).appendQueryParameter("targetId", mResultList.get(position).getId()).build();
//            Intent intent = new Intent(getActivity(), DeGroupDetailActivity.class);
//            intent.putExtra("INTENT_GROUP", mResultList.get(position));
//            intent.setData(uri);
//            startActivityForResult(intent, RESULTCODE);
            ApiResult apiResult = mResultList.get(position);
            RongIM.getInstance().startGroupChat(getActivity(), apiResult.getId(), apiResult.getName());
        }
    }


    @Override
    public void onDestroy() {
        if (mDemoGroupListAdapter != null) {
//            mDemoGroupListAdapter.destroy();
            mDemoGroupListAdapter = null;
        }
        super.onDestroy();
    }
}
