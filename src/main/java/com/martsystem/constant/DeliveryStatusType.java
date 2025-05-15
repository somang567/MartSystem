package com.martsystem.constant;

public enum DeliveryStatusType {
	PENDING("처리대기"),
	PROCESSING("처리중"),
	SHIPPED("배송중"),
	DELIVERED("배송완료");

	private final String description;


	DeliveryStatusType(String description) {
		this.description = description;
	}
}