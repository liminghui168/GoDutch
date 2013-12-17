package com.liminghui.godutch.adapter;

import java.util.List;

import com.liminghui.godutch.R;
import com.liminghui.godutch.domain.Stock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class StockAdapter extends BaseAdapter {

	private Context context;
	private List<Stock> list;

	private class Holder {
		TextView tv_main_stock_body_id;
		TextView tv_main_stock_body_brand;
		TextView tv_main_stock_body_stock_code;
		TextView tv_main_stock_body_vendor_modelNo;
		TextView tv_main_stock_body_client_modelNo;
	}

	// String[] name = new String[] { "1111", "2222", "3333", "4444" };

	public StockAdapter(Context context) {
		this.context = context;
	}

	public List<Stock> getList() {
		return list;
	}

	public void setList(List<Stock> list) {
		this.list = list;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder;
		if (convertView == null) {
			LayoutInflater mInflater = LayoutInflater.from(context);
			convertView = mInflater.inflate(R.layout.main_stock_body_list_item,
					null);
			holder = new Holder();
			holder.tv_main_stock_body_id = (TextView) convertView
					.findViewById(R.id.tv_main_stock_body_id);
			holder.tv_main_stock_body_brand = (TextView) convertView
					.findViewById(R.id.tv_main_stock_body_brand);
			holder.tv_main_stock_body_stock_code = (TextView) convertView
					.findViewById(R.id.tv_main_stock_body_stock_code);
			holder.tv_main_stock_body_vendor_modelNo = (TextView) convertView
					.findViewById(R.id.tv_main_stock_body_vendor_modelNo);
			holder.tv_main_stock_body_client_modelNo = (TextView) convertView
					.findViewById(R.id.tv_main_stock_body_client_modelNo);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		Stock stock = list.get(position);
		holder.tv_main_stock_body_id.setText(stock.getId() + "");
		holder.tv_main_stock_body_brand.setText(stock.getBrand());
		holder.tv_main_stock_body_stock_code.setText(stock.getStock());
		holder.tv_main_stock_body_vendor_modelNo.setText(stock.getVref());
		holder.tv_main_stock_body_client_modelNo.setText(stock.getPartno());
		return convertView;
	}

}
