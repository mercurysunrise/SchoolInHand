package com.zhilianxinke.schoolinhand;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import com.zhilianxinke.schoolinhand.domain.App_NewsInfoModel;
import com.zhilianxinke.schoolinhand.util.JSONHelper;
import com.zhilianxinke.schoolinhand.util.StaticRes;
import com.zhilianxinke.schoolinhand.R;

/**
 * 通告Fragment的界面
 * 
 * @author xuhao
 */
public class NewsInfoFragment extends Fragment {

	private static String TAG = "NewsInfoFragment";
	private ListView lv_newsInfo;
	private View view;
	private ProgressBar pb_list;

	private static List<App_NewsInfoModel> _app_newsInfoModels;
	private static SimpleAdapter _adapter;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.view_list_news, container, false);
		initView();
		return view;
	}

	public void initView() {

		lv_newsInfo = (ListView) view.findViewById(R.id.lv_listNormal);

		pb_list = (ProgressBar)view.findViewById(R.id.pb_list);
		pb_list.setVisibility(View.VISIBLE);
		
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();

		params.add(new BasicNameValuePair("pk", StaticRes.currCustom.getPk()));
		// 对参数编码
		final String param = URLEncodedUtils.format(params, "UTF-8");

		// baseUrl
		final String baseUrl = StaticRes.baseUrl + "/newsInfo/appNewsInfo";

		// String baseUrl =
		// "http://115.28.171.84:8080/DeviceManagement/newsInfo/appNewsInfo";

		if (_adapter == null) {
			final HttpClient httpClient = new DefaultHttpClient();

			new Thread() {
				public void run() {
					try {
						HttpGet getMethod = new HttpGet(baseUrl + "?" + param);
						HttpResponse response = httpClient.execute(getMethod); // 发起GET请求

						String result = EntityUtils.toString(response.getEntity(),
								"utf-8");
						_app_newsInfoModels = (List<App_NewsInfoModel>) JSONHelper
								.parseCollection(result, List.class,
										App_NewsInfoModel.class);

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
		
	}

	private Handler handler = new Handler();

	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			// 更新界面
			ArrayList<HashMap<String, String>> modelList = new ArrayList<HashMap<String, String>>();
			for (App_NewsInfoModel app_newsInfoModel : _app_newsInfoModels) {
				// 新建一个 HashMap
				HashMap<String, String> map = new HashMap<String, String>();

				// 每个子节点添加到HashMap关键= >值
				map.put("title", app_newsInfoModel.getTitle());
				map.put("artist", app_newsInfoModel.getPublicUserName());
				if (app_newsInfoModel.getStrPublicTime() != null) {
					map.put("duration", app_newsInfoModel.getStrPublicTime().substring(0, 10));
				}
				map.put("newsTypeName", app_newsInfoModel.getNewsTypeName());
				// HashList添加到数组列表
				modelList.add(map);
			}
			_adapter = new SimpleAdapter(view.getContext(),
					modelList, R.layout.newslist_row, new String[] { "title",
							"artist", "duration","newsTypeName" }, new int[] { R.id.title,
							R.id.artist, R.id.duration,R.id.tv_newsTypeName });
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
						NewsInfoActivity.class);
				App_NewsInfoModel app_NewsInfoModel = _app_newsInfoModels
						.get(position);
				intent.putExtra("app_NewsInfoModel", app_NewsInfoModel);
				startActivity(intent);
				Log.d("点击", "" + position);
			}
		});
		pb_list.setVisibility(View.GONE);
	}

}
