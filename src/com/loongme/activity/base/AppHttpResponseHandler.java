package com.loongme.activity.base;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Context;

import com.loongme.activity.utils.HttpUtil;
import com.loongme.activity.utils.UIUtil;
import com.loopj.android.http.JsonHttpResponseHandler;

public class AppHttpResponseHandler extends JsonHttpResponseHandler {
	private Context context;
	private AppHttpResponseCallBack callback;
	private int flagRequest = -1;

	public AppHttpResponseHandler(Context context, AppHttpResponseCallBack callback, int flagRequest) {
		this.context = context;
		this.callback = callback;
		this.flagRequest = flagRequest;
	}

	public int getFlagRequest() {
		return flagRequest;
	}

	public void setFlagRequest(int flagRequest) {
		this.flagRequest = flagRequest;
	}

	@Override
	public void onStart() {
		super.onStart();
		if (!HttpUtil.isNetworkAvailable(context)) {
			UIUtil.showMsg(context, "网络无连接,请检查网络");
			return;
		}
		if (null != callback) {
			callback.handleStart();
		}
	}

	@Override
	public void onProgress(int bytesWritten, int totalSize) {
		super.onProgress(bytesWritten, totalSize);
	}

	@Override
	public void onRetry(int retryNo) {
		super.onRetry(retryNo);
		// 返回重试次数
	}

	@Override
	public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
		if (null != callback) {
			callback.handleFailure(statusCode, responseString, throwable, flagRequest);
		}
		super.onFailure(statusCode, headers, responseString, throwable);
	}

	@Override
	public final void onSuccess(int statusCode, Header[] headers, JSONObject response) {
		if (null != callback) {
			callback.handleSuccess(statusCode, response, flagRequest);
		}
	}

}
