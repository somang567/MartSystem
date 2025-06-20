package com.martsystem.entity.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.DiscriminatorValue;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity // JPA 엔티티
@DiscriminatorValue("DISTRIBUTOR") // 부모(User)의 user_type 컬럼에 "DISTRIBUTOR"로 저장될 값
@Table(name = "distributors") // 이 엔티티의 고유 정보가 저장될 테이블 이름
@Getter @Setter @NoArgsConstructor
public class Distributor extends User { // User 추상 클래스를 상속받습니다.

	// 사업자등록번호 (가입 양식: 사업자등록번호 *)
	@Column(name = "business_registration_number", nullable = false, unique = true, length = 20)
	private String businessRegistrationNumber;

	// 사업장 주소 (가입 양식: 사업장 주소 * - 주소 API로 입력)
	@Column(name = "business_zip_code", nullable = false, length = 10)
	private String businessZipCode; // 우편번호
	@Column(name = "business_road_address", nullable = false, length = 255)
	private String businessRoadAddress; // 도로명 주소
	@Column(name = "business_detail_address", nullable = false, length = 255)
	private String businessDetailAddress; // 상세 주소 (사용자 직접 입력)

	// 업체명 (가입 양식: 업체명 *)
	@Column(name = "company_name", nullable = false, length = 100)
	private String companyName;

	// 대표자명 (가입 양식: 대표자명 *)
	@Column(name = "ceo_name", nullable = false, length = 50)
	private String ceoName;

	// 업종 (가입 양식: 업종 - 드롭다운/분류)
	// 유통업체의 세부 업종 분류 (예: "물류", "도소매", "택배" 등)
	@Column(name = "business_category", nullable = false, length = 50)
	private String businessCategory;

	// 사업장 번호 (가입 양식: 사업장 번호 (선택))
	@Column(name = "business_workplace_number", length = 50)
	private String businessWorkplaceNumber;

}