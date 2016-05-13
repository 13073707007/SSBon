package com.loongme.activity.widgets.cropimg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.loongme.activity.R;
import com.loongme.activity.common.Configuration;
import com.loongme.activity.utils.ImageUtils;

public class CutAvatarActivity extends Activity {

	public static Bitmap bitmap;
	private CutAvatarView mCutAvatarView;
	private int isFrom = -1; // 03拍照14系统相册
	private String imagePath = "";// 图片路径

	private int sysWidth;
	private int sysHeight;
	private int THUMBNAIL_SIZE;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cut_avatar);
		sysWidth = getWindowManager().getDefaultDisplay().getWidth();
		sysHeight = getWindowManager().getDefaultDisplay().getHeight();
		THUMBNAIL_SIZE = sysWidth;
		try {
			Intent intent = getIntent();
			isFrom = intent.getIntExtra("isFrom", -1);
			if (isFrom == Configuration.REQUESTCODE_TAKEPHOTOS) {
				bitmap = getThumbnail(Configuration.PHOTO_SAVEPATH);
			} else if (isFrom == Configuration.REQUESTCODE_SELECTPICS) {
				imagePath = intent.getStringExtra("imagePath");
				bitmap = getThumbnail(Uri.parse(imagePath));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		mCutAvatarView = (CutAvatarView) findViewById(R.id.cut_avatar_view);
		mCutAvatarView.setImageBitmap(bitmap);

		// findViewById(R.id.btn_cut).setOnClickListener(doCut());
		findViewById(R.id.btn_cancel).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// setResult(RESULT_FIRST_USER);
				File file = new File(Configuration.PHOTO_SAVEPATH);
				if (file.exists()) {
					file.delete();
				}
				finish();

			}
		});
		findViewById(R.id.btn_cut).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (bitmap != null && bitmap.isRecycled()) {
					bitmap.recycle();
				}
				bitmap = mCutAvatarView.clip(true);
				ImageUtils.saveFile(bitmap, Configuration.PHOTO_SAVEPATH);
				setResult(RESULT_OK);
				finish();

			}
		});
	}

	private View.OnClickListener doCut() {
		return new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (bitmap != null && bitmap.isRecycled()) {
					bitmap.recycle();
				}
				bitmap = mCutAvatarView.clip(true);
				setResult(RESULT_OK);
				finish();
			}
		};
	}

	public Bitmap getThumbnail(Uri uri) throws FileNotFoundException, IOException {
		InputStream input = this.getContentResolver().openInputStream(uri);

		BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
		onlyBoundsOptions.inJustDecodeBounds = true;
		onlyBoundsOptions.inDither = true;// optional
		onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional
		BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
		input.close();
		if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1))
			return null;

		int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight
				: onlyBoundsOptions.outWidth;

		double ratio = (originalSize > THUMBNAIL_SIZE) ? (originalSize / THUMBNAIL_SIZE) : 1.0;

		BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
		bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
		bitmapOptions.inDither = true;// optional
		bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional
		input = this.getContentResolver().openInputStream(uri);
		Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
		input.close();
		return bitmap;
	}

	public Bitmap getThumbnail(String path) throws FileNotFoundException, IOException {
		File file = new File(path);
		FileInputStream input = new FileInputStream(file);

		BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
		onlyBoundsOptions.inJustDecodeBounds = true;
		onlyBoundsOptions.inDither = true;// optional
		onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional
		BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
		input.close();
		if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1))
			return null;

		int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight
				: onlyBoundsOptions.outWidth;

		double ratio = (originalSize > THUMBNAIL_SIZE) ? (originalSize / THUMBNAIL_SIZE) : 1.0;

		BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
		bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
		bitmapOptions.inDither = true;// optional
		bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional
		input = new FileInputStream(file);
		Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
		input.close();
		return bitmap;
	}

	private static int getPowerOfTwoForSampleRatio(double ratio) {
		int k = Integer.highestOneBit((int) Math.floor(ratio));
		if (k == 0)
			return 1;
		else
			return k;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(RESULT_FIRST_USER);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
