package com.ttlock.bl.sdk.entity;

/**
 * 300个密码
 */
public class KeyboardPwdAuto {

	/** 当前使用密码的索引，范围：[0,299]，-1表示未使用 */
	private int currentIndex;

	/** 四位数密码列表，形式：[1111,2222,3333...] */
	private String fourKeyboardPwdList;

	/** 时间对照表 */
	private String timeControlTb;

	/** 位置，形式：[1,2,3] */
	private String position;

	/** 校验数字，长度为10的字符串 */
	private String checkDigit;

	private KeyboardPwdAuto() {

	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

	public String getFourKeyboardPwdList() {
		return fourKeyboardPwdList;
	}

	public void setFourKeyboardPwdList(String fourKeyboardPwdList) {
		this.fourKeyboardPwdList = fourKeyboardPwdList;
	}

	public String getTimeControlTb() {
		return timeControlTb;
	}

	public void setTimeControlTb(String timeControlTb) {
		this.timeControlTb = timeControlTb;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getCheckDigit() {
		return checkDigit;
	}

	public void setCheckDigit(String checkDigit) {
		this.checkDigit = checkDigit;
	}

}
