package com.zhilianxinke.schoolinhand.modules.news.adapters;

import android.nfc.Tag;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.zhilianxinke.schoolinhand.modules.news.Class2_NewsInfoFragment;
import com.zhilianxinke.schoolinhand.modules.news.Normal_NewsInfoFragment;
import com.zhilianxinke.schoolinhand.modules.news.School_NewsInfoFragment;
import com.zhilianxinke.schoolinhand.modules.news.System_NewsInfoFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hh on 2015-02-09.
 */
public class NewsFragmentPagerAdpter extends FragmentPagerAdapter {

    private final static String TAG = "NewsFragmentPagerAdpter";
    public NewsFragmentPagerAdpter(FragmentManager fm) {
        super(fm);
//        for (int i = 0;i<titles.length;i++){
//            Normal_NewsInfoFragment normal_NewsInfoFragment = new Normal_NewsInfoFragment();
//            normal_NewsInfoFragment.setTitle(titles[i]);
//            Log.d(TAG,"add+"+titles[i]);
//            fragmentList.add(normal_NewsInfoFragment);
//        }
    }

    private final String[] titles = { "班级", "校园", "系统" };
    private Map<String,Normal_NewsInfoFragment> fragmentMap = new HashMap<String, Normal_NewsInfoFragment>(titles.length);
//    private List<Normal_NewsInfoFragment> fragmentList = new ArrayList<Normal_NewsInfoFragment>(titles.length);

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
//        boolean isExist = (fragmentList.size() > position && fragmentList.get(0) != null);
//        if (!isExist){
//             Normal_NewsInfoFragment normal_NewsInfoFragment = new Normal_NewsInfoFragment();
//            normal_NewsInfoFragment.setTitle(titles[position]);
//            fragmentList.add(normal_NewsInfoFragment);
//        }
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