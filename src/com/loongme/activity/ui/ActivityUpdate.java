package com.loongme.activity.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.loongme.activity.R;
import com.loongme.activity.base.BaseActivity;
/**
 * 
 * @author xywy
 * @version 类说明: 检查更新
 */
public class ActivityUpdate extends BaseActivity {
	private ImageButton btn_goBack;
	private Button btn_versions_send;

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
		setContentView(R.layout.activity_update);
		btn_goBack = (ImageButton) findViewById(R.id.btn_goback);
		btn_versions_send = (Button) findViewById(R.id.btn_versions_send);
		super.initWidgets();
	}

	@Override
	protected void initUI() {
		// TODO Auto-generated method stub
		super.initUI();
	}

	@Override
	protected void regListener() {
		btn_goBack.setOnClickListener(this);
		btn_versions_send.setOnClickListener(this);
		super.regListener();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_goback:
			finish();
			break;
		case R.id.btn_versions_send:
			finish();
			break;

		default:
			break;
		}
		super.onClick(v);
	}
}
