package com.liminghui.godutch.baseactivity;

import com.liminghui.godutch.R;
import com.liminghui.godutch.control.SlideMenuItem;
import com.liminghui.godutch.control.SlideMenuView;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class FrameActivity extends Activity {
	
	private SlideMenuView menuView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
	}
	
	protected void appendMainBody(int resId) {
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.lay_main_body);
		View view = LayoutInflater.from(this).inflate(resId, null);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,RelativeLayout.LayoutParams.FILL_PARENT);
		linearLayout.addView(view, layoutParams);
	}
	
	protected void createSlideMenu(int resId) {
		menuView = new SlideMenuView(this);
		String[] menuItem = getResources().getStringArray(resId);
		for (int i = 0; i < menuItem.length; i++) {
			SlideMenuItem item = new SlideMenuItem(i,menuItem[i]);
			menuView.add(item);
		}
		
		menuView.bindData();
	}
	
}
