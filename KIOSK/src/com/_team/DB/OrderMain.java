package com._team.DB;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Vector;

public class OrderMain {
	private final static String[] COLUMN_NAME = { "code", "customerCode", "dateTime", "payAmount", "status", "payType",
			"takeOut" };
	private int code;
	private int customerCode;
	private Timestamp dateTime;
	private int payAmount;
	private int usePoint;
	private String payType;
	private boolean takeOut;

	public OrderMain() {
	};

	public OrderMain(Vector<String> values) {
		this.code = (int) DBController.parseData(values.get(0), "int");
		this.customerCode = (int) DBController.parseData(values.get(1), "int");
		this.dateTime = (Timestamp) DBController.parseData(values.get(2), "Timestamp");
		this.payAmount = (int) DBController.parseData(values.get(3), "int");
		this.payType = (String) DBController.parseData(values.get(5), "String");
		this.takeOut = (boolean) DBController.parseData(values.get(6), "boolean");
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(int customerCode) {
		this.customerCode = customerCode;
	}

	public Timestamp getDateTime() {
		return dateTime;
	}

	public void setDateTime(Timestamp dateTime) {
		this.dateTime = dateTime;
	}

	public int getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(int payAmount) {
		this.payAmount = payAmount;
	}

	public int getUsePoint() {
		return usePoint;
	}

	public void setUsePoint(int usePoint) {
		this.usePoint = usePoint;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public boolean isTakeOut() {
		return takeOut;
	}

	public void setTakeOut(boolean takeOut) {
		this.takeOut = takeOut;
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
	public LocalDate getDate() {
		String s = getDateTime().toString().substring(0,10);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate date = LocalDate.parse(s, formatter);
		return date;
	}
}