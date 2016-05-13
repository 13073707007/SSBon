package com.loongme.activity.bean;

/**
 * @author 作者:zhaojianyu
 * @version 创建时间：2016-5-7 下午5:22:34 类说明:菜单选项
 */
public class PerInfoMenuItem {

	private String name;// 名称
	private int iconResId;// 图标
	private boolean isBonke;

	public PerInfoMenuItem(String name, int iconResId, boolean isBonke) {
		super();
		this.name = name;
		this.iconResId = iconResId;
		this.isBonke = isBonke;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIconResId() {
		return iconResId;
	}

	public void setIconResId(int iconResId) {
		this.iconResId = iconResId;
	}

	public boolean isBonke() {
		return isBonke;
	}

	public void setBonke(boolean isBonke) {
		this.isBonke = isBonke;
	}

}
