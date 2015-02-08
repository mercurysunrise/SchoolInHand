package com.zhilianxinke.schoolinhand.util;

import android.os.Handler;

public class UpdateUtil {

	private static String TAG = "UpdateUtil";
//	public static boolean isNeedUpdate(){
//		String apkFilePath="sdcard/schoolInHand.apk";  //安装包路径
//		PackageManager pm = getPackageManager();
//		PackageInfo info = pm.getPackageArchiveInfo(apkFilePath, PackageManager.GET_ACTIVITIES);
//		if(info != null){
//			String sdcardVersion = info.versionCode;       //得到版本信息
//			Log.v(TAG, "Version="+sdcardVersion);
//		}
//		return false;
//	}
//
//	public Handler handler = new Handler() {
//		public void handleMessage(Message msg) {
//		super.handleMessage(msg);
//		Dialog dialog = new AlertDialog.Builder(MainActivity.this).setTitle("系统更新").setMessage("发现新版本，请更新！")
//		// 设置内容.setPositiveButton("确定",// 设置确定按钮new DialogInterface.OnClickListener() {
//		@Override
//		public void onClick(DialogInterface dialog, int which) {
//		pBar = new ProgressDialog(MainActivity.this);
//		pBar.setTitle("正在下载");pBar.setMessage("请稍候...");
//		pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);downFile(apkPath);}}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//		public void onClick(DialogInterface dialog, int whichButton) {
//		// 点击"取消"按钮操作}}).create();// 创建
//		// 显示对话框
//		dialog.show();
//		        }
//		};
//
//		public void update() {
//			Intent intent = new Intent(Intent.ACTION_VIEW);
//			intent.setDataAndType(Uri.fromFile(new File("/sdcard/demo.apk")),"application/vnd.android.package-archive");
//			startActivity(intent);
//			}
}
