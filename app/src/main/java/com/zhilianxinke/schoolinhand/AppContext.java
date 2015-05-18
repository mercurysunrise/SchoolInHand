package com.zhilianxinke.schoolinhand;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.zhilianxinke.schoolinhand.api.AppApi;
import com.zhilianxinke.schoolinhand.domain.AppGroup;
import com.zhilianxinke.schoolinhand.domain.AppUser;
import com.zhilianxinke.schoolinhand.modules.maps.SOSOLocationActivity;
import com.zhilianxinke.schoolinhand.util.CacheUtils;
import com.zhilianxinke.schoolinhand.util.JSONHelper;
import org.json.JSONException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;
import me.add1.network.Const;
import me.add1.network.DefaultHttpHandler;
import me.add1.network.HttpHandler;

/**
 * Created by hh on 2015-03-25.
 */
public class AppContext {
    private static final String TAG = "AppContext";
    public static Context mContext;
    private static HashMap<String, Group> groupMap;
    private static ArrayList<UserInfo> mUserInfos;
    private static ArrayList<UserInfo> mFriendInfos;
    private static SharedPreferences mPreferences;
    private static RongIM.LocationProvider.LocationCallback mLastLocationCallback;

    private static AppUser mAppUser;
    private static String groupCacheFile;

//    private DemoContext(Context context) {
//        mContext = context;
//        mDemoContext = this;
//        //http初始化 用于登录、注册使用
//        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//
//        RongIM.setLocationProvider(new LocationProvider());
//
//        mDemoApi = new DemoApi(context);
//    }

    public static void init(Context context) {
        mContext = context;
        //http初始化 用于登录、注册使用
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        RongIM.setLocationProvider(new LocationProvider());

        if (mAppUser == null) {
            mAppUser = getAppUser();
            if (mAppUser != null) {
                groupCacheFile = mAppUser.getId() + "_Cache";
            }
        }
//        mDemoApi = new DemoApi(context);
    }

    public static AppUser getAppUser() {
        if (mAppUser != null) return mAppUser;

        if (mPreferences.contains("LOGIN_TOKEN")){
            try {
                mAppUser = JSONHelper.parseObject(mPreferences.getString("LOGIN_TOKEN", null), AppUser.class);
            } catch (JSONException e) {
                Log.e("App",e.getMessage());
            }
        }
        return mAppUser;
    }

    public static void setAppUser(final AppUser appUser) {
        mPreferences.edit().putString("LOGIN_TOKEN",JSONHelper.toJSON(appUser)).commit();
        mAppUser = appUser;

        groupCacheFile = appUser.getId() + "_Cache";
    }

    public static SharedPreferences getSharedPreferences() {
        return mPreferences;
    }

    public static void setAppGroupList(ArrayList<AppGroup> groupList) {
        //缓存到本地
        CacheUtils.saveObject(mContext,groupList,groupCacheFile);
        //转型为rong中的类型
        List<Group> grouplist = new ArrayList<>();
        HashMap<String, Group> groupM = new HashMap<String, Group>();
        for(AppGroup appGroup : groupList){
            String id = appGroup.getId();
            String name = appGroup.getName();
            Group group = new Group(id, name, null);
            grouplist.add(group);
            groupM.put(id, group);
            Log.e("login", "------get Group name---------" + name);
        }
        groupMap = groupMap;
    }

    public static HashMap<String, Group> getGroupMap() {
        if (groupMap != null) {
            return groupMap;
        }
        if (CacheUtils.isExistDataCache(mContext,groupCacheFile)) {
            List<AppGroup> groups = (ArrayList<AppGroup>)CacheUtils.readObject(mContext,groupCacheFile);
            groupMap = new HashMap<String,Group>(groups.size());
            for (AppGroup appGroup : groups) {
                Group group = new Group(appGroup.getId(),appGroup.getName(),null);
                groupMap.put(appGroup.getId(),group);
            }
        }
        return groupMap;
    }


    public static ArrayList<UserInfo> getUserInfos() {
        return mUserInfos;
    }

    public static void setUserInfos(ArrayList<UserInfo> userInfos) {
        mUserInfos = userInfos;
    }

    /**
     * 临时存放用户数据
     *
     * @param userInfos
     */
    public static void setFriends(ArrayList<UserInfo> userInfos) {

        mFriendInfos = userInfos;
    }

    public static ArrayList<UserInfo> getFriends() {
        return mFriendInfos;
    }


    /**
     * 获取用户信息
     *
     * @param userId
     * @return
     */
    public static UserInfo getUserInfoById(String userId) {

        UserInfo userInfoReturn = null;

        if (!TextUtils.isEmpty(userId) && mUserInfos != null) {
            for (UserInfo userInfo : mUserInfos) {

                if (userId.equals(userInfo.getUserId())) {
                    userInfoReturn = userInfo;
                    break;
                }

            }
        }
        return userInfoReturn;
    }

    /**
     * 通过userid 获得username
     *
     * @param userId
     * @return
     */
    public static String getUserNameByUserId(String userId) {
        UserInfo userInfoReturn = null;
        if (!TextUtils.isEmpty(userId) && mUserInfos != null) {
            for (UserInfo userInfo : mUserInfos) {

                if (userId.equals(userInfo.getUserId())) {
                    userInfoReturn = userInfo;
                    break;
                }
            }
        }
        return userInfoReturn.getName();
    }

    /**
     * 获取用户信息列表
     *
     * @param userIds
     * @return
     */
    public static List<UserInfo> getUserInfoByIds(String[] userIds) {

        List<UserInfo> userInfoList = new ArrayList<UserInfo>();

        if (userIds != null && userIds.length > 0) {
            for (String userId : userIds) {
                for (UserInfo userInfo : mUserInfos) {
                    Log.e("", "0409-------getUserInfoByIds-" + userInfo.getUserId() + "---userid;" + userId);
                    if (userId.equals(userInfo.getUserId())) {
                        Log.e("", "0409-------getUserInfoByIds-" + userInfo.getName());
                        userInfoList.add(userInfo);
                    }
                }
            }
        }
        return userInfoList;
    }

    /**
     * 通过groupid 获得groupname
     *
     * @param groupid
     * @return
     */
    public static String getGroupNameById(String groupid) {
        HashMap<String, Group> groupMap = getGroupMap();
        if (groupMap.containsKey(groupid)){
            return groupMap.get(groupid).getName();
        }
        return "未找到群组名称";
    }


    public static RongIM.LocationProvider.LocationCallback getLastLocationCallback() {
        return mLastLocationCallback;
    }

    public static void setLastLocationCallback(RongIM.LocationProvider.LocationCallback lastLocationCallback) {
        mLastLocationCallback = lastLocationCallback;
    }

    private static class LocationProvider implements RongIM.LocationProvider {

        /**
         * 位置信息提供者:LocationProvider 的回调方法，打开第三方地图页面。
         *
         * @param context  上下文
         * @param callback 回调
         */
        @Override
        public void onStartLocation(Context context, RongIM.LocationProvider.LocationCallback callback) {
            /**
             * demo 代码  开发者需替换成自己的代码。
             */
            AppContext.setLastLocationCallback(callback);
            Intent intent = new Intent(context, SOSOLocationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);//SOSO地图
        }
    }
}
