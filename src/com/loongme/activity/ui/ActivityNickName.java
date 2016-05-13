package com.loongme.activity.ui;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.loongme.activity.R;
import com.loongme.activity.base.ApiClient;
import com.loongme.activity.base.BaseActivity;
import com.loongme.activity.bean.User;
import com.loongme.activity.business.Helpers;
import com.loongme.activity.common.Configuration;
import com.loongme.activity.utils.UIUtil;
/**
 * 
 * @author xywy
 * @version 类说明:修改昵称
 */
public class ActivityNickName extends BaseActivity {
	private ImageButton btn_goback;
	private Button btn_title_send;
	private EditText ed_nick_name;
	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		user = Helpers.getUserInfo(this);
	}

	@Override
	protected void initParas() {
		super.initParas();
	}

	@Override
	protected void initWidgets() {
		setContentView(R.layout.activity_nick_name);
		btn_goback = (ImageButton) findViewById(R.id.btn_goback);
		btn_title_send = (Button) findViewById(R.id.btn_title_send);
		ed_nick_name=(EditText) findViewById(R.id.ed_nick_name);
		super.initWidgets();
	}

	@Override
	protected void initUI() {
		super.initUI();
	}

	@Override
	protected void regListener() {
		btn_goback.setOnClickListener(this);
		btn_title_send.setOnClickListener(this);
		super.regListener();
	}
	private void parseHttpParams(int requestFlag) {
		showProgress();
		if (requestFlag == Configuration.serv_flag.RequstFlag_GetUserInfo) {
			JSONObject object = new JSONObject();
			try {
				object.put("userId", user.getUserId());
				object.put("nikename", ed_nick_name.getText().toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			String params = "requestMessage=" + object.toString();
			ApiClient.getInstance(this).load(Configuration.SERVER_HOST_PREFIX + Configuration.URL_NICKNAME, params,
					Configuration.serv_flag.RequstFlag_GetUserInfo, this);
			UIUtil.redirect(this, ActivityPersonalInfoSet.class);
			dismissProgress();
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_goback:
			finish();
			break;
		case R.id.btn_title_send:
			// 提交
			if (ed_nick_name.getText().toString().equals("")) {
				UIUtil.showMsg(this, "请填写您的昵称!!");
			}else {
				parseHttpParams(Configuration.serv_flag.RequstFlag_GetUserInfo);
			}

		default:
			break;
		}
		super.onClick(v);
	}
}
