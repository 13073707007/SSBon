package com.loongme.activity.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.DbUtils.DaoConfig;
import com.lidroid.xutils.exception.DbException;
import com.loongme.activity.R;
import com.loongme.activity.adapter.AdapterSpeakList;
import com.loongme.activity.adapter.AdapterSpeakMenuVP;
import com.loongme.activity.base.AppManager;
import com.loongme.activity.base.MenuItemClickCallBack;
import com.loongme.activity.bean.MoreMenuItem;
import com.loongme.activity.bean.SpeakItem;
import com.loongme.activity.business.Helpers;
import com.loongme.activity.business.SSBonLocationService;
import com.loongme.activity.business.SsbService;
import com.loongme.activity.business.VoiceAssistantManager;
import com.loongme.activity.utils.DateUtil;
import com.loongme.activity.utils.DeviceUtils;
import com.loongme.activity.utils.HttpUtil;
import com.loongme.activity.utils.Logger;
import com.loongme.activity.utils.StringUtils;
import com.loongme.activity.utils.UIUtil;
import com.loongme.activity.widgets.CustomProgressDialog;
import com.umeng.update.UmengUpdateAgent;
import com.viewpagerindicator.TabPageIndicator;

/**
 * @author 作者:zhaojianyu
 * @version 创建时间：2016-5-3 下午12:34:27 类说明:小帮页
 */
public class ActivityMain extends FragmentActivity implements OnClickListener, MenuItemClickCallBack {

	/**
	 * 标题栏
	 */
	private ImageView btn_voiceSwitch;// 语音播报开关
	private ImageButton btn_setting;// 设置

	private int keyBackClickCount = 0;// 返回键点击次数
	private ListView listview_speak;// 聊天列表
	private List<SpeakItem> datas;// 数据
	private AdapterSpeakList adapterSpeakList;// 适配器
	private VoiceAssistantManager voiceAssistantManager;// 语音助手管理类
	/**
	 * 监听软键盘
	 */
	private View rootLayout;// 用于监听软键盘的弹出和关闭
	private int screenHeight = 0; // 屏幕高度
	private int screenWidth = 0; // 屏幕宽度
	private int keyHeight = 0; // 软件盘弹起后所占高度阀值
	/**
	 * 工具栏
	 */
	// private View toolBar;// 工具栏
	private ImageView img_voice;// 录音
	private ImageView img_text;// 键盘输入
	private EditText edit_speakContent;// 编辑聊天内容
	private ImageView btn_showMenu;// 弹出菜单
	private Button btn_recordAudio;// 录音
	private Button btn_send;
	/**
	 * 更多菜单
	 */
	private LinearLayout layout_speak_menu;// 整个菜单栏vp+tab
	private ViewPager vp_speak_menu;
	private TabPageIndicator tabPageIndicator;
	private boolean isMenuShowing = false;// 更多菜单是否显示

	/**
	 * *******************小帮业务逻辑************************
	 */
	private SsbService iService = new SsbService();// socke请求的服务
	private String IMEI;// 序列号
	private Intent ssBonLocationServiceIntent;// 时时更新位置信息的服务
	private DbUtils db;// 数据库

	/**
	 * 语音听写与合成
	 */
	private RecognizerDialog mIatDialog;// 语音听写UI
	private StringBuffer userSpeakContent;// 用户说话内容记录
	private SpeechSynthesizer mTts;// 语音合成对象

	/**
	 * H5页面的展示
	 */
	private CustomProgressDialog progressDialog;
	private WebView webView;

	/**
	 * 监听网络状态，若无网络，则提示
	 */
	BroadcastReceiver mReceiver = new BroadcastReceiver() {

		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
				Logger.d("mark", "网络状态已经改变");
				if (HttpUtil.isNetworkAvailable(ActivityMain.this)) {
					// initUI();
				} else {
					UIUtil.showMsg(ActivityMain.this, "网络连接不可用,请检查网络!");
				}

			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		doinit();
		UmengUpdateAgent.setUpdateOnlyWifi(false);// 是否只在wifi下更新
		UmengUpdateAgent.update(this);// 友盟自动更新
	}

	private final void doinit() {
		initParas();
		initWidgets();
		regListener();
		initUI();
		AppManager.getAppManager().addActivity(this);
	}

	protected void initParas() {
		Helpers.saveFirstStartApp(this);
		datas = new ArrayList<SpeakItem>();
		// 获取屏幕高度
		screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
		screenWidth = this.getWindowManager().getDefaultDisplay().getWidth();
		// 阀值设置为屏幕高度的1/3
		keyHeight = screenHeight / 3;
		// 初始化语音助手管理类
		voiceAssistantManager = VoiceAssistantManager.getInstance(this);
		IMEI = DeviceUtils.getIMEINum(this);
		// 初始化语音听写动画UI
		mIatDialog = new RecognizerDialog(ActivityMain.this, mInitListener);
		voiceAssistantManager.initSpeechToText(mIatDialog);
		userSpeakContent = new StringBuffer();
		// 初始化语音合成
		mTts = SpeechSynthesizer.createSynthesizer(ActivityMain.this, mTtsInitListener);
		voiceAssistantManager.initTextToSpeech(mTts);
		// 初始化数据库
		DaoConfig config = new DaoConfig(this);
		config.setDbName("ssbon"); // db名
		config.setDbVersion(1); // db版本
		db = DbUtils.create(config);
		db.configAllowTransaction(true);
		db.configDebug(false);
	}

	protected void initWidgets() {
		setContentView(R.layout.activity_main);
		// 顶部菜单栏
		btn_voiceSwitch = (ImageView) findViewById(R.id.img_btn_control_voice);
		if (Helpers.getVoiceStatus(this)) {
			btn_voiceSwitch.setImageDrawable(getResources().getDrawable(R.drawable.laba));
		} else {
			btn_voiceSwitch.setImageDrawable(getResources().getDrawable(R.drawable.biyin));
		}
		btn_setting = (ImageButton) findViewById(R.id.img_btn_turn_personalInfo);
		rootLayout = (View) findViewById(R.id.layout_root);
		listview_speak = (ListView) findViewById(R.id.listview_speak);
		adapterSpeakList = new AdapterSpeakList(this, datas);
		listview_speak.setAdapter(adapterSpeakList);
		// 聊天工具栏
		// toolBar = (View) findViewById(R.id.toolbar_speak);
		img_voice = (ImageView) findViewById(R.id.img_vioce);
		img_text = (ImageView) findViewById(R.id.img_text);
		edit_speakContent = (EditText) findViewById(R.id.edit_speak_content);
		btn_showMenu = (ImageView) findViewById(R.id.btn_showmenu);
		btn_recordAudio = (Button) findViewById(R.id.btn_record_audio);
		btn_send = (Button) findViewById(R.id.btn_send);

		// 初始化更多菜单
		layout_speak_menu = (LinearLayout) findViewById(R.id.layout_speak_menu);
		vp_speak_menu = (ViewPager) findViewById(R.id.vp_speak_menu);
		vp_speak_menu.setAdapter(new AdapterSpeakMenuVP(getSupportFragmentManager(), voiceAssistantManager, this));
		tabPageIndicator = (TabPageIndicator) findViewById(R.id.vp_indicator);
		tabPageIndicator.setViewPager(vp_speak_menu);

		// 菜单H5
		webView = voiceAssistantManager.getH5WebView(this);
		progressDialog = new CustomProgressDialog(this, "加载中...", R.anim.loading);

	}

	protected void regListener() {
		btn_voiceSwitch.setOnClickListener(this);
		btn_setting.setOnClickListener(this);
		img_voice.setOnClickListener(this);
		img_text.setOnClickListener(this);
		btn_showMenu.setOnClickListener(this);
		btn_send.setOnClickListener(this);
		btn_recordAudio.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// 按下
					mIatDialog.setListener(mRecognizerDialogListener);
					mIatDialog.show();
					userSpeakContent.delete(0, userSpeakContent.length());
					break;
				case MotionEvent.ACTION_UP:
					// 抬起
					break;
				}
				return true;
			}
		});
		// 监听软键盘发送
		edit_speakContent.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (StringUtils.isEmpty(s.toString())) {
					btn_send.setVisibility(View.GONE);
				} else {
					btn_send.setVisibility(View.VISIBLE);
				}
			}
		});
		edit_speakContent.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				dismissSpeakMenu();
				isMenuShowing = false;
				return false;
			}
		});
		// 监听软键盘的弹出关闭
		rootLayout.addOnLayoutChangeListener(new OnLayoutChangeListener() {

			@Override
			public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop,
					int oldRight, int oldBottom) {

				// 现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
				if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
					// 弹起
					// listview_speak.smoothScrollToPosition(listview_speak.getCount());
					listview_speak.setSelection(listview_speak.getCount());
				} else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
					// 关闭
				}
			}
		});
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				progressDialog.show();
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// listview_speak.smoothScrollToPosition(listview_speak.getCount());
				listview_speak.setSelection(listview_speak.getCount());
				progressDialog.dismiss();
				super.onPageFinished(view, url);
			}
		});
		webView.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (event.getAction() == MotionEvent.ACTION_UP) {
					webView.getParent().requestDisallowInterceptTouchEvent(false);
				} else
					webView.getParent().requestDisallowInterceptTouchEvent(true);

				return false;
			}

		});
		// 注册监听网络状态的广播
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mReceiver, intentFilter);
	}

	protected void initUI() {
		// 取出缓存数据
		try {
			List<SpeakItem> lists = db.findAll(SpeakItem.class);
			datas.addAll(lists);
			adapterSpeakList.notifyDataSetChanged();
			// listview_speak.smoothScrollToPosition(listview_speak.getCount());//
			// 移动到尾部
			listview_speak.setSelection(listview_speak.getCount());
		} catch (Exception e) {
			e.printStackTrace();
		}
		new GetWelcomeMsgTask().execute();// 得到欢迎语

		// 启动位置信息的时时监控
		ssBonLocationServiceIntent = new Intent();
		ssBonLocationServiceIntent.setClass(this, SSBonLocationService.class);
		startService(ssBonLocationServiceIntent);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		// 录音状态
		case R.id.img_vioce:
			DeviceUtils.hideSoftInput(this, edit_speakContent);
			dismissSpeakMenu();
			isMenuShowing = false;
			img_voice.setVisibility(View.GONE);// 喇叭隐藏
			btn_recordAudio.setVisibility(View.VISIBLE);// 录音按钮显示
			img_text.setVisibility(View.VISIBLE);// 圆圈键盘显示
			edit_speakContent.setVisibility(View.GONE);// 内容编辑框隐藏
			break;
		// 键盘输入状态
		case R.id.img_text:
			img_voice.setVisibility(View.VISIBLE);// 喇叭显示
			btn_recordAudio.setVisibility(View.GONE);// 录音按钮隐藏
			img_text.setVisibility(View.GONE);// 圆圈键盘隐藏
			edit_speakContent.setVisibility(View.VISIBLE);// 内容编辑框显示
			dismissSpeakMenu();
			isMenuShowing = false;
			edit_speakContent.requestFocus();
			DeviceUtils.showSoftInput(this);
			// listview_speak.smoothScrollToPosition(listview_speak.getCount());//
			// 聊天列表移动到尾部
			listview_speak.setSelection(listview_speak.getCount());
			break;
		// 聊天菜单
		case R.id.btn_showmenu:
			DeviceUtils.hideSoftInput(this, edit_speakContent);
			if (isMenuShowing) {
				dismissSpeakMenu();
			} else {
				showSpeakMenu();
			}
			break;
		// 发送信息
		case R.id.btn_send:
			// 刷新聊天列表
			if (listview_speak.getFooterViewsCount() != 0) {
				listview_speak.removeFooterView(webView);
			}
			String sendMsg = edit_speakContent.getText().toString();
			SpeakItem speakItem = new SpeakItem(Helpers.getUserInfo(this).getUserPicture(), sendMsg,
					DateUtil.getCurrentSpeakTime(), 1);
			datas.add(speakItem);
			adapterSpeakList.notifyDataSetChanged();
			try {
				db.saveBindingId(speakItem);
			} catch (DbException e) {
				e.printStackTrace();
			}
			// listview_speak.smoothScrollToPosition(listview_speak.getCount());
			listview_speak.setSelection(listview_speak.getCount());
			// 发送信息
			new AskSSBonTask().execute(sendMsg);
			// 清空聊天内容
			edit_speakContent.setText("");
			break;
		// 关闭语音播报
		case R.id.img_btn_control_voice:
			if (Helpers.getVoiceStatus(ActivityMain.this)) {
				// if (mTts.isSpeaking()) {
				mTts.stopSpeaking();
				// }
				btn_voiceSwitch.setImageDrawable(getResources().getDrawable(R.drawable.biyin));
				Helpers.saveVoiceStatus(ActivityMain.this, false);
			} else {
				btn_voiceSwitch.setImageDrawable(getResources().getDrawable(R.drawable.laba));
				Helpers.saveVoiceStatus(ActivityMain.this, true);
			}
			break;
		// 跳转到设置页
		case R.id.img_btn_turn_personalInfo:
			UIUtil.redirect(this, ActivityPersonalInfo.class);
			break;
		default:
			break;
		}
	}

	/**
	 * 显示更多聊天菜单
	 */
	private void showSpeakMenu() {
		layout_speak_menu.setVisibility(View.VISIBLE);
		isMenuShowing = true;
		// listview_speak.smoothScrollToPosition(listview_speak.getCount());
		listview_speak.setSelection(listview_speak.getCount());
	}

	/**
	 * 隐藏更多聊天菜单
	 */
	private void dismissSpeakMenu() {
		layout_speak_menu.setVisibility(View.GONE);
		isMenuShowing = false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			switch (keyBackClickCount++) {
			case 0:
				UIUtil.showMsg(this, "再按一次退出");
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {

						keyBackClickCount = 0;
					}
				}, 3000);
				break;
			case 1:
				// 退出系统
				stopService(ssBonLocationServiceIntent);
				AppManager.getAppManager().AppExit(this);
				break;
			default:
				break;
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onMoreMenuItemClick(MoreMenuItem item) {
		try {
			SpeakItem speakItem = new SpeakItem(Helpers.getUserInfo(this).getUserPicture(), item.getMenuName(),
					DateUtil.getCurrentSpeakTime(), 1);
			datas.add(speakItem);
			adapterSpeakList.notifyDataSetChanged();
			db.saveBindingId(speakItem);
			// listview_speak.smoothScrollToPosition(listview_speak.getCount());
			listview_speak.setSelection(listview_speak.getCount());
			listview_speak.addFooterView(webView);
			webView.loadUrl(item.getMenuRedirectUrl());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDestroy() {
		AppManager.getAppManager().finishActivity(this);
		unregisterReceiver(mReceiver);
		if (Helpers.getVoiceStatus(this)) {
			mTts.stopSpeaking();
			mTts.destroy();
		}
		super.onDestroy();
	}

	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
	}

	/**
	 * ***********************讯飞SDK*******************************************
	 */
	// 初始化语音合成
	private InitListener mTtsInitListener = new InitListener() {
		@Override
		public void onInit(int code) {
			if (code != ErrorCode.SUCCESS) {
				UIUtil.showMsg(ActivityMain.this, "初始化失败，错误码：" + code);
			} else {
				// 初始化成功，之后可以调用startSpeaking方法
				// 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
				// 正确的做法是将onCreate中的startSpeaking调用移至这里
			}
		}
	};

	// 语音听写动画UI初始化是否成功的监听
	private InitListener mInitListener = new InitListener() {

		@Override
		public void onInit(int code) {
			if (code != ErrorCode.SUCCESS) {
				UIUtil.showMsg(ActivityMain.this, "初始化失败，错误码：" + code);
			}
		}
	};
	// 听写UI监听器
	private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
		public void onResult(RecognizerResult results, boolean isLast) {
			// 关闭掉语音合成
			if (mTts.isSpeaking()) {
				mTts.stopSpeaking();
			}
			try {
				JSONObject jsonObject = new JSONObject(results.getResultString());
				JSONArray array = jsonObject.getJSONArray("ws");
				for (int i = 0; i < array.length(); i++) {
					JSONObject element = array.getJSONObject(i);
					JSONArray arr = element.getJSONArray("cw");
					for (int j = 0; j < arr.length(); j++) {
						userSpeakContent.append(arr.getJSONObject(j).getString("w"));
					}
				}
				if (isLast) {
					SpeakItem speakItem = new SpeakItem(Helpers.getUserInfo(ActivityMain.this).getUserPicture(),
							userSpeakContent.toString(), DateUtil.getCurrentSpeakTime(), 1);
					datas.add(speakItem);
					adapterSpeakList.notifyDataSetChanged();
					db.saveBindingId(speakItem);
					new AskSSBonTask().execute(userSpeakContent.toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// 识别回调错误.
		public void onError(SpeechError error) {
			UIUtil.showMsg(ActivityMain.this, error.getPlainDescription(true));
		}

	};

	// 语音合成监听
	private SynthesizerListener mTtsListener = new SynthesizerListener() {

		@Override
		public void onSpeakBegin() {
		}

		@Override
		public void onSpeakPaused() {
		}

		@Override
		public void onSpeakResumed() {
		}

		@Override
		public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
		}

		@Override
		public void onSpeakProgress(int percent, int beginPos, int endPos) {
		}

		@Override
		public void onCompleted(SpeechError error) {
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
		}
	};

	/**
	 * ***********************小帮的业务逻辑*******************************************
	 */
	// 获取欢迎语
	private class GetWelcomeMsgTask extends AsyncTask<Void, Void, SpeakItem> {

		@Override
		protected SpeakItem doInBackground(Void... params) {
			String weicomeMsg = "Pis_Welcome_First  {COMM_INFO {BUSI_CODE 10011} {REGION_ID A} {COUNTY_ID A00} "
					+ "{OFFICE_ID 22342} {OPERATOR_ID 43643} {CHANNEL A2} {OP_MODE SUBMIT}} {   {XW_OPENID " + IMEI
					+ "} }";
			try {
				// 等待，接收来自服务器返回的消息
				iService.connecttoserver();
				if (iService.getSocket().isConnected()) {
					iService.SendMsg(iService.getSocket(), weicomeMsg);
				}
				String getFromSerciceMsg = iService.ReceiveMsg(iService.getSocket());
				if (!StringUtils.isEmpty(getFromSerciceMsg)) {
					if (getFromSerciceMsg.contains("ROBOT_OUTPUT")) {
						return new SpeakItem(null, voiceAssistantManager.dealWelcomeMsg(getFromSerciceMsg),
								DateUtil.getCurrentSpeakTime(), 0);
					} else {
						return null;
					}
				} else {
					return null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(SpeakItem result) {
			if (result != null) {
				// 更新列表
				datas.add(result);
				adapterSpeakList.notifyDataSetChanged();
				try {
					db.saveBindingId(result);
				} catch (DbException e) {
					e.printStackTrace();
				}
				// listview_speak.smoothScrollToPosition(listview_speak.getCount());
				listview_speak.setSelection(listview_speak.getCount());
				if (Helpers.getVoiceStatus(ActivityMain.this)) {
					// 读出
					if (mTts.isSpeaking()) {
						mTts.stopSpeaking();
					}
					int code = mTts.startSpeaking(UIUtil.stripHtml(result.getSpeakerContent()), mTtsListener);
					if (code != ErrorCode.SUCCESS) {
						UIUtil.showMsg(ActivityMain.this, "语音合成失败" + code);
					}
				}
			}
			super.onPostExecute(result);
		}
	}

	// 问答
	private class AskSSBonTask extends AsyncTask<String, Void, SpeakItem> {

		@Override
		protected SpeakItem doInBackground(String... params) {
			String msg = params[0];
			try {
				String msgPis = "Pis_Get_App_Info {COMM_INFO {BUSI_CODE 10011} {REGION_ID A} {COUNTY_ID A00} {OFFICE_ID robot} {OPERATOR_ID 44444} {CHANNEL A2} {OP_MODE SUBMIT}} { {ROLE {1}} {WORDS {"
						+ msg + "}} {ROBOT_TYPE {0}} {XW_OPENID " + IMEI + "} {SELFID " + IMEI + "} }";

				iService.connecttoserver();// 链接服务器
				if (iService.getSocket().isConnected()) {
					iService.SendMsg(iService.getSocket(), msgPis);
				}
				String receiveMsg = iService.ReceiveMsg(iService.getSocket());
				if (StringUtils.isEmpty(receiveMsg)) {
					return null;
				}
				return new SpeakItem(null, voiceAssistantManager.dealWelcomeMsg(receiveMsg).trim(), null, 0);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(SpeakItem result) {
			if (result != null) {
				datas.add(result);
				adapterSpeakList.notifyDataSetChanged();
				try {
					db.saveBindingId(result);
				} catch (DbException e) {
					e.printStackTrace();
				}
				// listview_speak.smoothScrollToPosition(listview_speak.getCount());
				listview_speak.setSelection(listview_speak.getCount());
				if (Helpers.getVoiceStatus(ActivityMain.this)) {
					// 读出
					if (mTts.isSpeaking()) {
						mTts.stopSpeaking();
					}
					int code = mTts.startSpeaking(UIUtil.stripHtml(result.getSpeakerContent()), mTtsListener);
					if (code != ErrorCode.SUCCESS) {
						UIUtil.showMsg(ActivityMain.this, "语音合成失败" + code);
					}
				}
			} else {
				UIUtil.showMsg(ActivityMain.this, "您问的问题太难了，小帮回答不上，请换个试试");
			}
			super.onPostExecute(result);
		}

	}
}
