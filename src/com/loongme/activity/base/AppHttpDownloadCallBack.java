package com.loongme.activity.base;

import org.json.JSONObject;

public interface AppHttpDownloadCallBack {

  /**
   * 下载开始
   */
  public void onDownloadStart();

  /**
   * 下载过程中
   */
  public void onDownloadProgress(int bytesWritten, int totalSize);

  /**
   * 下载完毕后的回调函数
   * 
   */
  public void onDownloadSuccess(JSONObject json, int flagRequest);

  /**
   * 下载失败
   * 
   */
  public void onDownloadFailure(JSONObject json, Throwable throwable, int flagRequest);
}
