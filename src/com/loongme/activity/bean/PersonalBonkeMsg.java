package com.loongme.activity.bean;

import java.io.Serializable;

/**
 * @author 作者:zhaojianyu
 * @version 创建时间：2016-4-27 下午7:39:41 类说明:个人帮客信息
 */
public class PersonalBonkeMsg implements Serializable {

	private String persNname;// 个人真实姓名
	private String persBack;// 个人身份证反面
	private String persFront;// 个人身份证正面
	private String persJob;// 职业
	private String persDesc;// 描述
	private String persIntroduce;// 详细介绍

	private PersonalBonkeMsg() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PersonalBonkeMsg(String persNname, String persBack, String persFront, String persJob, String persDesc,
			String persIntroduce) {
		super();
		this.persNname = persNname;
		this.persBack = persBack;
		this.persFront = persFront;
		this.persJob = persJob;
		this.persDesc = persDesc;
		this.persIntroduce = persIntroduce;
	}

	public String getPersNname() {
		return persNname;
	}

	public void setPersNname(String persNname) {
		this.persNname = persNname;
	}

	public String getPersBack() {
		return persBack;
	}

	public void setPersBack(String persBack) {
		this.persBack = persBack;
	}

	public String getPersFront() {
		return persFront;
	}

	public void setPersFront(String persFront) {
		this.persFront = persFront;
	}

	public String getPersJob() {
		return persJob;
	}

	public void setPersJob(String persJob) {
		this.persJob = persJob;
	}

	public String getPersDesc() {
		return persDesc;
	}

	public void setPersDesc(String persDesc) {
		this.persDesc = persDesc;
	}

	public String getPersIntroduce() {
		return persIntroduce;
	}

	public void setPersIntroduce(String persIntroduce) {
		this.persIntroduce = persIntroduce;
	}

	@Override
	public String toString() {
		return "PersonalBonkeMsg [persNname=" + persNname + ", persBack=" + persBack + ", persFront=" + persFront
				+ ", persJob=" + persJob + ", persDesc=" + persDesc + ", persIntroduce=" + persIntroduce + "]";
	}

}
