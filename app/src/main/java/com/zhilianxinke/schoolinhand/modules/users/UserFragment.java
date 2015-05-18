package com.zhilianxinke.schoolinhand.modules.users;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View.OnClickListener;

import com.zhilianxinke.schoolinhand.App;
import com.zhilianxinke.schoolinhand.LoginActivity;
import com.zhilianxinke.schoolinhand.MainActivity;
import com.zhilianxinke.schoolinhand.util.UpdateManager;
import com.zhilianxinke.schoolinhand.R;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.message.RichContentMessage;


public class UserFragment extends Fragment implements OnClickListener{

    public UserFragment() {
        // Required empty public constructor
    }

    private View view;

    private String customerServiceId = "KEFU1426171503766";

    private Button btnCustomerService;
    private Button btnUpdate;
    private Button btnLogout;
    private Button btnExit;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user, container, false);

        btnCustomerService = (Button) view.findViewById(R.id.btnCustomerService);
        btnCustomerService.setOnClickListener(this);

        btnUpdate = (Button) view.findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(this);

        btnLogout = (Button) view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(this);

        btnExit = (Button) view.findViewById(R.id.btnExit);
        btnExit.setOnClickListener(this);

        return view;
    }

    // 用来实现 UI 线程的更新。
//    Handler mHandler;
    @Override
    public void onClick(View v)
    {
        if (v == btnCustomerService){
//            Intent intent = new Intent(getActivity(),UserInfoActivity.class);
//            startActivity(intent);
//            mHandler.post(new Runnable() {
//                              @Override
//                              public void run() {
//                                  String customerServiceId = "KEFU1426171503766";
//                                  if (RongIM.getInstance() != null)
////                                      RongIM.getInstance().startCustomerServiceChat(this, "kefu114", "客服");
//                                  RongIM.getInstance().startCustomerServiceChat(getActivity(), customerServiceId, "在线客服");
//                              }
//                          }
//            );

//            if (RongIM.getInstance() != null) {
//
//                String content = "新华网莫斯科10月14日电 国务院总理李克强14日出席第三届莫斯科国际创新发展论坛并发表题为《以创新实现共同发展包容发展》的演讲。演讲全文如下：创新是人类发展进步的不熄引擎。当今世界正处于大变革、大调整之中，迫切要求更大范围、更深层次的创新。实现这样的创新，墨守成规不行，单打独斗也不行，需要开放、合作与分享。6年前，面对国际金融危机，国际社会同舟共济，避免了危机向纵深蔓延。随着经济全球化、社会信息化的深入推进，更需要各国携起手来，在合作创新中实现知识的倍增、价值的倍增，解决发展的难题，促进共同繁荣。这正是开放式创新的意义所在。";
//                String title = "新华网莫斯科10月14日电,李克强在第三届莫斯科国际创新发展论坛上的演讲";
//                String url = "http://img2.cache.netease.com/photo/0003/2014-10-15/900x600_A8J6CVA400AJ0003.jpg";
//                RichContentMessage imageTextMessage = new RichContentMessage(title, content, url);
//                imageTextMessage.setExtra("可以存放的网址，商品编号或URI,在点击消息时你可以取到进入你的商品页面");


//                RongIM.getInstance().sendMessage(RongIMClient.ConversationType.PRIVATE, "11", imageTextMessage, new RongIMClient.SendMessageCallback() {
//
//                            @Override
//                            public void onSuccess(int messageId) {
//
//                            }
//
//                            @Override
//                            public void onError(int messageId, ErrorCode errorCode) {
//
//                            }
//
//                            @Override
//                            public void onProgress(int messageId, int percent) {
//
//                            }
//                        }
//                );
//                RongIM.getInstance().sendMessage(RongIMClient.ConversationType.CUSTOMER_SERVICE, "kefu114", imageTextMessage, new RongIMClient.SendMessageCallback() {
//
//                            @Override
//                            public void onSuccess(int messageId) {
//
//                            }
//
//                            @Override
//                            public void onError(int messageId, ErrorCode errorCode) {
//
//                            }
//
//                            @Override
//                            public void onProgress(int messageId, int percent) {
//
//                            }
//                        }
//                );
                RongIM.getInstance().startCustomerServiceChat(getActivity(), customerServiceId, "客服");

        }
        if (v == btnUpdate){
            UpdateManager manager = new UpdateManager();
            manager.QueryApkVersion(getActivity(),true);
        }
        if (v == btnLogout){
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
        if (v == btnExit){
            App.finishAllActivities();
        }
    }



}
