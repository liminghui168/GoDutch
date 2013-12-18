package com.liminghui.godutch.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.Toast;

import com.liminghui.godutch.R;

public class StockDetailActivity extends TabActivity {

	private TabHost mTabHost;
	private Spinner sp_stock_detail_unit;
	private Spinner sp_stock_detail_unit_price;
	private Spinner sp_stock_detail_ref_cost;
	private Spinner sp_stock_detail_brand;
	private Spinner sp_stock_detail_category;
	private Spinner sp_stock_detail_class;
	private Spinner sp_stock_detail_sub_class;
	private Button bt_stock_detail_find_vendor;

	private String[] unitArr;// Unit数组
	private String[] unitPriceArr;// Unit Price数组
	private String[] refCostArr;// Ref.Cost数组
	private String[] brandArr;// Brand数组
	private String[] categoryArr; // Category数组
	private String[] classArr; // Class数组
	private String[] subClassArr; // SubClass数组
	private int stockId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initVariable();
		bindData();

		Intent mIntent = this.getIntent();
		stockId = mIntent.getIntExtra("stockId", 0);
	}

	private void initView() {
		mTabHost = this.getTabHost();
		LayoutInflater.from(this).inflate(R.layout.stock_detail,
				mTabHost.getTabContentView(), true);

		// mTabHost.setBackgroundResource(R.color.transparent);
		mTabHost.setBackgroundColor(Color.argb(150, 255, 255, 230));
		mTabHost.addTab(mTabHost.newTabSpec("General Infomation")
				.setIndicator("General Infomation")
				.setContent(R.id.sv_stock_detail));

		mTabHost.addTab(mTabHost.newTabSpec("Picture").setIndicator("Picture")
				.setContent(R.id.stock_detail_lay_pic));

		TabWidget tabWidget = mTabHost.getTabWidget();
		for (int i = 0; i < tabWidget.getChildCount(); i++) {
			tabWidget.getChildAt(i).getLayoutParams().height = 60;
		}
	}

	private void initVariable() {
		unitArr = new String[] { "PCS", "SET", "SETS" };
		unitPriceArr = new String[] { "EUR", "HKD", "JPY", "RMB", "USD" };
		refCostArr = new String[] { "EUR", "HKD", "JPY", "RMB", "USD" };
		brandArr = new String[] { "JAPAN", "PREMIER JAPAN +YESE", "PREMIER",
				"YESE" };
		classArr = new String[] { "97", "AAAA", "BBBB", "cc" };

		bt_stock_detail_find_vendor = (Button) findViewById(R.id.bt_stock_detail_find_vendor);

		sp_stock_detail_unit = (Spinner) findViewById(R.id.sp_stock_detail_unit);
		sp_stock_detail_unit_price = (Spinner) findViewById(R.id.sp_stock_detail_unit_price);
		sp_stock_detail_ref_cost = (Spinner) findViewById(R.id.sp_stock_detail_ref_cost);
		sp_stock_detail_brand = (Spinner) findViewById(R.id.sp_stock_detail_brand);
		sp_stock_detail_category = (Spinner) findViewById(R.id.sp_stock_detail_category);
		sp_stock_detail_class = (Spinner) findViewById(R.id.sp_stock_detail_class);
		sp_stock_detail_sub_class = (Spinner) findViewById(R.id.sp_stock_detail_sub_class);

	}

	private void bindData() {
		setAdapter(sp_stock_detail_unit, unitArr);

		setAdapter(sp_stock_detail_unit_price, unitPriceArr);
		setAdapter(sp_stock_detail_ref_cost, refCostArr);
		setAdapter(sp_stock_detail_brand, brandArr);
		setAdapter(sp_stock_detail_category, classArr);
		setAdapter(sp_stock_detail_class, classArr);
		setAdapter(sp_stock_detail_sub_class, classArr);

	}

	private void setAdapter(Spinner mSpinner, String[] items) {
		ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, items);
		mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinner.setAdapter(mAdapter);
	}

}
