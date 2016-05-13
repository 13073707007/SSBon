package com.loongme.activity.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.loongme.activity.R;
import com.loongme.activity.base.BaseActivity;
import com.loongme.activity.common.Configuration;
import com.loongme.activity.utils.UIUtil;

/**
 * @author 作者:zhaojianyu
 * @version 创建时间：2016年5月2日 下午7:52:36 类说明:引导页
 */
public class ActivitySplash extends BaseActivity {

	private ViewPager viewPager_splash;
	private List<View> datas;
	private TextView tx_registe;
	private TextView tx_login;
	private View view_try;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initParas() {
		datas = new ArrayList<View>();
		super.initParas();
	}

	@Override
	protected void initWidgets() {
		setContentView(R.layout.activity_splash);
		viewPager_splash = (ViewPager) findViewById(R.id.viewpager_splash);
		// 前三页
		for (int i = 0; i < 3; i++) {
			ImageView img = new ImageView(this);
			img.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			img.setScaleType(ScaleType.FIT_XY);
			datas.add(img);
		}
		((ImageView) datas.get(0)).setImageDrawable(getResources().getDrawable(R.drawable.icon_one));
		((ImageView) datas.get(1)).setImageDrawable(getResources().getDrawable(R.drawable.icon_splash_2));
		((ImageView) datas.get(2)).setImageDrawable(getResources().getDrawable(R.drawable.icon_splash_3));
		// 最后一页
		View splash_four = LayoutInflater.from(this).inflate(R.layout.view_splash_four, null);
		datas.add(splash_four);
		tx_registe = (TextView) splash_four.findViewById(R.id.tx_registe);
		tx_login = (TextView) splash_four.findViewById(R.id.tx_login);
		view_try = (View) splash_four.findViewById(R.id.view_try);

		super.initWidgets();
	}

	@Override
	protected void regListener() {
		super.regListener();
		tx_login.setOnClickListener(this);
		tx_registe.setOnClickListener(this);
		view_try.setOnClickListener(this);
	}

	@Override
	protected void initUI() {
		super.initUI();

		viewPager_splash.setAdapter(new VPAdapter());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tx_registe:
			Bundle bundle0 = new Bundle();
			bundle0.putInt("isFrom", Configuration.REQUESTCODE_REGISTEFROMSPLASH);
			UIUtil.redirect(ActivitySplash.this, ActivityRegiste.class, bundle0);
			finish();
			break;
		case R.id.tx_login:
			Bundle bundle = new Bundle();
			bundle.putInt("isFrom", Configuration.REQUESTCODE_LOGINFROMSPLASH);
			UIUtil.redirect(ActivitySplash.this, ActivityLogin.class, bundle);
			finish();
			break;
		case R.id.view_try:
			UIUtil.redirect(ActivitySplash.this, ActivityMain.class);
			finish();
			break;
		default:
			break;
		}
		super.onClick(v);
	}

	private class VPAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return datas.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager) container).addView(datas.get(position));
			return datas.get(position);
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(datas.get(position));
		}

	}

}
