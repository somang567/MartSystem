package com.martsystem.entity.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.DiscriminatorValue;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity // JPA 엔티티
@DiscriminatorValue("MART") // 부모(User)의 user_type 컬럼에 "MART_OWNER"로 저장될 값
@Table(name = "mart") // 이 엔티티의 고유 정보가 저장될 테이블 이름
@Getter @Setter @NoArgsConstructor
// 대표자명 (가입 양식: 대표자명 *)
// 업무 담당자명 (가입 양식: 업무 담당자명)
public class Mart extends User { // User 추상 클래스를 상속받습니다.

	@Column(name = "business_registration_number", nullable = false, unique = true, length = 20)
	private String businessRegistrationNumber;

	@Column(name = "company_name", nullable = false, length = 100)
	private String companyName;

	// 사업장 번호 (가입 양식: 사업장 번호 (선택))
	@Column(name = "business_workplace_number", length = 50)
	private String businessWorkplaceNumber;

	@Column(name = "business_zip_code", nullable = false, length = 10)
	private String businessZipCode;
	@Column(name = "business_road_address", nullable = false, length = 255)
	private String businessRoadAddress;
	@Column(name = "business_detail_address", nullable = false, length = 255)
	private String businessDetailAddress;

	@Column(name = "contact_person_name", nullable = false, length = 50)
	private String contactPersonName;

	@Column(name = "mart_phone_number", length = 20)
	private String martPhoneNumber;
}