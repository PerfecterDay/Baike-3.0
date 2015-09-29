package com.m2h.fragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.m2h.activity.CateActivity;
import com.m2h.activity.R;
import com.m2h.activity.ShowActivity;
import com.m2h.adapter.MyGridAdapter;
import com.m2h.bean.ListItem;
import com.m2h.utils.CateThread;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class FragHome extends Fragment implements OnItemClickListener {

	private View mView;
	private GridView mgridView;
	private List<ListItem> mLists;
	private MyGridAdapter gridAdapter;
	private String devbaseURL = "http://mhbb.mhedu.sh.cn:8080/hdwiki/index.php?app-get_top_cate";

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.frag_home, null);

		mgridView = (GridView) mView.findViewById(R.id.id_home_view);

		new MygridAsyncTask().execute(devbaseURL);

		mgridView.setOnItemClickListener(this);

		return mView;
	}

	class MygridAsyncTask extends AsyncTask<String, Void, List<ListItem>> {

		@Override
		protected List<ListItem> doInBackground(String... params) {
			return getJsonData(params[0]);
		}

		@Override
		protected void onPostExecute(List<ListItem> result) {
			super.onPostExecute(result);
			mLists = result;
			gridAdapter = new MyGridAdapter(getActivity(), result);
			mgridView.setAdapter(gridAdapter);
		}

	}

	private List<ListItem> getJsonData(String url) {
		List<ListItem> newsBeanList = new ArrayList<ListItem>();
		try {
			String jsonString = readStream(new URL(url).openStream());
			 Log.i("newsString", jsonString);
			JSONObject jsonobject = new JSONObject(jsonString);

			JSONArray jsonarray = jsonobject.getJSONArray("cate");
			for (int i = 0; i < jsonarray.length(); i++) {
				JSONObject object = jsonarray.getJSONObject(i);

				int cid = object.getInt("cid");
				String name = object.getString("name");
				String Icon_url = object.getString("image");

				newsBeanList.add(new ListItem(cid, name, 0, Icon_url));
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return newsBeanList;
	}

	private String readStream(InputStream is) {
		InputStreamReader isr;
		String result = "";
		try {
			String line = "";
			isr = new InputStreamReader(is, "utf-8");
			BufferedReader br = new BufferedReader(isr);
			while ((line = br.readLine()) != null) {
				result += line;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		ListItem itemObject = mLists.get(position);

		int cid = itemObject.getId();
		String title = itemObject.getName();
		
		List<ListItem> cateList = new ArrayList<ListItem>();
		CateThread subThread = new CateThread(cid);
		subThread.start();
		try {
			subThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		cateList = subThread.getMyList();
		if (cateList.size() == 0) {
			//没有分类，跳转到浏览页面
			Intent intent = new Intent(getActivity(),ShowActivity.class);

			// 用Bundle携带数据
			Bundle bundle = new Bundle();
			bundle.putInt("cid", cid);
			bundle.putInt("flag", 0);
			bundle.putString("title", title);
			intent.putExtras(bundle);

			startActivity(intent);
		} else {
			// 跳转到分类页面，用Bundle携带数据
			Intent intent = new Intent(getActivity(), CateActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("cid", cid);
			bundle.putString("title", title);
			bundle.putSerializable("subCate", (Serializable) cateList);
			intent.putExtras(bundle);

			startActivity(intent);
		}
		
	}
	
	public void refresh() {
		new MygridAsyncTask().execute(devbaseURL);
//		gridAdapter.notifyDataSetChanged();
	}
}
