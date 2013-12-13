package com.liminghui.godutch.baseactivity;

import com.liminghui.godutch.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class FrameActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
	}
	
	protected void appendMainBody(int resId) {
		LinearLayout linearLayout = (LinearLayout) findViewById(R.layout.main_body);
		View view = LayoutInflater.from(this).inflate(resId, null);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,RelativeLayout.LayoutParams.FILL_PARENT);
		linearLayout.addView(view, layoutParams);
	}
	
}
