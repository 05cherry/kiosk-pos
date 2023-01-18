package com._team.DB;

import java.util.Vector;

public class Product {
	private final static String[] COLUMN_NAME =  {"상품 코드","상 품 명","카테 고리","상품 가격","상품 이미지","품    절"};
	private int code;
	private String name;
	private int category;
	private int price;
	private String imgSrc;
	private boolean soldOut;
	
	public Product(int code, String name, int category, int price, String imgSrc, boolean soldOut) {
		this.code = code;
		this.name = name;
		this.category = category;
		this.price = price;
		this.imgSrc = imgSrc;
		this.soldOut = soldOut;
	}

	public Product(Vector<String> values) {
		this.code = (int) DBController.parseData(values.get(0), "int");
		this.name = (String) DBController.parseData(values.get(1), "String");
		this.category = (int) DBController.parseData(values.get(2), "int");
		this.price = (int) DBController.parseData(values.get(3), "int");
		this.imgSrc = (String) DBController.parseData(values.get(4), "String");
		this.soldOut = (boolean) DBController.parseData(values.get(5), "boolean");
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

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getImgSrc() {
		return imgSrc;
	}

	public void setImgSrc(String imgSrc) {
		this.imgSrc = imgSrc;
	}

	public boolean isSoldOut() {
		return soldOut;
	}

	public void setSoldOut(boolean soldOut) {
		this.soldOut = soldOut;
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
