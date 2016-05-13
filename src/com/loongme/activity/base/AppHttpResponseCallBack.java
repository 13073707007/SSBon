package com.loongme.activity.base;

import org.json.JSONObject;

/**
 * http调用反馈接口
 */
public interface AppHttpResponseCallBack {

	/**
	 * 开始访问
	 */
	public void handleStart();

	/**
	 * 成功
	 * 
	 * @param json
	 * @param flagRequest
	 * @return
	 */
	public boolean handleSuccess(int statusCode, JSONObject response, int flagRequest);

	/**
	 * 失败
	 * 
	 * @param throwable
	 * @param errorResponse
	 * @param flagRequest
	 * @return
	 */
	public boolean handleFailure(int statusCode, String responseString, Throwable throwable, int flagRequest);

}
