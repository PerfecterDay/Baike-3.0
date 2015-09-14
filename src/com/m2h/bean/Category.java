package com.m2h.bean;

public class Category extends ListItem {

	private int pid;

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public Category(int id, String name, int flag) {
		super(id, name, 0);
	}

}
