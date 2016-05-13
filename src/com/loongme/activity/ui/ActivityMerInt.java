package com.loongme.activity.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.loongme.activity.R;
import com.loongme.activity.base.BaseActivity;
import com.loongme.activity.utils.UIUtil;

/**
 * 详细介绍
 * 
 * @author xywy
 * 
 */
public class ActivityMerInt extends BaseActivity implements OnClickListener {
	ImageView iv_fanhumi;
	Button btn_sjjs_send;
	EditText ed_introduce;
	String merchant_introduce;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_mer_int);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		ed_introduce = (EditText) findViewById(R.id.ed_introduce);
		iv_fanhumi = (ImageView) findViewById(R.id.iv_fanhumi);
		btn_sjjs_send = (Button) findViewById(R.id.btn_sjjs_send);
		iv_fanhumi.setOnClickListener(this);
		btn_sjjs_send.setOnClickListener(this);
		merchant_introduce = ed_introduce.getText().toString();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_fanhumi:
			UIUtil.hideSoftKeyBoard(this, getCurrentFocus());
			finish();
			break;
		case R.id.btn_sjjs_send:
			// SharedPreferences spf = getSharedPreferences("merdes",
			// MODE_APPEND);
			// Editor edt = spf.edit();
			// edt.putString("busIntroduce", merchant_introduce);
			// edt.commit();
			// Intent it = new Intent();
			// it.setClass(getApplicationContext(), GuestActivity.class);
			// startActivity(it);
			UIUtil.hideSoftKeyBoard(this, getCurrentFocus());
			setResult(RESULT_OK, new Intent().putExtra("busIntroduce",
					ed_introduce.getText().toString()));
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			UIUtil.hideSoftKeyBoard(this, getCurrentFocus());
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
