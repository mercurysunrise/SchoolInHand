package com.zhilianxinke.schoolinhand;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.zhilianxinke.schoolinhand.domain.App_DeviceInfoModel;
import com.zhilianxinke.schoolinhand.modules.vedios.MediaPlayerActivity;
import com.zhilianxinke.schoolinhand.util.JSONHelper;
import com.zhilianxinke.schoolinhand.util.StaticRes;

/**
 * 视频Fragment的界面
 * 
 * http://blog.csdn.net/guolin_blog/article/details/26365683
 * 
 * @author xuhao
 */
public class MediaFragment extends Fragment {
	
	private static String TAG = "MediaFragment";
	private ListView lv_newsInfo;
	private View view;
	

	private static List<App_DeviceInfoModel> _app_deviceInfoModels;
	private static SimpleAdapter _adapter;
	
	private int[] imageIDs;
	private String[] titles;
	private ArrayList<View> dots;
	private TextView title;
	private RollViewPager rollViewPager;
	private ArrayList<String> uriList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.view_list_devices, container, false);
		initView();
		return view;
	}

	public void initView() {

		lv_newsInfo = (ListView) view.findViewById(R.id.lv_listNormal);

		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();

		params.add(new BasicNameValuePair("pk", StaticRes.currCustom.getPk()));
		// 对参数编码
		final String param = URLEncodedUtils.format(params, "UTF-8");

		// baseUrl
		final String baseUrl = StaticRes.baseUrl + "/deviceInfo/appDeviceInfo";

		if (_adapter == null) {
			final HttpClient httpClient = new DefaultHttpClient();

			new Thread() {
				public void run() {
					try {
						HttpGet getMethod = new HttpGet(baseUrl + "?" + param);
						HttpResponse response = httpClient.execute(getMethod); // 发起GET请求

						String result = EntityUtils.toString(response.getEntity(),
								"utf-8");
						_app_deviceInfoModels = (List<App_DeviceInfoModel>) JSONHelper
								.parseCollection(result, List.class,
										App_DeviceInfoModel.class);

						handler.post(runnable);
					} catch (ClientProtocolException e) {
						Log.e(TAG, "ClientProtocolException", e);
					} catch (IOException e) {
						Log.e(TAG, "IOException", e);
					} catch (JSONException e) {
						Log.e(TAG, "JSONException", e);
					}
				}
			}.start();
		}else {
			refreshList();
		}

		title = (TextView) view.findViewById(R.id.title);
		rollViewPager = (RollViewPager) view.findViewById(R.id.viewpager);
		
		imageIDs = new int[] { R.drawable.a, R.drawable.b, R.drawable.c,
				R.drawable.d, R.drawable.e };

		// 图片名称
		titles = new String[] { "校园图片1", "校园图片2", "校园图片3", "校园图片4",
				"校园图片5" };

		// 用来显示的点
		dots = new ArrayList<View>();
		dots.add(view.findViewById(R.id.dot_0));
		dots.add(view.findViewById(R.id.dot_1));
		dots.add(view.findViewById(R.id.dot_2));
		dots.add(view.findViewById(R.id.dot_3));
		dots.add(view.findViewById(R.id.dot_4));

		// 构造网络图片
		uriList = new ArrayList<String>();
//		uriList.add("http://h.hiphotos.baidu.com/album/w%3D2048/sign=730e7fdf95eef01f4d141fc5d4c69825/94cad1c8a786c917b8bf9482c83d70cf3ac757c9.jpg");
//		uriList.add("http://g.hiphotos.baidu.com/album/w%3D2048/sign=00d4819db8014a90813e41bd9d4f3812/562c11dfa9ec8a137de469cff603918fa0ecc026.jpg");
//		uriList.add("http://c.hiphotos.baidu.com/album/w%3D2048/sign=a8631adb342ac65c67056173cfcab011/b8389b504fc2d56217d11656e61190ef77c66cb4.jpg");
//		uriList.add("http://e.hiphotos.baidu.com/album/w%3D2048/sign=ffac8994a71ea8d38a227304a332314e/1ad5ad6eddc451da4d9d32c4b7fd5266d01632b1.jpg");
//		uriList.add("http://a.hiphotos.baidu.com/album/w%3D2048/sign=afbe93839a504fc2a25fb705d1e5e611/d058ccbf6c81800a99489685b03533fa838b478f.jpg");
		uriList.add("assest://image/a.jpg");
		uriList.add("assest://image/b.jpg");
		uriList.add("assest://image/c.jpg");
		uriList.add("assest://image/d.jpg");
		uriList.add("assest://image/e.jpg");

//		RollViewPager mViewPager = new RollViewPager(this, dots,
//				R.drawable.dot_focus, R.drawable.dot_normal,
//				new OnPagerClickCallback() {
//					@Override
//					public void onPagerClick(int position) {
//						Toast.makeText(MainActivity2.this,
//								"第" + position + "张图片被点击了", 0).show();
//					}
//				});
		
		rollViewPager.setResImageIds(imageIDs);//设置res的图片id
//		rollViewPager.setUriList(uriList);//设置网络图片的url
		
		rollViewPager.setDot(dots, R.drawable.dot_focus, R.drawable.dot_normal);
//		rollViewPager.setPagerCallback(new RollViewPager.OnPagerClickCallback() {
//			
//			@Override
//			public void onPagerClick(int position) {
//				// TODO Auto-generated method stub
//				Toast.makeText(MainActivity2.this,
//						"第" + position + "张图片被点击了", 0).show();
//			}
//		});
		
		rollViewPager.setTitle(title, titles);//不需要显示标题，可以不设置
		rollViewPager.startRoll();//不调用的话不滚动
	}

	private Handler handler = new Handler();

	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			// 更新界面
			ArrayList<HashMap<String, String>> modelList = new ArrayList<HashMap<String, String>>();
			for (App_DeviceInfoModel app_deviceInfoModel : _app_deviceInfoModels) {
				// 新建一个 HashMap
				HashMap<String, String> map = new HashMap<String, String>();

				// 每个子节点添加到HashMap关键= >值
				map.put("title", app_deviceInfoModel.getAddress());
				map.put("artist", app_deviceInfoModel.getStrName());
				
				// HashList添加到数组列表
				modelList.add(map);
			}
			_adapter = new SimpleAdapter(view.getContext(),
					modelList, R.layout.newslist_row, new String[] { "title",
							"artist" }, new int[] { R.id.title,
							R.id.artist });
			refreshList();
		}

		
	};
	
	private void refreshList() {
		lv_newsInfo.setAdapter(_adapter);

		lv_newsInfo.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(),
						MediaPlayerActivity.class);
				App_DeviceInfoModel app_DeviceInfoModel = _app_deviceInfoModels
						.get(position);
				intent.putExtra("app_DeviceInfoModel", app_DeviceInfoModel);
				startActivity(intent);
				Log.d("点击", "" + position);
			}
		});
	}
}