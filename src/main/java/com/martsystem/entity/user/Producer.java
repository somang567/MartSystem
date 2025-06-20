package com.martsystem.entity.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.DiscriminatorValue;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("PRODUCER")
@Table(name = "producers")
@Getter @Setter @NoArgsConstructor
public class Producer extends User {

	@Column(name = "business_registration_number", nullable = false, unique = true, length = 20)
	private String businessRegistrationNumber;

	@Column(name = "company_name", nullable = false, length = 100)
	private String companyName;

	@Column(name = "business_zip_code", nullable = false, length = 10)
	private String businessZipCode;

	@Column(name = "business_road_address", nullable = false, length = 255)
	private String businessRoadAddress;

	@Column(name = "business_detail_address", nullable = false, length = 255)
	private String businessDetailAddress;

	// 대표자명 (가입 양식: 대표자명 *)
	@Column(name = "ceo_name", nullable = false, length = 50)
	private String ceoName;


	// 사업장 번호 (가입 양식: 사업장 번호 (선택))
	@Column(name = "business_workplace_number", length = 50)
	private String businessWorkplaceNumber;

	// 우편번호
	@Column(name = "farm_zip_code", length = 10) // nullable 여부는 비즈니스 정책에 따름
	private String farmZipCode;
	// 도로명 주소
	@Column(name = "farm_road_address", length = 255)
	private String farmRoadAddress;
	// 상세 주소 (사용자 직접 입력)
	@Column(name = "farm_detail_address", length = 255)
	private String farmDetailAddress;
}