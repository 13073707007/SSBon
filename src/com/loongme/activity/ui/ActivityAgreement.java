package com.loongme.activity.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.loongme.activity.R;
import com.loongme.activity.base.BaseActivity;

/**
 * @author 作者:zhaojianyu
 * @version 创建时间：2016-5-9 上午10:59:07 类说明:注册协议
 */
public class ActivityAgreement extends BaseActivity {

	private ImageButton btn_goback;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initParas() {
		super.initParas();
	}

	@Override
	protected void initWidgets() {
		setContentView(R.layout.activity_agreement);
		btn_goback = (ImageButton) findViewById(R.id.btn_goback);
		btn_goback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		super.initWidgets();
	}

	@Override
	protected void regListener() {
		super.regListener();
	}

	@Override
	protected void initUI() {
		super.initUI();
	}
}
