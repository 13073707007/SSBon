package com.loongme.activity.ui;

import java.io.File;
import java.io.FileInputStream;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.loongme.activity.R;
import com.loongme.activity.base.AppManager;
import com.loongme.activity.base.BaseActivity;
import com.loongme.activity.business.Helpers;
import com.loongme.activity.common.Configuration;
import com.loongme.activity.utils.StringUtils;
import com.loongme.activity.utils.UIUtil;
import com.loongme.activity.widgets.CustomProgressDialog;

/**
 * 发布服务
 * 
 * @author xywy
 * 
 */
public class ActivityServer extends BaseActivity implements OnClickListener {
	ImageView iv_fanhuise;
	Button server_send;
	private RadioGroup group_selector, price_group;
	private RadioButton rb_radio_btn1, rb_radio_btn2, rb_radio_btn3, price_btn1, price_btn2;
	private RelativeLayout territory_scope;
	private LinearLayout price_unit;

	private String serverName;
	private String subServerName;
	private TextView txt_serverName;// 服务名称
	private TextView txt_subServerName;// 服务子类名称
	private RelativeLayout layout_select_serverType;// 重新选择服务

	private EditText edit_serverTitle;// 服务标题
	private EditText edit_serverDes;// 服务描述

	private ImageView imageCover;// 封面
	private ImageView imageWork;// 工作照或作品

	private EditText edit_advantage;// 您的优势

	private String serverMethod;// 服务方式
	private EditText edit_serverRange;// 服务范围

	private String priceType;// 价格
	private EditText edit_price;// 价格
	private EditText edit_unit;// 单位

	private CustomProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_server);
		serverName = getIntent().getExtras().getString("serverName", "");
		subServerName = getIntent().getExtras().getString("subServerName", "");
		initView();
	}

	private void initView() {
		progressDialog = new CustomProgressDialog(this, "加载中...", R.anim.loading);
		layout_select_serverType = (RelativeLayout) findViewById(R.id.layout_select_serverType);
		layout_select_serverType.setOnClickListener(this);

		// 初始化服务种类名称
		txt_serverName = (TextView) findViewById(R.id.txt_server_name);
		txt_serverName.setText(serverName);
		txt_subServerName = (TextView) findViewById(R.id.txt_subserver_name);
		txt_subServerName.setText(subServerName);

		edit_serverTitle = (EditText) findViewById(R.id.et_title);
		edit_serverDes = (EditText) findViewById(R.id.edit_server_des);
		edit_advantage = (EditText) findViewById(R.id.et_you_advantage);
		edit_serverRange = (EditText) findViewById(R.id.edit_server_range);
		edit_price = (EditText) findViewById(R.id.edit_price);
		edit_unit = (EditText) findViewById(R.id.edit_company);

		imageCover = (ImageView) findViewById(R.id.iv_cover);
		imageWork = (ImageView) findViewById(R.id.iv_word);
		imageCover.setOnClickListener(this);
		imageWork.setOnClickListener(this);

		territory_scope = (RelativeLayout) findViewById(R.id.territory_scope);
		price_unit = (LinearLayout) findViewById(R.id.price_unit);
		iv_fanhuise = (ImageView) findViewById(R.id.iv_fanhuise);
		server_send = (Button) findViewById(R.id.server_send);
		group_selector = (RadioGroup) findViewById(R.id.group_selector);
		price_group = (RadioGroup) findViewById(R.id.price_group);
		price_btn1 = (RadioButton) findViewById(R.id.price_btn1);
		price_btn2 = (RadioButton) findViewById(R.id.price_btn2);
		rb_radio_btn1 = (RadioButton) findViewById(R.id.rb_radio_btn1);
		rb_radio_btn2 = (RadioButton) findViewById(R.id.rb_radio_btn1);
		rb_radio_btn3 = (RadioButton) findViewById(R.id.rb_radio_btn1);
		iv_fanhuise.setOnClickListener(this);
		server_send.setOnClickListener(this);
		group_selector.setOnCheckedChangeListener(group);
		price_group.setOnCheckedChangeListener(group);
	}

	OnCheckedChangeListener group = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			if (checkedId == R.id.rb_radio_btn1) {
				territory_scope.setVisibility(View.VISIBLE);
				serverMethod = "上门服务";
			}
			if (checkedId == R.id.rb_radio_btn2) {
				territory_scope.setVisibility(View.VISIBLE);
				serverMethod = "约定地点";
			}
			if (checkedId == R.id.rb_radio_btn3) {
				territory_scope.setVisibility(View.GONE);
				price_btn2.setVisibility(View.GONE);
				serverMethod = "在线服务";
			}
			if (checkedId == R.id.price_btn1) {
				price_unit.setVisibility(View.VISIBLE);
				priceType = "一口价";
			}
			if (checkedId == R.id.price_btn2) {
				price_unit.setVisibility(View.GONE);
				priceType = "面议";
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_fanhuise:
			finish();
			break;
		case R.id.server_send:
			// TODO 提交服务
			parseHttpRequest();
			break;
		case R.id.layout_select_serverType:
			finish();
			break;
		case R.id.iv_cover:
			startActivityForResult(new Intent(ActivityServer.this, ActivityBusinessLicense.class),
					Configuration.REQUESTCODE_SELECT_SERVER_COVER);
			ActivityBusinessLicense.requestCode = Configuration.REQUESTCODE_SELECT_SERVER_COVER;
			break;
		case R.id.iv_word:
			startActivityForResult(new Intent(ActivityServer.this, ActivityBusinessLicense.class),
					Configuration.REQUESTCODE_SELECT_SERVER_WORK);
			ActivityBusinessLicense.requestCode = Configuration.REQUESTCODE_SELECT_SERVER_WORK;
			break;
		default:
			break;
		}
	}

	private void parseHttpRequest() {
		if (StringUtils.isEmpty(edit_serverTitle.getText().toString())) {
			UIUtil.showMsg(this, "标题不能为空");
			return;
		}
		if (StringUtils.isEmpty(edit_serverDes.getText().toString())) {
			UIUtil.showMsg(this, "服务描述不能为空");
			return;
		}
		if (StringUtils.isEmpty(edit_advantage.getText().toString())) {
			UIUtil.showMsg(this, "您的优势不能为空");
			return;
		}
		if (StringUtils.isEmpty(serverMethod)) {
			UIUtil.showMsg(this, "服务方式不能为空");
			return;
		}
//		if (StringUtils.isEmpty(edit_serverRange.getText().toString())) {
////			UIUtil.showMsg(this, "服务范围不能为空");
//			return;
//		}
		if (StringUtils.isEmpty(priceType)) {
			UIUtil.showMsg(this, "价格方式不能为空");
			return;
		}
//		if (StringUtils.isEmpty(edit_price.getText().toString())) {
////			UIUtil.showMsg(this, "价格不能为空");
//			return;
//		}
		if (StringUtils.isEmpty(edit_unit.getText().toString())) {
			UIUtil.showMsg(this, "价格单位不能为空");
			return;
		}
		File cover = new File(Configuration.SERVER_COVER_SAVEPATH);
		File work = new File(Configuration.SERVER_WORK_SAVEPATH);
		if (!cover.exists()) {
			UIUtil.showMsg(this, "请上传封面照片");
			return;
		}
		if (!work.exists()) {
			UIUtil.showMsg(this, "请上传工作照或作品找");
			return;
		}
		String url = "http://120.24.37.141:8080/LoginAndResigister/provideService";
		HttpUtils httpUtils = new HttpUtils(10000);
		RequestParams params = new RequestParams("UTF-8");
		JSONObject object = new JSONObject();
		try {
			object.put("userId", Helpers.getUserInfo(this).getUserId());
			object.put("provideTitle", edit_serverTitle.getText().toString());
			object.put("provideDesc", edit_serverDes.getText().toString());
			object.put("provideScope", edit_serverRange.getText().toString());
			object.put("provideType", serverMethod);
			object.put("priceType", priceType);
			object.put("providePrice", edit_price.getText().toString() + "/" + edit_unit.getText().toString());
			object.put("serverType", serverName + "/" + subServerName);
			object.put("advantage", edit_advantage.getText().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		params.addBodyParameter("requestMessage", object.toString());
		try {
			params.addBodyParameter("image0.png", new FileInputStream(cover), cover.length());
			params.addBodyParameter("image1.png", new FileInputStream(work), work.length());
		} catch (Exception e) {
		}

		progressDialog.show();
		httpUtils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				progressDialog.dismiss();
				UIUtil.showMsg(ActivityServer.this, "发布失败");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				progressDialog.dismiss();
				if (arg0.result == null) {
					UIUtil.showMsg(ActivityServer.this, "发布失败！");
				}
				try {
					if (arg0.statusCode == 200) {
						JSONObject object = new JSONObject(arg0.result);
						if (object.getBoolean("status")) {
							UIUtil.showMsg(ActivityServer.this, "发布成功");
							AppManager.getAppManager().finishActivity(ActivityRelease.class);
							finish();
						} else {
							UIUtil.showMsg(ActivityServer.this, "发布失败");
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();

		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}
		// 刷新封面
		if (requestCode == Configuration.REQUESTCODE_SELECT_SERVER_COVER) {
			Bitmap bitmap = BitmapFactory.decodeFile(Configuration.SERVER_COVER_SAVEPATH);
			imageCover.setImageBitmap(bitmap);
		}
		// 刷新工作照或作品图
		else if (requestCode == Configuration.REQUESTCODE_SELECT_SERVER_WORK) {
			Bitmap bitmap = BitmapFactory.decodeFile(Configuration.SERVER_WORK_SAVEPATH);
			imageWork.setImageBitmap(bitmap);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	// @Override
	// public void handleSucess(int statusCode, String result, int flagRequest)
	// {
	// progressDialog.dismiss();
	// if (statusCode == 200) {
	// System.out.println("--------" + result);
	// }
	// super.handleSucess(statusCode, result, flagRequest);
	// }
	//
	// @Override
	// public void hanleFailure(HttpException exception, String arg1,
	// int flagRequest) {
	// progressDialog.dismiss();
	// UIUtils.showToast(this, "提交失败");
	// super.hanleFailure(exception, arg1, flagRequest);
	// }

}
