package com.loongme.activity.enums;

/**
 * 枚举是否
 */
public enum EnumBoolean {

	NO(0, "否"),

	YES(1, "是");

	private int enumCode;// 枚举编码

	private String enumTitle;// 枚举标题

	EnumBoolean(int enumCode, String enumTitle) {
		this.enumCode = enumCode;
		this.enumTitle = enumTitle;
	}

	public int getEnumCode() {
		return enumCode;
	}

	public String getEnumTitle() {
		return enumTitle;
	}

	/**
	 * 通过枚举编码得到对应枚举，
	 * 如果编码不存在则返回null
	 * @param enumCode
	 * @return
	 */
	public static  EnumBoolean  valueOfByCode(int enumCode){
		for(EnumBoolean boo:values()){
			if(boo.getEnumCode()==enumCode){
				return boo;
			}
		}
		return null;
	}
	
	public String toString(){
		return this.enumTitle;
	}
}
