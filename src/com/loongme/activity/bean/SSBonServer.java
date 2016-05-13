package com.loongme.activity.bean;

import java.util.ArrayList;

/**
 * @author 作者:zhaojianyu
 * @version 创建时间：2016-4-28 下午1:06:46 类说明:服务分类
 */
public class SSBonServer {

	private int pid;
	private int id;
	private String name;// 服务名称
	private String nikename;//
	private ArrayList<SSBonServerSub> subName;// 子分类

	private SSBonServer() {
		super();
		// TODO Auto-generated constructor stub
	}

	private SSBonServer(int pid, int id, String name, String nikename,
			ArrayList<SSBonServerSub> subName) {
		super();
		this.pid = pid;
		this.id = id;
		this.name = name;
		this.nikename = nikename;
		this.subName = subName;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNikename() {
		return nikename;
	}

	public void setNikename(String nikename) {
		this.nikename = nikename;
	}

	public ArrayList<SSBonServerSub> getSubName() {
		return subName;
	}

	public void setSubName(ArrayList<SSBonServerSub> subName) {
		this.subName = subName;
	}

	@Override
	public String toString() {
		return "SSBonServer [pid=" + pid + ", id=" + id + ", name=" + name
				+ ", nikename=" + nikename + ", subName=" + subName + "]";
	}

}
