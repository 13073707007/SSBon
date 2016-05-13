package com.loongme.activity.bean;

import java.util.List;

/**
 * @author 作者:zhaojianyu
 * @version 创建时间：2016-5-4 下午2:58:55 类说明:聊天菜单标签
 */
public class MoreMenuIndicator {
	private String indicatorName;
	private int indicatorIconResId;
	private List<MoreMenuItem> menuItems;

	private MoreMenuIndicator() {
		super();
	}

	private MoreMenuIndicator(String indicatorName, int indicatorIconResId, List<MoreMenuItem> menuItems) {
		super();
		this.indicatorName = indicatorName;
		this.indicatorIconResId = indicatorIconResId;
		this.menuItems = menuItems;
	}

	public String getIndicatorName() {
		return indicatorName;
	}

	public void setIndicatorName(String indicatorName) {
		this.indicatorName = indicatorName;
	}

	public int getIndicatorIconResId() {
		return indicatorIconResId;
	}

	public void setIndicatorIconResId(int indicatorIconResId) {
		this.indicatorIconResId = indicatorIconResId;
	}

	public List<MoreMenuItem> getMenuItems() {
		return menuItems;
	}

	public void setMenuItems(List<MoreMenuItem> menuItems) {
		this.menuItems = menuItems;
	}

}
