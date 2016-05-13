package com.loongme.activity.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loongme.activity.R;
import com.loongme.activity.base.AppManager;
import com.loongme.activity.common.Configuration;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

public class UIUtil {

	/**
	 * 对小屏手机进行屏幕判断
	 * 
	 * @param context
	 * @return 返回宽度或高度中较小的值
	 * 
	 */
	public static int getDisplaySize(Context context) {
		int displayFlag = 480;
		int display_width = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
		int display_height = ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();
		try {

			if (display_width < display_height) {
				if (display_width == 320) {
					displayFlag = 320;
				} else if (display_width < 320) {
					displayFlag = 240;
				} else if (display_width >= 720 && display_width < 1080) {
					displayFlag = 720;
				} else if (display_width >= 1080) {
					displayFlag = 1080;
				}
			} else {
				if (display_height == 320) {
					displayFlag = 320;
				} else if (display_height < 320) {
					displayFlag = 240;
				} else if (display_height >= 720 && display_height < 1080) {
					displayFlag = 720;
				} else if (display_height >= 1080) {
					displayFlag = 1080;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return displayFlag;
	}

	/**
	 * 获取加载等待对话框
	 * 
	 * @param context
	 * @return
	 */
	public static Dialog getProgressDialog(Context context, String text) {
		Dialog dialog = new Dialog(context, R.style.ProgressDialog);
		LinearLayout outLayout = new LinearLayout(context);
		outLayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		outLayout.setOrientation(LinearLayout.HORIZONTAL);
		outLayout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.corners_wait));

		// 等待进度条
		ProgressBar progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleLarge);
		LinearLayout.LayoutParams progressBarlp = new LinearLayout.LayoutParams(new LayoutParams(80, 80));
		progressBarlp.gravity = Gravity.CENTER_VERTICAL;
		progressBar.setPadding(5, 5, 5, 5);
		progressBar.setLayoutParams(progressBarlp);

		// 提示信息
		TextView textView = new TextView(context);
		textView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
		textView.setText(text);
		textView.setTextColor(Color.parseColor("#FFFFFF"));
		textView.setTextSize(12);
		textView.setGravity(Gravity.CENTER_VERTICAL);
		outLayout.addView(progressBar);
		outLayout.addView(textView);
		dialog.setContentView(outLayout);
		return dialog;

	}

	public static void redirect(Context context, Class<?> targetActivity) {
		redirect(context, targetActivity, 0, null);
	}

	public static void redirect(Context context, Class<?> targetActivity, boolean isClearTop) {
		if (isClearTop) {
			redirect(context, targetActivity, Intent.FLAG_ACTIVITY_CLEAR_TOP, null);
		} else {
			redirect(context, targetActivity, 0, null);
		}
	}

	public static void redirect(Context context, Class<?> targetActivity, int flags) {
		redirect(context, targetActivity, flags, null);
	}

	public static void redirect(Context context, Class<?> targetActivity, Bundle extras) {
		redirect(context, targetActivity, 0, extras);
	}

	public static void redirect(Context context, Class<?> targetActivityClass, int flags, Bundle extras) {
		Intent intent = new Intent();
		intent.setClass(context, targetActivityClass);
		if (flags != 0) {
			intent.setFlags(flags);
		}
		if (null != extras) {
			intent.putExtras(extras);
		}
		context.startActivity(intent);
		if (context instanceof Activity) {
			Activity act = (Activity) context;
			act.overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
		}
	}

	/**
	 * UUID生成器，32位长
	 * 
	 * @return
	 */
	public static String getUUID() {
		String s = UUID.randomUUID().toString();
		// 去掉“-”符号
		return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18) + s.substring(19, 23) + s.substring(24);
	}

	public static DecimalFormat getDecimalFormat() {
		DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
		df.applyPattern("##.#");
		return df;
	}

	public static void showMsg(Context context, int msg) {
		showMsg(context, msg, Toast.LENGTH_SHORT);
	}

	public static void showMsg(Context context, int msg, int time) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 显示文字
	 * 
	 * @param context
	 * @param msg
	 */
	public static void showMsg(Context context, String msg) {
		if (context != null) {
			Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
		}
	}

	public static void showMsg(Context context, String msg, int time) {
		Toast.makeText(context, msg, time).show();
	}

	/**
	 * 在UI线程运行弹出
	 **/
	public static void showMsgOnUiThread(final Activity ctx, final String text) {
		if (ctx != null) {
			ctx.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					showMsg(ctx, text);
				}
			});
		}
	}

	/**
	 * 打电话
	 * 
	 * @param context
	 * @param mobile
	 */
	public static void dial(Context context, String mobile) {
		/* 建构一个新的Intent并执行action.CALL的常数与透过Uri将字符串带入 */
		Intent myIntentDial = new Intent("android.intent.action.CALL", Uri.parse("tel:" + mobile));
		/* 在startActivity()方法中带入自定义的Intent对象以执行拨打电话的工作 */
		context.startActivity(myIntentDial);
	}

	/**
	 * 拨打电话，先弹出对话框
	 * 
	 * @param context
	 * @param mobile
	 */
	public static void tryToDial(final Context context, final String mobile) {
		if (TextUtils.isEmpty(mobile)) {
			UIUtil.showMsg(context, "电话号码内容为空");
		} else {
			if (TextUtils.isDigitsOnly(mobile)) {
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setMessage("要给 " + mobile + " 打电话？");
				builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						try {
							UIUtil.dial(context, mobile);
						} catch (Exception e) {
							e.printStackTrace();
							UIUtil.showMsg(context, "很抱歉出现异常，异常信息为" + e.getMessage());
							Log.v(Configuration.TAG, "打电话出错", e);
						}
					}
				});
				builder.setNegativeButton("否", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				builder.create().show();
			} else {
				UIUtil.showMsg(context, "号码格式不正确，应该为纯数字");
			}
		}
	}

	/**
	 * 直接发短信
	 * 
	 * @param mContext
	 * @param phonenumber
	 * @param msg
	 */
	public static void sendSMS(String phonenumber, String msg) {
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phonenumber, null, msg, null, null);// 发送信息到指定号码
	}

	public static void sendSMS(String[] phonenumbers, String msg) {
		SmsManager sms = SmsManager.getDefault();

		sms.sendTextMessage(formatMobiles(phonenumbers), null, msg, null, null);// 发送信息到指定号码
	}

	public static String formatMobiles(String[] phonenumbers) {
		StringBuffer sb = new StringBuffer();
		String mobiles = "";
		for (String s : phonenumbers) {
			sb.append(s).append(",");
		}
		if (sb.toString().indexOf(",") == -1) {
			mobiles = sb.toString();
		} else {
			mobiles = sb.toString().substring(0, sb.lastIndexOf(","));
		}
		return mobiles;
	}

	/**
	 * 调用短信界面，由客户发送短信
	 * 
	 * @param context
	 * @param phonenumber
	 * @param msg
	 */
	public static void sendSmsUi(Context context, String phonenumber, String msg) {
		Uri smsToUri = Uri.parse("smsto:" + phonenumber);
		Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
		intent.putExtra("sms_body", msg);
		context.startActivity(intent);
	}

	public static void sendSmsUi(Context context, String[] phonenumbers, String msg) {
		Uri smsToUri = Uri.parse("smsto:" + formatMobiles(phonenumbers));
		Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
		intent.putExtra("sms_body", msg);
		context.startActivity(intent);
	}

	public static void confirm(Context context, String message, DialogInterface.OnClickListener confirmHandler) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message);
		builder.setPositiveButton("确定", confirmHandler);
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}

		});
		builder.create().show();
	}

	public static void confirm(Context context, String message, DialogInterface.OnClickListener confirmHandler,
			String positiveTitle, String negativeTitle) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message);
		builder.setPositiveButton(positiveTitle, confirmHandler);
		builder.setNegativeButton(negativeTitle, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.create().show();
	}

	public static void alert(Context context, String message, DialogInterface.OnClickListener confirmHandler) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message);
		builder.setNegativeButton("取消", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (builder.create().isShowing()) {
					builder.create().dismiss();
				}
			}
		});
		builder.setPositiveButton("确定", confirmHandler);
		builder.create().show();
	}

	/**
	 * 显示退出对话框
	 * 
	 * @param mContext
	 *            void
	 */
	public static void showExitSystemDialog(final Activity activity) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setMessage("您确定要退出吗？ ");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Log.v(UIUtil.LOG_TAG, "Do Exit MMC... PID: " +
				// android.os.Process.myPid());
				// activity.finish();
				// android.os.Process.killProcess(android.os.Process.myPid());
				AppManager.getAppManager().AppExit(activity);

			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.create().show();
	}

	public static double tryToDouble(String str) {
		try {
			return Double.parseDouble(str);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	public static long tryToLong(String str) {
		try {
			return Long.parseLong(str);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	public static int tryToInteger(String str) {
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	public static void toggleShowView(View view) {
		if (view.getVisibility() == View.GONE || view.getVisibility() == View.INVISIBLE) {
			showView(view);
		} else {
			hideView(view);
		}
	}

	public static void showView(View view) {
		// Animation animation = new AlphaAnimation(0, 1.0f);
		// animation.setDuration(500);
		// view.setAnimation(animation);
		view.setVisibility(View.VISIBLE);
	}

	public static void hideView(View view) {
		// Animation animation = new AlphaAnimation(0, 1.0f);
		// animation.setDuration(500);
		// view.setAnimation(animation);
		view.setVisibility(View.GONE);
	}

	/**
	 * DIP转PX
	 * 
	 * @param context
	 * @param dipValue
	 * @return int
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * PX转DIP
	 * 
	 * @param context
	 * @param pxValue
	 * @return int
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 判断字符串首字符是否为字母
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isLetter(String s) {
		char c = s.charAt(0);
		return isLetter(c);
	}

	/**
	 * 判断字符是否为字母
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isLetter(char c) {
		int i = (int) c;
		if ((i >= 65 && i <= 90) || (i >= 97 && i <= 122)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 验证手机格式
	 */
	public static boolean isMobileNO(String mobiles) {
		/*
		 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
		String telRegex = "[1][3578]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
		if (TextUtils.isEmpty(mobiles)) {
			return false;
		} else {
			return mobiles.matches(telRegex);
		}
	}

	private static long lastClickTime;

	/**
	 * 防止按钮连续点击，按钮在500毫秒内不能同时起效
	 * 
	 * <pre>
	 * public void onClick(View v) {
	 * 	if (UIUtil.isFastDoubleClick()) {
	 * 		return;
	 * 	}
	 * 	// 然后加其他代码
	 * }
	 * </pre>
	 * 
	 * @return 调用方法
	 */
	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < 1000) {
			return true;
		}
		lastClickTime = time;
		return false;
	}

	/**
	 * 冻结控件
	 * 
	 * @param view
	 * @param canUse
	 *            false为不可用，true为可用
	 */
	public static void freezeWidget(TextView view, boolean canUse) {
		view.clearFocus();
		view.setFocusable(canUse);
		view.setEnabled(canUse);
		view.setClickable(canUse);
	}

	/**
	 * 将输入流转换为字符串
	 * 
	 * @param inputStream
	 * @param charSetName
	 * @return String
	 * @throws IOException
	 */
	public static String readData(InputStream inputStream, String charSetName) throws IOException {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length = -1;
		while ((length = inputStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, length);
		}
		byte[] data = outStream.toByteArray();
		outStream.close();
		inputStream.close();
		return new String(data, charSetName);
	}

	/**
	 * 获取App安装包信息
	 * 
	 * @return
	 */
	public static PackageInfo getPackageInfo(Context context) {
		PackageInfo info = null;
		try {
			info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace(System.err);
		}
		if (info == null)
			info = new PackageInfo();
		return info;
	}

	/**
	 * 得到版本号
	 * 
	 * @param context
	 * @return
	 */
	public static int getAppVersionCode(Context context) {
		return getPackageInfo(context).versionCode;
	}

	/**
	 * 得到版本名称
	 * 
	 * @param context
	 * @return
	 */
	public static String getAppVersionName(Context context) {
		return getPackageInfo(context).versionName;
	}

	/**
	 * 安装包
	 * 
	 * @param a
	 * @param reqCode
	 * @param file
	 */
	public static void startInstaller(Activity a, int reqCode, File file) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		a.startActivityForResult(intent, reqCode);
	}

	/**
	 * 安装包
	 * 
	 * @param context
	 * @param file
	 */
	public static void startInstaller(Context context, File file) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	/**
	 * 格式化费用
	 * 
	 * @param price
	 *            单位“分”
	 * @return 格式化的钱，单位“元”带小数
	 */
	public static String formatMoney(String price) {
		if (price.length() <= 2) {
			return "0." + price;
		}
		return price.substring(0, price.length() - 2) + "." + price.substring(price.length() - 2);
		// return formatMoney(UIUtil.tryToDouble(price));
	}

	/**
	 * 格式化费用
	 * 
	 * @param price
	 *            单位“分”
	 * @return 格式化的钱，单位“元”带小数
	 */
	public static String formatMoney(double price) {
		return getDecimalFormat().format(price / 100);
	}

	/**
	 * 显示alert对话框
	 * 
	 * @param context
	 * @param title
	 * @param msg
	 * @param okButton
	 * @param clickedOk
	 * @param cancelButton
	 * @param clickedCancel
	 */
	public static void ShowAlertDlg(Context context, String title, String msg, String okButton,
			DialogInterface.OnClickListener clickedOk, String cancelButton,
			DialogInterface.OnClickListener clickedCancel) {
		AlertDialog.Builder builer = new AlertDialog.Builder(context);
		if (title != null && title.length() > 0)
			builer.setTitle(title);

		if (msg != null && msg.length() > 0) {
			builer.setMessage(msg);
		}

		if (okButton != null && clickedOk != null) {
			builer.setPositiveButton(okButton, clickedOk);
		}

		builer.setNegativeButton(cancelButton, null);

		AlertDialog adlg = builer.create();
		adlg.show();
	}

	/**
	 * 显示图片
	 * 
	 * @param context
	 * @param imgurl
	 * @param imageView
	 */
	public static void displayImage(Context context, String imgurl, ImageView imageView) {
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(imgurl, imageView);
	}

	/**
	 * 根据http参数串解析成RequestParams对象
	 * 
	 * @param params
	 * @return
	 */
	public static RequestParams parseRequestParam(String params) {
		RequestParams rq = new RequestParams();
		if (!TextUtils.isEmpty(params)) {
			String[] ps = params.split("&");
			if (null != ps && ps.length > 0) {
				for (String p : ps) {
					if (p.contains("=")) {
						String key = p.substring(0, p.indexOf("="));
						String value = p.substring(p.indexOf("=") + 1);
						rq.add(key, value);
					}
				}
			}
		}
		return rq;
	}

	/**
	 * 剔除内容的html相关标记
	 * 
	 * @param content
	 * @return
	 */
	public static String stripHtml(String content) {
		content = content.replaceAll("</?[^>]+>", ""); // 剔出<html>的标签
		content = content.replaceAll("\\s*|\t|\r|\n", "");// 去除字符串中的空格,回车,换行符,制表符
		content = content.replaceAll("&nbsp;", " ");
		return content;
	}

	/**
	 * 获取状态栏高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getStatusBarHeight(Context context) {
		int result = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	/**
	 * 拍照或选择相册
	 * 
	 * @param context
	 * @param camera
	 * @param pics
	 * @param cancel
	 * @return
	 */
	public static Dialog getChoosePicDialog(Context context, android.view.View.OnClickListener camera,
			android.view.View.OnClickListener pics, android.view.View.OnClickListener cancel) {
		View view = LayoutInflater.from(context).inflate(R.layout.layout_choosepic_dilog, null);
		Dialog showChooseWindow = new Dialog(context, R.style.ProgressDialog);
		showChooseWindow.setContentView(view);
		showChooseWindow.getWindow().setGravity(Gravity.BOTTOM);
		showChooseWindow.getWindow().setWindowAnimations(R.style.popwin_anim_style);
		WindowManager.LayoutParams lp = showChooseWindow.getWindow().getAttributes();
		lp.gravity = Gravity.BOTTOM;
		lp.width = LayoutParams.MATCH_PARENT;
		lp.height = LayoutParams.WRAP_CONTENT;
		showChooseWindow.getWindow().setAttributes(lp);
		view.findViewById(R.id.btn_take_phone).setOnClickListener(camera);
		view.findViewById(R.id.btn_select_imgs).setOnClickListener(pics);
		view.findViewById(R.id.btn_scancancel_icon).setOnClickListener(cancel);
		return showChooseWindow;
	}
	/**
	 * 强制隐藏软键盘
	 * 
	 * @param context
	 * @param view
	 */
	public static void hideSoftKeyBoard(Context context, View view) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
}
