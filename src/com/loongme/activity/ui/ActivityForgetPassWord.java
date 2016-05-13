package com.loongme.activity.ui;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.loongme.activity.R;
import com.loongme.activity.base.BaseActivity;
import com.loongme.activity.utils.DeviceUtils;
import com.loongme.activity.utils.StringUtils;
import com.loongme.activity.utils.UIUtil;

/**
 * @author 作者:zhaojianyu
 * @version 创建时间：2016-5-10 下午2:33:23 类说明:忘记密码
 */
public class ActivityForgetPassWord extends BaseActivity {

	private ImageButton btn_goback;
	private EditText edit_username;
	private EditText edit_matchCode;
	private Button btn_getCode;
	private Button btn_next;
	private EventHandler eventHandler;
	private Runnable timeRunnable = new Runnable() {

		@Override
		public void run() {
			btn_getCode.setClickable(false);
			new CountDownTimer(60000, 1000) {
				@Override
				public void onTick(long millisUntilFinished) {
					int secondsRemaining = (int) (millisUntilFinished / 1000);
					btn_getCode.setText(secondsRemaining + "s" + "后重新发送");
					btn_getCode.setTextColor(getResources().getColor(R.color.gray_light));
				}

				@Override
				public void onFinish() {
					btn_getCode.setClickable(true);
					btn_getCode.setText("获取验证码");
					btn_getCode.setTextColor(getResources().getColor(R.color.appcolor));

				}
			}.start();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initParas() {
		// 初始化短信验证SDK
		eventHandler = new EventHandler() {
			@Override
			public void afterEvent(final int event, int result, Object data) {
				if (result == SMSSDK.RESULT_COMPLETE) {
					// 回调完成
					if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
						// 提交验证码成功
						dismissProgress();
						Bundle bundle = new Bundle();
						bundle.putString("userPhone", edit_username.getText().toString().trim());
						UIUtil.redirect(ActivityForgetPassWord.this, ActivityResetPassWord.class, bundle);
					} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
						// 获取验证码成功
						dismissProgress();
						runOnUiThread(timeRunnable);

					} else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
						// 返回支持发送验证码的国家列表
					}
				} else {
					dismissProgress();
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
								UIUtil.showMsg(ActivityForgetPassWord.this, "获取验证码失败");
							} else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
								UIUtil.showMsg(ActivityForgetPassWord.this, "验证失败,请重试");
							}

						}
					});
					((Throwable) data).printStackTrace();
				}
			}
		};
		SMSSDK.registerEventHandler(eventHandler);
		super.initParas();
	}

	@Override
	protected void initWidgets() {
		setContentView(R.layout.activity_forgetpass);
		edit_username = (EditText) findViewById(R.id.edit_username);
		edit_matchCode = (EditText) findViewById(R.id.edit_matchcode);
		btn_getCode = (Button) findViewById(R.id.btn_getcode);
		btn_next = (Button) findViewById(R.id.btn_nextstep);
		btn_goback = (ImageButton) findViewById(R.id.btn_goback);
		super.initWidgets();
	}

	@Override
	protected void regListener() {
		btn_goback.setOnClickListener(this);
		btn_getCode.setOnClickListener(this);
		btn_next.setOnClickListener(this);
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
		// 发送验证码
		case R.id.btn_getcode:
			if (!StringUtils.isMobile(edit_username.getText().toString().trim())) {
				UIUtil.showMsg(ActivityForgetPassWord.this, "请输入正确的手机号");
				return;
			}
			showProgress();
			SMSSDK.getVerificationCode("86", edit_username.getText().toString().trim());
			break;
		// 下一步
		case R.id.btn_nextstep:
			DeviceUtils.hideSoftInput(ActivityForgetPassWord.this, edit_username);
			DeviceUtils.hideSoftInput(ActivityForgetPassWord.this, edit_matchCode);
			showProgress();
			SMSSDK.submitVerificationCode("86", edit_username.getText().toString().trim(), edit_matchCode.getText()
					.toString().trim());
			break;

		default:
			break;
		}
		super.onClick(v);
	}

	@Override
	public void onDestroy() {
		SMSSDK.unregisterEventHandler(eventHandler);
		super.onDestroy();
	}

}
