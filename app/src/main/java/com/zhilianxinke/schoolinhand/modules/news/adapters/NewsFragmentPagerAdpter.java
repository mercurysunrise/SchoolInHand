package com.zhilianxinke.schoolinhand.modules.news.adapters;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.zhilianxinke.schoolinhand.modules.news.Normal_NewsInfoFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hh on 2015-02-09.
 */
public class NewsFragmentPagerAdpter extends FragmentPagerAdapter {

    private final static String TAG = "NewsFragmentPagerAdpter";

    public NewsFragmentPagerAdpter(FragmentManager fm) {
        super(fm);

    }

//    private final String[] titles = { "班级", "校园", "活动" };
    private final String[] titles = { "最新", "推荐", "收藏" };
    private Map<String,Normal_NewsInfoFragment> fragmentMap = new HashMap<String, Normal_NewsInfoFragment>(titles.length);

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {

        final String title = titles[position];
        Log.i(TAG,"getItem+"+ title);
        if (!fragmentMap.containsKey(title)){
            Normal_NewsInfoFragment normal_NewsInfoFragment = new Normal_NewsInfoFragment();
            normal_NewsInfoFragment.setTitle(title);
            fragmentMap.put(title,normal_NewsInfoFragment);
            Log.i(TAG,"map.size()="+fragmentMap.size());
        }
        return fragmentMap.get(title);
    }

}