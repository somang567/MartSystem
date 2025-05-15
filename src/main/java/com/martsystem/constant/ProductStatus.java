package com.martsystem.constant;

public enum ProductStatus {
	AVAILABLE("판매가능"),
	OUT_OF_STOCK("품절"),
	DISCONTINUED("판매중지");

	private final String description;

	ProductStatus(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}