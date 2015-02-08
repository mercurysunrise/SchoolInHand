package com.zhilianxinke.schoolinhand.modules.users;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View.OnClickListener;
import com.zhilianxinke.schoolinhand.util.UpdateManager;
import com.zhilianxinke.schoolinhand.WelcomeActivity;
import com.zhilianxinke.schoolinhand.R;


public class UserFragment extends Fragment {

    public UserFragment() {
        // Required empty public constructor
    }

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user, container, false);

        Button updateBtn = (Button) view.findViewById(R.id.btnUpdate);
        updateBtn.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                UpdateManager manager = new UpdateManager(getActivity());
                // 检查软件更新
                manager.checkUpdate(getActivity());
            }
        });


        return view;
    }



}
