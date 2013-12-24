package com.liminghui.godutch.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.liminghui.godutch.R;
import com.liminghui.godutch.baseactivity.FrameActivity;
import com.zxing.activity.CaptureActivity;
import com.zxing.encoding.EncodingHandler;

public class BarcodeActivity extends FrameActivity implements OnClickListener {

	private TextView tv_scan_result;
	private EditText et_qr_string;
	private ImageView iv_qr_image;
	private Button bt_scan_barcode;
	private Button bt_add_qrcode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.barcode);
		initVariable();
		initListeners();
	}

	private void initVariable() {
		tv_scan_result = (TextView) this.findViewById(R.id.tv_scan_result);
		et_qr_string = (EditText) this.findViewById(R.id.et_qr_string);
		iv_qr_image = (ImageView) this.findViewById(R.id.iv_qr_image);
		bt_scan_barcode = (Button) this.findViewById(R.id.bt_scan_barcode);
		bt_add_qrcode = (Button) this.findViewById(R.id.bt_add_qrcode);
	}

	private void initListeners() {
		bt_scan_barcode.setOnClickListener(this);
		bt_add_qrcode.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_scan_barcode:
			Intent openCameraIntent = new Intent(this, CaptureActivity.class);
			startActivityForResult(openCameraIntent, 0);
			break;
		case R.id.bt_add_qrcode:
			try {
				String contentString = et_qr_string.getText().toString();
				if (!contentString.equals("")) {
					// 根据字符串生成二维码图片并显示在界面上，第二个参数为图片的大小（350*350）
					Bitmap qrCodeBitmap = EncodingHandler.createQRCode(
							contentString, 350);
					iv_qr_image.setImageBitmap(qrCodeBitmap);
				} else {
					Toast.makeText(this, "Text can not be empty", 1).show();
				}

			} catch (WriterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 处理扫描结果（在界面上显示）
		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			tv_scan_result.setText(scanResult);
		}
	}

}
