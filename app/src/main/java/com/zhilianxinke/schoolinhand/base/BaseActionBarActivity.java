package com.zhilianxinke.schoolinhand.base;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.zhilianxinke.schoolinhand.R;

/**
 * Created by hh on 2015-05-04.
 */
public abstract class BaseActionBarActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);

    }

    protected <T extends View> T getViewById(int id) {
        return (T) findViewById(id);
    }

    protected abstract int setContentViewResId();

    protected abstract void initView();

    protected abstract void initData();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}

