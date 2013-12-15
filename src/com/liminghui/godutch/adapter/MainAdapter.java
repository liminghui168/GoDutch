package com.liminghui.godutch.adapter;

import com.liminghui.godutch.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainAdapter extends BaseAdapter {

	private class Holder {
		ImageView iv_item_icon;
		TextView tv_item_name;
	}

	private Integer[] images = { R.drawable.grid_payout, R.drawable.grid_bill,
			R.drawable.grid_report, R.drawable.grid_account_book,
			R.drawable.grid_category, R.drawable.grid_user, R.drawable.grid_stock};

	private String[] names = new String[7];

	private Context context;

	public MainAdapter(Context context) {
		this.context = context;
		names[0] = this.context.getString(R.string.gv_payout_add);
		names[1] = this.context.getString(R.string.gv_payout_query);
		names[2] = this.context.getString(R.string.gv_statistics_manage);
		names[3] = this.context.getString(R.string.gv_accountbook_manage);
		names[4] = this.context.getString(R.string.gv_category_manage);
		names[5] = this.context.getString(R.string.gv_user_manage);
		names[6] = this.context.getString(R.string.gv_stock_manage);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return images.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return images[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		Holder holder;
		if (arg1 == null) {
			LayoutInflater mInflater = LayoutInflater.from(context);
			arg1 = mInflater.inflate(R.layout.main_body_item, null);
			holder = new Holder();
			holder.iv_item_icon = (ImageView) arg1
					.findViewById(R.id.iv_item_icon);
			holder.tv_item_name = (TextView) arg1
					.findViewById(R.id.tv_item_name);

			arg1.setTag(holder);
		} else {
			holder = (Holder) arg1.getTag();
		}

		holder.iv_item_icon.setImageResource(images[arg0]);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(80, 80);
		holder.iv_item_icon.setLayoutParams(layoutParams);
		holder.iv_item_icon.setScaleType(ImageView.ScaleType.FIT_XY);
		
		holder.tv_item_name.setText(names[arg0]);

		return arg1;
	}

}
