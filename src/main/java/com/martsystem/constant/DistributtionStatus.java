package com.martsystem.constant;

enum DistributionStatus {
	PENDING("처리대기"),
	PROCESSING("처리중"),
	SHIPPED("배송중"),
	DELIVERED("배송완료");

	private final String description;

	DistributionStatus(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}