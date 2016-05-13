package com.loongme.activity.ui;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import com.loongme.activity.R;
import com.loongme.activity.base.BaseActivity;

/**
 * @author 作者:zhaojianyu
 * @version 创建时间：2016-5-6 上午11:52:56 类说明:
 */
@SuppressLint("SetJavaScriptEnabled")
public class ActivityShowMoreInfo extends BaseActivity {

	private String url;
	private WebView webView;
	private ImageButton btn_goback;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initParas() {
		url = getIntent().getStringExtra("url");
		super.initParas();
	}

	@Override
	protected void initWidgets() {
		setContentView(R.layout.activity_showmoreinfo);
		btn_goback = (ImageButton) findViewById(R.id.btn_goback);
		webView = (WebView) findViewById(R.id.web_detail);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		super.initWidgets();
	}

	@Override
	protected void regListener() {
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				showProgress();
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				dismissProgress();
				super.onPageFinished(view, url);
			}

		});

		btn_goback.setOnClickListener(this);
		super.regListener();
	}

	@Override
	protected void initUI() {
		webView.loadUrl(url);
		super.initUI();
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (webView.canGoBack()) {
				webView.goBack();
			} else {
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
