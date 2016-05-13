package com.loongme.activity.ui;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.loongme.activity.R;
import com.loongme.activity.base.BaseActivity;

public class Bound3Activity extends BaseActivity implements OnClickListener {
	private ImageView iv_returnx;
	private Button btn_for_pwd, btn_enterx;
	private EditText et_bou_phone, et_bou_pwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_bound3);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		et_bou_phone = (EditText) findViewById(R.id.et_bou_phone);
		et_bou_pwd = (EditText) findViewById(R.id.et_bou_pwd);
		iv_returnx = (ImageView) findViewById(R.id.iv_returnx);
		btn_for_pwd = (Button) findViewById(R.id.btn_for_pwd);
		btn_enterx = (Button) findViewById(R.id.btn_enterx);
		iv_returnx.setOnClickListener(this);
		btn_for_pwd.setOnClickListener(this);
		btn_enterx.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_returnx:
			startActivity(new Intent(getApplicationContext(),
					Bound2Activity.class));
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			finish();
			break;
		case R.id.btn_for_pwd:
			startActivity(new Intent(getApplicationContext(),
					ActivityForgetPassWord.class));
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

			finish();
			break;
		case R.id.btn_enterx:
			// startActivity(new Intent(getApplicationContext(),
			// UnderstanderDemo.class));
			// finish();
			HttpPostData();
			break;

		default:
			break;
		}
	}

	private void HttpPostData() {
		// TODO Auto-generated method stub
		String url = "http://120.24.37.141:8080/LoginAndResigister/register";
		HttpUtils httpUtils = new HttpUtils(5000);
		RequestParams params = new RequestParams("UTF-8");
		params.setContentType("application/json;charset=UTF-8");
		params.addHeader(
				"Content-Type",
				"application/json;charset=UTF-8;text/json;text/javascript;application/soap+xml;text/html;text/xml;text/plain");
		JSONObject obj = new JSONObject();
		JSONObject obj2 = new JSONObject();
		JSONObject obj3 = new JSONObject();
		SharedPreferences sf = getSharedPreferences("MOBSSO", MODE_APPEND);
		String relationOpenid = sf.getString("id", "");
		String userNickname = sf.getString("name", "");
		String relationIcon = sf.getString("profile_image_url", "");
		try {
			obj.put("userType", URLEncoder.encode("普通", "UTF-8"));
			obj.put("userPhone", et_bou_phone.getText().toString());
			obj.put("userPassword", et_bou_pwd.getText().toString());
			obj.put("userNickname", userNickname);
			obj.put("ifRelation", "1");
			obj2.put("relationType", URLEncoder.encode("新浪微博", "UTF-8"));
			obj2.put("relationAcct", "001654");
			obj2.put("relationOpenid", relationOpenid);
			obj2.put("relationIcon", relationIcon);
			obj2.put("userSex", URLEncoder.encode("", "UTF-8"));
			obj3.put("tmUserRegister", obj);
			obj3.put("tmRelationMsg", obj2);
			System.out.println("::" + obj3.toString());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		params.addQueryStringParameter("requestMessage", obj3.toString());
		httpUtils.send(HttpMethod.POST, url, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						Toast.makeText(getApplicationContext(), "绑定失败!!请检查网络",
								Toast.LENGTH_LONG).show();
						return;
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						if (arg0.statusCode == 200) {
							System.out.println("----返回信息----" + arg0.result);
							String ss;
							try {
								ss = new String(arg0.result.getBytes(), "utf-8");
								ss = URLDecoder.decode(ss, "utf-8");
								com.alibaba.fastjson.JSONObject JsonBean = JSON
										.parseObject(ss);
								int status = JsonBean.getInteger("status");
								if (status == 1) {
									// Toast.makeText(getApplicationContext(),
									// "注册成功", Toast.LENGTH_LONG).show();
									startActivity(new Intent(
											getApplicationContext(),
											ActivityMain.class));
									overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
									finish();
								} else {
									Toast.makeText(getApplicationContext(),
											"该手机号已绑定!!", Toast.LENGTH_LONG)
											.show();
								}
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				});
	}
}
