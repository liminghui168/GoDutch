package com.liminghui.godutch.activity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.Toast;

import com.liminghui.godutch.R;
import com.liminghui.godutch.domain.Stock;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class StockDetailActivity extends TabActivity {

	private TabHost mTabHost;

	private EditText et_stock_detail_stock_code;
	private CheckBox cb_stock_detail_active;
	private EditText et_stock_detail_vendor_code;
	private EditText et_stock_detail_vendor_name;
	private Button bt_stock_detail_find_vendor;
	private EditText et_stock_detail_vendor_model_no;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initVariable();
		bindData();

		// 获取从StockActivity传过来的值
		Intent mIntent = this.getIntent();
		stockId = mIntent.getIntExtra("stockId", 0);

		getStockDetail();
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
		unitArr = new String[] { "PCS", "SET", "SETS" };
		unitPriceArr = new String[] { "EUR", "HKD", "JPY", "RMB", "USD" };
		refCostArr = new String[] { "EUR", "HKD", "JPY", "RMB", "USD" };
		brandArr = new String[] { "JAPAN", "PREMIER JAPAN +YESE", "PREMIER",
				"YESE" };
		classArr = new String[] { "97", "AAAA", "BBBB", "cc" };

		mStock = new Stock();

		et_stock_detail_stock_code = (EditText) findViewById(R.id.et_stock_detail_stock_code);
		cb_stock_detail_active = (CheckBox) findViewById(R.id.cb_stock_detail_active);
		et_stock_detail_vendor_code = (EditText) findViewById(R.id.et_stock_detail_vendor_code);
		et_stock_detail_vendor_name = (EditText) findViewById(R.id.et_stock_detail_vendor_name);
		bt_stock_detail_find_vendor = (Button) findViewById(R.id.bt_stock_detail_find_vendor);
		et_stock_detail_vendor_model_no = (EditText) findViewById(R.id.et_stock_detail_vendor_model_no);
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
	 * 绑定适配器数据
	 */
	private void bindData() {
		setAdapter(sp_stock_detail_unit, unitArr);
		setAdapter(sp_stock_detail_unit_price, unitPriceArr);
		setAdapter(sp_stock_detail_ref_cost, refCostArr);
		setAdapter(sp_stock_detail_brand, brandArr);
		setAdapter(sp_stock_detail_category, classArr);
		setAdapter(sp_stock_detail_class, classArr);
		setAdapter(sp_stock_detail_sub_class, classArr);

	}

	/**
	 * 赋值给控件
	 */
	private void setControlText() {
		et_stock_detail_stock_code.setText(mStock.getStock());
		cb_stock_detail_active.setChecked(mStock.isAcvite());
		et_stock_detail_vendor_code.setText(mStock.getVendor());
		et_stock_detail_vendor_name.setText(mStock.getVendorname());
		// bt_stock_detail_find_vendor
		et_stock_detail_vendor_model_no.setText(mStock.getVref());
		et_stock_detail_short_des.setText(mStock.getShortdes());
		et_stock_detail_power_RMS.setText(mStock.getPower());
		et_stock_detail_output_power_RMS.setText(mStock.getOpowerrms());
		et_stock_detail_output_power_PMPO.setText(mStock.getOpowerpmpo());
		et_stock_detail_client_model_no.setText(mStock.getPartno());
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
		et_stock_detail_first_date.setText(mStock.getFirstmdate());

		for (int i = 0; i < sp_stock_detail_brand.getCount(); i++) {
			String item = (String) sp_stock_detail_brand.getItemAtPosition(i);
			if (item.equals(mStock.getBrand())) {
				sp_stock_detail_brand.setSelection(i);
			}
		}

		// sp_stock_detail_category = (Spinner)
		// findViewById(R.id.sp_stock_detail_category);
		et_stock_detail_gift.setText(mStock.getGbbarcode());

		et_stock_detail_carton.setText(mStock.getOcbarcode());
		et_stock_detail_product.setText(mStock.getPbarcode());
		for (int i = 0; i < sp_stock_detail_class.getCount(); i++) {
			String item = (String) sp_stock_detail_brand.getItemAtPosition(i);
			if (item.equals(mStock.getSclass())) {
				sp_stock_detail_class.setSelection(i);
			}
		}
		// sp_stock_detail_sub_class = (Spinner)
		// findViewById(R.id.sp_stock_detail_sub_class);

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
					setControlText();
				}

				super.onSuccess(content);
			}

		});
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
			mStock.setStock(jsonObject.getString("stock1"));
			mStock.setVendor(jsonObject.getString("vendor"));
			mStock.setVendorname(jsonObject.getString("vendorname"));
			mStock.setVref(jsonObject.getString("vref"));
			mStock.setShortdes(jsonObject.getString("shortdes"));
			mStock.setPower(jsonObject.getString("power"));
			mStock.setOpowerrms(jsonObject.getString("opowerrms"));
			mStock.setOpowerrms(jsonObject.getString("opowerpmpo"));
			mStock.setPartno(jsonObject.getString("partno"));
			mStock.setUnit(jsonObject.getString("unit"));
			mStock.setPricecurr(jsonObject.getString("pricecurr"));
			mStock.setPrice(jsonObject.getDouble("price"));
			mStock.setCostcurr(jsonObject.getString("costcurr"));
			mStock.setCost(jsonObject.getDouble("cost"));
			mStock.setFirstmdate(jsonObject.getString("firstmdate"));
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
