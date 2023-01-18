package com._team.pos;

class Attr {
	private String name, value;
	private int compType;

	Attr(String name, String value, int compType) {
		this.name = name;
		this.value = value;
		this.compType = compType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getCompType() {
		return compType;
	}

	public void setCompType(int compType) {
		this.compType = compType;
	}

}
