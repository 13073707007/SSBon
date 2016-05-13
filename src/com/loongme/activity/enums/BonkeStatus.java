package com.loongme.activity.enums;

/**
 * @author 作者:zhaojianyu
 * @version 创建时间：2016-4-27 下午6:34:10 类说明:帮客审核状态
 */
public enum BonkeStatus {

	BonkeChecking("已经注册，正在审核", 0),

	BonkeChecked("已经通过审核", 1),

	BonkeUnChecked("审核失败", 2),

	BonkeUnRegiste("不是帮客", 3);

	private String vaule;
	private int code;

	BonkeStatus(String vaule, int code) {
		this.vaule = vaule;
		this.code = code;
	}

	public static String valueByCode(int code) {
		for (BonkeStatus e : values()) {
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
