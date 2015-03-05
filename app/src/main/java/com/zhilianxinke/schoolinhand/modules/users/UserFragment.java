package com.zhilianxinke.schoolinhand.modules.users;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View.OnClickListener;

import com.zhilianxinke.schoolinhand.App;
import com.zhilianxinke.schoolinhand.util.UpdateManager;
import com.zhilianxinke.schoolinhand.R;


public class UserFragment extends Fragment implements OnClickListener{

    public UserFragment() {
        // Required empty public constructor
    }

    private View view;

    private Button btnUserInfo;
    private Button btnUpdate;
    private Button btnExit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user, container, false);

//        btnUserInfo = (Button) view.findViewById(R.id.btnUserInfo);
//        btnUserInfo.setOnClickListener(this);

        btnUpdate = (Button) view.findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(this);

        btnExit = (Button) view.findViewById(R.id.btnExit);
        btnExit.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v)
    {
        if (v == btnUserInfo){
//            Intent intent = new Intent(getActivity(),UserInfoActivity.class);
//            startActivity(intent);
        }
        if (v == btnUpdate){
            UpdateManager manager = new UpdateManager();
            manager.QueryApkVersion(getActivity(),true);
            // 检查软件更新
//            manager.checkUpdate(getActivity());
        }
        if (v == btnExit){
            App.finishAll();
        }
    }



}
