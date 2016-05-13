package com.loongme.activity.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.client.CookieStore;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.loongme.activity.common.Configuration;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.SyncHttpClient;

public class HttpUtil {

	private static final String TAG = Configuration.TAG;
	/**
	 * 有线网络USB连接
	 * 
	 */
	public static final String KEY_LAN_STAT = "dhcp.eth0.eth0_stat";
	/**
	 * 有线网络状态: 已连接
	 * 
	 */
	public static final String LAN_STAT_CONNECTED = "connected";
	/**
	 * 有线网络状态: 正在连接
	 * 
	 */
	public static final String LAN_STAT_ACQUIRED = "acquired";
	/**
	 * 有线网络状态: 未连接
	 * 
	 */
	public static final String LAN_STAT_DISCONNECTED = "disconnected";

	/**
	 * 等待结果的最大时间1秒钟
	 */
	private static final double MAX_TIME_TO_WAIT = 700;

	private static AsyncHttpClient mHttpClient;
	private static SyncHttpClient mSyncHttpClient;
	private static Context mContext;
	/**
	 * 超时时间
	 */
	private static final int TIMEOUT_NUM = 60 * 1000;// 5000
	/**
	 * 最大连接数
	 */
	private static final int MAX_CONNECTIONS = 100;

	/**
	 * Use the application's context so that memory leakage doesn't occur.
	 * 
	 * @param context
	 * @return null if context is null
	 * 
	 *         <pre>
	 * AsyncHttpClient client = new AsyncHttpClient();
	 * client.get(&quot;http://www.google.com&quot;, new AsyncHttpResponseHandler() {
	 * 	&#064;Override
	 * 	public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
	 * 		System.out.println(response);
	 * 	}
	 * 
	 * 	&#064;Override
	 * 	public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
	 * 		error.printStackTrace(System.out);
	 * 	}
	 * });
	 * </pre>
	 */
	public static AsyncHttpClient getHttpClient(Context context) {
		if (null == context) {
			return null;
		}
		mContext = context;
		if (null == mHttpClient) {
			mHttpClient = new AsyncHttpClient();
			setupHttpClient(mHttpClient);
		}
		return mHttpClient;
	}

	/**
	 * 默认超时时间10s 默认最大连接数10s 默认最大重试次数5次
	 * 
	 * @param mHttpClient
	 */
	private static void setupHttpClient(AsyncHttpClient mHttpClient) {
		CookieStore mCookieStore = CookieUtil.getCookieStore(mContext);
		// mHttpClient.addHeader("Content-Type",
		// "application/json;text/json;text/javascript;application/soap+xml;text/html;text/xml;text/plain");
		mHttpClient.setCookieStore(mCookieStore);
		mHttpClient.setUserAgent("android");
		// 超时时间
		mHttpClient.setResponseTimeout(TIMEOUT_NUM);
		mHttpClient.setConnectTimeout(TIMEOUT_NUM);
		// 最大连接数
		mHttpClient.setMaxConnections(MAX_CONNECTIONS);
		mHttpClient.setURLEncodingEnabled(false);
	}

	public static SyncHttpClient getSyncHttpClient(Context context) {
		if (null == context) {
			return null;
		}
		mContext = context;
		if (null == mSyncHttpClient) {
			mSyncHttpClient = new SyncHttpClient();
			setupHttpClient(mSyncHttpClient);
		}
		return mSyncHttpClient;
	}

	/**
	 * Returns whether the network is Available
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cwjManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cwjManager.getActiveNetworkInfo();
		// NetworkInfo networkInfo =
		// cwjManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		// 有可能为null，如果为null，则返回false（认为网路不通）;如果网络不可用，也返回false
		if (null == networkInfo || !networkInfo.isAvailable()) {
			Log.v(TAG, "network is not available");
			return false;
		}

		// 判断网络是否连接
		return networkInfo.isConnected();

		// return true;//checkNetWorkSpeedIsAccepted()
	}

	/**
	 * Returns whether the wifi is Available
	 */
	public static boolean isWifiNetworkAvailable(Context context) {
		ConnectivityManager cwjManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cwjManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (null == networkInfo || !networkInfo.isAvailable()) {
			Log.v(TAG, "network is not available");
			return false;
		}
		return networkInfo.isConnected();
		// return false;//checkNetWorkSpeedIsAccepted()
	}

	// ping -c 1 www.baidu.com | awk '/time/ {print $7}'
	/**
	 * 得到ping的时间，单位毫秒，如果超时或者命令失败，则返回-1
	 * 
	 * @param address
	 * @return
	 */
	public static double getPingTime(String address) {
		try {
			StringBuffer sb = new StringBuffer();
			// String result = "";
			Process p = null;
			// ping 一次，并且如果超时或远端服务器地址不对的话，等待时间为50毫秒
			String cmd = "/system/bin/ping -c 1 -W 50 " + address;
			// "| awk '/time/ {print $7}'"
			// Log.v("NETWORK", "cmd: " + cmd);
			p = Runtime.getRuntime().exec(cmd);
			if (p.waitFor() == 0) {
				// 正常情况
				// result = "success";
			} else {
				// 除非系统里面没有ping这个命令，或者位置不对
				// result = "failed";
				return -1;
			}
			// Log.v("NETWORK", "ping exec " + result);
			BufferedReader buf = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String str = "";

			// 读出所有信息并显示

			while ((str = buf.readLine()) != null) {
				// str += "\r\n";
				sb.append(str).append("\r\n");
			}
			if (!TextUtils.isEmpty(sb.toString())) {
				int idxTime = sb.toString().indexOf("time=");
				int idxms = sb.toString().indexOf(" ms");
				if (idxTime != -1 && idxms != -1) {
					String sequence = sb.toString().substring(idxTime, idxms);
					return Double.parseDouble(sequence.substring(sequence.indexOf("=") + 1));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return -1;
	}

	/**
	 * true能忍，false不能忍
	 * 
	 * @return
	 */
	public static boolean checkNetWorkSpeedIsAccepted() {
		double pingtime = getPingTime("www.baidu.com");
		if (pingtime == -1 || pingtime > MAX_TIME_TO_WAIT) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Returns whether the network is roaming
	 */
	public static boolean isNetworkRoaming(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (null != connectivity) {
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info != null && info.getType() == ConnectivityManager.TYPE_MOBILE) {
				if (((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).isNetworkRoaming()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 连接wifi
	 */
	public static void startWifi(final Context context) {

		if (context != null) {
			Intent intent = new Intent("android.settings.WIRELESS_SETTINGS");
			intent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");
			intent.putExtra("currentIndex", 0);
			intent.putExtra("inSetting", true);
			intent.putExtra("topActivity", true);
			((Activity) context).startActivity(intent);
		}
	}

}
