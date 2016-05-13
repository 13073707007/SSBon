package com.loongme.activity.bean;

/**
 * @author 作者:zhaojianyu
 * @version 创建时间：2016-4-28 下午5:39:44 类说明:我的服务
 */
public class MyServer {
	private String provideData;// 发布日期
	private String provideTitle;// 服务标题
	private int provideDeldata;// 服务删除状态
	private String provideRefusedesc;// 服务申请详细信息
	private int provideStatus;// 服务发布状态
	private int userId;// 用户id
	private int provideId;// 服务id

	private MyServer() {
		super();
		// TODO Auto-generated constructor stub
	}

	private MyServer(String provideData, String provideTitle,
			int provideDeldata, String provideRefusedesc, int provideStatus,
			int userId, int provideId) {
		super();
		this.provideData = provideData;
		this.provideTitle = provideTitle;
		this.provideDeldata = provideDeldata;
		this.provideRefusedesc = provideRefusedesc;
		this.provideStatus = provideStatus;
		this.userId = userId;
		this.provideId = provideId;
	}

	public String getProvideData() {
		return provideData;
	}

	public void setProvideData(String provideData) {
		this.provideData = provideData;
	}

	public String getProvideTitle() {
		return provideTitle;
	}

	public void setProvideTitle(String provideTitle) {
		this.provideTitle = provideTitle;
	}

	public int getProvideDeldata() {
		return provideDeldata;
	}

	public void setProvideDeldata(int provideDeldata) {
		this.provideDeldata = provideDeldata;
	}

	public String getProvideRefusedesc() {
		return provideRefusedesc;
	}

	public void setProvideRefusedesc(String provideRefusedesc) {
		this.provideRefusedesc = provideRefusedesc;
	}

	public int getProvideStatus() {
		return provideStatus;
	}

	public void setProvideStatus(int provideStatus) {
		this.provideStatus = provideStatus;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getProvideId() {
		return provideId;
	}

	public void setProvideId(int provideId) {
		this.provideId = provideId;
	}

	@Override
	public String toString() {
		return "MyServer [provideData=" + provideData + ", provideTitle="
				+ provideTitle + ", provideDeldata=" + provideDeldata
				+ ", provideRefusedesc=" + provideRefusedesc
				+ ", provideStatus=" + provideStatus + ", userId=" + userId
				+ ", provideId=" + provideId + "]";
	}

}
