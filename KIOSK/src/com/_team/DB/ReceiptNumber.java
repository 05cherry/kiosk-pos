package com._team.DB;

import java.util.Vector;

public class ReceiptNumber {
	private final static String[] COLUMN_NAME = {};
	private int orderCode;
	private int receiptNumber;
	
	public ReceiptNumber(Vector<String> values) {
		this.orderCode = (int) DBController.parseData(values.get(0), "int");
		this.receiptNumber = (int) DBController.parseData(values.get(1), "int");
	}

	public int getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(int orderCode) {
		this.orderCode = orderCode;
	}

	public int getReceiptNumber() {
		return receiptNumber;
	}

	public void setReceiptNumber(int receiptNumber) {
		this.receiptNumber = receiptNumber;
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
