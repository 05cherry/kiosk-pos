package com._team.DB;

import java.util.Vector;

public class Category {
	private final static String[] COLUMN_NAME = { "code", "name" };

	private int code;
	private String name;

	public Category(int code, String name) {
		this.code = code;
		this.name = name;
	}

	public Category(Vector<String> values) {
		this.code = (int) DBController.parseData(values.get(0), "int");
		this.name = (String) DBController.parseData(values.get(1), "String");
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
