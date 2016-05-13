package com.loongme.activity.common;

import java.io.File;

import android.os.Environment;

public class Configuration {

	public static final String TAG = "SSBon";

	public static final String FILE_SEPARATOR = "/";

	/** SD卡根路径 */
	public static final String SDCARD_PATH = getSDCardDirectory();
	/** 错误日志保存路径 */
	public static final String Errorlogs_Directory = getSDCardDirectory() + File.separator + TAG + File.separator
			+ "errorlogs";
	/** 业务逻辑文件缓存路径 */
	public static final String BUSINESS_CACHE_PATH = "business";
	/** 头像缓存路径 */
	public static final String PHOTO_SAVEPATH = getSDCardDirectory() + File.separator + TAG + File.separator
			+ "photo.png";
	/** 营业执照缓存路径 */
	public static final String BUSLICENSE_SAVEPATH = SDCARD_PATH + File.separator + TAG + File.separator
			+ "buslicense.png";
	/** 身份证正面 */
	public static final String IDCARD_MAINSIDE_SAVEPATH = SDCARD_PATH + File.separator + TAG + File.separator
			+ "idcard_mainside.png";
	/** 身份证反面 */
	public static final String IDCARD_REVERSESIDE_SAVEPATH = SDCARD_PATH + File.separator + TAG + File.separator
			+ "idcard_reverseside.png";
	/** 服务封面 */
	public static final String SERVER_COVER_SAVEPATH = SDCARD_PATH + File.separator + TAG + File.separator
			+ "server_cover.png";
	/** 工作照或作品图 */
	public static final String SERVER_WORK_SAVEPATH = SDCARD_PATH + File.separator + TAG + File.separator
			+ "server_work.png";

	/**
	 * 系统配置文件key
	 */
	public static final String SYSTEM_PRE = "system_pre";
	public static final String ACCOUNT = "AccountInfo";// 保存用户信息的key
	public static final String BONKE_STATUS = "BonkeStatus";// 保存申请帮客状态的key
	public static final String VOICE_ISOPENED = "VoiceIsOpened";// 是否允许播报声音

	// ****************************RequestCode4Intent****************************
	public static final int REQUESTCODE_LOGINFROMPERSONAL = 500;// 由个人信息页到登录页
	public static final int REQUESTCODE_LOGINFROMSPLASH = 501;// 由欢迎页到登录页
	public static final int REQUESTCODE_REGISTEFROMPERSONAL = 502;// 由个人信息页到注册页
	public static final int REQUESTCODE_REGISTEFROMLOGIN = 503;// 由登录页到注册页
	public static final int REQUESTCODE_REGISTEFROMSPLASH = 504;// 由欢迎页到注册页
	public static final int REQUESTCODE_SETPERSONNALINFO = 505;// 修改个人信息
	public static final int REQUESTCODE_SELECT_SERVER_COVER = 996;// 提交服务选择封面
	public static final int REQUESTCODE_SELECT_SERVER_WORK = 997;// 提交服务选择工作照获作品图
	public static final int REQUESTCODE_SELECT_IDCARD_MAINSIDE = 998;// 选择身份证正面
	public static final int REQUESTCODE_SELECT_IDCARD_REVERSESIDEE = 999;// 选择身份证反面
	public static final int REQUESTCODE_SELECT_LICENSE = 1000;// 选择营业执照
	public static final int REQUESTCODE_REQUEST_BUSDES = 1001;// 填写商家描述
	public static final int REQUESTCODE_REQUEST_BUSINTRODUCE = 1002;// 填写商家详细介绍
	public static final int REQUESTCODE_REQUEST_PERSONAL_DES = 1003;// 填写个人描述
	public static final int REQUESTCODE_REQUEST_PERSONAL_INTRODUCE = 1004;// 填写个人详细介绍
	public static final int REQUESTCODE_REQUEST_APPLYBONKE = 1005;// 个人信息页点击申请帮客
	public static final int REQUESTCODE_TAKEPHOTOS = 1006;// 拍照
	public static final int REQUESTCODE_SELECTPICS = 1007;// 选择相册
	public static final int REQUESTCODE_CROPPHOTOS = 1008;// 图片裁剪
	public static final int REQUESTCODE_STATE=1009;//帮客注册状态
	public static final int REQUESTCODE_STATES=1010;//返回帮客注册状态

	/**
	 * ***********************about http api************************************
	 */
	public static final String PROTOCOL = "http://";
	// 服务器地址
	public static final String SERVER_ADDRESS = "120.24.37.141";// 测试：172.16.80.136
																// 正式：120.24.37.141
	// 端口
	public static final String SERVER_PORT = "8080";
	// 真实服务器路径
	public static final String SERVER_HOST_PREFIX = PROTOCOL + SERVER_ADDRESS + ":" + SERVER_PORT;

	// 请求用户信息
	public static final String URL_GETUSERINFO = "/LoginAndResigister/PersonMessageServlet";
	// 登录
	public static final String URL_LOGIN = "/LoginAndResigister/UserLoginServlet";
	// 注册
	public static final String URL_REGISTE = "/LoginAndResigister/register";
	// 上传头像
	public static final String URL_UPLOAD_PHOTO = "/LoginAndResigister/HeadImageServlet";
	// 修改性别
	public static final String URL_FEEDBACK = "/LoginAndResigister/feedBack";
	// 修改性别
	public static final String URL_SEX = "/LoginAndResigister/UpdateSexServlet";
	// 修改昵称
	public static final String URL_NICKNAME = "/LoginAndResigister/UpdateNikenameServlet";
	// 个性宣言
	public static final String URL_MANIFESTO = "/LoginAndResigister/UpdateDeclarationServlet";
	// 修改密码
	public static final String URL_UPDATEPASS = "/LoginAndResigister/UpdatePasswordServlet";

	/**
	 * 网络访问标志位
	 * 
	 */
	public static class serv_flag {
		// 必须在线
		public static final int RequstFlag_Registe = 0;// 注册
		public static final int RequstFlag_Login = 1;// 登录
		public static final int RequstFlag_UpdatePass = 2;// 修改密码
		// 不必在线
		public static final int RequstFlag_GetUserInfo = 501;
	}

	/**
	 * ***********************about sp keys************************************
	 */
	public static class sp_key {
		public static final String IS_FIRST_IN = "isFirstIn";// 是否第一次启动app
	}

	/**
	 * 获取SD卡路径
	 * 
	 * @return
	 */
	public static String getSDCardDirectory() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().getAbsolutePath();
		} else {
			return Environment.getRootDirectory().getAbsolutePath();
		}
	}

	/**
	 * 针对单个用户的网络数据缓存路径
	 * 
	 * @param userName
	 * @return
	 */
	public static String getCacheDirWithUserName(String userName) {
		return getSDCardDirectory() + File.separator + TAG + File.separator + userName + File.separator + "NetCache";
	}

}
