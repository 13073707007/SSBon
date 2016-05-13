package com.loongme.activity.ui;

import java.io.File;
import java.net.URLEncoder;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.loongme.activity.R;
import com.loongme.activity.base.ApiClient;
import com.loongme.activity.base.AppManager;
import com.loongme.activity.base.BaseFragment;
import com.loongme.activity.bean.User;
import com.loongme.activity.business.Helpers;
import com.loongme.activity.business.SsbService;
import com.loongme.activity.common.Configuration;
import com.loongme.activity.common.ResultObj;
import com.loongme.activity.utils.StringUtils;
import com.loongme.activity.utils.UIUtil;

/**
 * @author 作者:zhaojianyu
 * @version 创建时间：2016-5-8 下午3:55:29 类说明:
 */
public class FragmentRegisteStepTwo extends BaseFragment {

	private EditText edit_pass1;
	private EditText edit_pass2;
	private CheckBox checkbox_agree;
	private TextView tx_agreement;
	private Button btn_registe;
	private View contentView;
	private String pass1, pass2;
	private SsbService iService = new SsbService();
	private int isFrom;
	private String userPhone;

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public int getIsFrom() {
		return isFrom;
	}

	public void setIsFrom(int isFrom) {
		this.isFrom = isFrom;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		contentView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_registe_step2, null);
		contentView
				.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		edit_pass1 = (EditText) contentView.findViewById(R.id.edit_password1);
		edit_pass2 = (EditText) contentView.findViewById(R.id.edit_password2);
		checkbox_agree = (CheckBox) contentView.findViewById(R.id.checkbox_agree_agreement);
		tx_agreement = (TextView) contentView.findViewById(R.id.tx_agreement);
		btn_registe = (Button) contentView.findViewById(R.id.btn_registe);
		tx_agreement.setOnClickListener(this);
		btn_registe.setOnClickListener(this);
		return contentView;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 协议
		case R.id.tx_agreement:
			UIUtil.redirect(getActivity(), ActivityAgreement.class);
			break;
		// 注册
		case R.id.btn_registe:
			pass1 = edit_pass1.getText().toString().trim();
			pass2 = edit_pass2.getText().toString().trim();
			if (StringUtils.isEmpty(pass1)) {
				UIUtil.showMsg(getActivity(), "密码不能为空");
				return;
			}
			if (StringUtils.isEmpty(pass2)) {
				UIUtil.showMsg(getActivity(), "再次输入密码不能为空");
				return;
			}
			if (!pass1.equals(pass2)) {
				UIUtil.showMsg(getActivity(), "两次输入密码不一致");
				return;
			}
			if (pass1.length() > 20 || pass1.length() < 6 || pass2.length() > 20 || pass2.length() < 6) {
				UIUtil.showMsg(getActivity(), "请输入6-20位数字或字符");
				return;
			}

			if (checkbox_agree.isChecked()) {
				parseHttpRquest();
			} else {
				UIUtil.showMsg(getActivity(), "请仔细阅读事事帮注册协议");
			}
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
			JSONObject obj2 = new JSONObject();
			JSONObject obj3 = new JSONObject();
			obj.put("userType", URLEncoder.encode("普通用户", "UTF-8"));
			obj.put("userPhone", userPhone);
			obj.put("userPassword", pass1);
			obj.put("userNickname", URLEncoder.encode("小帮", "UTF-8"));
			obj.put("ifRelation", "0");
			obj2.put("relationType", "safs");
			obj2.put("relationAcct", "001654");
			obj2.put("relationOpenid", "001654");
			obj2.put("relationIcon", "fss");
			obj2.put("userSex", URLEncoder.encode("男", "UTF-8"));
			obj3.put("tmUserRegister", obj);
			obj3.put("tmRelationMsg", obj2);
			String params = "requestMessage=" + obj3.toString();
			ApiClient.getInstance(getActivity()).load(Configuration.SERVER_HOST_PREFIX + Configuration.URL_REGISTE,
					params, Configuration.serv_flag.RequstFlag_Registe, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean handleSuccess(int statusCode, JSONObject response, int flagRequest) {
		try {
			if (statusCode == ResultObj.ERRCODE_STATE_OK) {
				if (StringUtils.isEmpty(response.toString())) {
					UIUtil.showMsg(getActivity(), "注册失败");
					return false;
				}
				JSONObject jsonObject = new JSONObject(response.toString());
				if (jsonObject.getInt("status") != 1) {
					UIUtil.showMsg(getActivity(), "该手机号已注册");
					return false;
				}
				UIUtil.showMsg(getActivity(), "注册成功");
				User user = JSON.parseObject(response.toString(), User.class);
				// 保存用户信息
				Helpers.saveUserInfoToLocal(getActivity(), user);
				// 创建用户缓存文件路径
				File file = new File(Configuration.SDCARD_PATH + File.separator + Configuration.TAG + File.separator
						+ user.getUserNickname());
				if (!file.exists()) {
					file.mkdir();
				}
				if (isFrom == Configuration.REQUESTCODE_REGISTEFROMSPLASH) {
					UIUtil.redirect(getActivity(), ActivityMain.class);
					getActivity().finish();
				} else if (isFrom == Configuration.REQUESTCODE_REGISTEFROMLOGIN) {
					UIUtil.redirect(getActivity(), ActivityMain.class);
					AppManager.getAppManager().finishActivity(ActivityLogin.class);
					getActivity().finish();
				} else {
					AppManager.getAppManager().finishActivity(ActivityPersonalInfo.class);
					getActivity().finish();
				}
			} else {
				UIUtil.showMsg(getActivity(), "访问网络失败" + statusCode);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.handleSuccess(statusCode, response, flagRequest);
	}

	@Override
	public boolean handleFailure(int statusCode, String responseString, Throwable throwable, int flagRequest) {
		dismissProgress();
		UIUtil.showMsg(getActivity(), "网络错误" + responseString);
		return super.handleFailure(statusCode, responseString, throwable, flagRequest);
	}
}
