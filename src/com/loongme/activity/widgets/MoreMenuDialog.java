package com.loongme.activity.widgets;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.loongme.activity.R;
import com.loongme.activity.base.MenuItemClickCallBack;
import com.loongme.activity.bean.MoreMenuItem;
import com.loongme.activity.business.VoiceAssistantManager;
import com.loongme.activity.ui.FragmentMoreMenuVP;
import com.loongme.activity.utils.DeviceUtils;
import com.loongme.activity.utils.UIUtil;
import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.TabPageIndicator;

/**
 * @author 作者:zhaojianyu
 * @version 创建时间：2016-5-4 上午11:24:45 类说明:聊天菜单
 */
public class MoreMenuDialog extends Dialog implements MenuItemClickCallBack {

	/** tab名称 */
	private static final String[] CONTENT = new String[] { "推荐", "健康", "上门", "生活", "一起晒", "年轻派" };
	/** tab图标 */
	private static final int[] ICONS = new int[] { R.drawable.moremenu_tuijian, R.drawable.moremenu_jiankang,
			R.drawable.moremenu_shangmen, R.drawable.moremenu_shenghuo, R.drawable.moremenu_yiqishai,
			R.drawable.moremenu_nianqingpai };

	private ViewPager viewPager;
	/** 标签 */
	private TabPageIndicator tabPageIndicator;
	/** 语音助手 */
	private VoiceAssistantManager voiceAssistantManager;
	/** 菜单点击回调 */
	private MenuItemClickCallBack menuItemClickCallBack;
	/** ac上下文 */
	private FragmentManager fragmentManager;

	private final int width;
	private final int height;

	public MoreMenuDialog(Context context, FragmentManager fragmentManager, VoiceAssistantManager voiceAssistantManager) {
		super(context);
		this.fragmentManager = fragmentManager;
		this.voiceAssistantManager = voiceAssistantManager;
		width = DeviceUtils.getScreenWidth(context);
		height = UIUtil.dip2px(context, 300);
		initWidgets();
		regListener();
		initUI();
	}

	private void initWidgets() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_contentview_menudialog);
		Window window = getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.width = width;
		lp.height = height;
		lp.gravity = Gravity.BOTTOM;
		window.setAttributes(lp);
		window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
				| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		window.setWindowAnimations(R.style.popwin_anim_style);
		setCanceledOnTouchOutside(false);
		setCancelable(false);

		// viewPager = (ViewPager) findViewById(R.id.viewpager_moremenu);
		// viewPager.setAdapter(new VPAdapter(fragmentManager));
		// tabPageIndicator = (TabPageIndicator)
		// findViewById(R.id.vp_indicator);
		// tabPageIndicator.setViewPager(viewPager);
	}

	private void regListener() {

	}

	private void initUI() {

	}

	/**
	 * viewpager适配器
	 */
	private class VPAdapter extends FragmentStatePagerAdapter implements IconPagerAdapter {

		public VPAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return new FragmentMoreMenuVP(voiceAssistantManager.getDatasWithPostion(position), MoreMenuDialog.this);
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

	@Override
	public void onMoreMenuItemClick(MoreMenuItem item) {
		if (menuItemClickCallBack != null) {
			menuItemClickCallBack.onMoreMenuItemClick(item);
		}
	}
}
