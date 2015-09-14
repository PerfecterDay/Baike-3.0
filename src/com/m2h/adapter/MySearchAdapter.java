package com.m2h.adapter;

import java.util.List;

import com.m2h.activity.R;
import com.m2h.bean.ListItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class MySearchAdapter extends BaseAdapter {

	private List<ListItem> mLists;
	private LayoutInflater mInflater;
	
	public MySearchAdapter(Context context, List<ListItem> mLists) {
		super();
		this.mLists = mLists;
		mInflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		return mLists.size();
	}

	@Override
	public Object getItem(int position) {
		return mLists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView==null){
			convertView = mInflater.inflate(R.layout.frag_search_item, null);
		}
		
		TextView tv_search = (TextView) convertView.findViewById(R.id.tv_search_name);
		
		ListItem item = mLists.get(position);
		
		tv_search.setText(item.getName());
		
		return convertView;
	}

}
