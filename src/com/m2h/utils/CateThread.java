package com.m2h.utils;

import java.util.ArrayList;
import java.util.List;

import com.m2h.bean.ListItem;

public class CateThread extends Thread {

	private String devbaseURL = "http://mhbb.mhedu.sh.cn:8080/hdwiki/index.php";
	private List<ListItem> myList = new ArrayList<ListItem>();
	private int cid;
	
	public CateThread(int cid) {
		super();
		this.cid = cid;
	}


	@Override
	public void run() {
		List<ListItem> list = GetJsonUtils.getJsonData(devbaseURL
				+ "?app-get_sub_cate-" + String.valueOf(cid), "GET");
		if (list != null) {
			myList = list;
		}
	}

	public List<ListItem> getMyList() {
		return myList;
	}

	public void setMyList(List<ListItem> myList) {
		this.myList = myList;
	}

}
