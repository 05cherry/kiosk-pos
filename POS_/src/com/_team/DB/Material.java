package com._team.DB;

import java.sql.Date;
import java.util.Vector;

public class Material {
	private final static String[] COLUMN_NAME = {"재료 코드","재 료 명","재료 가격","유통 기한","남은 재고"};
	private int code;
	private String name;
	private int price;
	private Date outOfDate;
	private int num;
	
	public Material(int code, String name, int price, Date outOfDate, int num) {
		this.code = code;
		this.name = name;
		this.price = price;
		this.outOfDate = outOfDate;
		this.num = num;
	}

	public Material(Vector<String> values) {
		this.code = (int) DBController.parseData(values.get(0), "int");
		this.name = (String) DBController.parseData(values.get(1), "String");
		this.price = (int) DBController.parseData(values.get(2), "int");
		this.outOfDate = (Date) DBController.parseData(values.get(3), "Date");
		this.num = (int) DBController.parseData(values.get(4), "int");
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public Date getOutOfDate() {
		return outOfDate;
	}

	public void setOutOfDate(Date outOfDate) {
		this.outOfDate = outOfDate;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
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
