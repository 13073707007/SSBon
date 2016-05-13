package com.loongme.activity.base;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.json.JSONObject;

import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.ResponseHandlerInterface;

public class AppHttpDownloadHandler extends BinaryHttpResponseHandler {

  private AppHttpDownloadCallBack callback;
  private int flagRequest;
  private JSONObject item;

  /**
   * @param item
   */
  public AppHttpDownloadHandler(JSONObject item, AppHttpDownloadCallBack callback, int flagRequest) {
    this.item = item;
    this.callback = callback;
    this.flagRequest = flagRequest;

  }

  @Override
  public void onStart() {
    super.onStart();
    // 把当前要下载的文件的概要信息写入到日志中，等到下载完毕的时候再更新该日志
    if (null != callback) {
      callback.onDownloadStart();
    }
  }

  @Override
  public void onFailure(int stateCode, Header[] headers, byte[] bytes, Throwable throwable) {
    if (null != callback) {
      callback.onDownloadFailure(item, throwable, flagRequest);
    }
  }

  @Override
  public void onSuccess(int stateCode, Header[] headers, byte[] bytes) {
    if (null != callback) {
      callback.onDownloadSuccess(item, flagRequest);
    }
  }

  @Override
  public void onProgress(int bytesWritten, int totalSize) {
    super.onProgress(bytesWritten, totalSize);
    // Trace.d("MyBinaryHttpResponseHandler onProgress() " + "bytesWritten:"
    // + bytesWritten
    // + " totalSize:" + totalSize);
    if (null != callback) {
      callback.onDownloadProgress(bytesWritten, totalSize);
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
    // if (!Helpers.isNetworkAvailable(adapter.getContext())) {
    // sendCancelMessage();
    // }
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
