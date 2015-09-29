package com.m2h.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.m2h.activity.R;
import com.m2h.bean.ListItem;
import com.m2h.bean.ViewHolder;
import com.m2h.utils.ImageLoader;

public class MyGridAdapter extends BaseAdapter {
	private String devbaseURL = "http://mhbb.mhedu.sh.cn:8080/hdwiki/";
	private List<ListItem> mLists;
	private LayoutInflater mInflater;
	private ImageLoader mImageLoader;

	public MyGridAdapter(Context context, List<ListItem> mLists) {
		super();
		this.mLists = mLists;
		mInflater = LayoutInflater.from(context);
		mImageLoader = new ImageLoader();
	}

	public int getCount() {
		return mLists.size();
	}

	public Object getItem(int position) {
		return mLists.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.home_item, null);

			viewHolder.mImageView = (ImageView) convertView
					.findViewById(R.id.home_imageView);
			viewHolder.mTextView = (TextView) convertView
					.findViewById(R.id.home_textView);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		int flag = mLists.get(position).getFlag();
		if (flag == 0) {
			// 默认图片
			viewHolder.mImageView.setImageResource(R.drawable.ic_launcher);
			// 动态加载图片
			String image_url = mLists.get(position).getPicurl();
			viewHolder.mTextView.setText(mLists.get(position).getName());
//			viewHolder.mImageView.setImageResource(R.drawable.p002);
		if (!image_url.equals("")) {
				viewHolder.mImageView.setTag(devbaseURL+image_url);
				mImageLoader.showImageByAsyncTask(viewHolder.mImageView, devbaseURL+image_url);
			}else {
				image_url="cate_uploads/default.png";
				viewHolder.mImageView.setTag(devbaseURL+image_url);
				mImageLoader.showImageByAsyncTask(viewHolder.mImageView, devbaseURL+image_url);
			}
			
		}
		return convertView;
	}
}
