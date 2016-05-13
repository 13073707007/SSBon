package com.loongme.activity.enums;

/**
 * @author 作者:zhaojianyu
 * @version 创建时间：2016-4-28 下午6:01:19 类说明:用户发布的服务状态
 */
public enum SSBonServerStatus {

	ServerChecking("待审核", 0),

	ServerChecked("审核通过", 1),

	ServerUnChecked("审核未通过", 2),

	ServerDelete("已删除", 3);

	private String vaule;
	private int code;

	SSBonServerStatus(String vaule, int code) {
		this.vaule = vaule;
		this.code = code;
	}

	public static String valueByCode(int code) {
		for (SSBonServerStatus e : values()) {
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
