package com.liminghui.godutch.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.liminghui.godutch.R;
import com.liminghui.godutch.adapter.MainAdapter;
import com.liminghui.godutch.adapter.StockAdapter;
import com.liminghui.godutch.baseactivity.FrameActivity;

public class StockActivity extends FrameActivity implements OnItemClickListener {

	private String[] titleItems;
	private GridView gv_stock_main_body_title;
	private ListView lv_stock_main_body_list;
	private StockAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.main_stock_body);
		initVariable();
		initView();
		initListeners();
		bindData();
	}

	private void initVariable() {
		gv_stock_main_body_title = (GridView) findViewById(R.id.gv_stock_main_body_title);
		lv_stock_main_body_list = (ListView) findViewById(R.id.lv_stock_main_body_list);
		titleItems = new String[] { "Brand", "Stock Code",
				"Vendors's Model No.", "Client Model No." };
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.main_stock_body_item, R.id.tv_main_stock_body_item, titleItems);
		gv_stock_main_body_title.setAdapter(adapter);
	}

	private void initView() {
		mAdapter = new StockAdapter(this);
	}

	private void initListeners() {
		gv_stock_main_body_title.setOnItemClickListener(this);
		lv_stock_main_body_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String name =  (String) mAdapter.getItem(position);
				showMsg(name);
			}
		});
	}

	private void bindData() {
		lv_stock_main_body_list.setAdapter(mAdapter);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
	}

}
