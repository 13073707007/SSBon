package com.loongme.activity.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

/**
 * @author 作者:zhaojianyu
 * @version 创建时间：2016-5-3 下午4:12:36 类说明:聊天记录实体
 */
@Table(name = "SpeakItem")
public class SpeakItem {

	private int id;
	@Column(column = "speakerPhoto")
	private String speakerPhoto;// 头像
	@Column(column = "speakerContent")
	private String speakerContent;// 内容
	@Column(column = "speakTime")
	private String speakTime;// 发送时间
	@Column(column = "speakerType")
	private int speakerType;// 用户类型 小帮0 用户1

	public SpeakItem() {
		super();
	}

	public SpeakItem(String speakerPhoto, String speakerContent, String speakTime, int speakerType) {
		super();
		this.speakerPhoto = speakerPhoto;
		this.speakerContent = speakerContent;
		this.speakTime = speakTime;
		this.speakerType = speakerType;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSpeakerPhoto() {
		return speakerPhoto;
	}

	public void setSpeakerPhoto(String speakerPhoto) {
		this.speakerPhoto = speakerPhoto;
	}

	public String getSpeakerContent() {
		return speakerContent;
	}

	public void setSpeakerContent(String speakerContent) {
		this.speakerContent = speakerContent;
	}

	public String getSpeakTime() {
		return speakTime;
	}

	public void setSpeakTime(String speakTime) {
		this.speakTime = speakTime;
	}

	public int getSpeakerType() {
		return speakerType;
	}

	public void setSpeakerType(int speakerType) {
		this.speakerType = speakerType;
	}

	@Override
	public String toString() {
		return "SpeakItem [id=" + id + ", speakerPhoto=" + speakerPhoto + ", speakerContent=" + speakerContent
				+ ", speakTime=" + speakTime + ", speakerType=" + speakerType + "]";
	}

}
