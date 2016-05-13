package com.loongme.activity.bean;

/**
 * @author 作者:zhaojianyu
 * @version 创建时间：2016-5-4 上午11:26:22 类说明:聊天菜单项
 */
public class MoreMenuItem {

	private String menuName;// 菜单名称
	private int menuIconResId;// 菜单图标
	private String menuRedirectUrl;// 菜单项指向的url

	private MoreMenuItem() {
		super();
	}

	public MoreMenuItem(String menuName, int menuIconResId, String menuRedirectUrl) {
		super();
		this.menuName = menuName;
		this.menuIconResId = menuIconResId;
		this.menuRedirectUrl = menuRedirectUrl;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public int getMenuIconResId() {
		return menuIconResId;
	}

	public void setMenuIconResId(int menuIconResId) {
		this.menuIconResId = menuIconResId;
	}

	public String getMenuRedirectUrl() {
		return menuRedirectUrl;
	}

	public void setMenuRedirectUrl(String menuRedirectUrl) {
		this.menuRedirectUrl = menuRedirectUrl;
	}

}
