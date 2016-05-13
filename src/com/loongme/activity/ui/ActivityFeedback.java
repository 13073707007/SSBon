package com.loongme.activity.ui;

import org.json.JSONObject;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

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
 * @version 类说明: 意见反馈
 */
public class ActivityFeedback extends BaseActivity {
	private ImageButton btn_goback;
	private Button bt_feedback_send;
	private EditText et_feedback, et_feedback_phone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		User user=Helpers.getUserInfo(this);
	}

	@Override
	protected void initParas() {
		super.initParas();
	}
	@Override
	protected void initWidgets() {
		setContentView(R.layout.activity_feedback);
		btn_goback = (ImageButton) findViewById(R.id.btn_goback);
		bt_feedback_send = (Button) findViewById(R.id.bt_feedback_send);
		et_feedback = (EditText) findViewById(R.id.et_feedback);
		et_feedback_phone = (EditText) findViewById(R.id.et_feedback_phone);
		// 获取控件EditText中的hint文本
		String ss = et_feedback.getHint().toString();
		String ss2 = et_feedback_phone.getHint().toString();

		// 新建一个可以添加属性的文本对象
		SpannableString feedback = new SpannableString(ss);
		SpannableString feedback_phone = new SpannableString(ss2);
		// 新建一个属性对象,设置文字的大小。14表示字体大小
		AbsoluteSizeSpan size = new AbsoluteSizeSpan(14, true);
		// 附加属性到文本
		feedback.setSpan(size, 0, feedback.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		feedback_phone.setSpan(size, 0, feedback_phone.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		// 最后一步转换，把内容放进控件里
		et_feedback.setHint(new SpannableString(feedback));
		et_feedback_phone.setHint(new SpannableString(feedback_phone));
		super.initWidgets();
	}

	@Override
	protected void initUI() {
		super.initUI();
	}

	@Override
	protected void regListener() {
		btn_goback.setOnClickListener(this);
		bt_feedback_send.setOnClickListener(this);
		super.regListener();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_goback:
			finish();
			break;
		case R.id.bt_feedback_send:
			if (et_feedback.getText().toString().equals("")) {
				UIUtil.showMsg(this, "建议不能为空!!");
			}else if (et_feedback_phone.getText().toString().equals("")) {
				UIUtil.showMsg(this, "联系方式不能为空!!");
			}else {
				parseHttpParams(Configuration.serv_flag.RequstFlag_GetUserInfo);
			}
		default:
			break;
		}
		super.onClick(v);
	}

	private void parseHttpParams(int requestFlag) {
		// TODO Auto-generated method stub
		showProgress();
		if (requestFlag==Configuration.serv_flag.RequstFlag_GetUserInfo) {
			JSONObject object=new JSONObject();
			try {
				object.put("fbContent", et_feedback.getText().toString());
				object.put("fbContact", et_feedback_phone.getText().toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			String params="requestMessage="+object.toString();
			ApiClient.getInstance(this).load(Configuration.SERVER_HOST_PREFIX+Configuration.URL_FEEDBACK, params,
					Configuration.serv_flag.RequstFlag_GetUserInfo, this);
			UIUtil.redirect(this, ActivityPersonalInfo.class);
		}
	}
}
