package com.loongme.activity.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.loongme.activity.R;
import com.loongme.activity.adapter.MyAdapterLL;
import com.loongme.activity.api.DeleteServerSucessCallBack;
import com.loongme.activity.base.BaseActivity;
import com.loongme.activity.bean.Data2;
import com.loongme.activity.bean.MyServer;
import com.loongme.activity.business.Helpers;
import com.loongme.activity.utils.UIUtil;

public class ActivityMyServe extends BaseActivity implements OnClickListener, DeleteServerSucessCallBack {
	private ListView lvserve;
	private List<Data2> lists;
	private MyAdapterLL adapter;
	private ImageButton btn_goback;
	private List<MyServer> datas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_serve);
		lvserve = (ListView) findViewById(R.id.lvserve);
		datas = new ArrayList<MyServer>();
		adapter = new MyAdapterLL(datas, this, this);
		lvserve.setAdapter(adapter);
		initView();
		parseHttpRequest();

		lvserve.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			}
		});
	}

	private void initView() {
		btn_goback = (ImageButton) findViewById(R.id.btn_goback);
		btn_goback.setOnClickListener(this);
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
	}

	private void parseHttpRequest() {
		String url = "http://120.24.37.141:8080/LoginAndResigister/ProvideInfoServlet";
		HttpUtils httpUtils = new HttpUtils(10000);
		RequestParams params = new RequestParams("UTF-8");
		JSONObject object = new JSONObject();
		try {
			object.put("userId", Helpers.getUserInfo(this).getUserId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		params.addBodyParameter("requestMessage", object.toString());
		httpUtils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				UIUtil.showMsg(ActivityMyServe.this, "请求数据失败，请重试");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if (arg0.result == null) {
					UIUtil.showMsg(ActivityMyServe.this, "请求数据失败，请重试！");
				}
				try {
					if (arg0.statusCode == 200) {
						JSONObject object = new JSONObject(arg0.result);
						if (object.getInt("status") != 1) {
							UIUtil.showMsg(ActivityMyServe.this, "请求数据失败");
							return;
						}
						List<MyServer> list = JSON.parseArray(object.getString("provideMsg"), MyServer.class);
						if (datas.size() != 0) {
							datas.clear();
						}
						datas.addAll(list);
						adapter.notifyDataSetChanged();

					} else {
						UIUtil.showMsg(ActivityMyServe.this, "请求数据失败，请重试");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	@Override
	public void onDeleteServerSucess() {
		adapter.notifyDataSetChanged();
	}

}
