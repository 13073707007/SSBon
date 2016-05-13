package com.loongme.activity.bean;

/**
 * @author 作者:zhaojianyu
 * @version 创建时间：2016-4-28 下午1:09:56 类说明:服务的子分类
 */
public class SSBonServerSub {

	private int pid;
	private int id;
	private String name;// 子分类名称
	private String nikename;//

	private SSBonServerSub() {
		super();
		// TODO Auto-generated constructor stub
	}

	private SSBonServerSub(int pid, int id, String name, String nikename) {
		super();
		this.pid = pid;
		this.id = id;
		this.name = name;
		this.nikename = nikename;
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

	@Override
	public String toString() {
		return "SSBonServerSub [pid=" + pid + ", id=" + id + ", name=" + name
				+ ", nikename=" + nikename + "]";
	}

}
