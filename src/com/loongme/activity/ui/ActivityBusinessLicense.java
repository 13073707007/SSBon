package com.loongme.activity.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loongme.activity.R;
import com.loongme.activity.base.BaseActivity;
import com.loongme.activity.common.Configuration;

public class ActivityBusinessLicense extends BaseActivity implements OnClickListener {
	ImageView iv_bus_lic;
	LinearLayout ll_bus_lic, ll_photo_album;
	Button btn_cancel;
	// 设置证件照
	private static final int IMAGE_REQUEST_CODE = 0; // 请求码 本地图片
	private static final int CAMERA_REQUEST_CODE = 1; // 拍照
	private static final String SAVE_AVATORNAME = "head.png";// 保存的图片名
	private TextView txt_notice;
	public static int requestCode;
	private Bitmap bm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_business_license);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		iv_bus_lic = (ImageView) findViewById(R.id.iv_bus_lic);
		ll_bus_lic = (LinearLayout) findViewById(R.id.ll_bus_lic);
		ll_photo_album = (LinearLayout) findViewById(R.id.ll_photo_album);
		txt_notice = (TextView) findViewById(R.id.txt_notice);
		ll_photo_album.setOnClickListener(this);
		ll_bus_lic.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);

		if (requestCode == Configuration.REQUESTCODE_SELECT_LICENSE) {
			txt_notice.setText("请上传营业执照(副本)");
		} else if (requestCode == Configuration.REQUESTCODE_SELECT_IDCARD_MAINSIDE) {
			txt_notice.setText("请上传身份证正面(副本)");
		} else if (requestCode == Configuration.REQUESTCODE_SELECT_IDCARD_REVERSESIDEE) {
			txt_notice.setText("请上传身份证反面(副本)");
		} else if (requestCode == Configuration.REQUESTCODE_SELECT_SERVER_COVER) {
			txt_notice.setText("请上传服务封面");
		} else if (requestCode == Configuration.REQUESTCODE_SELECT_SERVER_WORK) {
			txt_notice.setText("请上传单位工作照或作品图");
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_bus_lic:
			Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(it, CAMERA_REQUEST_CODE);
			break;
		case R.id.ll_photo_album:
			Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
			intent2.addCategory(Intent.CATEGORY_OPENABLE);
			intent2.setType("image/*");
			intent2.putExtra("return-data", true);
			startActivityForResult(intent2, IMAGE_REQUEST_CODE);
			break;
		case R.id.btn_cancel:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_CANCELED) {
			switch (requestCode) {
			case IMAGE_REQUEST_CODE:// 本地
				ContentResolver resolver = getContentResolver();
				Uri bitmapUri = data.getData();// 获得图片uri
				try {
					bm = MediaStore.Images.Media.getBitmap(resolver, bitmapUri);
				} catch (Exception e) {
					e.printStackTrace();
				}
				saveMyBitmap(bm);
				setResult(RESULT_OK);
				finish();
				break;
			case CAMERA_REQUEST_CODE:// 拍照
				Bitmap bitmap = data.getParcelableExtra("data");
				saveMyBitmap(bitmap);
				setResult(RESULT_OK);
				finish();
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 将头像保存在SDcard
	 */
	public void saveMyBitmap(Bitmap bitmap) {
		String filePath = "";
		if (requestCode == Configuration.REQUESTCODE_SELECT_LICENSE) {
			filePath = Configuration.BUSLICENSE_SAVEPATH;
		} else if (requestCode == Configuration.REQUESTCODE_SELECT_IDCARD_MAINSIDE) {
			filePath = Configuration.IDCARD_MAINSIDE_SAVEPATH;
		} else if (requestCode == Configuration.REQUESTCODE_SELECT_IDCARD_REVERSESIDEE) {
			filePath = Configuration.IDCARD_REVERSESIDE_SAVEPATH;
		} else if (requestCode == Configuration.REQUESTCODE_SELECT_SERVER_COVER) {
			filePath = Configuration.SERVER_COVER_SAVEPATH;
		} else if (requestCode == Configuration.REQUESTCODE_SELECT_SERVER_WORK) {
			filePath = Configuration.SERVER_WORK_SAVEPATH;
		}
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
		try {
			file.createNewFile();
			FileOutputStream fOut = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);

			fOut.flush();
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
