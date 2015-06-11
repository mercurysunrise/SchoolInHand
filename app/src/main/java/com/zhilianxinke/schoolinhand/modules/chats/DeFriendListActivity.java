package com.zhilianxinke.schoolinhand.modules.chats;

import android.support.v7.app.ActionBar;
import android.widget.EditText;

import com.zhilianxinke.schoolinhand.R;
import com.zhilianxinke.schoolinhand.base.BaseActivity;


/**
 * Created by Bob on 2015/3/18.
 */
public class DeFriendListActivity extends BaseActivity {

    private EditText mSearch;

    @Override
    protected int setContentViewResId() {

        return R.layout.de_ui_list_test;
    }

    @Override
    protected void initView() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.drawable.de_ic_logo);
        actionBar .setDisplayHomeAsUpEnabled(true);
        actionBar.hide();
//        actionBar.setBackgroundDrawable(R.color.rc_action_color);
//        mSearch = (EditText) findViewById(R.id.de_ui_search);
    }

    @Override
    protected void initData() {

    }

}
