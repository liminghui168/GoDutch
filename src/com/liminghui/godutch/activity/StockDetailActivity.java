package com.liminghui.godutch.activity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.loopj.android.image.SmartImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class StockDetailActivity extends TabActivity implements OnClickListener {

	protected static final int REQ_CODE_CAMERA = 100;

	protected static final int REQ_CODE_PICTURE = 200;

	private static final String FILE_PATH = "/temp";

	private TabHost mTabHost;

	private final int DETAIL = 1;
	private final int FIRST = 2;
	private final int PREVIOUS = 3;
	private final int NEXT = 4;
	private final int LAST = 5;
	private final int DELETE = 6;

	private Button bt_stock_detail_add;
	private Button bt_stock_detail_edit;
	private Button bt_stock_detail_delete;
	private Button bt_stock_detail_save;
	private Button bt_stock_detail_undo;
	private Button bt_stock_detail_first;
	private Button bt_stock_detail_pre;
	private Button bt_stock_detail_next;
	private Button bt_stock_detail_last;

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

	private SmartImageView iv_stock_detail_viewer;
	private Button bt_stock_detail_upload_img;
	private Button bt_stock_detail_select_img;

	private String[] unitArr;// Unit数组
	private String[] unitPriceArr;// Unit Price数组
	private String[] refCostArr;// Ref.Cost数组
	private String[] brandArr;// Brand数组
	private String[] categoryArr; // Category数组
	private String[] classArr; // Class数组
	private String[] subClassArr; // SubClass数组
	private int stockId;
	private Stock mStock;
	private Bitmap bmp;
	private String filePath;

	private boolean isAdd; // 是否为添加，true为添加

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.stock_detail);
		initView();
		initVariable();
		setControlStatus(false);
		bindData();

		// 获取从StockActivity传过来的值
		Intent mIntent = this.getIntent();
		stockId = mIntent.getIntExtra("stockId", 0);

		pb_stock_detail_loading.setVisibility(View.VISIBLE);

		getStockService(DETAIL);
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
			RelativeLayout tabView = (RelativeLayout) mTabHost.getTabWidget()
					.getChildAt(i);

			TextView text = (TextView) tabWidget.getChildAt(i).findViewById(
					android.R.id.title);
			text.setTextSize(18);

			// 文字居中
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) text
					.getLayoutParams();
			params.width = RelativeLayout.LayoutParams.MATCH_PARENT;
			params.height = RelativeLayout.LayoutParams.MATCH_PARENT;
			text.setLayoutParams(params);
			text.setGravity(Gravity.CENTER);

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
		bt_stock_detail_first = (Button) findViewById(R.id.bt_stock_detail_first);
		bt_stock_detail_pre = (Button) findViewById(R.id.bt_stock_detail_pre);
		bt_stock_detail_next = (Button) findViewById(R.id.bt_stock_detail_next);
		bt_stock_detail_last = (Button) findViewById(R.id.bt_stock_detail_last);

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

		iv_stock_detail_viewer = (SmartImageView) findViewById(R.id.iv_stock_detail_viewer);
		bt_stock_detail_upload_img = (Button) findViewById(R.id.bt_stock_detail_upload_img);
		bt_stock_detail_select_img = (Button) findViewById(R.id.bt_stock_detail_select_img);
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
		bt_stock_detail_first.setOnClickListener(this);
		bt_stock_detail_pre.setOnClickListener(this);
		bt_stock_detail_next.setOnClickListener(this);
		bt_stock_detail_last.setOnClickListener(this);

		bt_stock_detail_refresh.setOnClickListener(this);
		bt_stock_detail_close.setOnClickListener(this);
		bt_stock_detail_upload_img.setOnClickListener(this);
		bt_stock_detail_select_img.setOnClickListener(this);
	}

	/**
	 * 顶部按钮的点击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_stock_detail_add: // 添加
			isAdd = true;
			setTopBtnInEdit();
			setControlTextForEmpty();
			setControlStatus(true);
			break;
		case R.id.bt_stock_detail_edit: // 修改
			isAdd = false;
			setTopBtnInEdit();
			setControlStatus(true);
			break;
		case R.id.bt_stock_detail_delete: // 删除
			shwoDeleteDialog();
			break;
		case R.id.bt_stock_detail_save: // 保存
			Stock s = getControlText();
			String json = JSONHelper.toJSON(s);
			showConfirmDialog(json);
			break;
		case R.id.bt_stock_detail_undo: // 撤销
			showCancelDialog();
			break;
		case R.id.bt_stock_detail_first: // 第一条
			getStockService(FIRST);
			break;
		case R.id.bt_stock_detail_pre: // 上一条
			getStockService(PREVIOUS);
			break;
		case R.id.bt_stock_detail_next: // 下一条
			getStockService(NEXT);
			break;
		case R.id.bt_stock_detail_last: // 最后一条
			getStockService(LAST);
			break;

		case R.id.bt_stock_detail_refresh: // 刷新
			getStockService(DETAIL);
			break;
		case R.id.bt_stock_detail_close: // 关闭
			finish();
			break;
		case R.id.bt_stock_detail_select_img:// 选择图片
			selectLoactionImg();
			break;
		case R.id.bt_stock_detail_upload_img: // 上传图片
			uploadImgService();
			break;
		}
	}

	private void uploadImgService() {
		if (isAdd) {
			return;
		} else {
			byte[] buff = null;
			iv_stock_detail_viewer.setDrawingCacheEnabled(true);
			Bitmap bitmap = iv_stock_detail_viewer.getDrawingCache();
			// TODO:上传图片，处理流
			if (TextUtils.isEmpty(filePath)) {
				//如果是选择本地照片上传
			} else {
				//如果是
			}
		}
	}

	/**
	 * 图片转换成文件流
	 * 
	 * @param bm
	 * @return
	 */
	public InputStream Bitmap2InputStream(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		InputStream is = new ByteArrayInputStream(baos.toByteArray());
		return is;
	}

	/**
	 * 选择图片
	 */
	private void selectLoactionImg() {
		new AlertDialog.Builder(this)
				.setTitle("选择图片")
				.setItems(R.array.gallery_camera_icon,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								case 0: // 拍照
									Intent i = new Intent(
											MediaStore.ACTION_IMAGE_CAPTURE);
									// i.putExtra(MediaStore.EXTRA_MEDIA_ALBUM,true);
									startActivityForResult(i, REQ_CODE_CAMERA);

									// ContentValues values = new
									// ContentValues();
									// Uri uriPath =
									// getApplicationContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
									// values);
									// i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
									// uriPath);
									// startActivityForResult(i,
									// REQ_CODE_CAMERA);

									/*
									 * Intent i = new Intent(
									 * MediaStore.ACTION_IMAGE_CAPTURE);
									 * 
									 * long current =
									 * System.currentTimeMillis(); File out =
									 * new
									 * File(Environment.getExternalStorageDirectory
									 * (), "temp/camera.jpg"); Uri uri =
									 * Uri.fromFile(out);
									 * i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
									 * startActivityForResult(i,
									 * REQ_CODE_CAMERA);
									 */

									break;
								case 1: // 选择本地图片
									Intent intent = new Intent(
											Intent.ACTION_GET_CONTENT);
									intent.addCategory(Intent.CATEGORY_OPENABLE);
									intent.setType("image/*");
									startActivityForResult(Intent
											.createChooser(intent, "选择图片"),
											REQ_CODE_PICTURE);

									break;
								}
							}
						}).create().show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case REQ_CODE_CAMERA:// 拍照
				Bitmap camerabmp = (Bitmap) data.getExtras().get("data");
				iv_stock_detail_viewer.setImageBitmap(camerabmp);
				filePath = "";
				break;
			case REQ_CODE_PICTURE:// 选择本地图片
				// 选择图片
				Uri uri = data.getData();
				// ContentResolver cr = this.getContentResolver();
				// bmp = BitmapFactory.decodeStream(cr.openInputStream(uri));

				String[] proj = { MediaStore.Images.Media.DATA };
				Cursor cursor = this.managedQuery(uri, proj, null, null, null);

				int column_index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				cursor.moveToFirst();

				String path = cursor.getString(column_index);
				try {
					if (bmp != null)// 如果不释放的话，不断取图片，将会内存不够
						bmp.recycle();
					bmp = BitmapFactory.decodeFile(path);
					filePath = path;
				} catch (Exception e) {
					e.printStackTrace();
				}

				iv_stock_detail_viewer.setImageBitmap(bmp);
				break;
			}

		}

		super.onActivityResult(requestCode, resultCode, data);
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
		s.setPower(et_stock_detail_power_RMS.getText().toString().trim());
		s.setOpowerrms(et_stock_detail_output_power_RMS.getText().toString()
				.trim());
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
		bt_stock_detail_first.setEnabled(true);
		bt_stock_detail_pre.setEnabled(true);
		bt_stock_detail_next.setEnabled(true);
		bt_stock_detail_last.setEnabled(true);
		bt_stock_detail_refresh.setEnabled(true);
		bt_stock_detail_close.setEnabled(true);
	}

	/**
	 * 设置顶部控件的全部状态
	 * 
	 * @param flag
	 */
	private void setTopBtnToFalse(boolean flag) {
		bt_stock_detail_add.setEnabled(flag);
		bt_stock_detail_edit.setEnabled(flag);
		bt_stock_detail_delete.setEnabled(flag);
		bt_stock_detail_save.setEnabled(flag);
		bt_stock_detail_undo.setEnabled(flag);
		bt_stock_detail_first.setEnabled(flag);
		bt_stock_detail_pre.setEnabled(flag);
		bt_stock_detail_next.setEnabled(flag);
		bt_stock_detail_last.setEnabled(flag);
		bt_stock_detail_refresh.setEnabled(flag);
		bt_stock_detail_close.setEnabled(flag);
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
		bt_stock_detail_first.setEnabled(false);
		bt_stock_detail_pre.setEnabled(false);
		bt_stock_detail_next.setEnabled(false);
		bt_stock_detail_last.setEnabled(false);
		bt_stock_detail_refresh.setEnabled(false);
		bt_stock_detail_close.setEnabled(false);
	}

	/**
	 * 弹出确认对话框
	 */
	private void showConfirmDialog(final String json) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("提示信息");
		builder.setMessage("确认要保存吗？");
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				saveStockDetail(json);
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();
	}

	/**
	 * 弹出删除对话框
	 */
	private void shwoDeleteDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("提示信息");
		builder.setMessage("确认要取消吗？");
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				getStockService(DELETE);
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				/*
				 * if (isAdd) { initTopBtnStatus(); getStockService(DETAIL); }
				 */
				dialog.dismiss();
			}
		});
		builder.show();
	}

	/**
	 * 弹出取消对话框
	 */
	private void showCancelDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("提示信息");
		builder.setMessage("确认要取消吗？");
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				initTopBtnStatus();
				if (isAdd) {
					getStockService(DETAIL);
				}
				setControlStatus(false);

				dialog.dismiss();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				/*
				 * if (isAdd) { initTopBtnStatus(); getStockService(DETAIL); }
				 */
				dialog.dismiss();
			}
		});
		builder.show();
	}

	/**
	 * 获取远程服务StockDetail
	 */
	private void getStockService(int loadFlag) {
		String path = getString(R.string.url_path) + "LoadStockDetail/"
				+ stockId;

		switch (loadFlag) {
		case DETAIL:// 获取详细
			path = getString(R.string.url_path) + "LoadStockDetail/" + stockId;
			break;

		case FIRST:// 第一条
			path = getString(R.string.url_path) + "LoadStockDetailFirst";
			break;

		case PREVIOUS:// 上一条
			path = getString(R.string.url_path) + "LoadStockDetailPre/"
					+ stockId;
			break;

		case NEXT:// 下一条
			path = getString(R.string.url_path) + "LoadStockDetailNext/"
					+ stockId;
			break;

		case LAST:// 最后一条
			path = getString(R.string.url_path) + "LoadStockDetailLast";
			break;

		case DELETE:// 删除
			path = getString(R.string.url_path) + "LoadStockDetailLast";
			break;
		}

		AsyncHttpClient client = new AsyncHttpClient();
		client.setTimeout(30000);
		int visibility = pb_stock_detail_loading.getVisibility();
		if (visibility == View.GONE) {
			pb_stock_detail_loading.setVisibility(View.VISIBLE);
		}

		client.get(path, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(Throwable error, String content) {
				Toast.makeText(getApplicationContext(), "Loading Error", 0)
						.show();
				pb_stock_detail_loading.setVisibility(View.GONE);
				super.onFailure(error, content);
			}

			@Override
			public void onSuccess(String content) {
				if ("null".equals(content)) {
					// 数据为空
				} else if ("isFirst".equals(content)) {
					Toast.makeText(getApplicationContext(), "已经是第一条！", 0)
							.show();
				} else if ("isLast".equals(content)) {
					Toast.makeText(getApplicationContext(), "已经是最后一条！", 0)
							.show();
				} else if ("ok".equals(content)) {
					Toast.makeText(getApplicationContext(), "删除成功！", 0).show();
					getStockService(NEXT);
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
		client.setTimeout(30000);
		setTopBtnToFalse(false);// 设置顶部按钮全部不可用
		setControlStatus(false);// 设置文本框全部不可用
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(Throwable error, String content) {
				initTopBtnStatus();
				getStockService(DETAIL);
				Toast.makeText(getApplicationContext(), "Edit Error", 0).show();
				pb_stock_detail_loading.setVisibility(View.GONE);
				super.onFailure(error, content);

			}

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

	private String convertNullToEmpty(String text) {
		if ("null".equals(text) || text == null) {
			return "";
		}
		return text;
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
			stockId = mStock.getId();
			mStock.setStock1(convertNullToEmpty(jsonObject.getString("stock1")));
			mStock.setVendor(convertNullToEmpty(jsonObject.getString("vendor")));
			mStock.setVendorname(convertNullToEmpty(jsonObject
					.getString("vendorname")));
			mStock.setVref(convertNullToEmpty(jsonObject.getString("vref")));
			mStock.setDes(convertNullToEmpty(jsonObject.getString("des")));
			mStock.setShortdes(convertNullToEmpty(jsonObject
					.getString("shortdes")));
			mStock.setPower(convertNullToEmpty(jsonObject.getString("power")));
			mStock.setOpowerrms(convertNullToEmpty(jsonObject
					.getString("opowerrms")));
			mStock.setOpowerpmpo(convertNullToEmpty(jsonObject
					.getString("opowerpmpo")));
			mStock.setPartno(convertNullToEmpty(jsonObject.getString("partno")));
			mStock.setUnit(convertNullToEmpty(jsonObject.getString("unit")));
			mStock.setPricecurr(convertNullToEmpty(jsonObject
					.getString("pricecurr")));
			mStock.setPrice(jsonObject.getDouble("price"));
			mStock.setCostcurr(convertNullToEmpty(jsonObject
					.getString("costcurr")));
			mStock.setCost(jsonObject.getDouble("cost"));
			mStock.setCreatedate(convertNullToEmpty(jsonObject
					.getString("createdata")));
			mStock.setPicfile(convertNullToEmpty(jsonObject
					.getString("picfile")));
			mStock.setPricecurr(convertNullToEmpty(jsonObject
					.getString("pricecurr")));
			mStock.setBrand(convertNullToEmpty(jsonObject.getString("brand")));
			mStock.setCategory(convertNullToEmpty(jsonObject
					.getString("category")));
			mStock.setGbbarcode(convertNullToEmpty(jsonObject
					.getString("gbbarcode")));
			mStock.setOcbarcode(convertNullToEmpty(jsonObject
					.getString("ocbarcode")));
			mStock.setPbarcode(convertNullToEmpty(jsonObject
					.getString("pbarcode")));
			mStock.setSclass(convertNullToEmpty(jsonObject.getString("sclass")));
			mStock.setSubclass(convertNullToEmpty(jsonObject
					.getString("subclass")));
			mStock.setAcvite(jsonObject.getBoolean("active"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
