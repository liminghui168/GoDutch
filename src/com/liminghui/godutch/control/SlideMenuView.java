package com.liminghui.godutch.control;

import java.util.ArrayList;
import java.util.List;

import com.liminghui.godutch.R;
import com.liminghui.godutch.adapter.SlideMenuAdapter;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class SlideMenuView {
	
	private Activity activity;
	private List menuList;
	private boolean isClosed;
	private RelativeLayout il_bottom_box;
	private OnSlideMenuItemListener mOnSlideMenuItemListener;
	
	public interface OnSlideMenuItemListener{
		public abstract void MyOnItemClickListener(View view,SlideMenuItem item);
	}
	
	public SlideMenuView(Activity activity){
		this.activity = activity;
		mOnSlideMenuItemListener = (OnSlideMenuItemListener) activity;
		initVariable();
		initView();
		initListeners();
	}
	
	private void initVariable(){
		menuList = new ArrayList();
		isClosed = true;
	}
	
	private void initView(){
		il_bottom_box = (RelativeLayout) activity.findViewById(R.id.il_bottom_box);
	}
	
	private void initListeners(){
		il_bottom_box.setOnClickListener(new MyOnClickListener());
	}
	
	private void open(){
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,RelativeLayout.LayoutParams.FILL_PARENT);
		layoutParams.addRule(RelativeLayout.BELOW, R.id.il_top_title);
		
		il_bottom_box.setLayoutParams(layoutParams);
		isClosed = false;
	}
	
	private void close(){
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,68);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		il_bottom_box.setLayoutParams(layoutParams);
		isClosed = true;
	}
	
	private void toggle(){
		if(isClosed)
			open();
		else
			close();
	}
	
	public void add(SlideMenuItem slideMenuItem){
		menuList.add(slideMenuItem);
	}
	
	public void bindData(){
		SlideMenuAdapter mAdapter = new SlideMenuAdapter(activity, menuList);
		ListView lv_slide_list = (ListView) activity.findViewById(R.id.lv_slide_list);
		lv_slide_list.setAdapter(mAdapter);
		lv_slide_list.setOnItemClickListener(new MyOnItemClickListener());
	}
	
	
	private class MyOnClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			toggle();
		}
		
	}
	
	private class MyOnItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			SlideMenuItem item = (SlideMenuItem) parent.getItemAtPosition(position);
			mOnSlideMenuItemListener.MyOnItemClickListener(view, item);
		}
		
	}
	
}
