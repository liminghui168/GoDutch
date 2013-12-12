package com.liminghui.godutch.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class MyBaseAdapter extends BaseAdapter {

	private List list;
	private Context context;
	private LayoutInflater mInflater;

	public MyBaseAdapter(Context context, List list) {
		this.context = context;
		this.list = list;
		this.mInflater = LayoutInflater.from(context);
	}

	public LayoutInflater GetInflater() {
		return this.mInflater;
	}

	public Context GetContext() {
		return this.context;
	}

	public List GetList() {
		return this.list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

}
