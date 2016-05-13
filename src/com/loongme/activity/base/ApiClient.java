package com.loongme.activity.base;

import org.json.JSONObject;

import android.content.Context;

import com.loongme.activity.utils.HttpUtil;
import com.loongme.activity.utils.StringUtils;
import com.loongme.activity.utils.UIUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

/**
 * API客户端接口：用于访问网络数据
 * 
 */
public class ApiClient {

	private AsyncHttpClient client;
	private static ApiClient apiClient;
	private Context mContext;

	public static ApiClient getInstance(Context context) {
		if (apiClient == null) {
			apiClient = new ApiClient(context);
		}

		return apiClient;

	}

	private ApiClient(Context context) {
		super();
		this.mContext = context;
		client = HttpUtil.getHttpClient(context);
	}

	public void load(String url, String params, int flag, AppHttpResponseCallBack responseCallBack) {
		if (StringUtils.isEmpty(params)) {
			get(url, flag, responseCallBack);
		} else {
			post(url, params, flag, responseCallBack);
		}

	}

	/**
	 * get访问网络
	 * 
	 * @param url
	 * @param flag简单标记
	 * @param responseCallBack回调接口
	 */
	public void get(String url, int flag, AppHttpResponseCallBack responseCallBack) {
		AppHttpResponseHandler responseHandler = new AppHttpResponseHandler(mContext, responseCallBack, flag);
		client.get(url, responseHandler);
	}

	/**
	 * post访问网络
	 * 
	 * @param uri
	 * @param params
	 * @param flag
	 */
	public void post(String url, String params, int flag, AppHttpResponseCallBack responseCallBack) {
		AppHttpResponseHandler responseHandler = new AppHttpResponseHandler(mContext, responseCallBack, flag);
		client.post(url, UIUtil.parseRequestParam(params), responseHandler);
	}

	/**
	 * 下载文件
	 * 
	 * @param url下载路径
	 * @param object下载文件的信息
	 *            存储路径、文件名、log日志路径、下载状态统计文件路径...
	 * @param flag
	 */
	public void download(String url, JSONObject object, int flag, AppHttpDownloadCallBack downloadCallBack) {
		client.get(url, new AppHttpDownloadHandler(object, downloadCallBack, flag));
	}

	/**
	 * 上传文件
	 * 
	 * @param url
	 * @param rp
	 * @param object上传文件的信息
	 *            存储文件路径、文件名、上传状态文件路径...
	 * @param flag
	 */
	public void upload(String url, RequestParams rp, JSONObject object, int flag, AppHttpUploadCallBack uploadCallBack) {
		AppHttpUploadHandler appHttpUploadHandler = new AppHttpUploadHandler(object, uploadCallBack, flag);
		client.post(url, rp, appHttpUploadHandler);
	}

}
