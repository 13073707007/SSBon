package com.loongme.activity.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageButton;

import com.loongme.activity.R;
import com.loongme.activity.base.BaseActivity;
import com.loongme.activity.base.RegisteNextCallBack;

/**
 * @author 作者:zhaojianyu
 * @version 创建时间：2016-5-8 下午3:39:51 类说明:注册
 */
public class ActivityRegiste extends BaseActivity implements RegisteNextCallBack {

	private FragmentReisteStepOne fragmentReisteStepOne;
	private FragmentRegisteStepTwo fragmentRegisteStepTwo;
	private FragmentManager fragmentManager;
	private ImageButton btn_goback;
	private int isFrom;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initParas() {
		if (getIntent() != null && getIntent().getExtras() != null) {
			isFrom = getIntent().getExtras().getInt("isFrom");
		}
		super.initParas();
	}

	@Override
	protected void initWidgets() {
		setContentView(R.layout.activity_registe);
		btn_goback = (ImageButton) findViewById(R.id.btn_goback);
		fragmentReisteStepOne = new FragmentReisteStepOne();
		fragmentReisteStepOne.setRegisteNextCallBack(this);
		fragmentRegisteStepTwo = new FragmentRegisteStepTwo();
		fragmentRegisteStepTwo.setIsFrom(isFrom);
		super.initWidgets();
	}

	@Override
	protected void regListener() {
		btn_goback.setOnClickListener(this);
		super.regListener();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_goback:
			finish();
			break;

		default:
			break;
		}
		super.onClick(v);
	}

	@Override
	protected void initUI() {
		fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		if (!fragmentReisteStepOne.isAdded()) {
			transaction.add(R.id.layout_registe_container, fragmentReisteStepOne);
			transaction.commit();
		}
		super.initUI();
	}

	public void switchFragment(Fragment currentFragment, Fragment addedFragment) {
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		if (addedFragment.isAdded()) {
			transaction.hide(currentFragment).show(addedFragment).commit();
		} else {
			transaction.hide(currentFragment).add(R.id.layout_registe_container, addedFragment).commit();
		}

	}

	@Override
	public void onRegisteNext(String userName) {
		fragmentRegisteStepTwo.setUserPhone(userName);
		switchFragment(fragmentReisteStepOne, fragmentRegisteStepTwo);

	}
}
