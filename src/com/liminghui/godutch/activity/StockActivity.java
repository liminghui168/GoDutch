package com.liminghui.godutch.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.main_stock_body);
		initVariable();
		initView();
		initListeners();
		loadStockGrid();

	}

	private void initVariable() {
		gv_stock_main_body_title = (GridView) findViewById(R.id.gv_stock_main_body_title);
		lv_stock_main_body_list = (ListView) findViewById(R.id.lv_stock_main_body_list);
		pb_stock_main_body = (ProgressBar) findViewById(R.id.pb_stock_main_body);
		main_stock_body_more_view = (RelativeLayout) getLayoutInflater()
				.inflate(R.layout.main_stock_body_more_view, null);
		bt_move_view_text = (Button) main_stock_body_more_view
				.findViewById(R.id.bt_move_view_text);
		pb_move_view_load = (ProgressBar) main_stock_body_more_view
				.findViewById(R.id.pb_move_view_load);

		pageIndex = 1;
		pageSize = 13;
		list = new ArrayList<Stock>();
		// 加入到底部
		lv_stock_main_body_list.addFooterView(main_stock_body_more_view);
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
						// String name = (String) mAdapter.getItem(position);
						if (tv_main_stock_body_id != null) {
							showMsg(tv_main_stock_body_id.getText() + "");
						}

					}
				});

		bt_move_view_text.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
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
				/*
				 * ListView lv = (ListView) view; view.getScrollX()
				 */
				lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
				// lv_stock_main_body_list.setSelection(count);
				// lv_stock_main_body_list.setSelected(true);
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
				loadStockGrid();
				pb_move_view_load.setVisibility(View.GONE);
				bt_move_view_text.setVisibility(View.VISIBLE);
				pb_move_view_load.setEnabled(true);
				bt_move_view_text.setEnabled(true);
				// mAdapter.getList().clear();
				// mAdapter.getList().addAll(list);
				mAdapter.notifyDataSetChanged();
				// setListViewPos(lastVisibleIndex);// 滚动到指定位置
			}
		}, 2000);
	}

	private void bindData() {
		lv_stock_main_body_list.setAdapter(mAdapter);
	}

	private void setListViewPos(int pos) {
		if (android.os.Build.VERSION.SDK_INT >= 8) {
			lv_stock_main_body_list.smoothScrollToPosition(pos);
		} else {
			lv_stock_main_body_list.setSelection(pos);
		}
		lv_stock_main_body_list.setSelected(true);
	}

	private void loadStockGrid() {
		getStockListService();
		pageIndex++;
		/*
		 * if (count + 13 < total) { // 每次加载13条
		 * 
		 * } else {
		 * 
		 * }
		 */

	}

	private void getStockListService() {

		String path = getString(R.string.url_path) + "LoadList";
		RequestParams params = new RequestParams();
		params.put("pageIndex", pageIndex + "");
		params.put("pageSize", pageSize + "");
		AsyncHttpClient client = new AsyncHttpClient();

		client.post(path, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(String content) {
				pb_stock_main_body.setVisibility(View.GONE);
				parseJson(content);
				mAdapter.setList(list);
				bindData();
				super.onSuccess(content);
			}
		});
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
				stock.setStock(obj.getString("stock1"));
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
