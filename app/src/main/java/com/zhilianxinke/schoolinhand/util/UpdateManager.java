package com.zhilianxinke.schoolinhand.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.zhilianxinke.schoolinhand.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.zhilianxinke.schoolinhand.domain.AppVersion;
import com.zhilianxinke.schoolinhand.domain.SdkHttpResult;
import com.zhilianxinke.schoolinhand.modules.autoupdate.ApkUpdateModel;
import com.zhilianxinke.schoolinhand.modules.autoupdate.HttpUtils;
import com.zhilianxinke.schoolinhand.modules.autoupdate.IUpdateObject;
import com.zhilianxinke.schoolinhand.modules.autoupdate.PackageUtils;
import com.zhilianxinke.schoolinhand.modules.autoupdate.ParseXmlService;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *@author xuhao
 *@date 2015-03-06
 */

public class UpdateManager
{
    private static String TAG = "UpdateManager";
    /* 下载中 */
    private static final int DOWNLOAD = 1;
    /* 下载结束 */
    private static final int DOWNLOAD_FINISH = 2;

    /* 下载保存路径 */
    private String mSavePath;
    /* 记录进度条数量 */
    private int progress;
    /* 是否取消更新 */
    private boolean cancelUpdate = false;

    private Context mContext;
    private int versionCode;
    private String versionName;

    private AppVersion _appVersion;

    private boolean mIsShowResult;

    /* 更新进度条 */
    private ProgressBar mProgress;
    private Dialog mDownloadDialog;

    private Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                // 正在下载
                case DOWNLOAD:
                    // 设置进度条位置
                    mProgress.setProgress(progress);
                    break;
                case DOWNLOAD_FINISH:
                    // 安装文件
                    installApk();
                    break;
                default:
                    break;
            }
        };
    };

    public void QueryApkVersion(Context context,boolean isShowResult){
        mContext = context;
        mIsShowResult = isShowResult;
        if (buildVersionInfo()){
            new QueryUpdateTask().execute();
        }
    }


    private class QueryUpdateTask extends AsyncTask<Void, Void, AppVersion> {

        @Override
        protected AppVersion doInBackground(Void... params) {
            Map<String,String> map = new HashMap<String,String>(1);
            map.put("type","Android");
            String url = UrlBuilder.build("/api/lastVer", map);
            JSONObject jsonObject = HttpUtils.getJSONObj(url);
            _appVersion = JSON.parseObject(jsonObject.toString(),AppVersion.class);

                    return _appVersion;

        }

        @Override
        protected void onPostExecute(AppVersion apkUpdateModel) {
            if (apkUpdateModel != null){
//                boolean isNeedUpdate = apkUpdateModel.getVersionCode() != versionCode;
                boolean isNeedUpdate = !apkUpdateModel.getName().equals(versionName);
                if (isNeedUpdate){
                    // 显示提示对话框
                    showNoticeDialog(apkUpdateModel.getFeature());
                }
                if (!isNeedUpdate && mIsShowResult){
                    Toast.makeText(mContext, R.string.soft_update_no, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /**
     * 获取软件版本信息
     *
     * @return
     */
    private boolean buildVersionInfo()
    {
        try
        {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
            PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);

            versionCode = packageInfo.versionCode;
            versionName = packageInfo.versionName;
            return true;
        } catch (NameNotFoundException e)
        {
            Log.e(TAG,"获取Apk版本错误",e);
        }
        return false;
    }

    /**
     * 显示软件更新对话框
     */
    private void showNoticeDialog(String noticeContent)
    {
        // 构造对话框
        AlertDialog.Builder builder = new Builder(mContext);
//        builder.setTitle(R.string.soft_update_title);
//        builder.setMessage(R.string.soft_update_info);
        builder.setTitle(R.string.soft_update_info);

        builder.setItems(noticeContent.split("\n"),null);
        // 更新
        builder.setPositiveButton(R.string.soft_update_updatebtn, new OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                // 显示下载对话框
                showDownloadDialog();
            }
        });
        // 稍后更新
        builder.setNegativeButton(R.string.soft_update_later, new OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        Dialog noticeDialog = builder.create();
        noticeDialog.show();
    }

    /**
     * 显示软件下载对话框
     */
    private void showDownloadDialog()
    {
        // 构造软件下载对话框
        AlertDialog.Builder builder = new Builder(mContext);
        builder.setTitle(R.string.soft_updating);
        // 给下载对话框增加进度条
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.softupdate_progress, null);
        mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
        builder.setView(v);
        // 取消更新
        builder.setNegativeButton(R.string.soft_update_cancel, new OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                // 设置取消状态
                cancelUpdate = true;
            }
        });
        mDownloadDialog = builder.create();
        mDownloadDialog.show();
        // 现在文件
        downloadApk();
    }

    /**
     * 下载apk文件
     */
    private void downloadApk()
    {
        // 启动新线程下载软件
        new downloadApkThread().start();
    }

    /**
     * 下载文件线程
     *
     * @author coolszy
     *@date 2012-4-26
     *@blog http://blog.92coding.com
     */
    private class downloadApkThread extends Thread
    {
        @Override
        public void run()
        {
            try
            {
                // 判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
                {
                    // 获得存储卡的路径
                    String sdpath = Environment.getExternalStorageDirectory() + "/";
                    mSavePath = sdpath + "download";

                    URL url = new URL(_appVersion.getUrl());
                    // 创建连接
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    // 获取文件大小
                    int length = conn.getContentLength();
                    // 创建输入流
                    InputStream is = conn.getInputStream();

                    File file = new File(mSavePath);
                    // 判断文件目录是否存在
                    if (!file.exists())
                    {
                        file.mkdir();
                    }
                    File apkFile = new File(mSavePath, "schoolInHand.apk");
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    int count = 0;
                    // 缓存
                    byte buf[] = new byte[1024];
                    // 写入到文件中
                    do
                    {
                        int numread = is.read(buf);
                        count += numread;
                        // 计算进度条位置
                        progress = (int) (((float) count / length) * 100);
                        // 更新进度
                        mHandler.sendEmptyMessage(DOWNLOAD);
                        if (numread <= 0)
                        {
                            // 下载完成
                            mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                            break;
                        }
                        // 写入文件
                        fos.write(buf, 0, numread);
                    } while (!cancelUpdate);// 点击取消就停止下载.
                    fos.close();
                    is.close();
                }
            } catch (MalformedURLException e)
            {
                e.printStackTrace();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            // 取消下载对话框显示
            mDownloadDialog.dismiss();
        }
    };

    /**
     * 安装APK文件
     */
    private void installApk()
    {
        File apkfile = new File(mSavePath, "schoolInHand.apk");
        if (!apkfile.exists())
        {
            return;
        }
        // 通过Intent安装APK文件
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        mContext.startActivity(i);
    }
}
