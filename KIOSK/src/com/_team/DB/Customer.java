package com._team.DB;

import java.util.Vector;

public class Customer {
	private final static String[] COLUMN_NAME = {"고객코드","전화번호","포인트"};

	private int code;
	private String phone;
	private int point;
	
	public Customer(Vector<String> values) {
		this.code = (int) DBController.parseData(values.get(0),"int");
		this.phone = (String) DBController.parseData(values.get(1), "String");
		this.point = (int) DBController.parseData(values.get(2), "int");
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public static String[] getColumnName() {
		return COLUMN_NAME;
	}

	public static Vector<String> getVectorColumnName() {
		Vector<String> v = new Vector<String>();
		for (String s : COLUMN_NAME)
			v.add(s);
		return v;
	}
	
}
