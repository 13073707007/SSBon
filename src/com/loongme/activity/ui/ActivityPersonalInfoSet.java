package com.loongme.activity.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.loongme.activity.R;
import com.loongme.activity.base.ApiClient;
import com.loongme.activity.base.BaseActivity;
import com.loongme.activity.bean.User;
import com.loongme.activity.business.Helpers;
import com.loongme.activity.common.Configuration;
import com.loongme.activity.common.ResultObj;
import com.loongme.activity.enums.UserType;
import com.loongme.activity.utils.StringUtils;
import com.loongme.activity.utils.UIUtil;
import com.loongme.activity.widgets.cropimg.CutAvatarActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author 作者:zhaojianyu
 * @version 创建时间：2016-5-9 下午6:01:01 类说明:修改个人资料
 */
public class ActivityPersonalInfoSet extends BaseActivity {

	private ImageButton btn_goback;
	// 头像
	private ImageView img_userPhoto;
	private View view_setPhoto;
	// 昵称
	private TextView tx_nicheng;
	private View view_setNicheng;
	// 性别
	private TextView tx_sex;
	private View view_setsex;
	// 手机
	private TextView tx_phone;

	// 个性宣言
	private TextView tx_gexuan;
	private View view_setgexuan;

	// 帮客认证状态
	private ImageView img_notice;
	private TextView tx_bonkeStatus;

	private User user;
	private boolean isInfoReset = false;// 信息是否更改
	private Dialog choosePicDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		showProgress();
		initPOST();
	}

	private void initPOST() {
		HttpUtils utlis = new HttpUtils(5000);
		RequestParams param = new RequestParams();
		JSONObject obj = new JSONObject();
		try {
			obj.put("userId", user.getUserId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		param.addQueryStringParameter("requestMessage", obj.toString());
		utlis.send(HttpMethod.POST, Configuration.SERVER_HOST_PREFIX + Configuration.URL_GETUSERINFO, param,
				new RequestCallBack<String>() {

					private String nickname, iphone, sex, persDeclaration;

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						UIUtil.showMsg(ActivityPersonalInfoSet.this, "获取用户信息失败");
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						if (arg0.statusCode == 200) {
							dismissProgress();
							com.alibaba.fastjson.JSONObject jsonBean = JSON.parseObject(arg0.result);
							nickname = jsonBean.getString("userNickname");
							iphone = jsonBean.getString("userPhone");
							sex = jsonBean.getString("userSex");
							persDeclaration = jsonBean.getString("declaration");
							tx_nicheng.setText(nickname);
							tx_sex.setText(sex);
							tx_phone.setText(iphone);
							tx_gexuan.setText(persDeclaration);

						}
					}
				});
	}

	@Override
	protected void initParas() {
		user = Helpers.getUserInfo(this);
		super.initParas();
	}

	@Override
	protected void initWidgets() {
		setContentView(R.layout.activity_personalinfo_set);
		btn_goback = (ImageButton) findViewById(R.id.btn_goback);
		img_userPhoto = (ImageView) findViewById(R.id.img_userPhoto);
		view_setPhoto = (View) findViewById(R.id.view_setphoto);
		tx_nicheng = (TextView) findViewById(R.id.tx_nicheng);
		view_setNicheng = (View) findViewById(R.id.view_setnicheng);
		tx_sex = (TextView) findViewById(R.id.tx_sex);
		view_setsex = (View) findViewById(R.id.view_setsex);
		tx_phone = (TextView) findViewById(R.id.tx_phone);
		tx_gexuan = (TextView) findViewById(R.id.tx_gexuan);
		view_setgexuan = (View) findViewById(R.id.view_setgexuan);
		img_notice = (ImageView) findViewById(R.id.img_yrz);
		tx_bonkeStatus = (TextView) findViewById(R.id.tx_bonke_status);
		choosePicDialog = UIUtil.getChoosePicDialog(this, new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismissChoosePicDialog();
				// 拍照
				File file = new File(Configuration.PHOTO_SAVEPATH);
				if (!file.exists()) {
					try {
						file.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
				intent.putExtra("isFrom", Configuration.REQUESTCODE_TAKEPHOTOS);
				startActivityForResult(intent, Configuration.REQUESTCODE_TAKEPHOTOS);

			}
		}, new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismissChoosePicDialog();
				// 选取相册
				// Intent intent = new Intent(Intent.ACTION_PICK,
				// android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				// startActivityForResult(Intent.createChooser(intent,
				// "Select Picture"),
				// Configuration.REQUESTCODE_SELECTPICS);

				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(intent, Configuration.REQUESTCODE_SELECTPICS);
			}
		}, new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 取消
				dismissChoosePicDialog();

			}
		});
		super.initWidgets();
	}

	@Override
	protected void regListener() {
		btn_goback.setOnClickListener(this);
		view_setPhoto.setOnClickListener(this);
		view_setNicheng.setOnClickListener(this);
		view_setsex.setOnClickListener(this);
		view_setgexuan.setOnClickListener(this);
		super.regListener();
	}

	@Override
	protected void initUI() {
		if (!StringUtils.isEmpty(user.getUserPicture())) {
			ImageLoader.getInstance().displayImage(user.getUserPicture(), img_userPhoto);
		}
		tx_nicheng.setText(user.getUserNickname());
		tx_sex.setText(user.getUserSex());
		tx_phone.setText(user.getUserPhone());
		tx_gexuan.setText(user.getDeclaration());
		if (!StringUtils.isEmpty(user.getUserType())) {
			if (user.getUserType().equals(UserType.Normal.getVaule()) || user.getUserType().equals("普通")) {
				img_notice.setImageDrawable(getResources().getDrawable(R.drawable.wrz));
				tx_bonkeStatus.setText("未认证");
				tx_bonkeStatus.setTextColor(getResources().getColor(R.color.gray));
			} else {
				img_notice.setImageDrawable(getResources().getDrawable(R.drawable.yrz));
				tx_bonkeStatus.setText("认证通过");
				tx_bonkeStatus.setTextColor(getResources().getColor(R.color.appcolor));
			}

		}
		super.initUI();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_goback:
			if (isInfoReset) {
				setResult(RESULT_OK);
				finish();
			} else {
				finish();
			}
			break;
		case R.id.view_setphoto:
			showChoosePicDialog();
			break;
		case R.id.view_setnicheng:// 修改昵称
			UIUtil.redirect(this, ActivityNickName.class);
			break;
		case R.id.view_setsex:// 修改性别
			UIUtil.redirect(this, ActivitySex.class);
			break;
		case R.id.view_setgexuan:// 个性宣言
			UIUtil.redirect(this, ActivityManifesto.class);
			break;

		default:
			break;
		}
		super.onClick(v);
	}

	private void showChoosePicDialog() {
		if (choosePicDialog != null) {
			choosePicDialog.show();
		}
	}

	private void dismissChoosePicDialog() {
		if (choosePicDialog != null && choosePicDialog.isShowing()) {
			choosePicDialog.dismiss();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}
		if (requestCode == Configuration.REQUESTCODE_TAKEPHOTOS) {
			Intent intent0 = new Intent(this, CutAvatarActivity.class);
			intent0.putExtra("isFrom", Configuration.REQUESTCODE_TAKEPHOTOS);
			startActivityForResult(intent0, Configuration.REQUESTCODE_CROPPHOTOS);
		}
		if (requestCode == Configuration.REQUESTCODE_SELECTPICS) {
			if (data != null) {
				Uri uri = data.getData();
				Intent intent1 = new Intent(this, CutAvatarActivity.class);
				intent1.putExtra("imagePath", uri.toString());
				intent1.putExtra("isFrom", Configuration.REQUESTCODE_SELECTPICS);
				startActivityForResult(intent1, Configuration.REQUESTCODE_CROPPHOTOS);
			}
		}
		if (requestCode == Configuration.REQUESTCODE_CROPPHOTOS) {
			// 上传头像
			showProgress();
			UploadBitmap();
		}

	}

	private void parseHttpParams(int requestFlag) {
		if (requestFlag == Configuration.serv_flag.RequstFlag_GetUserInfo) {
			JSONObject object = new JSONObject();
			try {
				object.put("userId", user.getUserId());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			String params = "requestMessage=" + object.toString();
			ApiClient.getInstance(this).load(Configuration.SERVER_HOST_PREFIX + Configuration.URL_GETUSERINFO, params,
					Configuration.serv_flag.RequstFlag_GetUserInfo, this);
			dismissProgress();
		}
	}

	private void UploadBitmap() {

		HttpUtils httpUtils = new HttpUtils(5000);
		RequestParams params = new RequestParams();
		JSONObject object = new JSONObject();
		try {
			object.put("userId", Helpers.getUserInfo(this).getUserId());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		params.addBodyParameter("image0.png", "");
		try {
			params.addBodyParameter("image0.png", new FileInputStream(Configuration.PHOTO_SAVEPATH), new File(
					Configuration.PHOTO_SAVEPATH).length());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		params.addQueryStringParameter("requestMessage", object.toString());
		httpUtils.send(HttpMethod.POST, Configuration.SERVER_HOST_PREFIX + Configuration.URL_UPLOAD_PHOTO, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgress();
						UIUtil.showMsg(ActivityPersonalInfoSet.this, "头像上传失败");
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						dismissProgress();
						if (arg0.statusCode == 200) {
							com.alibaba.fastjson.JSONObject json = JSON.parseObject(arg0.result);
							boolean status = json.getBoolean("status");
							if (status == true) {
								UIUtil.showMsg(ActivityPersonalInfoSet.this, "头像上传成功");
								parseHttpParams(Configuration.serv_flag.RequstFlag_GetUserInfo);
								isInfoReset = true;
								File file = new File(Configuration.PHOTO_SAVEPATH);
								if (file.exists()) {
									file.delete();
								}
							}

						}
					}
				});
	}

	@Override
	public boolean handleSuccess(int statusCode, JSONObject response, int flagRequest) {
		if (statusCode == ResultObj.ERRCODE_STATE_OK) {
			if (StringUtils.isEmpty(response.toString())) {
				return false;
			}
			User user = com.alibaba.fastjson.JSONObject.parseObject(response.toString(), User.class);
			Helpers.saveUserInfoToLocal(this, user);
			ImageLoader.getInstance().displayImage(user.getUserPicture(), img_userPhoto);
		} else {
		}
		return super.handleSuccess(statusCode, response, flagRequest);
	}

	@Override
	public boolean handleFailure(int statusCode, String responseString, Throwable throwable, int flagRequest) {
		return super.handleFailure(statusCode, responseString, throwable, flagRequest);
	}
}
