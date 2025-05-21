package com.martsystem.constant;

public enum NotificationType {
	// 생산자
	NEW_ORDER, // ( 새 주문 접수 )
	STOCK_LOW, // ( 재고 부족 )
	PAYMENT_CONFIRMATION, // ( 결제 확인 )

	// 유통업자
	ORDER_RECEIVED, // ( 마트에서 주문한 품목 발주 접수 )
	DELIVERY_SCHEDULED, // ( 배송알림 )
	DELIVERY_DELAYED, // ( 재고알림 )

	// 마트 (소비자 역할)
	ORDER_STATUS_UPDATED, // ( 주문 상태 변경 )
	PROMOTION, // ( 프로모션 정보 )
	STOCK_ALERT, // ( 재고 알림 )

	// 관리자
	SYSTEM_ALERT, // ( 시스템 경고 )
	USER_REPORT, // ( 사용자 신고 )
	SECURITY_ALERT // ( 보안 경고 )
}
