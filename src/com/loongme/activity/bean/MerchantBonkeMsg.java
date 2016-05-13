package com.loongme.activity.bean;

import java.io.Serializable;

/**
 * @author 作者:zhaojianyu
 * @version 创建时间：2016-4-27 下午7:39:18 类说明:商家帮客信息
 */
public class MerchantBonkeMsg implements Serializable {

	private String busContact;// 商家帮客联系人
	private String busGhone;// 座机
	private String busDesc;// 描述
	private String busIntroduce;// 详细介绍
	private String busLicense;// 营业执照
	private String busName;// 商家真实名称
	private String busAddress;// 商家地址

	private MerchantBonkeMsg() {
		super();
	}

	public MerchantBonkeMsg(String busContact, String busGhone, String busDesc, String busIntroduce, String busLicense,
			String busName, String busAddress) {
		super();
		this.busContact = busContact;
		this.busGhone = busGhone;
		this.busDesc = busDesc;
		this.busIntroduce = busIntroduce;
		this.busLicense = busLicense;
		this.busName = busName;
		this.busAddress = busAddress;
	}

	public String getBusContact() {
		return busContact;
	}

	public void setBusContact(String busContact) {
		this.busContact = busContact;
	}

	public String getBusGhone() {
		return busGhone;
	}

	public void setBusGhone(String busGhone) {
		this.busGhone = busGhone;
	}

	public String getBusDesc() {
		return busDesc;
	}

	public void setBusDesc(String busDesc) {
		this.busDesc = busDesc;
	}

	public String getBusIntroduce() {
		return busIntroduce;
	}

	public void setBusIntroduce(String busIntroduce) {
		this.busIntroduce = busIntroduce;
	}

	public String getBusLicense() {
		return busLicense;
	}

	public void setBusLicense(String busLicense) {
		this.busLicense = busLicense;
	}

	public String getBusName() {
		return busName;
	}

	public void setBusName(String busName) {
		this.busName = busName;
	}

	public String getBusAddress() {
		return busAddress;
	}

	public void setBusAddress(String busAddress) {
		this.busAddress = busAddress;
	}

	@Override
	public String toString() {
		return "MerchantBonkeMsg [busContact=" + busContact + ", busGhone=" + busGhone + ", busDesc=" + busDesc
				+ ", busIntroduce=" + busIntroduce + ", busLicense=" + busLicense + ", busName=" + busName
				+ ", busAddress=" + busAddress + "]";
	}

}
