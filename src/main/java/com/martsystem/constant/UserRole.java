package com.martsystem.constant;

public enum UserRole {
	ADMIN("관리자"),
	PRODUCER("생산자"),
	MART("마트"),
	DISTRIBUTOR("유통업체");

	private final String description;

	UserRole(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}