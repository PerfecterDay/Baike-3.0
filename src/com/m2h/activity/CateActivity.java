package com.m2h.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.m2h.adapter.CateListViewAdapter;
import com.m2h.bean.ListItem;
import com.m2h.utils.CateThread;


public class CateActivity extends Activity {

	private ListView topcateLV;
	private ListView subcateLV;
	private ListView thirdcateLV;
	private int oriCid;
	private String title;

	// 三个List数据源
	private List<ListItem> topList;
	private List<ListItem> subList;
	private List<ListItem> thirdList;

	// 三个适配器Adaptor
	private CateListViewAdapter topAdapter;
	private CateListViewAdapter subAdapter;
	private CateListViewAdapter thirdAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_category);

		initView();
		initData();
		
		getActionBar().setTitle(title);
		
		initTopLv();
		intiEvent();
	}

	private void intiEvent() {
		topcateLV.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {

				// // 设置背景色
				topAdapter.setSelectposition(position);
				topAdapter.notifyDataSetChanged();

				// 获取点击的ListItem对象，并获取ID、name等...
				ListItem itemSelected = topList.get(position);

				int cid = itemSelected.getId();
				String cate_title = itemSelected.getName();
				// 获取对应ID分类下的子分类和词条信息
				initSubLv(cid);
				if (0 == subList.size()) {
					// 跳转到分类浏览页面
					Intent intent = new Intent(CateActivity.this,ShowActivity.class);

					// 用Bundle携带数据
					Bundle bundle = new Bundle();
					bundle.putInt("cid", cid);
					bundle.putInt("flag", 0);
					bundle.putString("title", cate_title);
					intent.putExtras(bundle);

					startActivity(intent);
					subcateLV.setVisibility(View.GONE);
					return;
				} else {
					subcateLV.setVisibility(View.VISIBLE);
					// subcateLV.setBackgroundResource(R.color.ic_pressed);
					System.out.println("_---------------------");
					thirdcateLV.setVisibility(View.GONE);
				}

			}

		});

		subcateLV.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {

				// // 设置背景色
				subAdapter.setSelectposition(position);
				subAdapter.notifyDataSetChanged();

				// 获取点击的ListItem对象，并获取ID、name等...
				ListItem itemSelected = subList.get(position);

				int cid = itemSelected.getId();
				String cate_title = itemSelected.getName();
				// 获取对应ID分类下的子分类和词条信息
				initThirdLv(cid);
				if (0 == thirdList.size()) {
					// 跳转到分类浏览页面
					Intent intent = new Intent(CateActivity.this,ShowActivity.class);

					// 用Bundle携带数据
					Bundle bundle = new Bundle();
					bundle.putInt("cid", cid);
					bundle.putInt("flag", 0);
					bundle.putString("title", cate_title);
					intent.putExtras(bundle);

					startActivity(intent);
					thirdcateLV.setVisibility(View.GONE);
					return;
				} else {
					thirdcateLV.setVisibility(View.VISIBLE);
					// subcateLV.setBackgroundResource(R.color.ic_pressed);
					System.out.println("_---------------------");
				}

			}

		});

		thirdcateLV.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {

				// 跳转到浏览分类页
				ListItem itemObject = thirdList.get(position);

				int cid = itemObject.getId();
				String cate_title = itemObject.getName();
				
				Intent intent = new Intent(CateActivity.this,
						ShowActivity.class);

				// 用Bundle携带数据
				Bundle bundle = new Bundle();
				bundle.putInt("cid", cid);
				bundle.putInt("flag", 0);
				bundle.putString("title", cate_title);
				intent.putExtras(bundle);

				startActivity(intent);
			}

		});
	}

	private void initData() {
		// 新页面接收数据
		Bundle bundle = this.getIntent().getExtras();
		oriCid = bundle.getInt("cid");
		title = bundle.getString("title");
		// 初始化List数据源
		topList = new ArrayList<ListItem>();
		topList = (List<ListItem>) bundle.getSerializable("subCate");
		subList = new ArrayList<ListItem>();
		thirdList = new ArrayList<ListItem>();

	}

	private void initView() {
		topcateLV = (ListView) findViewById(R.id.topcateListView);
		subcateLV = (ListView) findViewById(R.id.subcateListView);
		thirdcateLV = (ListView) findViewById(R.id.thirdcateListview);

	}

	private void initTopLv() {
//		CateThread topThread = new CateThread(oriCid);
//		topThread.start();
//		try {
//			topThread.join();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		topList = topThread.getMyList();
//		
		topAdapter = new CateListViewAdapter(this, topList, 0);
		topcateLV.setAdapter(topAdapter);
	}

	private void initSubLv(int cid) {
		CateThread subThread = new CateThread(cid);
		subThread.start();
		try {
			subThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		subList = subThread.getMyList();

		subAdapter = new CateListViewAdapter(this, subList, 1);
		subcateLV.setAdapter(subAdapter);
	}

	private void initThirdLv(int cid) {
		CateThread thread = new CateThread(cid);
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		thirdList = thread.getMyList();

		thirdAdapter = new CateListViewAdapter(this, thirdList, 2);
		thirdcateLV.setAdapter(thirdAdapter);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		
		case R.id.action_settings:					//单击刷新，获取侧边栏及刷新Webview	
			initTopLv();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
