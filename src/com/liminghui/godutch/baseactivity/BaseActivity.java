package com.liminghui.godutch.baseactivity;

import android.content.Intent;
import android.widget.Toast;

public class BaseActivity extends FrameActivity {
	
	protected void ShowMsg(String msg) {
		Toast.makeText(this, msg, 1).show();
	}
	
	protected void OpenActivity(Class<?> cls) {
		Intent intent = new Intent();
		intent.setClass(this, cls);
		startActivity(intent);
	}
	
}
