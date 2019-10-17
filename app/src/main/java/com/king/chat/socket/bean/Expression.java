/*
 * Copyright 2014-2024 setNone. All rights reserved. 
 */
package com.king.chat.socket.bean;

/**
 * Expression.java - 定义表情属性
 * 
 * @author jiengyh
 * 
 *         2014年6月25日 上午9:24:49
 */
public class Expression {
	private String name;
	private String flag;
	/**
	 * assets对应的文件名
	 */
	private String fileName;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the flag
	 */
	public String getFlag() {
		return flag;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */

	/**
	 * @param flag
	 *            the flag to set
	 */
	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public String toString() {
		return "Expression [name=" + name + ", flag=" + flag + ", fileName=" + fileName + "]";
	}

}
