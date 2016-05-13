package com.loongme.activity.business;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.loongme.activity.common.Configuration;
import com.loongme.activity.utils.DeviceUtils;
import com.loongme.activity.utils.Logger;
import com.loongme.activity.utils.UIUtil;

/**
 * @author 作者:zhaojianyu
 * @version 创建时间：2016-5-5 下午6:46:26 类说明:
 */
@SuppressLint("HandlerLeak")
public class SSBonLocationService extends Service {

	private SsbService requestManager = new SsbService();
	private String locationInfo;// 时时的位置信息
	private boolean getLocationSwitch = true;// 时时提交位置信息的开关
	private LocationClient locationClient;
	private boolean startThreadOnce = true;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		initLocationClient();
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	private class LocationThread extends Thread {
		@Override
		public void run() {
			while (getLocationSwitch) {
				try {
					requestManager.connecttoserver();
					if (requestManager.getSocket().isConnected()) {
						requestManager.SendMsg(requestManager.getSocket(), locationInfo);
					}
					String getNoticeServiceMsg = requestManager.ReceiveMsg(requestManager.getSocket());
					Logger.d(Configuration.TAG, getNoticeServiceMsg);
				} catch (Exception e) {
					e.printStackTrace();
				}

				try {
					sleep(45 * 1000);// 每45秒提交一次
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
			// Looper.prepare();
			// mHandler = new Handler() {
			// @Override
			// public void handleMessage(Message msg) {
			// try {
			// if (msg.what == requestLocationTag) {
			// System.out.println("-----接收到msg------");
			// requestManager.connecttoserver();
			// System.out.println("run locationThread");
			// if (requestManager.getSocket().isConnected()) {
			// requestManager.SendMsg(requestManager.getSocket(), locationInfo);
			// }
			// String getNoticeServiceMsg =
			// requestManager.ReceiveMsg(requestManager.getSocket());
			// if (getNoticeServiceMsg != null &&
			// getNoticeServiceMsg.contains("ROBOTOUTPUT")) {
			// System.out.println("-----------" + getNoticeServiceMsg);
			// }
			//
			// Message msg0 = mHandler.obtainMessage();
			// msg0.what = requestLocationTag;
			// mHandler.sendMessageDelayed(msg0, 2 * 1000);
			// }
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			// }
			// };
			// Looper.loop();
			super.run();
		}
	}

	private void initLocationClient() {
		locationClient = new LocationClient(this.getApplicationContext());
		locationClient.registerLocationListener(new SSBonLocationListener());
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
		// option.setCoorType(tempcoor);//返回的定位结果是百度经纬度，默认值gcj02
		option.setIgnoreKillProcess(false);// 杀死service
		int span = 15000;// 15秒定位一次
		option.setScanSpan(span);// 设置发起定位请求的间隔时间
		option.setIsNeedAddress(true);
		locationClient.setLocOption(option);
		locationClient.start();
	}

	@Override
	public void onDestroy() {
		System.out.println("-----locationservice  destroy--------");
		getLocationSwitch = false;
		locationClient.stop();
		super.onDestroy();
	}

	private class SSBonLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation) {
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
				sb.append("\ndirection : ");
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				sb.append(location.getDirection());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				// 运营商信息
				sb.append("\noperationers : ");
				sb.append(location.getOperators());
			}
			if (location.getAddrStr() != null && location.getLatitude() != 0.0 && location.getLongitude() != 0.0) {
				String locationResult = location.getAddrStr();
				double lat = location.getLatitude();
				double lon = location.getLongitude();
				// 获取版本号
				try {
					int versionCode = UIUtil.getAppVersionCode(getApplicationContext());
					locationInfo = "Ssbon_Location_WX  {COMM_INFO {BUSI_CODE 10011} {REGION_ID A} {COUNTY_ID A00} {OFFICE_ID 22342} {OPERATOR_ID 43643} {CHANNEL A2} {OP_MODE SUBMIT}} { {XW_OPENID "
							+ DeviceUtils.getIMEINum(getApplicationContext())
							+ "} {LOC_X "
							+ lat
							+ "} {LOC_Y "
							+ lon
							+ "} {USER_ADDR " + locationResult + "} {VERSION " + versionCode + "} }";
					if (startThreadOnce) {
						Thread t = new LocationThread();
						t.start();
						startThreadOnce = false;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
