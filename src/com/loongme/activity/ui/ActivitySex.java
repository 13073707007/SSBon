package com.loongme.activity.ui;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
/***
 * 
 * @author xywy
 * @version 类说明: 修改性别
 */
public class ActivitySex extends BaseActivity {
	private ImageButton btn_goback;
	private Button btn_sex;
	private RadioGroup group_sex;
	private RadioButton btn_man, btn_woman;
	private User user;
	private String str;

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
		setContentView(R.layout.activity_sex);
		btn_goback = (ImageButton) findViewById(R.id.btn_goback);
		btn_sex = (Button) findViewById(R.id.btn_sex);
		group_sex = (RadioGroup) findViewById(R.id.group_sex);
		super.initWidgets();
	}

	@Override
	protected void initUI() {
		super.initUI();
	}

	@Override
	protected void regListener() {
		btn_goback.setOnClickListener(this);
		btn_sex.setOnClickListener(this);
		group_sex.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				// 获取变更后的选中项的ID
				int radioButtonId = group.getCheckedRadioButtonId();
				// 根据ID获取RadioButton的实例
				RadioButton rb = (RadioButton) ActivitySex.this.findViewById(radioButtonId);
				str = (String) rb.getText();
			}
		});
		super.regListener();
	}
	private void parseHttpParams(int requestFlag) {
		showProgress();
		if (requestFlag == Configuration.serv_flag.RequstFlag_GetUserInfo) {
			JSONObject object = new JSONObject();
			try {
				object.put("userId", user.getUserId());
				object.put("userSex", str);
			} catch (Exception e) {
				e.printStackTrace();
			}
			String params = "requestMessage=" + object.toString();
			ApiClient.getInstance(this).load(Configuration.SERVER_HOST_PREFIX + Configuration.URL_SEX, params,
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
		case R.id.btn_sex:
			// 提交
			if (str.equals("")) {
				UIUtil.showMsg(this, "请选择您的性别!!");
			}else {
				
				parseHttpParams(Configuration.serv_flag.RequstFlag_GetUserInfo);
			}

		default:
			break;
		}
		super.onClick(v);
	}
}
