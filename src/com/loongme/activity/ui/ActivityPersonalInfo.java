package com.loongme.activity.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.loongme.activity.R;
import com.loongme.activity.adapter.AdapterPerInfoMenuList;
import com.loongme.activity.adapter.AdapterPerInfoMenuList.ViewHolder;
import com.loongme.activity.base.ApiClient;
import com.loongme.activity.base.BaseActivity;
import com.loongme.activity.bean.PerInfoMenuItem;
import com.loongme.activity.bean.User;
import com.loongme.activity.business.Helpers;
import com.loongme.activity.common.Configuration;
import com.loongme.activity.common.ResultObj;
import com.loongme.activity.enums.UserType;
import com.loongme.activity.utils.StringUtils;
import com.loongme.activity.utils.UIUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author 作者:zhaojianyu
 * @version 创建时间：2016-5-7 下午2:25:44 类说明:个人信息页
 */
public class ActivityPersonalInfo extends BaseActivity {

	private final String[] menuItemNames = { "通知消息", "设置", "推荐好友", "意见反馈" };
	private final String[] menuItemNames_bonke = { "我的服务", "通知消息", "设置", "推荐好友", "意见反馈" };
	private final int[] menuIcons = { R.drawable.tzxx, R.drawable.sz, R.drawable.tjhy, R.drawable.yjfk };
	private final int[] menuIcons_bonke = { R.drawable.wdfwc, R.drawable.tz, R.drawable.szc, R.drawable.tjhyc,
			R.drawable.yjfkc };
	private ImageButton btn_goBack;
	/**
	 * 用户未登录
	 */
	private TextView tx_login;
	private TextView tx_registe;
	private View view_viewNotLogin;
	/**
	 * 普通用户
	 */
	private ImageView image_userPhoto;
	private TextView tx_userName;
	private TextView tx_userPhone;
	private Button btn_applybon;
	private View view_normalInfo;

	/**
	 * 帮客用户
	 */
	private ImageView image_bonkePhoto;
	private TextView tx_bonkeName;
	private TextView tx_bonkeNotice;
	private TextView tx_bonkeJobOrAddress;
	private View view_bokeInfo;
	private TextView tx_issueServer;

	/**
	 * 菜单选项
	 */
	private ListView listview_menu;
	private List<PerInfoMenuItem> menuItems;
	private AdapterPerInfoMenuList adapterPerInfoMenuList;
	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		user = Helpers.getUserInfo(this);
		parseHttpParams(Configuration.serv_flag.RequstFlag_GetUserInfo);
		init();
	}

	@Override
	protected void initParas() {
		menuItems = new ArrayList<PerInfoMenuItem>();
		super.initParas();
	}

	private void init() {
		HttpUtils utils = new HttpUtils(5000);
		RequestParams param = new RequestParams();
		JSONObject obj = new JSONObject();
		try {
			obj.put("userId", user.getUserId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		param.addQueryStringParameter("requestMessage", obj.toString());
		utils.send(HttpMethod.POST, Configuration.SERVER_HOST_PREFIX + Configuration.URL_GETUSERINFO, param,
				new RequestCallBack<String>() {

					private int audit;

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						UIUtil.showMsg(ActivityPersonalInfo.this, "获取个人信息失败");
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						if (arg0.statusCode == 200) {
							System.out.println("------"+arg0.result);
							com.alibaba.fastjson.JSONObject jsonBean = JSON.parseObject(arg0.result);
							audit = jsonBean.getInteger("auditStatus");
							if (audit == 0) {
								btn_applybon.setText("帮客审核中");
								btn_applybon.setClickable(false);
								btn_applybon.setBackgroundResource(R.drawable.selector_gray);
							} else if (audit == 2) {
								btn_applybon.setText("帮客审核失败");
								btn_applybon.setClickable(true);
								btn_applybon.setBackgroundResource(R.drawable.selector_btn_applybon);
							} else if (audit == 3) {
								btn_applybon.setText("我要做帮客");
								btn_applybon.setClickable(true);
								btn_applybon.setBackgroundResource(R.drawable.selector_btn_applybon);
							}
						}
					}
				});
	}

	@Override
	protected void initWidgets() {
		setContentView(R.layout.activity_personalinfo);
		btn_goBack = (ImageButton) findViewById(R.id.btn_goback);
		tx_login = (TextView) findViewById(R.id.tx_login);
		tx_registe = (TextView) findViewById(R.id.tx_registe);
		view_viewNotLogin = (View) findViewById(R.id.layout_userinfo_notlogin);
		image_userPhoto = (ImageView) findViewById(R.id.img_userPhoto_loginnormal);
		tx_userName = (TextView) findViewById(R.id.tx_username);
		tx_userPhone = (TextView) findViewById(R.id.tx_userphone);
		btn_applybon = (Button) findViewById(R.id.btn_applybon);
		view_normalInfo = (View) findViewById(R.id.layout_userinfo_login_normal);
		image_bonkePhoto = (ImageView) findViewById(R.id.img_userPhoto_bonke);
		tx_bonkeName = (TextView) findViewById(R.id.tx_bokename);
		tx_bonkeNotice = (TextView) findViewById(R.id.tx_notice);
		tx_bonkeJobOrAddress = (TextView) findViewById(R.id.tx_joboradress);
		tx_issueServer = (TextView) findViewById(R.id.tx_issue_server);
		view_bokeInfo = (View) findViewById(R.id.layout_userinfo_login_bonke);
		listview_menu = (ListView) findViewById(R.id.list_personalinfo_menu);
		listview_menu.addFooterView(LayoutInflater.from(this).inflate(R.layout.line, null));
		adapterPerInfoMenuList = new AdapterPerInfoMenuList(menuItems, this);
		listview_menu.setAdapter(adapterPerInfoMenuList);
		super.initWidgets();
	}

	@Override
	protected void regListener() {
		btn_goBack.setOnClickListener(this);
		tx_login.setOnClickListener(this);
		tx_registe.setOnClickListener(this);
		view_normalInfo.setOnClickListener(this);
		btn_applybon.setOnClickListener(this);
		view_bokeInfo.setOnClickListener(this);
		tx_issueServer.setOnClickListener(this);
		listview_menu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ViewHolder viewHolder = (ViewHolder) view.getTag();
				String menuName = viewHolder.tx_menuName.getText().toString();
				if (StringUtils.isEmpty(menuName)) {
					return;
				}
				if (menuName.equals("我的服务")) {
					UIUtil.redirect(ActivityPersonalInfo.this, ActivityMyServe.class);
				} else if (menuName.equals("通知消息")) {
					UIUtil.redirect(ActivityPersonalInfo.this, ActivityNotificationMeg.class);
				} else if (menuName.equals("设置")) {
					UIUtil.redirect(ActivityPersonalInfo.this, ActivitySet.class);
				} else if (menuName.equals("推荐好友")) {
					UIUtil.redirect(ActivityPersonalInfo.this, ActivityRecommendFriend.class);
				} else if (menuName.equals("意见反馈")) {
					UIUtil.redirect(ActivityPersonalInfo.this, ActivityFeedback.class);
				}
			}
		});
		super.regListener();
	}

	@Override
	protected void initUI() {
		super.initUI();
	}

	private void refreshInfo() {
		if (menuItems.size() != 0) {
			menuItems.clear();
		}
		// 已登录
		if (!StringUtils.isEmpty(user.getUserType())) {
			// 普通用户
			tx_issueServer.setVisibility(View.GONE);
			if (user.getUserType().equals(UserType.Normal.getVaule()) || user.getUserType().equals("普通")) {
				for (int i = 0; i < menuItemNames.length; i++) {
					menuItems.add(new PerInfoMenuItem(menuItemNames[i], menuIcons[i], false));
					view_normalInfo.setVisibility(View.VISIBLE);
					view_viewNotLogin.setVisibility(View.GONE);
					view_bokeInfo.setVisibility(View.GONE);
					if (!StringUtils.isEmpty(user.getUserPicture())) {
						ImageLoader.getInstance().displayImage(user.getUserPicture(), image_userPhoto);
					}
					tx_userName.setText(user.getUserNickname());
					tx_userPhone.setText(user.getUserPhone());
				}
			}
			// 帮客用户
			else {
				tx_issueServer.setVisibility(View.VISIBLE);
				for (int i = 0; i < menuItemNames_bonke.length; i++) {
					menuItems.add(new PerInfoMenuItem(menuItemNames_bonke[i], menuIcons_bonke[i], true));
				}
				view_normalInfo.setVisibility(View.GONE);
				view_viewNotLogin.setVisibility(View.GONE);
				view_bokeInfo.setVisibility(View.VISIBLE);
				if (!StringUtils.isEmpty(user.getUserPicture())) {
					ImageLoader.getInstance().displayImage(user.getUserPicture(), image_bonkePhoto);
				}
				// 商家帮客
				if (user.getUserType().equals(UserType.Merchant.getVaule())) {
					tx_bonkeNotice.setText("地址:");
					if (user.getBusMsgs().size() != 0) {
						tx_bonkeName.setText(user.getBusMsgs().get(0).getBusName());
						tx_bonkeJobOrAddress.setText(user.getBusMsgs().get(0).getBusAddress());
					}
				} else {// 个人帮客
					tx_bonkeNotice.setText("职位:");
					if (user.getPersMsgs().size() != 0) {
						tx_bonkeName.setText(user.getUserNickname());
						tx_bonkeJobOrAddress.setText(user.getPersMsgs().get(0).getPersJob());
					}

				}
			}
		}
		// 未登录
		else {
			for (int i = 0; i < menuItemNames.length; i++) {
				menuItems.add(new PerInfoMenuItem(menuItemNames[i], menuIcons[i], false));
			}
		}
		adapterPerInfoMenuList.notifyDataSetChanged();
	}

	private void parseHttpParams(int requestFlag) {
		showProgress();
		if (requestFlag == Configuration.serv_flag.RequstFlag_GetUserInfo) {
			JSONObject object = new JSONObject();
			try {
				object.put("userId", user.getUserId());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			String params = "requestMessage=" + object.toString();
			ApiClient.getInstance(this).load(Configuration.SERVER_HOST_PREFIX + Configuration.URL_GETUSERINFO, params,
					Configuration.serv_flag.RequstFlag_GetUserInfo, this);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_goback:
			finish();
			break;

		case R.id.tx_login:
			UIUtil.redirect(this, ActivityLogin.class);
			break;
		case R.id.tx_registe:
			UIUtil.redirect(this, ActivityRegiste.class);
			break;
		case R.id.btn_applybon:
			// startActivityForResult(new Intent(getApplicationContext(),
			// ActivityGuest.class),
			// Configuration.REQUESTCODE_STATE);
			UIUtil.redirect(this, ActivityGuest.class);
			break;
		case R.id.layout_userinfo_login_normal:
			Intent intent0 = new Intent(this, ActivityPersonalInfoSet.class);
			startActivityForResult(intent0, Configuration.REQUESTCODE_SETPERSONNALINFO);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
			break;
		case R.id.layout_userinfo_login_bonke:
			Intent intent = new Intent(this, ActivityPersonalInfoSet.class);
			startActivityForResult(intent, Configuration.REQUESTCODE_SETPERSONNALINFO);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
			break;

		case R.id.tx_issue_server:
			UIUtil.redirect(this, ActivityRelease.class);
			break;

		default:
			break;
		}
		super.onClick(v);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// switch (resultCode) {
		// case Configuration.REQUESTCODE_STATES:
		// btn_applybon.setText("我要做帮客");
		// btn_applybon.setClickable(true);
		// btn_applybon.setBackgroundResource(R.drawable.selector_btn_applybon);
		// break;
		// case Configuration.REQUESTCODE_STATE:
		// btn_applybon.setText("帮客审核中");
		// btn_applybon.setClickable(false);
		// btn_applybon.setBackgroundResource(R.drawable.selector_gray);
		// break;
		// default:
		// break;
		// }
		if (resultCode != RESULT_OK) {
			return;
		}
		if (requestCode == Configuration.REQUESTCODE_SETPERSONNALINFO) {
			parseHttpParams(Configuration.serv_flag.RequstFlag_GetUserInfo);
		}
	}

	@Override
	public boolean handleSuccess(int statusCode, JSONObject response, int flagRequest) {
		dismissProgress();
		if (statusCode == ResultObj.ERRCODE_STATE_OK) {
			if (StringUtils.isEmpty(response.toString())) {
				UIUtil.showMsg(this, "获取用户信息失败");
				return false;
			}
			user = com.alibaba.fastjson.JSONObject.parseObject(response.toString(), User.class);
			Helpers.saveUserInfoToLocal(this, user);
			refreshInfo();
		} else {
			UIUtil.showMsg(this, "获取用户信息失败");
		}
		return super.handleSuccess(statusCode, response, flagRequest);
	}

	@Override
	public boolean handleFailure(int statusCode, String responseString, Throwable throwable, int flagRequest) {
		dismissProgress();
		UIUtil.showMsg(this, "获取用户信息失败");
		return super.handleFailure(statusCode, responseString, throwable, flagRequest);
	}
}
