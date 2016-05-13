package com.loongme.activity.base;

import org.json.JSONObject;

public interface AppHttpUploadCallBack {

  /**
   * 上传开始
   */
  public void onUploadStart();

  /**
   * 上传过程中
   * 
   * @param bytesWritten
   * @param totalSize
   */
  public void onUploadProgress(int bytesWritten, int totalSize);

  /**
   * 上传完毕后的回调函数
   * 
   */
  public void onUploadSuccess(JSONObject json, int flagRequest);

  /**
   * 上传失败
   * 
   * @param json
   * @param throwable
   * @param flagRequest
   */
  public void onUploadFailure(JSONObject json, Throwable throwable, int flagRequest);

}
