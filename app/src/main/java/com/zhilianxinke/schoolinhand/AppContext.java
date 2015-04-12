package com.zhilianxinke.schoolinhand;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.sea_monster.core.common.Const;
import com.sea_monster.core.network.DefaultHttpHandler;
import com.sea_monster.core.network.HttpHandler;
import com.sea_monster.core.resource.io.FileSysHandler;
import com.sea_monster.core.resource.io.IFileSysHandler;
import com.zhilianxinke.schoolinhand.api.AppApi;
import com.zhilianxinke.schoolinhand.domain.App_CustomModel;
import com.zhilianxinke.schoolinhand.domain.User;
import com.zhilianxinke.schoolinhand.rongyun.ui.WinToast;
import com.zhilianxinke.schoolinhand.util.JSONHelper;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by hh on 2015-03-25.
 */
public class AppContext {
    private static final String TAG = "AppContext";
    private static final String NOMEDIA = ".nomedia";

    private static AppContext self;
    private AppApi mDemoApi;

    private BlockingQueue<Runnable> mWorkQueue;
    private ThreadFactory mThreadFactory;
    private static ThreadPoolExecutor sExecutor;

    private IFileSysHandler mFileSysHandler;
    private static HttpHandler mHttpHandler;

    private SharedPreferences mPreferences;

    public Context mContext;

    private String mResourceDir;

    private App_CustomModel currUser;

    private ArrayList<RongIMClient.UserInfo> mUserInfos;

    private HashMap<String, RongIMClient.Group> groupMap;

    private RongIM.LocationProvider.LocationCallback mLastLocationCallback;

    public static AppContext getInstance() {

        return self;
    }

    private AppContext() {
    }


    private AppContext(Context context) {
        mContext=context;
        self = this;

        //http初始化 用于登录、注册使用
        initHttp();

        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mDemoApi = new AppApi(mHttpHandler, context);

        initGroupInfo();
    }

    public static void init(Context context) {
        self = new AppContext(context);
    }


    public App_CustomModel getCurrUser() {
        if (currUser != null) return currUser;

        if (mPreferences.contains("LOGIN_TOKEN")){
            try {
                currUser = JSONHelper.parseObject(mPreferences.getString("LOGIN_TOKEN", null), App_CustomModel.class);
            } catch (JSONException e) {
                Log.e("App",e.getMessage());
            }
        }
        return currUser;
    }

    public void setCurrUser(App_CustomModel currUser) {
        mPreferences.edit().putString("LOGIN_TOKEN", JSONHelper.toJSON(currUser)).commit();

        this.currUser = currUser;
    }

    public RongIM.LocationProvider.LocationCallback getLastLocationCallback() {
        return mLastLocationCallback;
    }

    public void setLastLocationCallback(RongIM.LocationProvider.LocationCallback lastLocationCallback) {
        this.mLastLocationCallback = lastLocationCallback;
    }

    void initHttp() {

        mWorkQueue = new PriorityBlockingQueue<Runnable>(Const.SYS.WORK_QUEUE_MAX_COUNT);
        mThreadFactory = new ThreadFactory() {
            private final AtomicInteger mCount = new AtomicInteger(1);

            public Thread newThread(Runnable r) {
                return new Thread(r, "RongCloudTask #" + mCount.getAndIncrement());
            }
        };

        sExecutor = new ThreadPoolExecutor(Const.SYS.DEF_THREAD_WORDER_COUNT, Const.SYS.MAX_THREAD_WORKER_COUNT, 1, TimeUnit.SECONDS, mWorkQueue, mThreadFactory);

        sExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());

        File location;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            location = Environment.getExternalStorageDirectory();
        } else {
            location = mContext.getFilesDir();
        }
//        location.mkdirs();

        // New Handler
        mFileSysHandler = new FileSysHandler(sExecutor, location, "RongCloud", "img");
        mHttpHandler = new DefaultHttpHandler(mContext, sExecutor);
    }


    /**
     * 临时存放用户数据
     *
     * @param userInfos
     */
    public void setFriends(ArrayList<RongIMClient.UserInfo> userInfos) {

        this.mUserInfos = userInfos;
    }


    /**
     * 获取用户信息
     *
     * @param userId
     * @return
     */
    public RongIMClient.UserInfo getUserInfoById(String userId) {

        RongIMClient.UserInfo userInfoReturn = null;

        if (!TextUtils.isEmpty(userId) && mUserInfos != null) {
            for (RongIMClient.UserInfo userInfo : mUserInfos) {

                if (userId.equals(userInfo.getUserId())) {
                    userInfoReturn = userInfo;
                    break;
                }

            }
        }
        return userInfoReturn;
    }

    /**
     * 获取用户信息列表
     *
     * @param userIds
     * @return
     */
    public List<RongIMClient.UserInfo> getUserInfoByIds(String[] userIds) {

        List<RongIMClient.UserInfo> userInfoList = new ArrayList<RongIMClient.UserInfo>();

        if (userIds != null && userIds.length > 0) {
            for (String userId : userIds) {
                for (RongIMClient.UserInfo userInfo : mUserInfos) {
                    if (userId.equals(userInfo.getUserId())) {
                        userInfoList.add(userInfo);
                    }
                }
            }
        }
        return userInfoList;
    }

    public AppApi getDemoApi() {
        return mDemoApi;
    }


    private final String getFileSysDir(String dir) {

        if (!TextUtils.isEmpty(mResourceDir)) {
            return mResourceDir;
        }

        File environmentPath = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            environmentPath = Environment.getExternalStorageDirectory();
        } else {
            environmentPath = mContext.getFilesDir();
        }

        File baseDirectory = new File(environmentPath, dir);

        createDirectory(baseDirectory);

        return mResourceDir = baseDirectory.getAbsolutePath();

    }

    private static final void createDirectory(File storageDirectory) {

        if (!storageDirectory.exists()) {

            Log.d(TAG, "Trying to create storageDirectory: " + String.valueOf(storageDirectory.mkdirs()));

            Log.d(TAG, "Exists: " + storageDirectory + " " + String.valueOf(storageDirectory.exists()));
            Log.d(TAG, "State: " + Environment.getExternalStorageState());
            Log.d(TAG, "Isdir: " + storageDirectory + " " + String.valueOf(storageDirectory.isDirectory()));
            Log.d(TAG, "Readable: " + storageDirectory + " " + String.valueOf(storageDirectory.canRead()));
            Log.d(TAG, "Writable: " + storageDirectory + " " + String.valueOf(storageDirectory.canWrite()));

            File tmp = storageDirectory.getParentFile();

            Log.d(TAG, "Exists: " + tmp + " " + String.valueOf(tmp.exists()));
            Log.d(TAG, "Isdir: " + tmp + " " + String.valueOf(tmp.isDirectory()));
            Log.d(TAG, "Readable: " + tmp + " " + String.valueOf(tmp.canRead()));
            Log.d(TAG, "Writable: " + tmp + " " + String.valueOf(tmp.canWrite()));

            tmp = tmp.getParentFile();

            Log.d(TAG, "Exists: " + tmp + " " + String.valueOf(tmp.exists()));
            Log.d(TAG, "Isdir: " + tmp + " " + String.valueOf(tmp.isDirectory()));
            Log.d(TAG, "Readable: " + tmp + " " + String.valueOf(tmp.canRead()));
            Log.d(TAG, "Writable: " + tmp + " " + String.valueOf(tmp.canWrite()));

            File nomediaFile = new File(storageDirectory, NOMEDIA);

            if (!nomediaFile.exists()) {
                try {
                    Log.d(TAG, "Created file: " + nomediaFile + " " + String.valueOf(nomediaFile.createNewFile()));
                } catch (IOException e) {
                    Log.d(TAG, "Unable to create .nomedia file for some reason.", e);
                    throw new IllegalStateException("Unable to create nomedia file.");
                }
            }

            if (!(storageDirectory.isDirectory() && nomediaFile.exists())) {
                throw new RuntimeException("Unable to create storage directory and nomedia file.");
            }
        }

    }


    public SharedPreferences getSharedPreferences() {
        return mPreferences;
    }

    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.mPreferences = sharedPreferences;
    }


    public void setGroupMap(HashMap<String, RongIMClient.Group> groupMap) {
        this.groupMap = groupMap;
    }

    public HashMap<String, RongIMClient.Group> getGroupMap() {
        return groupMap;
    }


    private void initGroupInfo() {


        RongIMClient.Group group1 = new RongIMClient.Group("group001", "群组一", "http://www.yjz9.com/uploadfile/2014/0807/20140807114030812.jpg");
        RongIMClient.Group group2 = new RongIMClient.Group("group002", "群组二", "http://www.yjz9.com/uploadfile/2014/0330/20140330023925331.jpg");
        RongIMClient.Group group3 = new RongIMClient.Group("group003", "群组三", "http://www.yjz9.com/uploadfile/2014/0921/20140921013004454.jpg");
        List<RongIMClient.Group> groups = new ArrayList<RongIMClient.Group>();
        groups.add(group1);
        groups.add(group2);
        groups.add(group3);

        HashMap<String, RongIMClient.Group> groupM = new HashMap<String, RongIMClient.Group>();
        groupM.put("group001", group1);
        groupM.put("group002", group2);
        groupM.put("group003", group3);

        if (AppContext.getInstance() != null)
            AppContext.getInstance().setGroupMap(groupM);
        else
            throw new RuntimeException("同步群组异常");
    }

    public ArrayList<RongIMClient.UserInfo> getUserInfos() {
        return mUserInfos;
    }

    public void setUserInfos(ArrayList<RongIMClient.UserInfo> userInfos) {
        mUserInfos = userInfos;
    }
}
