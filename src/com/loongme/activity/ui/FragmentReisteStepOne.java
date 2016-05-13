package com.loongme.activity.ui;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.loongme.activity.R;
import com.loongme.activity.base.BaseFragment;
import com.loongme.activity.base.RegisteNextCallBack;
import com.loongme.activity.utils.DeviceUtils;
import com.loongme.activity.utils.StringUtils;
import com.loongme.activity.utils.UIUtil;

/**
 * @author 作者:zhaojianyu
 * @version 创建时间：2016-5-8 下午3:43:37 类说明:
 */
public class FragmentReisteStepOne extends BaseFragment {

	private EditText edit_username;
	private EditText edit_matchCode;
	private Button btn_getCode;
	private Button btn_next;
	private View contentView;
	private RegisteNextCallBack registeNextCallBack;
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

	public RegisteNextCallBack getRegisteNextCallBack() {
		return registeNextCallBack;
	}

	public void setRegisteNextCallBack(RegisteNextCallBack registeNextCallBack) {
		this.registeNextCallBack = registeNextCallBack;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// 初始化短信验证SDK
		eventHandler = new EventHandler() {
			@Override
			public void afterEvent(final int event, int result, Object data) {
				if (result == SMSSDK.RESULT_COMPLETE) {
					// 回调完成
					if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
						// 提交验证码成功
						dismissProgress();
						registeNextCallBack.onRegisteNext(edit_username.getText().toString().trim());
					} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
						// 获取验证码成功
						dismissProgress();
						getActivity().runOnUiThread(timeRunnable);

					} else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
						// 返回支持发送验证码的国家列表
					}
				} else {
					dismissProgress();
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
								UIUtil.showMsg(getActivity(), "获取验证码失败");
							} else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
								UIUtil.showMsg(getActivity(), "验证失败,请重试");
							}

						}
					});
					((Throwable) data).printStackTrace();
				}
			}
		};
		SMSSDK.registerEventHandler(eventHandler);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		contentView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_registe_step1, null);
		edit_username = (EditText) contentView.findViewById(R.id.edit_username);
		edit_matchCode = (EditText) contentView.findViewById(R.id.edit_matchcode);
		btn_getCode = (Button) contentView.findViewById(R.id.btn_getcode);
		btn_next = (Button) contentView.findViewById(R.id.btn_nextstep);
		contentView
				.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		btn_getCode.setOnClickListener(this);
		btn_next.setOnClickListener(this);
		return contentView;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 发送验证码
		case R.id.btn_getcode:
			if (!StringUtils.isMobile(edit_username.getText().toString().trim())) {
				UIUtil.showMsg(getActivity(), "请输入正确的手机号");
				return;
			}
			showProgress();
			SMSSDK.getVerificationCode("86", edit_username.getText().toString().trim());
			break;
		// 下一步
		case R.id.btn_nextstep:
			DeviceUtils.hideSoftInput(getActivity(), edit_username);
			DeviceUtils.hideSoftInput(getActivity(), edit_matchCode);
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
