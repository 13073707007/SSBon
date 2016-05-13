package com.loongme.activity.base;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.json.JSONObject;

import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

public class AppHttpUploadHandler extends BinaryHttpResponseHandler {
	private JSONObject item;
	private AppHttpUploadCallBack callback;
	private int flagRequest;

	// private AppHttpUploadHandler(String[] allowedContentTypes) {
	// super(allowedContentTypes);
	// }

	public AppHttpUploadHandler(JSONObject item, AppHttpUploadCallBack callback, int flagRequest) {
		super(new String[] { "*/*", RequestParams.APPLICATION_OCTET_STREAM, "image/jpeg", "image/png", "image/gif",
				"text/html;charset=utf-8" });
		this.item = item;
		this.callback = callback;
		this.flagRequest = flagRequest;
	}

	@Override
	public void onStart() {
		super.onStart();
		// 把当前要上传的文件的概要信息写入到日志中，等到上传完毕的时候再更新该日志
		// FileUtils.writeContent(item.toString(), logFilePath, false);
		if (null != callback) {
			callback.onUploadStart();
		}
	}

	@Override
	public void onFailure(int stateCode, Header[] headers, byte[] bytes, Throwable throwable) {
		if (null != callback) {
			callback.onUploadFailure(item, throwable, flagRequest);
		}
	}

	@Override
	public void onSuccess(int stateCode, Header[] headers, byte[] bytes) {
		if (null != callback) {
			callback.onUploadSuccess(item, flagRequest);
		}
	}

	@Override
	public void onProgress(int bytesWritten, int totalSize) {
		super.onProgress(bytesWritten, totalSize);
		// Trace.d("AttachmentUploadHandler onProgress() " + "bytesWritten:" +
		// bytesWritten
		// + " totalSize:" + totalSize);
		if (null != callback) {
			callback.onUploadProgress(bytesWritten, totalSize);
		}
	}

	@Override
	public void onPostProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {
		super.onPostProcessResponse(instance, response);
	}

	@Override
	public void onPreProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {
		super.onPreProcessResponse(instance, response);
	}

	@Override
	public void onRetry(int retryNo) {
		super.onRetry(retryNo);
	}

	@Override
	public void onCancel() {
		super.onCancel();
	}

	@Override
	public void onFinish() {
		super.onFinish();
	}
}
