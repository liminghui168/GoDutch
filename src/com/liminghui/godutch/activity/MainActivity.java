package com.liminghui.godutch.activity;

import com.liminghui.godutch.R;
import com.liminghui.godutch.adapter.MainAdapter;
import com.liminghui.godutch.baseactivity.FrameActivity;

import android.os.Bundle;
import android.widget.GridView;

public class MainActivity extends FrameActivity {
	
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

}
