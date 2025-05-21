package com.martsystem.constant;

public enum ReportType {
	/*
	 * 전체 판매 요약 보고서
	 * - 총 판매량과 매출을 요약해서 보여줌
	 */
	SALES_SUMMARY,

	/*
	 * 재고 현황 및 변동 추적
	 * - 현재 재고 상태와 재고 변동 내역 분석
	 */
	INVENTORY_STATUS,

	/*
	    * 수익 상세 분석
	    * 판매 수익과 비용, 이익 등을 상세히 분석
	 */
	REVENUE_ANALYSIS,

	/*
	 * 주문 처리 내역 및 상태
	 * - 주문별 처리 현황과 이력 확인
	 */
	ORDER_HISTORY,

	/*
	    * 개별 상품별 판매 실적 분석
	    * 상품별 판매량, 매출 등의 성과 지표 제공
	 */
	PRODUCT_PERFORMANCE
}
