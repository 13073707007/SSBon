package com.loongme.activity.business;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.loongme.activity.bean.MerchantBonkeMsg;
import com.loongme.activity.bean.PersonalBonkeMsg;
import com.loongme.activity.bean.User;
import com.loongme.activity.common.Configuration;
import com.loongme.activity.enums.BonkeStatus;
import com.loongme.activity.utils.DiskCache;
import com.loongme.activity.utils.StringUtils;

/**
 * @author 作者:zhaojianyu
 * @version 创建时间：2016年5月2日 下午7:34:12 类说明:
 */
public class Helpers {

	/**
	 * 是否第一次启动app
	 * 
	 * @return
	 */
	public static boolean isFirstStartApp(Context context) {
		SharedPreferences sp = context.getSharedPreferences(Configuration.SYSTEM_PRE, context.MODE_PRIVATE);
		return sp.getBoolean(Configuration.sp_key.IS_FIRST_IN, true);
	}

	/**
	 * 将是否第一次启动设置为false
	 * 
	 */
	public static void saveFirstStartApp(Context context) {
		SharedPreferences sp = context.getSharedPreferences(Configuration.SYSTEM_PRE, context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putBoolean(Configuration.sp_key.IS_FIRST_IN, false);
		editor.commit();
	}

	/**
	 * 保存用户信息到本地
	 */
	public static void saveUserInfoToLocal(Context context, User user) {
		SharedPreferences preferences = context.getSharedPreferences(Configuration.ACCOUNT, context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("userPhone", user.getUserPhone());
		editor.putString("userNickname", user.getUserNickname());
		editor.putString("userPassword", user.getUserPassword());
		editor.putString("userType", user.getUserType());
		editor.putString("relationType", user.getRelationType());
		editor.putString("relationAcct", user.getRelationAcct());
		editor.putString("relationIcon", user.getRelationIcon());
		editor.putString("userSex", user.getUserSex());
		editor.putString("userPicture", user.getUserPicture());
		editor.putInt("userId", user.getUserId());
		editor.putInt("auditStatus", user.getAuditStatus());
		editor.putString("declaration", user.getDeclaration());
		if (user.getBusMsgs() != null && user.getBusMsgs().size() > 0) {
			MerchantBonkeMsg merchantBonkeMsg = user.getBusMsgs().get(0);
			editor.putString("busbonke_busContact", merchantBonkeMsg.getBusContact());
			editor.putString("busbonke_busGhone", merchantBonkeMsg.getBusGhone());
			editor.putString("busbonke_busDesc", merchantBonkeMsg.getBusDesc());
			editor.putString("busbonke_busIntroduce", merchantBonkeMsg.getBusIntroduce());
			editor.putString("busbonke_busLicense", merchantBonkeMsg.getBusLicense());
			editor.putString("busbonke_busName", merchantBonkeMsg.getBusName());
			editor.putString("busbonke_busAddress", merchantBonkeMsg.getBusAddress());
		}
		if (user.getPersMsgs() != null && user.getPersMsgs().size() > 0) {
			PersonalBonkeMsg personalBonkeMsg = user.getPersMsgs().get(0);
			editor.putString("perbonke_persNname", personalBonkeMsg.getPersNname());
			editor.putString("perbonke_persBack", personalBonkeMsg.getPersBack());
			editor.putString("perbonke_persFront", personalBonkeMsg.getPersFront());
			editor.putString("perbonke_persJob", personalBonkeMsg.getPersJob());
			editor.putString("perbonke_persDesc", personalBonkeMsg.getPersDesc());
			editor.putString("perbonke_persIntroduce", personalBonkeMsg.getPersIntroduce());
		}
		editor.commit();

	}

	/**
	 * 保存帮客审核状态到本地
	 */
	public static void saveBonkeStatusToLocal(Context context, BonkeStatus bonkeStatus) {
		SharedPreferences preferences = context.getSharedPreferences(Configuration.SYSTEM_PRE, context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putInt(Configuration.BONKE_STATUS, bonkeStatus.getCode());
		editor.commit();

	}

	/**
	 * 获取帮客审核
	 */
	public static int getBonkeStatusFromLocal(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(Configuration.SYSTEM_PRE, context.MODE_PRIVATE);
		return preferences.getInt(Configuration.BONKE_STATUS, -1);
	}

	/**
	 * 得到用户信息
	 * 
	 * @param context
	 * @return
	 */
	public static User getUserInfo(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(Configuration.ACCOUNT, context.MODE_PRIVATE);
		String userPhone = preferences.getString("userPhone", "");
		String userNickname = preferences.getString("userNickname", "");
		String userPassword = preferences.getString("userPassword", "");
		String userType = preferences.getString("userType", "");
		String relationType = preferences.getString("relationType", "");
		String relationAcct = preferences.getString("relationAcct", "");
		String relationIcon = preferences.getString("relationIcon", "");
		String userSex = preferences.getString("userSex", "");
		String userPicture = preferences.getString("userPicture", "");
		String declaration = preferences.getString("declaration", "");
		int userId = preferences.getInt("userId", -1);
		int auditStatus = preferences.getInt("auditStatus", -1);
		// 商家帮客
		ArrayList<MerchantBonkeMsg> busMsgs = new ArrayList<MerchantBonkeMsg>();
		if (!StringUtils.isEmpty(preferences.getString("busbonke_busName", ""))) {
			String busContact = preferences.getString("busbonke_busContact", "");
			String busGhone = preferences.getString("busbonke_busGhone", "");
			String busDesc = preferences.getString("busbonke_busDesc", "");
			String busIntroduce = preferences.getString("busbonke_busIntroduce", "");
			String busLicense = preferences.getString("busbonke_busLicense", "");
			String busName = preferences.getString("busbonke_busName", "");
			String busAddress = preferences.getString("busbonke_busAddress", "");
			MerchantBonkeMsg merchantBonkeMsg = new MerchantBonkeMsg(busContact, busGhone, busDesc, busIntroduce,
					busLicense, busName, busAddress);
			busMsgs.add(merchantBonkeMsg);
		}

		// 个人帮客
		ArrayList<PersonalBonkeMsg> persMsgs = new ArrayList<PersonalBonkeMsg>();
		if (!StringUtils.isEmpty(preferences.getString("perbonke_persNname", ""))) {
			String persNname = preferences.getString("perbonke_persNname", "");
			String persBack = preferences.getString("perbonke_persBack", "");
			String persFront = preferences.getString("perbonke_persFront", "");
			String persJob = preferences.getString("perbonke_persJob", "");
			String persDesc = preferences.getString("perbonke_persDesc", "");
			String persIntroduce = preferences.getString("perbonke_persIntroduce", "");
			PersonalBonkeMsg personalBonkeMsg = new PersonalBonkeMsg(persNname, persBack, persFront, persJob, persDesc,
					persIntroduce);
			persMsgs.add(personalBonkeMsg);
		}
		User user = new User(userPhone, userNickname, userPassword, userType, relationType, relationAcct, relationIcon,
				userSex, userId, userPicture, declaration, auditStatus, busMsgs, persMsgs);
		return user;
	}

	/**
	 * 清空保存登录状态的文件
	 * 
	 * @param context
	 */
	public static void clearLoginStatusFile(Context context) {
		SharedPreferences settings = context.getSharedPreferences(Configuration.ACCOUNT, Context.MODE_PRIVATE);
		SharedPreferences.Editor editer = settings.edit();
		editer.clear();
		editer.commit();
	}

	/**
	 * 缓存业务逻辑数据
	 * 
	 * @param context
	 * @return
	 */
	public static String getBusinessCache(Context context) {
		User user = getUserInfo(context);
		return Configuration.SDCARD_PATH + File.separator + Configuration.TAG + File.separator + user.getUserNickname()
				+ File.separator + "/business_cache";
	}

	/**
	 * 根据url和参数来生成一个key，用来在缓存中存储结果
	 * 
	 * @param urlAndParams
	 * @return
	 */
	public static String generateCacheKey(String urlAndParams) {
		return DiskCache.md5(DiskCache.md5(urlAndParams));
	}

	/**
	 * 初始化语音播报
	 */
	public static void saveVoiceStatus(Context context, boolean status) {
		SharedPreferences sp = context.getSharedPreferences(Configuration.SYSTEM_PRE, context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putBoolean(Configuration.VOICE_ISOPENED, status);
		editor.commit();
	}

	/**
	 * 得到是否允许播报语音
	 * 
	 * @param context
	 * @return
	 */
	public static Boolean getVoiceStatus(Context context) {
		SharedPreferences sp = context.getSharedPreferences(Configuration.SYSTEM_PRE, context.MODE_PRIVATE);
		return sp.getBoolean(Configuration.VOICE_ISOPENED, true);
	}

}
