package com.loongme.activity.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.sina.weibo.SinaWeibo.ShareParams;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.tencent.weibo.TencentWeibo;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.loongme.activity.R;
import com.loongme.activity.base.BaseActivity;
import com.mob.tools.utils.UIHandler;
/**
 * 
 * @author xywy
 * @version 类说明 :推荐好友
 */
public class ActivityRecommendFriend extends BaseActivity implements PlatformActionListener, Callback {

	private ImageButton btn_goback;
	ImageView weixin,qq,pengyou,qzone,sina,tencen;
	private static final int MSG_TOAST = 1;
	private static final int MSG_ACTION_CCALLBACK = 2;
	private static final int MSG_CANCEL_NOTIFY = 3;
	private static final String FILE_NAME = "/share_pic.jpg";
	public static String TEST_IMAGE;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initParas() {
		ShareSDK.initSDK(this);
		super.initParas();
	}

	@Override
	protected void initWidgets() {
		setContentView(R.layout.activity_recommend);
		btn_goback = (ImageButton) findViewById(R.id.btn_goback);
		weixin = (ImageView) findViewById(R.id.weixin);
		qq = (ImageView) findViewById(R.id.qq);
		pengyou = (ImageView) findViewById(R.id.pengyou);
		qzone = (ImageView) findViewById(R.id.qzone);
		sina = (ImageView) findViewById(R.id.sina);
		tencen = (ImageView) findViewById(R.id.tencen);
		super.initWidgets();
	}

	@Override
	protected void initUI() {
		super.initUI();
	}

	@Override
	protected void regListener() {
		btn_goback.setOnClickListener(this);
		weixin.setOnClickListener(this);
		qq.setOnClickListener(this);
		pengyou.setOnClickListener(this);
		qzone.setOnClickListener(this);
		sina.setOnClickListener(this);
		tencen.setOnClickListener(this);
		super.regListener();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_goback:
			finish();
			break;
		case R.id.weixin:
			cn.sharesdk.wechat.friends.Wechat.ShareParams shp = new cn.sharesdk.wechat.friends.Wechat.ShareParams();
			shp.setTitle("推荐事事帮");
			shp.setText("事事帮语音助手");
			shp.shareType = Platform.SHARE_WEBPAGE;
			shp.url = "http://60.206.195.26/dd.myapp.com/16891/ECAFB3071769470F2D07ACAD7D2405FE.apk?mkey=572cb7b425b4e989&f=d388&c=0&fsname=com.loongme.activity_3.0.0_20160505.apk&p=.apk";
			shp.imageData = BitmapFactory.decodeResource(v.getResources(), R.drawable.icon_tubiao);
			Platform wx = ShareSDK.getPlatform(Wechat.NAME);
			wx.setPlatformActionListener(this);
			wx.share(shp);
			break;
		case R.id.qq:
			cn.sharesdk.wechat.friends.Wechat.ShareParams spqq = new cn.sharesdk.wechat.friends.Wechat.ShareParams();
			spqq.setText("http://60.206.195.26/dd.myapp.com/16891/ECAFB3071769470F2D07ACAD7D2405FE.apk?mkey=572cb7b425b4e989&f=d388&c=0&fsname=com.loongme.activity_3.0.0_20160505.apk&p=.apk");
			Platform qq = ShareSDK.getPlatform(QQ.NAME);
			qq.setPlatformActionListener(this);
			qq.share(spqq);
			break;
		case R.id.pengyou:
			cn.sharesdk.wechat.friends.Wechat.ShareParams sppy = new cn.sharesdk.wechat.friends.Wechat.ShareParams();
			sppy.setTitle("推荐事事帮");
			sppy.setShareType(Platform.SHARE_WEBPAGE);
			sppy.setUrl("http://60.206.195.26/dd.myapp.com/16891/ECAFB3071769470F2D07ACAD7D2405FE.apk?mkey=572cb7b425b4e989&f=d388&c=0&fsname=com.loongme.activity_3.0.0_20160505.apk&p=.apk");
			sppy.setText("事事帮语音助手");
			sppy.imageData = BitmapFactory.decodeResource(v.getResources(), R.drawable.icon_tubiao);
			Platform py = ShareSDK.getPlatform(WechatMoments.NAME);
			py.setPlatformActionListener(this);
			py.share(sppy);
			break;
		case R.id.qzone:
			cn.sharesdk.wechat.friends.Wechat.ShareParams spkj = new cn.sharesdk.wechat.friends.Wechat.ShareParams();
			spkj.setText("推荐事事帮:http://60.206.195.26/dd.myapp.com/16891/ECAFB3071769470F2D07ACAD7D2405FE.apk?mkey=572cb7b425b4e989&f=d388&c=0&fsname=com.loongme.activity_3.0.0_20160505.apk&p=.apk");
			Platform kj = ShareSDK.getPlatform(QZone.NAME);
			kj.setPlatformActionListener(this);
			kj.share(spkj);
			break;
		case R.id.sina:
			ShareParams sp = new ShareParams();
			sp.setText("事事帮语音助手:http://60.206.195.26/dd.myapp.com/16891/ECAFB3071769470F2D07ACAD7D2405FE.apk?mkey=572cb7b425b4e989&f=d388&c=0&fsname=com.loongme.activity_3.0.0_20160505.apk&p=.apk");
			sp.setImagePath(TEST_IMAGE);
			Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
			weibo.setPlatformActionListener(this);
			weibo.share(sp);
			break;
		case R.id.tencen:
			cn.sharesdk.tencent.weibo.TencentWeibo.ShareParams shareParams = new cn.sharesdk.tencent.weibo.TencentWeibo.ShareParams();
			shareParams
					.setText("推荐事事帮:http://60.206.195.26/dd.myapp.com/16891/ECAFB3071769470F2D07ACAD7D2405FE.apk?mkey=572cb7b425b4e989&f=d388&c=0&fsname=com.loongme.activity_3.0.0_20160505.apk&p=.apk");
			Platform tx = ShareSDK.getPlatform(TencentWeibo.NAME);
			tx.setPlatformActionListener(this);
			tx.share(shareParams);
			break;

		default:
			break;
		}
		new Thread() {
			public void run() {
				initImagePath();
			};
		}.start();
		super.onClick(v);
	}

	/*
	 * 设置监听 监听是子线程 不能Toast，要用handler处理 (non-Javadoc)
	 * 
	 * @see
	 * cn.sharesdk.framework.PlatformActionListener#onCancel(cn.sharesdk.framework
	 * .Platform, int)
	 */
	@Override
	public void onCancel(Platform platform, int action) {
		// TODO Auto-generated method stub
		// 取消方法
		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 3;
		msg.arg2 = action;
		msg.obj = platform;
		UIHandler.sendMessage(msg, this);
	}

	@Override
	public void onComplete(Platform platform, int action, HashMap<String, Object> arg2) {
		// TODO Auto-generated method stub
		// 成功方法
		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 1;
		msg.arg2 = action;
		msg.obj = platform;
		UIHandler.sendMessage(msg, this);
	}

	@Override
	public void onError(Platform platform, int action, Throwable arg2) {
		// TODO Auto-generated method stub
		// 失败方法
		arg2.printStackTrace();
		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 2;
		msg.arg2 = action;
		msg.obj = arg2;
		UIHandler.sendMessage(msg, this);
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		switch (msg.what) {
		case MSG_TOAST: {
			String text = String.valueOf(msg.obj);
			Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
		}

			break;
		case MSG_ACTION_CCALLBACK: {
			switch (msg.arg1) {
			case 1: {// 成功
				showNotification(2000, getString(R.string.ssdk_oks_share_completed));
				// 删除授权
				// if (weibo.isValid()) {
				// weibo.removeAccount();
				// }
			}
				break;
			case 2: {// 失败
				String expName = msg.obj.getClass().getSimpleName();
				if ("WechatClientNotExistException".equals(expName)
						|| "WechatTimelineNotSupportedException".equals(expName)) {
					showNotification(2000, getString(R.string.ssdk_wechat_client_inavailable));
				} else if ("GooglePlusClientNotExistException".equals(expName)) {
					showNotification(2000, getString(R.string.ssdk_google_plus_client_inavailable));
				} else if ("QQClientNotExistException".equals(expName)) {
					showNotification(2000, getString(R.string.ssdk_qq_client_inavailable));
				} else {
					showNotification(2000, getString(R.string.ssdk_oks_share_failed));
				}
			}
				break;
			case 3: {// 取消
				showNotification(2000, getString(R.string.ssdk_oks_share_canceled));
			}
				break;

			default:
				break;
			}
		}

			break;
		case MSG_CANCEL_NOTIFY: {
			NotificationManager nm = (NotificationManager) msg.obj;
			if (nm != null) {
				nm.cancel(msg.arg1);
			}
		}
			break;

		}
		return false;
	}

	// 在状态栏显示分享操作
	protected void showNotification(long cancelTime, String text) {
		try {
			Context app = getApplicationContext();
			NotificationManager nm = (NotificationManager) app.getSystemService(Context.NOTIFICATION_SERVICE);
			final int id = Integer.MAX_VALUE / 13 + 1;
			nm.cancel(id);

			long when = System.currentTimeMillis();
			Notification notification = new Notification(R.drawable.icon_tubiao, text, when);
			PendingIntent pi = PendingIntent.getActivity(app, 0, new Intent(), 0);
			notification.setLatestEventInfo(app, "sharesdk test", text, pi);
			notification.flags = Notification.FLAG_AUTO_CANCEL;
			nm.notify(id, notification);

			if (cancelTime > 0) {
				Message msg = new Message();
				msg.what = MSG_CANCEL_NOTIFY;
				msg.obj = nm;
				msg.arg1 = id;
				UIHandler.sendMessageDelayed(msg, cancelTime, this);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	// 把图片从drawable复制到sdcard中
	// copy the picture from the drawable to sdcard
	private void initImagePath() {
		try {
			if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
					&& Environment.getExternalStorageDirectory().exists()) {
				TEST_IMAGE = Environment.getExternalStorageDirectory().getAbsolutePath() + FILE_NAME;
			} else {
				TEST_IMAGE = getApplication().getFilesDir().getAbsolutePath() + FILE_NAME;
			}
			File file = new File(TEST_IMAGE);
			if (!file.exists()) {
				file.createNewFile();
				Bitmap pic = BitmapFactory.decodeResource(getResources(), R.drawable.icon_tubiao);
				FileOutputStream fos = new FileOutputStream(file);
				pic.compress(CompressFormat.JPEG, 100, fos);
				fos.flush();
				fos.close();
			}
		} catch (Throwable t) {
			t.printStackTrace();
			TEST_IMAGE = null;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		ShareSDK.stopSDK(this);
	}
}
