package com.liminghui.godutch.activity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.liminghui.godutch.R;
import com.liminghui.godutch.domain.Stock;
import com.liminghui.godutch.utility.JSONHelper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class StockDetailActivity extends TabActivity implements OnClickListener {

	private TabHost mTabHost;

	private Button bt_stock_detail_add;
	private Button bt_stock_detail_edit;
	private Button bt_stock_detail_delete;
	private Button bt_stock_detail_save;
	private Button bt_stock_detail_undo;
	private Button bt_stock_detail_refresh;
	private Button bt_stock_detail_close;

	private ProgressBar pb_stock_detail_loading;

	private EditText et_stock_detail_stock_code;
	private CheckBox cb_stock_detail_active;
	private EditText et_stock_detail_vendor_code;
	private EditText et_stock_detail_vendor_name;
	private Button bt_stock_detail_find_vendor;
	private EditText et_stock_detail_vendor_model_no;
	private EditText et_stock_detail_des;
	private EditText et_stock_detail_short_des;
	private EditText et_stock_detail_power_RMS;
	private EditText et_stock_detail_output_power_RMS;
	private EditText et_stock_detail_output_power_PMPO;

	private EditText et_stock_detail_client_model_no;
	private Spinner sp_stock_detail_unit;
	private Spinner sp_stock_detail_unit_price;
	private EditText et_stock_detail_unit_price;
	private Spinner sp_stock_detail_ref_cost;
	private EditText et_stcok_detail_ref_cost;
	private EditText et_stock_detail_first_date;
	private Spinner sp_stock_detail_brand;
	private Spinner sp_stock_detail_category;
	private EditText et_stock_detail_gift;
	private EditText et_stock_detail_carton;
	private EditText et_stock_detail_product;
	private Spinner sp_stock_detail_class;
	private Spinner sp_stock_detail_sub_class;

	private String[] unitArr;// Unit数组
	private String[] unitPriceArr;// Unit Price数组
	private String[] refCostArr;// Ref.Cost数组
	private String[] brandArr;// Brand数组
	private String[] categoryArr; // Category数组
	private String[] classArr; // Class数组
	private String[] subClassArr; // SubClass数组
	private int stockId;
	private Stock mStock;

	private boolean isAdd; // 是否为添加

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.stock_detail);
		initView();
		initVariable();
		bindData();

		// 获取从StockActivity传过来的值
		Intent mIntent = this.getIntent();
		stockId = mIntent.getIntExtra("stockId", 0);

		pb_stock_detail_loading.setVisibility(View.VISIBLE);

		getStockDetail();
		initTopBtnStatus();
		initListeners();
	}

	/**
	 * 初始化布局
	 */
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

	/**
	 * 初始化变量
	 */
	private void initVariable() {
		unitArr = new String[] { "", "PCS", "SET", "SETS" };
		unitPriceArr = new String[] { "", "EUR", "HKD", "JPY", "RMB", "USD" };
		refCostArr = new String[] { "", "EUR", "HKD", "JPY", "RMB", "USD" };
		brandArr = new String[] { "", "JAPAN", "PREMIER JAPAN +YESE",
				"PREMIER", "YESE" };
		categoryArr = new String[] { "", "001", "002", "003", "004" };
		classArr = new String[] { "", "001", "002", "003", "004" };
		subClassArr = new String[] { "", "001", "002", "003", "004" };

		mStock = new Stock();
		isAdd = false;
		bt_stock_detail_add = (Button) findViewById(R.id.bt_stock_detail_add);
		bt_stock_detail_edit = (Button) findViewById(R.id.bt_stock_detail_edit);
		bt_stock_detail_delete = (Button) findViewById(R.id.bt_stock_detail_delete);
		bt_stock_detail_save = (Button) findViewById(R.id.bt_stock_detail_save);
		bt_stock_detail_undo = (Button) findViewById(R.id.bt_stock_detail_undo);
		bt_stock_detail_refresh = (Button) findViewById(R.id.bt_stock_detail_refresh);
		bt_stock_detail_close = (Button) findViewById(R.id.bt_stock_detail_close);

		pb_stock_detail_loading = (ProgressBar) findViewById(R.id.pb_stock_detail_loading);

		et_stock_detail_stock_code = (EditText) findViewById(R.id.et_stock_detail_stock_code);
		cb_stock_detail_active = (CheckBox) findViewById(R.id.cb_stock_detail_active);
		et_stock_detail_vendor_code = (EditText) findViewById(R.id.et_stock_detail_vendor_code);
		et_stock_detail_vendor_name = (EditText) findViewById(R.id.et_stock_detail_vendor_name);
		bt_stock_detail_find_vendor = (Button) findViewById(R.id.bt_stock_detail_find_vendor);
		et_stock_detail_vendor_model_no = (EditText) findViewById(R.id.et_stock_detail_vendor_model_no);
		et_stock_detail_des = (EditText) findViewById(R.id.et_stock_detail_des);
		et_stock_detail_short_des = (EditText) findViewById(R.id.et_stock_detail_short_des);
		et_stock_detail_power_RMS = (EditText) findViewById(R.id.et_stock_detail_power_RMS);
		et_stock_detail_output_power_RMS = (EditText) findViewById(R.id.et_stock_detail_output_power_RMS);
		et_stock_detail_output_power_PMPO = (EditText) findViewById(R.id.et_stock_detail_output_power_PMPO);
		et_stock_detail_client_model_no = (EditText) findViewById(R.id.et_stock_detail_client_model_no);
		sp_stock_detail_unit = (Spinner) findViewById(R.id.sp_stock_detail_unit);
		sp_stock_detail_unit_price = (Spinner) findViewById(R.id.sp_stock_detail_unit_price);
		et_stock_detail_unit_price = (EditText) findViewById(R.id.et_stock_detail_unit_price);
		sp_stock_detail_ref_cost = (Spinner) findViewById(R.id.sp_stock_detail_ref_cost);
		et_stcok_detail_ref_cost = (EditText) findViewById(R.id.et_stcok_detail_ref_cost);
		et_stock_detail_first_date = (EditText) findViewById(R.id.et_stock_detail_first_date);
		sp_stock_detail_brand = (Spinner) findViewById(R.id.sp_stock_detail_brand);
		sp_stock_detail_category = (Spinner) findViewById(R.id.sp_stock_detail_category);
		et_stock_detail_gift = (EditText) findViewById(R.id.et_stock_detail_gift);
		et_stock_detail_carton = (EditText) findViewById(R.id.et_stock_detail_carton);
		et_stock_detail_product = (EditText) findViewById(R.id.et_stock_detail_product);
		sp_stock_detail_class = (Spinner) findViewById(R.id.sp_stock_detail_class);
		sp_stock_detail_sub_class = (Spinner) findViewById(R.id.sp_stock_detail_sub_class);
	}

	/**
	 * 初始化监听事件
	 */
	private void initListeners() {
		bt_stock_detail_add.setOnClickListener(this);
		bt_stock_detail_edit.setOnClickListener(this);
		bt_stock_detail_delete.setOnClickListener(this);
		bt_stock_detail_save.setOnClickListener(this);
		bt_stock_detail_undo.setOnClickListener(this);
		bt_stock_detail_refresh.setOnClickListener(this);
		bt_stock_detail_close.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_stock_detail_add:
			isAdd = true;
			setTopBtnInEdit();
			setControlTextForEmpty();
			break;
		case R.id.bt_stock_detail_edit:
			isAdd = false;
			setTopBtnInEdit();
			setControlStatus(true);
			break;
		case R.id.bt_stock_detail_delete:

			break;
		case R.id.bt_stock_detail_save:
			Stock s = getControlText();
			String json = JSONHelper.toJSON(s);
			saveStockDetail(json);

			break;
		case R.id.bt_stock_detail_undo:
			initTopBtnStatus();
			setControlStatus(false);
			break;
		case R.id.bt_stock_detail_refresh:

			break;
		case R.id.bt_stock_detail_close:
			finish();
			break;
		}
	}

	/**
	 * 绑定适配器数据
	 */
	private void bindData() {
		setAdapter(sp_stock_detail_unit, unitArr);
		setAdapter(sp_stock_detail_unit_price, unitPriceArr);
		setAdapter(sp_stock_detail_ref_cost, refCostArr);
		setAdapter(sp_stock_detail_brand, brandArr);
		setAdapter(sp_stock_detail_category, categoryArr);
		setAdapter(sp_stock_detail_class, classArr);
		setAdapter(sp_stock_detail_sub_class, subClassArr);

	}

	/**
	 * 设置控件状态
	 * 
	 * @param flag
	 */
	private void setControlStatus(boolean flag) {
		et_stock_detail_stock_code.setEnabled(flag);
		cb_stock_detail_active.setEnabled(flag);
		et_stock_detail_vendor_code.setEnabled(flag);
		et_stock_detail_vendor_name.setEnabled(flag);
		bt_stock_detail_find_vendor.setEnabled(flag);
		et_stock_detail_vendor_model_no.setEnabled(flag);
		et_stock_detail_des.setEnabled(flag);
		et_stock_detail_short_des.setEnabled(flag);
		et_stock_detail_power_RMS.setEnabled(flag);
		et_stock_detail_output_power_RMS.setEnabled(flag);
		et_stock_detail_output_power_PMPO.setEnabled(flag);
		et_stock_detail_client_model_no.setEnabled(flag);
		sp_stock_detail_unit.setClickable(flag);
		sp_stock_detail_unit_price.setClickable(flag);
		et_stock_detail_unit_price.setEnabled(flag);
		sp_stock_detail_ref_cost.setClickable(flag);
		et_stcok_detail_ref_cost.setEnabled(flag);
		et_stock_detail_first_date.setEnabled(flag);
		sp_stock_detail_brand.setClickable(flag);
		sp_stock_detail_category.setClickable(flag);
		et_stock_detail_gift.setEnabled(flag);
		et_stock_detail_carton.setEnabled(flag);
		et_stock_detail_product.setEnabled(flag);
		sp_stock_detail_class.setClickable(flag);
		sp_stock_detail_sub_class.setClickable(flag);
	}

	/**
	 * 获取控制设置的值
	 * 
	 * @return
	 */
	private Stock getControlText() {
		Stock s = new Stock();

		if (!isAdd) {
			s.setId(stockId);
		}

		s.setCost_wa_dt(getCurrentDate());
		s.setFirstmdate(getCurrentDate());
		s.setLastmdate(getCurrentDate());

		s.setStock1(et_stock_detail_stock_code.getText().toString().trim());
		s.setAcvite(cb_stock_detail_active.isChecked());
		s.setVendor(et_stock_detail_vendor_code.getText().toString().trim());
		s.setVendorname(et_stock_detail_vendor_name.getText().toString().trim());
		s.setPartno(et_stock_detail_vendor_model_no.getText().toString().trim());
		s.setDes(et_stock_detail_des.getText().toString().trim());
		s.setShortdes(et_stock_detail_short_des.getText().toString().trim());
		s.setOpowerrms(et_stock_detail_power_RMS.getText().toString().trim());
		s.setOpowerpmpo(et_stock_detail_output_power_PMPO.getText().toString()
				.trim());
		s.setVref(et_stock_detail_client_model_no.getText().toString().trim());
		s.setUnit(sp_stock_detail_unit.getSelectedItem().toString().trim());
		s.setPricecurr(sp_stock_detail_unit_price.getSelectedItem().toString()
				.trim());
		s.setPrice(Double.parseDouble(et_stock_detail_unit_price.getText()
				.toString().trim()));
		s.setCostcurr(sp_stock_detail_ref_cost.getSelectedItem().toString());
		s.setCost(Double.parseDouble(et_stcok_detail_ref_cost.getText()
				.toString().trim()));
		String createData = et_stock_detail_first_date.getText().toString()
				.trim();
		if (TextUtils.isEmpty(createData)) {
			s.setCreatedate(getCurrentDate());
		} else {
			s.setCreatedate(createData);
		}

		s.setBrand(sp_stock_detail_brand.getSelectedItem().toString());
		s.setCategory(sp_stock_detail_category.getSelectedItem().toString());
		s.setGbbarcode(et_stock_detail_gift.getText().toString().trim());
		s.setOcbarcode(et_stock_detail_carton.getText().toString().trim());
		s.setPbarcode(et_stock_detail_product.getText().toString().trim());
		s.setSclass(sp_stock_detail_class.getSelectedItem().toString());
		s.setSubclass(sp_stock_detail_sub_class.getSelectedItem().toString());

		return s;
	}

	/**
	 * 赋值给文本框
	 */
	private void setControlTextForEntity() {
		et_stock_detail_stock_code.setText(mStock.getStock1());
		cb_stock_detail_active.setChecked(mStock.isAcvite());
		et_stock_detail_vendor_code.setText(mStock.getVendor());
		et_stock_detail_vendor_name.setText(mStock.getVendorname());
		// bt_stock_detail_find_vendor
		et_stock_detail_vendor_model_no.setText(mStock.getPartno());
		et_stock_detail_des.setText(mStock.getDes());
		et_stock_detail_short_des.setText(mStock.getShortdes());
		et_stock_detail_power_RMS.setText(mStock.getPower());
		et_stock_detail_output_power_RMS.setText(mStock.getOpowerrms());
		et_stock_detail_output_power_PMPO.setText(mStock.getOpowerpmpo());
		et_stock_detail_client_model_no.setText(mStock.getVref());
		for (int i = 0; i < sp_stock_detail_unit.getCount(); i++) {
			String item = (String) sp_stock_detail_unit.getItemAtPosition(i);
			if (item.equals(mStock.getUnit())) {
				sp_stock_detail_unit.setSelection(i);
			}
		}

		for (int i = 0; i < sp_stock_detail_unit_price.getCount(); i++) {
			String item = (String) sp_stock_detail_unit_price
					.getItemAtPosition(i);
			if (item.equals(mStock.getPricecurr())) {
				sp_stock_detail_unit_price.setSelection(i);
			}
		}

		et_stock_detail_unit_price.setText(mStock.getPrice() + "");

		for (int i = 0; i < sp_stock_detail_ref_cost.getCount(); i++) {
			String item = (String) sp_stock_detail_ref_cost
					.getItemAtPosition(i);
			if (item.equals(mStock.getCostcurr())) {
				sp_stock_detail_ref_cost.setSelection(i);
			}
		}
		et_stcok_detail_ref_cost.setText(mStock.getCost() + "");
		et_stock_detail_first_date.setText(mStock.getCreatedate());

		for (int i = 0; i < sp_stock_detail_brand.getCount(); i++) {
			String item = (String) sp_stock_detail_brand.getItemAtPosition(i);
			if (item.equals(mStock.getBrand())) {
				sp_stock_detail_brand.setSelection(i);
			}
		}

		for (int i = 0; i < sp_stock_detail_category.getCount(); i++) {
			String item = (String) sp_stock_detail_category
					.getItemAtPosition(i);
			if (item.equals(mStock.getCategory())) {
				sp_stock_detail_category.setSelection(i);
			}
		}

		et_stock_detail_gift.setText(mStock.getGbbarcode());

		et_stock_detail_carton.setText(mStock.getOcbarcode());
		et_stock_detail_product.setText(mStock.getPbarcode());
		for (int i = 0; i < sp_stock_detail_class.getCount(); i++) {
			String item = (String) sp_stock_detail_brand.getItemAtPosition(i);
			if (item.equals(mStock.getSclass())) {
				sp_stock_detail_class.setSelection(i);
			}
		}

		for (int i = 0; i < sp_stock_detail_sub_class.getCount(); i++) {
			String item = (String) sp_stock_detail_sub_class
					.getItemAtPosition(i);
			if (item.equals(mStock.getSubclass())) {
				sp_stock_detail_sub_class.setSelection(i);
			}
		}

	}

	/**
	 * 设置文本框为空
	 */
	private void setControlTextForEmpty() {
		et_stock_detail_stock_code.setText("");
		cb_stock_detail_active.setChecked(false);
		et_stock_detail_vendor_code.setText("");
		et_stock_detail_vendor_name.setText("");
		// bt_stock_detail_find_vendor
		et_stock_detail_vendor_model_no.setText("");
		et_stock_detail_des.setText("");
		et_stock_detail_short_des.setText("");
		et_stock_detail_power_RMS.setText("");
		et_stock_detail_output_power_RMS.setText("");
		et_stock_detail_output_power_PMPO.setText("");
		et_stock_detail_client_model_no.setText("");
		sp_stock_detail_unit.setSelection(0);
		sp_stock_detail_unit_price.setSelection(0);
		et_stock_detail_unit_price.setText("");
		sp_stock_detail_ref_cost.setSelection(0);

		et_stcok_detail_ref_cost.setText("");
		et_stock_detail_first_date.setText("");
		sp_stock_detail_brand.setSelection(0);
		sp_stock_detail_category.setSelection(0);
		et_stock_detail_gift.setText("");
		et_stock_detail_carton.setText("");
		et_stock_detail_product.setText("");
		sp_stock_detail_class.setSelection(0);
		sp_stock_detail_sub_class.setSelection(0);

	}

	/**
	 * 设置数据到适配器
	 * 
	 * @param mSpinner
	 * @param items
	 */
	private void setAdapter(Spinner mSpinner, String[] items) {
		ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, items);
		mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinner.setAdapter(mAdapter);
	}

	/**
	 * 初始化顶部按钮的状态
	 */
	private void initTopBtnStatus() {
		bt_stock_detail_add.setEnabled(true);
		bt_stock_detail_edit.setEnabled(true);
		bt_stock_detail_delete.setEnabled(true);
		bt_stock_detail_save.setEnabled(false);
		bt_stock_detail_undo.setEnabled(false);
		bt_stock_detail_refresh.setEnabled(true);
		bt_stock_detail_close.setEnabled(true);
	}

	/**
	 * 设置按钮状态为编辑
	 */
	private void setTopBtnInEdit() {
		bt_stock_detail_add.setEnabled(false);
		bt_stock_detail_edit.setEnabled(false);
		bt_stock_detail_delete.setEnabled(false);
		bt_stock_detail_save.setEnabled(true);
		bt_stock_detail_undo.setEnabled(true);
		bt_stock_detail_refresh.setEnabled(false);
		bt_stock_detail_close.setEnabled(false);
	}

	/**
	 * 获取远程服务StockDetail
	 */
	private void getStockDetail() {
		String path = getString(R.string.url_path) + "LoadStockDetail/"
				+ stockId;
		AsyncHttpClient client = new AsyncHttpClient();

		client.get(path, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(String content) {
				if (content == "null") {
					// 数据为空
				} else {
					parseJson(content);
					setControlTextForEntity();
				}
				pb_stock_detail_loading.setVisibility(View.GONE);
				super.onSuccess(content);
			}

		});
	}

	/**
	 * 保存StockDetail
	 * 
	 * @param json
	 */
	private void saveStockDetail(String json) {
		pb_stock_detail_loading.setVisibility(View.VISIBLE);

		String url = getString(R.string.url_path) + "UpdateStockDetail";
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("stockJson", json);

		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(String content) {
				if (content.equals("ok")) {
					Toast.makeText(getApplicationContext(), "Edit Success", 0)
							.show();
					initTopBtnStatus();
					setControlStatus(false);
				}
				pb_stock_detail_loading.setVisibility(View.GONE);
				super.onSuccess(content);
			}

		});
	}

	/**
	 * 获取系统当前时间
	 * 
	 * @return
	 */
	private String getCurrentDate() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
		return df.format(new Date());// new Date()为获取当前系统时间
	}

	/**
	 * 转换Json
	 * 
	 * @param json
	 */
	private void parseJson(String json) {
		try {
			JSONObject jsonObject = new JSONObject(json);
			mStock.setId(jsonObject.getInt("Id"));
			mStock.setStock1(jsonObject.getString("stock1"));
			mStock.setVendor(jsonObject.getString("vendor"));
			mStock.setVendorname(jsonObject.getString("vendorname"));
			mStock.setVref(jsonObject.getString("vref"));
			mStock.setDes(jsonObject.getString("des"));
			mStock.setShortdes(jsonObject.getString("shortdes"));
			mStock.setPower(jsonObject.getString("power"));
			mStock.setOpowerrms(jsonObject.getString("opowerrms"));
			mStock.setOpowerpmpo(jsonObject.getString("opowerpmpo"));
			mStock.setPartno(jsonObject.getString("partno"));
			mStock.setUnit(jsonObject.getString("unit"));
			mStock.setPricecurr(jsonObject.getString("pricecurr"));
			mStock.setPrice(jsonObject.getDouble("price"));
			mStock.setCostcurr(jsonObject.getString("costcurr"));
			mStock.setCost(jsonObject.getDouble("cost"));
			mStock.setCreatedate(jsonObject.getString("createdata"));
			mStock.setPricecurr(jsonObject.getString("pricecurr"));
			mStock.setBrand(jsonObject.getString("brand"));
			mStock.setCategory(jsonObject.getString("category"));
			mStock.setGbbarcode(jsonObject.getString("gbbarcode"));
			mStock.setOcbarcode(jsonObject.getString("ocbarcode"));
			mStock.setPbarcode(jsonObject.getString("pbarcode"));
			mStock.setSclass(jsonObject.getString("sclass"));
			mStock.setSubclass(jsonObject.getString("subclass"));
			mStock.setAcvite(jsonObject.getBoolean("active"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
