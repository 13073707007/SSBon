package com.loongme.activity.ui;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;

import com.loongme.activity.R;
import com.loongme.activity.base.BaseActivity;
import com.loongme.activity.business.Helpers;
import com.loongme.activity.utils.UIUtil;

/**
 * @author 作者:zhaojianyu
 * @version 创建时间：2016年5月2日 下午7:26:39 类说明:欢迎页面
 */
public class ActivityWelcome extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// 判断是否是第一次启动
				if (Helpers.isFirstStartApp(ActivityWelcome.this)) {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							UIUtil.redirect(ActivityWelcome.this, ActivitySplash.class);
							finish();
						}
					});

				} else {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							UIUtil.redirect(ActivityWelcome.this, ActivityMain.class);
							finish();
						}
					});
				}
			}
		}, 2000);

	}

	@Override
	protected void initParas() {
		super.initParas();
	}

	@Override
	protected void initWidgets() {
		super.initWidgets();
		setContentView(R.layout.activity_welcome);

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
