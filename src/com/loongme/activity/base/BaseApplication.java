package com.loongme.activity.base;

import java.io.File;

import android.app.Application;
import android.content.Context;
import cn.smssdk.SMSSDK;

import com.baidu.mapapi.SDKInitializer;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.loongme.activity.common.Configuration;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class BaseApplication extends Application {

	private static BaseApplication instance;
	private boolean isLogin = false;

	@Override
	public void onCreate() {
		super.onCreate();
		// 崩溃处理器，会在硬件上保持崩溃日志
		// CrashHandler.getInstance().init(getApplicationContext());
		// 初始化UniversalImageLoader
		initImageLoader(getApplicationContext());
		// 初始化百度地图
		SDKInitializer.initialize(getApplicationContext());
		// 初始化讯飞SDK
		SpeechUtility.createUtility(this, SpeechConstant.APPID + "=548ffc88");
		// 初始化短信验证码
		SMSSDK.initSDK(this, "10b6280b3b5c0", "0a53b1dbde76e24a8997cfefc2b2e87a");
		// 创建缓存根目录
		File file = new File(Configuration.getSDCardDirectory() + File.separator + Configuration.TAG);
		if (!file.exists()) {
			file.mkdir();
		}
	}

	public static void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator()).diskCacheSize(50 * 1024 * 1024)
				// 50 Mb
				.tasksProcessingOrder(QueueProcessingType.LIFO).writeDebugLogs() // Remove
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	// 单例模式中获取唯一的ExitApplication实例
	public static BaseApplication getInstance() {
		if (null == instance) {
			instance = new BaseApplication();
		}
		return instance;
	}

	public static Context getContext() {
		return getInstance();
	}

	/**
	 * 设置登录状况，true为登录，false为不登录
	 * 
	 * @param isLogin
	 */
	public void setLoggedIn(boolean isLogin) {
		this.isLogin = isLogin;
	}

	/**
	 * 得到登录状况
	 * 
	 * @return
	 */
	public boolean isLogin() {
		return isLogin;
	}

}