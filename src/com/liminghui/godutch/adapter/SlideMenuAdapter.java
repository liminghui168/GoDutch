package com.liminghui.godutch.adapter;

import java.util.List;

import com.liminghui.godutch.R;
import com.liminghui.godutch.control.SlideMenuItem;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SlideMenuAdapter extends MyBaseAdapter {
	
	public SlideMenuAdapter(Context context, List list) {
		super(context, list);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = GetInflater().inflate(R.layout.slidemenu_bottom_box_list_item, null);
		TextView tv_bottom_menu_name = (TextView) convertView.findViewById(R.id.tv_bottom_menu_name);
		
		SlideMenuItem item = (SlideMenuItem) GetList().get(position);
		
		tv_bottom_menu_name.setText(item.getTitle());
		
		return convertView;
	}

}
