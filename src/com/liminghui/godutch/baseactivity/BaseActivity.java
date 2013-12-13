package com.liminghui.godutch.baseactivity;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

public class BaseActivity extends Activity {
	
	protected void showMsg(String msg) {
		Toast.makeText(this, msg, 0).show();
	}
	
	protected void openActivity(Class<?> cls) {
		Intent intent = new Intent();
		intent.setClass(this, cls);
		startActivity(intent);
	}
	
}
