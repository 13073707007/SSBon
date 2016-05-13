package com.loongme.activity.base;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

import com.loongme.activity.R;
import com.loongme.activity.common.Configuration;
import com.loongme.activity.widgets.CustomProgressDialog;
import com.umeng.analytics.MobclickAgent;

/**
 * @author zhaojianyu
 */
public class BaseActivity extends FragmentActivity implements OnClickListener, AppHttpResponseCallBack,
		AppHttpUploadCallBack {

	protected BaseApplication mAppContext;

	/** 等待提示框 */
	private Dialog progressDialog;
	private List<Dialog> progressBarList = new ArrayList<Dialog>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// 竖屏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(getWindow().FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		doinit();

	}

	private final void doinit() {
		initParas();
		initWidgets();
		regListener();
		initUI();

		// 将activity加入到AppManager堆栈中
		AppManager.getAppManager().addActivity(this);
	}

	protected void initParas() {
		// 初始化参数，接收外界参数等
	}

	protected void initWidgets() {
		// 控件初始化
	}

	protected void regListener() {
		// 注册监听器
	}

	protected void initUI() {
		// 界面初始化
	}

	@Override
	protected void onStart() {
		// LogUtil.d(TAG, this.getClass().getSimpleName() +
		// " onStart() invoked!!");
		super.onStart();
	}

	@Override
	protected void onRestart() {
		// LogUtil.d(TAG, this.getClass().getSimpleName() +
		// " onRestart() invoked!!");
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		// LogUtil.d(TAG, this.getClass().getSimpleName() +
		// " onResume() invoked!!");

	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		// LogUtil.d(TAG, this.getClass().getSimpleName() +
		// " onPause() invoked!!");
	}

	@Override
	protected void onStop() {
		// LogUtil.d(TAG, this.getClass().getSimpleName() +
		// " onStop() invoked!!");
		super.onStop();
	}

	@Override
	public void onDestroy() {
		// 结束Activity&从堆栈中移除
		AppManager.getAppManager().finishActivity(this);
		super.onDestroy();
	}

	public void finish() {
		super.finish();
		// 从右到左动画
		overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		return super.onMenuOpened(featureId, menu);
	}

	boolean isCanBack = true;

	@Override
	public void onBackPressed() {
		if (isCanBack) {
			isCanBack = false;
			super.onBackPressed();
			new Thread() {
				public void run() {
					SystemClock.sleep(3000);
					isCanBack = true;
				};
			}.start();
		}
	}

	/**
	 * 显示等待框
	 */
	public void showProgress() {
		try {
			if (!this.isFinishing() && this != null) {
				progressDialog = new CustomProgressDialog(this, "加载中...", R.anim.loading);
				if (null != progressDialog) {
					progressDialog.show();
					if (progressBarList != null) {
						progressBarList.add(progressDialog);
					}
				}
			}
		} catch (Exception e) {
			Log.d(Configuration.TAG, "" + e.getMessage());
		}
	}

	/**
	 * 取消等待框
	 */
	public void dismissProgress() {
		if (progressBarList != null) {
			for (Dialog progressDialog : progressBarList) {
				if (progressDialog != null && progressDialog.isShowing()) {
					progressDialog.dismiss();
					progressDialog = null;
				}
			}

			progressBarList.clear();
		} else {
			if (null != progressDialog) {
				progressDialog.dismiss();
				progressDialog = null;
			}
		}

	}

	@Override
	public void handleStart() {

	}

	@Override
	public boolean handleSuccess(int statusCode, JSONObject response, int flagRequest) {
		return false;
	}

	@Override
	public boolean handleFailure(int statusCode, String responseString, Throwable throwable, int flagRequest) {
		return false;
	}

	@Override
	public void onUploadStart() {

	}

	@Override
	public void onUploadProgress(int bytesWritten, int totalSize) {

	}

	@Override
	public void onUploadSuccess(JSONObject json, int flagRequest) {

	}

	@Override
	public void onUploadFailure(JSONObject json, Throwable throwable, int flagRequest) {

	}
}
