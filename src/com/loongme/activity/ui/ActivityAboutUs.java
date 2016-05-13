package com.loongme.activity.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.loongme.activity.R;
import com.loongme.activity.base.BaseActivity;
/**
 * 
 * @author xywy
 * @version 类说明: 产品介绍
 */
public class ActivityAboutUs extends BaseActivity {
	private ImageButton btn_goback;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void initParas() {
		// TODO Auto-generated method stub
		super.initParas();
	}

	@Override
	protected void initWidgets() {
		setContentView(R.layout.activity_about_us);
		btn_goback = (ImageButton) findViewById(R.id.btn_goback);
		super.initWidgets();
	}

	@Override
	protected void initUI() {
		// TODO Auto-generated method stub
		super.initUI();
	}

	@Override
	protected void regListener() {
		// TODO Auto-generated method stub
		btn_goback.setOnClickListener(this);
		super.regListener();
	}

	@Override
	public void onClick(View v) {
		finish();
		super.onClick(v);
	}
}
