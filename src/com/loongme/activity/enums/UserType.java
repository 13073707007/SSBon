package com.loongme.activity.enums;

/**
 * @author 作者:zhaojianyu
 * @version 创建时间：2016-4-26 下午6:49:25 类说明:用户类型
 */
public enum UserType {

	Normal("普通用户", 0), Personal("个人帮客", 1), Merchant("商家帮客", 2);

	private String vaule;
	private int code;

	UserType(String vaule, int code) {
		this.vaule = vaule;
		this.code = code;
	}

	public static String valueByCode(int code) {
		for (UserType e : values()) {
			if (e.code == code) {
				return e.vaule;
			}
		}

		return null;
	}

	public String getVaule() {
		return vaule;
	}

	public void setVaule(String vaule) {
		this.vaule = vaule;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
