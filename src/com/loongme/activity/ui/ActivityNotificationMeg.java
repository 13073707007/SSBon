package com.loongme.activity.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.loongme.activity.R;
import com.loongme.activity.base.BaseActivity;
/**
 * 
 * @author xywy
 * @version 类说明:通知消息
 *
 */
public class ActivityNotificationMeg extends BaseActivity {

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
		setContentView(R.layout.activity_notification_meg);
		btn_goback = (ImageButton) findViewById(R.id.btn_goback);
		super.initWidgets();
	}

	@Override
	protected void initUI() {
		super.initUI();
	}

	@Override
	protected void regListener() {
		btn_goback.setOnClickListener(this);
		super.regListener();
	}

	@Override
	public void onClick(View v) {
		finish();
		super.onClick(v);
	}
}
