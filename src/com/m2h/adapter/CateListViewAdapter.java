package com.m2h.adapter;

import java.util.ArrayList;
import java.util.List;

import com.m2h.activity.R;
import com.m2h.bean.ListItem;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CateListViewAdapter extends BaseAdapter {
	private int TOP_LEVEL = 0;
	private int SUB_LEVEL = 1;
	private int THIRD_LEVEL = 2;

	private Context context;
	private List<ListItem> catList;
	private int selectposition = -1;
	private ImageView imageView;
	private int level = -1;

	public CateListViewAdapter() {
		super();
		catList = new ArrayList<ListItem>();
	}

	public CateListViewAdapter(Context context, List<ListItem> list, int level) {
		super();
		this.context = context;
		catList = list;
		this.level = level;
	}

	public int getCount() {
		return catList.size();
	}

	public Object getItem(int position) {
		return catList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.cate_item, null);
		}

		TextView cateTitle = (TextView) convertView
				.findViewById(R.id.cate_name);
		imageView = (ImageView) convertView.findViewById(R.id.image);

		ListItem item = catList.get(position);

		int flag = item.getFlag();
		if (flag == 0) {
			imageView.setImageResource(R.drawable.ic_cate);

			if (level == TOP_LEVEL) {
				convertView.setBackgroundColor(Color.parseColor("#F8F8F8"));
				if (selectposition == position) {
					convertView.setBackgroundColor(Color.parseColor("#EFEDEE"));
					cateTitle.setTextColor(Color.BLUE);
				} else {
					convertView.setBackgroundColor(Color.parseColor("#F8F8F8"));
					cateTitle.setTextColor(Color.BLACK);
				}

			} else if (level == SUB_LEVEL) {
				convertView.setBackgroundColor(Color.parseColor("#EFEDEE"));
				if (selectposition == position) {
					convertView.setBackgroundColor(Color.parseColor("#E7E3E2"));
					cateTitle.setTextColor(Color.BLUE);
				} else {
					convertView.setBackgroundColor(Color.parseColor("#EFEDEE"));
					cateTitle.setTextColor(Color.BLACK);
				}
			} else if (level == THIRD_LEVEL) {
				convertView.setBackgroundColor(Color.parseColor("#E7E3E2"));
				if (selectposition == position) {
					convertView.setBackgroundColor(Color.parseColor("#00edfc"));
					cateTitle.setTextColor(Color.BLUE);
				} else {
					convertView.setBackgroundColor(Color.parseColor("#E7E3E2"));
					cateTitle.setTextColor(Color.BLACK);
				}
			} /*else {
				convertView.setBackgroundColor(Color.parseColor("#F8F8F8"));
				if (selectposition == position) {
					convertView.setBackgroundColor(Color.parseColor("#00edfc"));
					cateTitle.setTextColor(Color.BLUE);
				} else {
					convertView.setBackgroundColor(Color.parseColor("#F8F8F8"));
					cateTitle.setTextColor(Color.BLACK);
				}
			}*/

			cateTitle.setText(item.getName());
		}

		return convertView;

	}

	public void setSelectposition(int position) {
		this.selectposition = position;
	}

}
