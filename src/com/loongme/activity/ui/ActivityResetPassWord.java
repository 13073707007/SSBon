package com.loongme.activity.ui;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.loongme.activity.R;
import com.loongme.activity.base.ApiClient;
import com.loongme.activity.base.AppManager;
import com.loongme.activity.base.BaseActivity;
import com.loongme.activity.common.Configuration;
import com.loongme.activity.common.ResultObj;
import com.loongme.activity.utils.StringUtils;
import com.loongme.activity.utils.UIUtil;

/**
 * @author 作者:zhaojianyu
 * @version 创建时间：2016-5-10 下午2:36:01 类说明:重置密码
 */
public class ActivityResetPassWord extends BaseActivity {
	private ImageButton btn_goback;
	private EditText edit_pass1;
	private EditText edit_pass2;
	private Button btn_complete;
	private String pass1, pass2;
	private String userPhone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initParas() {
		if (getIntent() != null) {
			userPhone = getIntent().getExtras().getString("userPhone");
		}
		super.initParas();
	}

	@Override
	protected void initWidgets() {
		setContentView(R.layout.activity_resetpass);
		btn_goback = (ImageButton) findViewById(R.id.btn_goback);
		edit_pass1 = (EditText) findViewById(R.id.edit_password1);
		edit_pass2 = (EditText) findViewById(R.id.edit_password2);
		btn_complete = (Button) findViewById(R.id.btn_complete);
		super.initWidgets();
	}

	@Override
	protected void regListener() {
		btn_goback.setOnClickListener(this);
		btn_complete.setOnClickListener(this);
		super.regListener();
	}

	@Override
	protected void initUI() {
		super.initUI();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_goback:
			finish();
			break;
		case R.id.btn_complete:
			pass1 = edit_pass1.getText().toString().trim();
			pass2 = edit_pass2.getText().toString().trim();
			if (StringUtils.isEmpty(pass1)) {
				UIUtil.showMsg(this, "密码不能为空");
				return;
			}
			if (StringUtils.isEmpty(pass2)) {
				UIUtil.showMsg(this, "再次输入密码不能为空");
				return;
			}
			if (!pass1.equals(pass2)) {
				UIUtil.showMsg(this, "两次输入密码不一致");
				return;
			}
			if (pass1.length() > 20 || pass1.length() < 6 || pass2.length() > 20 || pass2.length() < 6) {
				UIUtil.showMsg(this, "请输入6-20位数字或字符");
				return;
			}
			parseHttpRquest();
			break;
		default:
			break;
		}
		super.onClick(v);
	}

	private void parseHttpRquest() {
		showProgress();
		try {
			JSONObject obj = new JSONObject();
			obj.put("userPhone", userPhone);
			obj.put("userPassword", pass1);
			String params = "requestMessage=" + obj.toString();
			ApiClient.getInstance(this).load(Configuration.SERVER_HOST_PREFIX + Configuration.URL_UPDATEPASS, params,
					Configuration.serv_flag.RequstFlag_UpdatePass, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean handleSuccess(int statusCode, JSONObject response, int flagRequest) {
		dismissProgress();
		try {
			if (statusCode == ResultObj.ERRCODE_STATE_OK) {
				if (StringUtils.isEmpty(response.toString())) {
					UIUtil.showMsg(this, "修改失败");
					return false;
				}
				if (!StringUtils.isEmpty(response.getString("userStatus"))
						&& response.getString("userStatus").equals("正常")) {
					UIUtil.showMsg(this, "修改成功");
					AppManager.getAppManager().finishActivity(ActivityForgetPassWord.class);
					finish();
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
		UIUtil.showMsg(this, "修改失败");
		return super.handleFailure(statusCode, responseString, throwable, flagRequest);
	}
}
