package com.loongme.activity.base;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.loongme.activity.R;
import com.loongme.activity.common.Configuration;
import com.loongme.activity.widgets.CustomProgressDialog;

/**
 * 碎片基类
 * 
 */
public class BaseFragment extends Fragment implements OnClickListener, AppHttpResponseCallBack {

	private static final String TAG = Configuration.TAG;
	/** 等待提示框 */
	private Dialog progressDialog;
	private List<Dialog> progressBarList = new ArrayList<Dialog>();

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 显示等待框
	 */
	public void showProgress() {
		try {
			if (!getActivity().isFinishing() && getActivity() != null) {
				progressDialog = new CustomProgressDialog(getActivity(), "加载中...", R.anim.loading);
				if (null != progressDialog) {
					progressDialog.show();
					if (progressBarList != null) {
						progressBarList.add(progressDialog);
					}
				}
			}
		} catch (Exception e) {
			Log.d(TAG, "" + e.getMessage());
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
	public void onClick(View v) {

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

}
