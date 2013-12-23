package com.liminghui.godutch.activity;

import com.liminghui.godutch.R;
import com.liminghui.godutch.adapter.MainAdapter;
import com.liminghui.godutch.baseactivity.FrameActivity;
import com.liminghui.godutch.control.SlideMenuItem;
import com.liminghui.godutch.control.SlideMenuView.OnSlideMenuItemListener;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class MainActivity extends FrameActivity implements
		OnSlideMenuItemListener, OnItemClickListener {

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
		//createSlideMenu(R.array.SlideMenuActivityMain);
	}

	private void initVariable() {
		mMainAdapter = new MainAdapter(this);
	}

	private void initView() {
		gv_main_body = (GridView) findViewById(R.id.gv_main_body);
		setTopTitle(getString(R.string.top_title));
	}

	private void initListeners() {
		gv_main_body.setOnItemClickListener(this);
	}

	private void bindData() {
		gv_main_body.setAdapter(mMainAdapter);
	}

	@Override
	public void MyOnItemClickListener(View view, SlideMenuItem item) {
		Toast.makeText(this, item.getTitle(), 0).show();
	}

	// �������GridView����¼�
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String menuName = parent.getAdapter().getItem(position).toString();
		if (menuName.equals(getString(R.string.gv_stock_manage))) {
			// showMsg("aaa");
			openActivity(StockActivity.class);
			return;
		}
	}

}
