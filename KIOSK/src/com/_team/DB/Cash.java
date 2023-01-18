package com._team.DB;

import java.util.Vector;

public class Cash {
	private final static String[] COLUMN_NAME = { "code", "currentCash" };

	private int code;
	private int currentCash;

	public Cash(Vector<String> values) {
		this.code = (int) DBController.parseData(values.get(0), "int");
		this.currentCash = (int) DBController.parseData(values.get(1), "int");
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getCurrentCash() {
		return currentCash;
	}

	public void setCurrentCash(int currentCash) {
		this.currentCash = currentCash;
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
