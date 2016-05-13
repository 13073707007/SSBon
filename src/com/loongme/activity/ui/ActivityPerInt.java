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
public class ActivityPerInt extends BaseActivity implements OnClickListener {
	ImageView iv_fanhupi;
	Button btn_grjs_send;
	private EditText ed_introduce;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_per_int);
		initView();
	}

	private void initView() {
		iv_fanhupi = (ImageView) findViewById(R.id.iv_fanhupi);
		btn_grjs_send = (Button) findViewById(R.id.btn_grjs_send);
		ed_introduce = (EditText) findViewById(R.id.ed_introduce);
		iv_fanhupi.setOnClickListener(this);
		btn_grjs_send.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_fanhupi:
			finish();
			break;
		case R.id.btn_grjs_send:
			String intoduce = ed_introduce.getText().toString();
			setResult(RESULT_OK,
					new Intent().putExtra("persIntroduce", intoduce));
			UIUtil.hideSoftKeyBoard(this, getCurrentFocus());
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			UIUtil.hideSoftKeyBoard(this, getCurrentFocus());
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_right_out);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
