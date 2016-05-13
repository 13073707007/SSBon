package com.loongme.activity.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.loongme.activity.R;
import com.loongme.activity.base.MenuItemClickCallBack;
import com.loongme.activity.business.VoiceAssistantManager;
import com.loongme.activity.ui.FragmentMoreMenuVP;
import com.viewpagerindicator.IconPagerAdapter;

/**
 * @author 作者:zhaojianyu
 * @version 创建时间：2016-5-4 下午5:49:29 类说明:
 */
public class AdapterSpeakMenuVP extends FragmentPagerAdapter implements IconPagerAdapter {

	/** tab名称 */
	private static final String[] CONTENT = new String[] { "推荐", "健康", "上门", "生活", "一起晒", "年轻派" };
	/** tab图标 */
	private static final int[] ICONS = new int[] { R.drawable.moremenu_tuijian, R.drawable.moremenu_jiankang,
			R.drawable.moremenu_shangmen, R.drawable.moremenu_shenghuo, R.drawable.moremenu_yiqishai,
			R.drawable.moremenu_nianqingpai };

	/** 语音助手 */
	private VoiceAssistantManager voiceAssistantManager;
	/** 菜单点击回调 */
	private MenuItemClickCallBack menuItemClickCallBack;

	public AdapterSpeakMenuVP(FragmentManager fm, VoiceAssistantManager voiceAssistantManager,
			MenuItemClickCallBack menuItemClickCallBack) {
		super(fm);
		this.voiceAssistantManager = voiceAssistantManager;
		this.menuItemClickCallBack = menuItemClickCallBack;
	}

	@Override
	public Fragment getItem(int position) {
		return new FragmentMoreMenuVP(voiceAssistantManager.getDatasWithPostion(position), menuItemClickCallBack);
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return CONTENT[position % CONTENT.length];
	}

	@Override
	public int getIconResId(int index) {
		return ICONS[index];
	}

	@Override
	public int getCount() {
		return CONTENT.length;
	}

}
