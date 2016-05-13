package com.loongme.activity.bean;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class User {

	private String userPhone;// 用户手机号
	private String userNickname;// 用户姓名
	private String userPassword;// 密码
	private String userType;// 用户类型
	private String relationType;
	private String relationAcct;
	private String relationIcon;
	private String userSex;// 性别
	private int userId;// id
	private String userPicture;
	private String declaration;// 宣言

	private int auditStatus;// 帮客申请状态
	private ArrayList<MerchantBonkeMsg> busMsgs;// 商家帮客信息
	private ArrayList<PersonalBonkeMsg> persMsgs;// 个人帮客信息

	private User() {
		super();
	}

	public User(String userPhone, String userNickname, String userPassword, String userType, String relationType,
			String relationAcct, String relationIcon, String userSex, int userId, String userPicture,
			String declaration, int auditStatus, ArrayList<MerchantBonkeMsg> busMsgs,
			ArrayList<PersonalBonkeMsg> persMsgs) {
		super();
		this.userPhone = userPhone;
		this.userNickname = userNickname;
		this.userPassword = userPassword;
		this.userType = userType;
		this.relationType = relationType;
		this.relationAcct = relationAcct;
		this.relationIcon = relationIcon;
		this.userSex = userSex;
		this.userId = userId;
		this.userPicture = userPicture;
		this.declaration = declaration;
		this.auditStatus = auditStatus;
		this.busMsgs = busMsgs;
		this.persMsgs = persMsgs;
	}

	public static byte[] readBytes(InputStream is) {
		try {
			byte[] buffer = new byte[1024];
			int len = -1;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while ((len = is.read(buffer)) != -1) {
				baos.write(buffer, 0, len);
			}
			baos.close();
			return baos.toByteArray();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	public static String readString(InputStream is) {
		return new String(readBytes(is));

	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getUserNickname() {
		return userNickname;
	}

	public void setUserNickname(String userNickname) {
		this.userNickname = userNickname;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getRelationType() {
		return relationType;
	}

	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}

	public String getRelationAcct() {
		return relationAcct;
	}

	public void setRelationAcct(String relationAcct) {
		this.relationAcct = relationAcct;
	}

	public String getRelationIcon() {
		return relationIcon;
	}

	public void setRelationIcon(String relationIcon) {
		this.relationIcon = relationIcon;
	}

	public String getUserSex() {
		return userSex;
	}

	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserPicture() {
		return userPicture;
	}

	public void setUserPicture(String userPicture) {
		this.userPicture = userPicture;
	}

	public String getDeclaration() {
		return declaration;
	}

	public void setDeclaration(String declaration) {
		this.declaration = declaration;
	}

	public int getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(int auditStatus) {
		this.auditStatus = auditStatus;
	}

	public ArrayList<MerchantBonkeMsg> getBusMsgs() {
		return busMsgs;
	}

	public void setBusMsgs(ArrayList<MerchantBonkeMsg> busMsgs) {
		this.busMsgs = busMsgs;
	}

	public ArrayList<PersonalBonkeMsg> getPersMsgs() {
		return persMsgs;
	}

	public void setPersMsgs(ArrayList<PersonalBonkeMsg> persMsgs) {
		this.persMsgs = persMsgs;
	}

	@Override
	public String toString() {
		return "User [userPhone=" + userPhone + ", userNickname=" + userNickname + ", userPassword=" + userPassword
				+ ", userType=" + userType + ", relationType=" + relationType + ", relationAcct=" + relationAcct
				+ ", relationIcon=" + relationIcon + ", userSex=" + userSex + ", userId=" + userId + ", userPicture="
				+ userPicture + ", auditStatus=" + auditStatus + ", busMsgs=" + busMsgs + ", persMsgs=" + persMsgs
				+ "]";
	}

}
