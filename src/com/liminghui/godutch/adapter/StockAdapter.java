package com.liminghui.godutch.adapter;

import com.liminghui.godutch.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class StockAdapter extends BaseAdapter {

	private Context context;

	private class Holder {
		TextView tv_main_stock_body_brand;
		TextView tv_main_stock_body_stock_code;
		TextView tv_main_stock_body_vendor_modelNo;
		TextView tv_main_stock_body_client_modelNo;
	}

	String[] name = new String[] { "1111", "2222", "3333", "4444" };

	public StockAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return name.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return name[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder;
		if (convertView == null) {
			LayoutInflater mInflater = LayoutInflater.from(context);
			convertView = mInflater.inflate(R.layout.main_stock_body_list_item, null);
			holder = new Holder();
			holder.tv_main_stock_body_brand = (TextView) convertView.findViewById(R.id.tv_main_stock_body_brand);
			holder.tv_main_stock_body_stock_code = (TextView) convertView.findViewById(R.id.tv_main_stock_body_stock_code);
			holder.tv_main_stock_body_vendor_modelNo = (TextView) convertView.findViewById(R.id.tv_main_stock_body_vendor_modelNo);
			holder.tv_main_stock_body_client_modelNo = (TextView) convertView.findViewById(R.id.tv_main_stock_body_client_modelNo);
			convertView.setTag(holder);
		}
		else{
			holder = (Holder) convertView.getTag();
		}
		
		holder.tv_main_stock_body_brand.setText(name[position]);
		holder.tv_main_stock_body_stock_code.setText(name[position]);
		holder.tv_main_stock_body_vendor_modelNo.setText(name[position]);
		holder.tv_main_stock_body_client_modelNo.setText(name[position]);
		return convertView;
	}

}
