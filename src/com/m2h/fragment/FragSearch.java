package com.m2h.fragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.m2h.activity.R;
import com.m2h.activity.ShowActivity;
import com.m2h.adapter.MySearchAdapter;
import com.m2h.bean.ListItem;

public class FragSearch extends Fragment implements OnClickListener,
		TextWatcher, OnItemClickListener {

	private View mView;
	private EditText mEditText;
	private ImageView mDelete;
	private Button mSearch;
	private ListView mListView;
	private List<ListItem> mSearchLists;

	private String devbaseURL = "http://mhbb.mhedu.sh.cn:8080/hdwiki/index.php";
//	private String devbaseURL = "http://192.168.1.106/hdwiki/index.php";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.frag_search, null);

		initView();

		initEvent();

		return mView;
	}

	private void initView() {
		mEditText = (EditText) mView.findViewById(R.id.id_etSearch);
		mDelete = (ImageView) mView.findViewById(R.id.id_ivDeleteText);
		mSearch = (Button) mView.findViewById(R.id.id_btn_Search);
		mListView = (ListView) mView.findViewById(R.id.id_search_content);
	}

	private void initEvent() {
		mDelete.setOnClickListener(this);
		mEditText.addTextChangedListener(this);
		mSearch.setOnClickListener(this);
		mListView.setOnItemClickListener(this);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_ivDeleteText:
			mEditText.setText("");
			break;

		case R.id.id_btn_Search:
			// 搜索 显示listview
			String et_cont = mEditText.getText().toString().trim();
			InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE); 
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			if (!et_cont.isEmpty()) {
				String url;
				try {
					url = devbaseURL + "?app-search-" + URLEncoder.encode(et_cont,"UTF-8");
					new MySearchAsyncTask().execute(url);

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				Toast.makeText(getActivity(), "请输入关键词",
						Toast.LENGTH_LONG).show();
			}
			break;
		}
	}

	/**
	 * 搜索时是否显示删除图标
	 */
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	public void afterTextChanged(Editable s) {
		if (s.length() == 0) {
			mDelete.setVisibility(View.GONE);
		} else {
			mDelete.setVisibility(View.VISIBLE);
		}
	}

	class MySearchAsyncTask extends AsyncTask<String, Void, List<ListItem>> {

		@Override
		protected List<ListItem> doInBackground(String... params) {
			return getJsonData(params[0]);
		}

		@Override
		protected void onPostExecute(List<ListItem> result) {
			super.onPostExecute(result);
			mSearchLists = result;
			if (mSearchLists.size() == 0) {
				Toast.makeText(getActivity(), "抱歉，没有相关内容，请重新输入..",
						Toast.LENGTH_LONG).show();
			} else {
				mListView
						.setAdapter(new MySearchAdapter(getActivity(), result));
			}
		}

	}

	private List<ListItem> getJsonData(String url) {
		List<ListItem> newsBeanList = new ArrayList<ListItem>();
		try {
			String jsonString = readStream(new URL(url).openStream());
			Log.i("newsString",jsonString);
			JSONObject jsonobject = new JSONObject(jsonString);
			JSONArray jsonarray = jsonobject.getJSONArray("docs");
			for (int i = 0; i < jsonarray.length(); i++) {
				JSONObject object = jsonarray.getJSONObject(i);

				int cid = object.getInt("did");
				String name = object.getString("title");
//				String Icon_url = object.getString("image");

				newsBeanList.add(new ListItem(cid, name, 1, null));
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// 跳转到浏览词条页
		ListItem itemObject = mSearchLists.get(position);

		int did = itemObject.getId();
		String title = itemObject.getName();
		Intent intent = new Intent(getActivity(), ShowActivity.class);

		// 用Bundle携带数据
		Bundle bundle = new Bundle();
		bundle.putInt("cid", did);
		bundle.putInt("flag", 1);
		bundle.putString("title", title);
		intent.putExtras(bundle);

		startActivity(intent);
	}
}
