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

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
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
 *@version 类说明:个性宣言
 */
public class ActivityManifesto extends BaseActivity {
	private ImageButton btn_goback;
	private Button btn_title_send;
	private EditText ed_manifesto;
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
		setContentView(R.layout.activity_manifesto);
		btn_goback = (ImageButton) findViewById(R.id.btn_goback);
		btn_title_send = (Button) findViewById(R.id.btn_title_send);
		ed_manifesto=(EditText) findViewById(R.id.ed_manifesto);
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
				object.put("persDeclaration", ed_manifesto.getText().toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			String params = "requestMessage=" + object.toString();
			ApiClient.getInstance(this).load(Configuration.SERVER_HOST_PREFIX + Configuration.URL_MANIFESTO, params,
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
			if (ed_manifesto.getText().toString().equals("")) {
				UIUtil.showMsg(this, "请填写您的个人宣言!!");
			}else {
				parseHttpParams(Configuration.serv_flag.RequstFlag_GetUserInfo);
			}

		default:
			break;
		}
		super.onClick(v);
	}
}
