package com.loongme.activity.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loongme.activity.R;
import com.loongme.activity.base.BaseActivity;
import com.loongme.activity.common.Configuration;

public class BusinessLicenseActivity extends BaseActivity implements
		OnClickListener {
	ImageView iv_bus_lic;
	LinearLayout ll_bus_lic, ll_photo_album;
	Button btn_cancel;
	// 设置头像
	private static final int IMAGE_REQUEST_CODE = 0; // 请求码 本地图片
	private static final int CAMERA_REQUEST_CODE = 1; // 拍照
	private static final int RESULT_REQUEST_CODE = 2; // 裁剪
	private static final String SAVE_AVATORNAME = "head.png";// 保存的图片名
	private TextView txt_notice;
	public static int requestCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
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
			it.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(new File(Environment
							.getExternalStorageDirectory(), SAVE_AVATORNAME)));
			startActivityForResult(it, CAMERA_REQUEST_CODE);
			break;
		case R.id.ll_photo_album:
			Intent it2 = new Intent();
			it2.setType("image/*");
			it2.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(it2, IMAGE_REQUEST_CODE);
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
			case IMAGE_REQUEST_CODE:
//				startPhotoZoom(data.getData());
				break;
			case CAMERA_REQUEST_CODE:
//				startPhotoZoom(Uri.fromFile(new File(Environment
//						.getExternalStorageDirectory(), SAVE_AVATORNAME)));
				break;
//			case RESULT_REQUEST_CODE:
//				if (data != null) {
//					getImageToView(data);
//					setResult(RESULT_OK);
//					finish();
//				}
//				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	/**
	 * 裁剪图片方法实现
	 */
//	public void startPhotoZoom(Uri uri) {
//		Intent intent = new Intent("com.android.camera.action.CROP");
//		intent.setDataAndType(uri, "image/*");
//		// 设置裁剪
//		intent.putExtra("crop", "true");
//		// aspectX aspectY 是宽高的比例
//		intent.putExtra("aspectX", 1);
//		intent.putExtra("aspectY", 1);
//		// outputX outputY 是裁剪图片宽高
//		intent.putExtra("outputX", 300);
//		intent.putExtra("outputY", 300);
//		intent.putExtra("return-data", true);
//		startActivityForResult(intent, RESULT_REQUEST_CODE);
//	}

	/**
	 * 保存裁剪之后的图片数据
	 */
	private void getImageToView(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			saveMyBitmap(photo); // 保存裁剪后的图片到SD
			// iv_bus_lic.setImageBitmap(photo);
		}
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
