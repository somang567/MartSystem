package com.martsystem.dto.users;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class DistributorRegisterDTO extends UserRegisterDTO{
	@NotBlank(message = "사업자등록번호는 필수 입력 항목입니다.")
	@Size(max = 20, message = "사업자등록번호는 최대 20자까지 입력 가능합니다.")
	private String businessRegistrationNumber; // 기존 엔티티에서 변경된 이름 유지

	// 사업장 주소 (가입 양식: 사업장 주소 * - 주소 API로 입력)
	// 엔티티 필드명: business_zip_code, business_road_address, business_detail_address
	// DTO 필드명: businessZipCode, businessRoadAddress, businessDetailAddress
	@NotBlank(message = "사업장 우편번호는 필수입니다.")
	@Size(max = 10, message = "사업장 우편번호는 최대 10자까지 입력 가능합니다.")
	private String businessZipCode; // 우편번호

	@NotBlank(message = "사업장 도로명 주소는 필수입니다.")
	@Size(max = 255, message = "사업장 도로명 주소는 최대 255자까지 입력 가능합니다.")
	private String businessRoadAddress; // 도로명 주소

	@NotBlank(message = "사업장 상세 주소는 필수입니다.")
	@Size(max = 255, message = "사업장 상세 주소는 최대 255자까지 입력 가능합니다.")
	private String businessDetailAddress; // 상세 주소 (사용자 직접 입력)

	// 업체명 (가입 양식: 업체명 *)
	// 엔티티 필드명: company_name -> DTO 필드명: companyName
	@NotBlank(message = "업체명은 필수 입력 항목입니다.")
	@Size(max = 100, message = "업체명은 최대 100자까지 입력 가능합니다.")
	private String companyName;

	// 대표자명 (가입 양식: 대표자명 *)
	// 엔티티 필드명: ceo_name -> DTO 필드명: ceoName
	@NotBlank(message = "대표자명은 필수 입력 항목입니다.")
	@Size(max = 50, message = "대표자명은 최대 50자까지 입력 가능합니다.")
	private String ceoName;

	// 업종 (가입 양식: 업종 - 드롭다운/분류)
	// 엔티티 필드명: business_category -> DTO 필드명: businessCategory
	@NotBlank(message = "업종은 필수 선택 항목입니다.") // 드롭다운이므로 @NotNull도 가능
	@Size(max = 50, message = "업종은 최대 50자까지 입력 가능합니다.")
	private String businessCategory;

	// 업무 담당자명 (가입 양식: 업무 담당자명)
	// 엔티티 필드명: contact_person_name -> DTO 필드명: contactPersonName
	@NotBlank(message = "업무 담당자명은 필수 입력 항목입니다.")
	@Size(max = 50, message = "업무 담당자명은 최대 50자까지 입력 가능합니다.")
	private String contactPersonName;

	// 사업장 번호 (가입 양식: 사업장 번호 (선택))
	// 엔티티 필드명: business_workplace_number -> DTO 필드명: businessWorkplaceNumber
	@Size(max = 50, message = "사업장 번호는 최대 50자까지 입력 가능합니다.") // 선택 사항이므로 @NotBlank 없음
	private String businessWorkplaceNumber;
}
