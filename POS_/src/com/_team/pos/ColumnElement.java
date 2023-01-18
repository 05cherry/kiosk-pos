package com._team.pos;

public class ColumnElement {
	public String day;
	public long value;
	public ColumnElement(String day, int value) {
		this.day = day;
		this.value = value;
	}
	
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public long getValue() {
		return value;
	}
	public void addValue(long value) {
		this.value += value;
	}
	
	
}
