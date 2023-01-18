package com._team.DB;

import java.util.Vector;

public class OrderEach {
	private final static String[] COLUMN_NAME = { "orderCode", "customerCode", "dateTime", "payAmount", "status",
			"payType", "takeOut" };
	private int order_main_code;
	private int product_code;
	private int eachPrice;
	private int eachNum;
	private boolean size;
	private boolean shot;
	private boolean hotOrIce;
	private boolean cream;
	private boolean hazelSyrup;
	private boolean almondSyrup;
	private boolean vanillaSyrup;

	public OrderEach() {
	};

	public OrderEach(Vector<String> values) {
		this.order_main_code = (int) DBController.parseData(values.get(0), "int");
		this.product_code = (int) DBController.parseData(values.get(1), "int");
		this.eachPrice = (int) DBController.parseData(values.get(2), "int");
		this.eachNum = (int) DBController.parseData(values.get(3), "int");
		this.size = (boolean) DBController.parseData(values.get(4), "boolean");
		this.shot = (boolean) DBController.parseData(values.get(5), "boolean");
		this.hotOrIce = (boolean) DBController.parseData(values.get(6), "boolean");
		this.cream = (boolean) DBController.parseData(values.get(7), "boolean");
		this.hazelSyrup = (boolean) DBController.parseData(values.get(8), "boolean");
		this.almondSyrup = (boolean) DBController.parseData(values.get(9), "boolean");
		this.vanillaSyrup = (boolean) DBController.parseData(values.get(10), "boolean");
	}

	public int getOrder_main_code() {
		return order_main_code;
	}

	public void setOrder_main_code(int order_main_code) {
		this.order_main_code = order_main_code;
	}

	public int getProduct_code() {
		return product_code;
	}

	public void setProduct_code(int product_code) {
		this.product_code = product_code;
	}

	public int getEachPrice() {
		return eachPrice;
	}

	public void setEachPrice(int eachPrice) {
		this.eachPrice = eachPrice;
	}

	public int getEachNum() {
		return eachNum;
	}

	public void setEachNum(int eachNum) {
		this.eachNum = eachNum;
	}

	public boolean isSize() {
		return size;
	}

	public void setSize(boolean size) {
		this.size = size;
	}

	public boolean isShot() {
		return shot;
	}

	public void setShot(boolean shot) {
		this.shot = shot;
	}

	public boolean isHotOrIce() {
		return hotOrIce;
	}

	public void setHotOrIce(boolean hotOrIce) {
		this.hotOrIce = hotOrIce;
	}

	public boolean isCream() {
		return cream;
	}

	public void setCream(boolean cream) {
		this.cream = cream;
	}

	public boolean isHazelSyrup() {
		return hazelSyrup;
	}

	public void setHazelSyrup(boolean hazelSyrup) {
		this.hazelSyrup = hazelSyrup;
	}

	public boolean isAlmondSyrup() {
		return almondSyrup;
	}

	public void setAlmondSyrup(boolean almondSyrup) {
		this.almondSyrup = almondSyrup;
	}

	public boolean isVanillaSyrup() {
		return vanillaSyrup;
	}

	public void setVanillaSyrup(boolean vanillaSyrup) {
		this.vanillaSyrup = vanillaSyrup;
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