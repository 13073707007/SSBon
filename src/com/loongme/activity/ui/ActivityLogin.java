package com.loongme.activity.ui;

import java.io.File;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.loongme.activity.R;
import com.loongme.activity.base.ApiClient;
import com.loongme.activity.base.AppManager;
import com.loongme.activity.base.BaseActivity;
import com.loongme.activity.bean.User;
import com.loongme.activity.business.Helpers;
import com.loongme.activity.common.Configuration;
import com.loongme.activity.common.ResultObj;
import com.loongme.activity.utils.DeviceUtils;
import com.loongme.activity.utils.StringUtils;
import com.loongme.activity.utils.UIUtil;

/**
 * @author 作者:zhaojianyu
 * @version 创建时间：2016-5-3 下午2:23:59 类说明:
 */
public class ActivityLogin extends BaseActivity implements PlatformActionListener {

	private ImageButton btn_goback;
	private EditText edit_username;
	private EditText edit_password;
	private TextView view_forgetpass;
	private Button btn_login;
	private TextView tx_registe;
	private ImageButton btn_oauth_sina;
	private Platform weibo;
	String id, name, description, profile_image_url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ShareSDK.initSDK(this, "1297452235");
	}

	@Override
	protected void initParas() {
		super.initParas();
	}

	@Override
	protected void initWidgets() {
		setContentView(R.layout.activity_login);
		btn_goback = (ImageButton) findViewById(R.id.btn_goback);
		edit_username = (EditText) findViewById(R.id.edit_username);
		edit_password = (EditText) findViewById(R.id.edit_password);
		view_forgetpass = (TextView) findViewById(R.id.tx_forgetpass);
		btn_login = (Button) findViewById(R.id.btn_login);
		tx_registe = (TextView) findViewById(R.id.tx_registe);
		btn_oauth_sina = (ImageButton) findViewById(R.id.oauth_sina);
		super.initWidgets();
	}

	@Override
	protected void regListener() {
		btn_goback.setOnClickListener(this);
		view_forgetpass.setOnClickListener(this);
		btn_login.setOnClickListener(this);
		tx_registe.setOnClickListener(this);
		btn_oauth_sina.setOnClickListener(this);
		super.regListener();
	}

	@Override
	protected void initUI() {
		super.initUI();
	}

	private void parseHttpRequst(int flagRequest) {
		showProgress();
		try {
			if (flagRequest == Configuration.serv_flag.RequstFlag_Login) {
				JSONObject object = new JSONObject();
				object.put("userPhone", edit_username.getText().toString().trim());
				object.put("userPassword", edit_password.getText().toString().trim());
				object.put("relationOpenid", "");
				String params = "requestMessage=" + object.toString();
				ApiClient.getInstance(this).load(Configuration.SERVER_HOST_PREFIX + Configuration.URL_LOGIN, params,
						Configuration.serv_flag.RequstFlag_Login, this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_goback:
			UIUtil.redirect(this, ActivityMain.class);
			break;
		case R.id.tx_forgetpass:
			// 忘记密码
			UIUtil.redirect(this, ActivityForgetPassWord.class);
			DeviceUtils.hideSoftInput(this, edit_username);
			DeviceUtils.hideSoftInput(this, edit_password);
			break;
		case R.id.btn_login:
			// 登录
			if (!StringUtils.isMobile(edit_username.getText().toString().trim())) {
				UIUtil.showMsg(this, "请输入正确的手机号");
				return;
			}

			if (edit_password.getText().toString().trim().length() < 6) {
				UIUtil.showMsg(this, "密码长度为6~20之间");
				return;
			}
			if (edit_password.getText().toString().trim().length() > 20) {
				UIUtil.showMsg(this, "密码长度为6~20之间");
				return;
			}
			parseHttpRequst(Configuration.serv_flag.RequstFlag_Login);
			break;
		case R.id.tx_registe:
			// 注册
			Bundle bundle = new Bundle();
			bundle.putInt("isFrom", Configuration.REQUESTCODE_REGISTEFROMLOGIN);
			UIUtil.redirect(this, ActivityRegiste.class, bundle);
			break;
		case R.id.oauth_sina:
			weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
			weibo.SSOSetting(false);
			weibo.setPlatformActionListener(this); // 设置分享事件回调
			weibo.showUser(null);
			weibo.authorize();
			break;
		default:
			break;
		}
		super.onClick(v);
	}

	@Override
	public boolean handleSuccess(int statusCode, JSONObject response, int flagRequest) {
		try {
			dismissProgress();
			if (statusCode == ResultObj.ERRCODE_STATE_OK) {
				if (flagRequest == Configuration.serv_flag.RequstFlag_Login) {
					if (StringUtils.isEmpty(response.toString())) {
						UIUtil.showMsg(this, "登录失败");
						return false;
					}
					JSONObject jsonObject = new JSONObject(response.toString());
					if (jsonObject.getInt("status") != 1) {
						UIUtil.showMsg(this, "账号或密码错误");
						return false;
					}
					UIUtil.showMsg(this, "登录成功");
					User user = JSON.parseObject(response.toString(), User.class);
					// 保存用户信息
					Helpers.saveUserInfoToLocal(ActivityLogin.this, user);
					// 创建用户缓存文件路径
					File file = new File(Configuration.SDCARD_PATH + File.separator + Configuration.TAG
							+ File.separator + user.getUserNickname());
					if (!file.exists()) {
						file.mkdir();
					}
					if (getIntent() != null && getIntent().getExtras() != null
							&& getIntent().getExtras().getInt("isFrom") == Configuration.REQUESTCODE_LOGINFROMSPLASH) {
						UIUtil.redirect(this, ActivityMain.class);
						finish();
					} else {
						AppManager.getAppManager().finishActivity(ActivityPersonalInfo.class);
						finish();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.handleSuccess(statusCode, response, flagRequest);
	}

	@Override
	public boolean handleFailure(int statusCode, String responseString, Throwable throwable, int flagRequest) {
		dismissProgress();
		UIUtil.showMsg(this, "登录失败" + statusCode);
		return super.handleFailure(statusCode, responseString, throwable, flagRequest);
	}

	// 取消方法
	@Override
	public void onCancel(Platform arg0, int arg1) {
		// startActivity(new Intent(getApplicationContext(),
		// BoundActivity.class));
		// finish();
		Toast.makeText(getApplicationContext(), "取消授权", 0).show();
	}

	// 成功方法
	@Override
	public void onComplete(Platform arg0, int arg1, HashMap<String, Object> res) {
		// 解析部分用户资料字段
		id = res.get("id").toString();// ID
		name = res.get("name").toString();// 用户名
		description = res.get("description").toString();// 描述
		profile_image_url = res.get("profile_image_url").toString();// 头像链接
		String str = "ID: " + id + ";\n" + "用户名： " + name + ";\n" + "描述：" + description + ";\n" + "用户头像地址："
				+ profile_image_url;
		System.out.println("用户资料: " + str);
		SharedPreferences sf = getSharedPreferences("MOBSSO", MODE_APPEND);
		Editor e = sf.edit();
		e.putString("id", id);
		e.putString("name", name);
		e.putString("description", description);
		e.putString("profile_image_url", profile_image_url);
		e.commit();
		if (weibo.isValid()) {
			weibo.removeAccount();
		}
		initbound();
	}

	private void initbound() {
		String url = "http://120.24.37.141:8080/LoginAndResigister/CheckRelationServlet";
		HttpUtils utils = new HttpUtils(5000);
		RequestParams params = new RequestParams();
		JSONObject obj = new JSONObject();
		SharedPreferences mPreferences = getSharedPreferences("MOBSSO", MODE_APPEND);
		String openid = mPreferences.getString("id", "");
		try {
			obj.put("openID", openid);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		params.addQueryStringParameter("requestMessage", obj.toString());
		utils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(getApplicationContext(), "查询绑定信息失败!!", 0).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> a) {
				if (a.statusCode == 200) {
					com.alibaba.fastjson.JSONObject json = JSON.parseObject(a.result);
					int status = json.getInteger("status");
					if (status == 0) {
						startActivity(new Intent(getApplicationContext(), BoundActivity.class));
						finish();
					} else {
						startActivity(new Intent(getApplicationContext(), ActivityMain.class));
						finish();
					}
				}
			}
		});
		// -----返回绑定信息----{"userPhone":null,"userPassword":null,
		// "userNickname":null,"userType":null,"relationType":null,"relationAcct":null,
		// "relationIcon":null,"userSex":null,"userId":0,"userPicture":null,"status":0}

	}

	// 失败方法
	@Override
	public void onError(Platform arg0, int arg1, Throwable arg2) {
		Toast.makeText(getApplicationContext(), "授权失败", 0).show();

	}

}
