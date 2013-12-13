package com.liminghui.godutch.activity;

import com.liminghui.godutch.R;
import com.liminghui.godutch.adapter.MainAdapter;
import com.liminghui.godutch.baseactivity.FrameActivity;
import com.liminghui.godutch.control.SlideMenuItem;
import com.liminghui.godutch.control.SlideMenuView.OnSlideMenuItemListener;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

public class MainActivity extends FrameActivity implements OnSlideMenuItemListener {
	
	private GridView gv_main_body;
	private MainAdapter mMainAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.main_body);
		initVariable();
		initView();
		initListeners();
		bindData();
		createSlideMenu(R.array.SlideMenuActivityMain);
	}

	private void initVariable() {
		mMainAdapter = new MainAdapter(this);
	}

	private void initView() {
		gv_main_body = (GridView) findViewById(R.id.gv_main_body);
	}

	private void initListeners() {

	}

	private void bindData() {
		gv_main_body.setAdapter(mMainAdapter);
	}

	@Override
	public void MyOnItemClickListener(View view, SlideMenuItem item) {
		// TODO Auto-generated method stub
		Toast.makeText(this, item.getTitle(), 0).show();
	}
	
	
	
}
