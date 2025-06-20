// src/main/java/com/yourcompany/yourapp/dto/BusinessStatusData.java
// ⭐ 새로 생성하는 파일입니다 ⭐
package com.martsystem.dto.api; // ⭐ 실제 패키지 경로로 변경하세요 ⭐

import lombok.Data; // ⭐ Lombok 임포트 ⭐

@Data // ⭐ Lombok 어노테이션으로 Getter/Setter 등을 자동 생성 ⭐
public class BusinessStatusData {
	private String b_no;       // 사업자등록번호
	private String b_stt;      // 사업자상태 (예: 계속사업자, 휴업자, 폐업자)
	private String b_stt_cd;   // 사업자상태코드
	private String tax_type;   // 과세유형
	private String tax_type_cd; // 과세유형코드
	private String rprt_cd;    // 신고코드
	private String close_dt;   // 폐업일자 (YYYYMMDD, 폐업 시에만 존재)
}