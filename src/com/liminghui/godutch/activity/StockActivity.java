package com.liminghui.godutch.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liminghui.godutch.R;
import com.liminghui.godutch.adapter.MainAdapter;
import com.liminghui.godutch.adapter.StockAdapter;
import com.liminghui.godutch.baseactivity.FrameActivity;
import com.liminghui.godutch.domain.Stock;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class StockActivity extends FrameActivity implements OnItemClickListener {

	private String[] titleItems;
	private GridView gv_stock_main_body_title;
	private ListView lv_stock_main_body_list;
	private StockAdapter mAdapter;
	private ProgressBar pb_stock_main_body;
	private List<Stock> list;
	private int total = 0;
	private RelativeLayout main_stock_body_more_view;
	private Button bt_move_view_text;
	private ProgressBar pb_move_view_load;
	private Handler handler;
	private int pageIndex;
	private int pageSize;
	private int lastVisibleIndex;// 计算最后可见条目的索引

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.main_stock_body);
		initVariable();
		initView();
		initListeners();
		// getStockListService();

	}

	private void initVariable() {
		gv_stock_main_body_title = (GridView) findViewById(R.id.gv_stock_main_body_title);
		lv_stock_main_body_list = (ListView) findViewById(R.id.lv_stock_main_body_list);
		pb_stock_main_body = (ProgressBar) findViewById(R.id.pb_stock_main_body);

		bt_move_view_text = (Button) findViewById(R.id.bt_move_view_text);
		pb_move_view_load = (ProgressBar) findViewById(R.id.pb_move_view_load);

		/*
		 * main_stock_body_more_view = (RelativeLayout) getLayoutInflater()
		 * .inflate(R.layout.main_stock_body_more_view, null); bt_move_view_text
		 * = (Button) main_stock_body_more_view
		 * .findViewById(R.id.bt_move_view_text); pb_move_view_load =
		 * (ProgressBar) main_stock_body_more_view
		 * .findViewById(R.id.pb_move_view_load);
		 */

		pageIndex = 1;
		pageSize = 14;
		list = new ArrayList<Stock>();
		// 加入到底部
		// lv_stock_main_body_list.addFooterView(main_stock_body_more_view);
		handler = new Handler();

		titleItems = new String[] { "Brand", "Stock Code",
				"Vendors's Model No.", "Client Model No." };

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.main_stock_body_item, R.id.tv_main_stock_body_item,
				titleItems);
		gv_stock_main_body_title.setAdapter(adapter);
	}

	private void initView() {
		mAdapter = new StockAdapter(this);
		setTopTitle(getString(R.string.gv_stock_manage));
		bt_move_view_text.setVisibility(View.GONE);
	}

	private void initListeners() {
		gv_stock_main_body_title.setOnItemClickListener(this);
		lv_stock_main_body_list
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						TextView tv_main_stock_body_id = (TextView) view
								.findViewById(R.id.tv_main_stock_body_id);
						// TextView tv_main_stock_body_brand = (TextView)
						// view.findViewById(R.id.tv_main_stock_body_brand);
						// tv_main_stock_body_brand.setBackgroundResource(R.color.green);

						if (tv_main_stock_body_id != null) {
							// showMsg(tv_main_stock_body_id.getText() + "");
							Intent mIntent = new Intent();
							mIntent.setClass(getApplicationContext(),
									StockDetailActivity.class);
							String stockId = tv_main_stock_body_id.getText()
									.toString();
							mIntent.putExtra("stockId",
									Integer.parseInt(stockId));
							startActivity(mIntent);
							// openActivity(StockDetailActivity.class);
						}

					}
				});

		bt_move_view_text.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onScrollLast();
			}

		});

		lv_stock_main_body_list.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
						&& lastVisibleIndex == mAdapter.getCount()) {
					// 当滑倒底部的时候自动加载
					onScrollLast();

				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				lastVisibleIndex = firstVisibleItem + visibleItemCount;
			}
		});

	}

	private void onScrollLast() {

		pb_move_view_load.setVisibility(View.VISIBLE);// 进度条可见
		bt_move_view_text.setVisibility(View.GONE);// 按钮不可见

		pb_move_view_load.setEnabled(false);
		bt_move_view_text.setEnabled(false);

		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				getStockListService();
				pb_move_view_load.setVisibility(View.GONE);
				bt_move_view_text.setVisibility(View.VISIBLE);
				pb_move_view_load.setEnabled(true);
				bt_move_view_text.setEnabled(true);
			}
		}, 2000);
	}

	private void bindData() {
		lv_stock_main_body_list.setAdapter(mAdapter);
		bt_move_view_text.setVisibility(View.VISIBLE);
	}

	private void getStockListService() {
		String path = getString(R.string.url_path) + "LoadList";
		RequestParams params = new RequestParams();
		params.put("pageIndex", pageIndex + "");
		params.put("pageSize", pageSize + "");

		/*if (pb_stock_main_body.getVisibility() == View.GONE) {
			pb_stock_main_body.setVisibility(View.VISIBLE);
		}*/

		AsyncHttpClient client = new AsyncHttpClient();
		client.setTimeout(30000);

		client.post(path, params, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(Throwable error, String content) {
				Toast.makeText(getApplicationContext(), "Loading Error", 1)
						.show();
				pb_stock_main_body.setVisibility(View.GONE);
				super.onFailure(error, content);
			}

			@Override
			public void onSuccess(String content) {
				pb_stock_main_body.setVisibility(View.GONE);
				parseJson(content);
				mAdapter.setList(list);
				if (pageIndex == 1) {
					bindData();
				} else {
					mAdapter.notifyDataSetChanged();
					// lv_stock_main_body_list.setSelection((mAdapter.getCount()
					// / 2) - 5);
				}
				Toast toast = Toast.makeText(getApplicationContext(),
						"Loading Success", 0);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				pageIndex++;
				super.onSuccess(content);
			}
		});
	}

	@Override
	protected void onResume() {
		list.clear();
		pageIndex = 1;
		getStockListService();
		super.onResume();
	}

	/**
	 * 解析Json数据
	 * 
	 * @param json
	 *            字符串
	 * @return
	 */
	private List<Stock> parseJson(String json) {
		// list.clear();
		try {
			JSONObject jsonObject = new JSONObject(json);
			total = jsonObject.getInt("total");
			JSONArray jsonArray = jsonObject.getJSONArray("rows");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = (JSONObject) jsonArray.opt(i);
				Stock stock = new Stock();
				stock.setId(obj.getInt("Id"));
				stock.setStock1(obj.getString("stock1"));
				stock.setBrand(obj.getString("brand"));
				stock.setVref(obj.getString("vref"));
				stock.setPartno(obj.getString("partno"));

				list.add(stock);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return list;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
	}

}
