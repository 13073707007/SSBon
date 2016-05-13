package com.loongme.activity.adapter;

import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.loongme.activity.R;
import com.loongme.activity.api.DeleteServerSucessCallBack;
import com.loongme.activity.bean.MyServer;
import com.loongme.activity.business.Helpers;
import com.loongme.activity.enums.SSBonServerStatus;
import com.loongme.activity.utils.StringUtils;
import com.loongme.activity.utils.UIUtil;
import com.loongme.activity.widgets.CustomProgressDialog;

public class MyAdapterLL extends BaseAdapter {
	private List<MyServer> lists;
	private Context context;
	private CustomProgressDialog progressDialog;
	private DeleteServerSucessCallBack callBack;

	public MyAdapterLL(List<MyServer> lists, Context context, DeleteServerSucessCallBack callBack) {
		super();
		this.lists = lists;
		this.context = context;
		this.callBack = callBack;
		progressDialog = new CustomProgressDialog(context, "加载中...", R.anim.loading);
	}

	@Override
	public int getCount() {
		return lists.size();
	}

	@Override
	public Object getItem(int arg0) {
		return lists.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item2, null);
			holder = new ViewHolder();
			holder.image_photo = (ImageView) convertView.findViewById(R.id.iv1);
			holder.txt_title = (TextView) convertView.findViewById(R.id.tv1);
			holder.txt_status = (TextView) convertView.findViewById(R.id.tv5);
			holder.txt_statusDes = (TextView) convertView.findViewById(R.id.tv4);
			holder.txt_time = (TextView) convertView.findViewById(R.id.tv2);
			holder.btn_delete = (Button) convertView.findViewById(R.id.bt_delete);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (lists.size() != 0) {
			final MyServer myServer = lists.get(position);
			// 头像
			// if
			// (StringUtils.isEmpty(Helpers.getUserInfo(context).getUserPicture()))
			// {
			// holder.image_photo.setImageResource(R.drawable.dtx);
			// } else {
			// ImageLoader.getInstance().displayImage(Helpers.getUserInfo(context).getUserPicture(),
			// holder.image_photo);
			// }
			holder.txt_title.setText(myServer.getProvideTitle());
			holder.txt_status.setText(SSBonServerStatus.valueByCode(myServer.getProvideStatus()));
			holder.txt_time.setText("发布于:  " + myServer.getProvideData().substring(0, 10));
			if (StringUtils.isEmpty(myServer.getProvideRefusedesc())) {
				holder.txt_statusDes.setText(SSBonServerStatus.valueByCode(myServer.getProvideStatus()));
			} else {
				holder.txt_statusDes.setText(myServer.getProvideRefusedesc());
			}
			holder.btn_delete.setVisibility(View.GONE);
			holder.btn_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					deleteMyServer(myServer);
				}
			});
		}
		return convertView;
	}

	class ViewHolder {
		public ImageView image_photo;
		public TextView txt_title;
		public TextView txt_status;
		public TextView txt_statusDes;
		public TextView txt_time;
		public Button btn_delete;

	}

	private void deleteMyServer(MyServer myServer) {
		String url = "http://120.24.37.141:8080/LoginAndResigister/ProvideInfoServlet";
		HttpUtils httpUtils = new HttpUtils(10000);
		RequestParams params = new RequestParams("UTF-8");
		JSONObject object = new JSONObject();
		try {
			object.put("provideId", myServer.getProvideId());
			object.put("userId", Helpers.getBonkeStatusFromLocal(context));
		} catch (Exception e) {
			e.printStackTrace();
		}
		params.addBodyParameter("requestMessage", object.toString());
		progressDialog.show();
		httpUtils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				progressDialog.dismiss();
				UIUtil.showMsg(context, "删除失败，请重试");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				progressDialog.dismiss();
				if (arg0.result == null) {
					UIUtil.showMsg(context, "删除失败，请重试！");
				}
				try {
					if (arg0.statusCode == 200) {
						JSONObject object = new JSONObject(arg0.result);
						if (object.getBoolean("status")) {
							UIUtil.showMsg(context, "删除成功");
							callBack.onDeleteServerSucess();
						} else {
							UIUtil.showMsg(context, "删除失败！");
						}
					} else {
						UIUtil.showMsg(context, "删除失败，请重试");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

}
