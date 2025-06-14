package com.martsystem.dto.users;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProducerRegisterDTO extends UserRegisterDTO {
	@NotBlank(message = "사업자등록번호는 필수 입력 항목입니다.")
	@Size(max = 20, message = "사업자등록번호는 최대 20자까지 입력 가능합니다.") // 엔티티 길이와 맞춤
	private String businessRegistrationNumber;

	// 업체명 (가입 양식: 업체명 *)
	// Entitiy: company_name
	@NotBlank(message = "업체명은 필수 입력 항목입니다.")
	@Size(max = 100, message = "업체명은 최대 100자까지 입력 가능합니다.")
	private String companyName;

	// 사업장 주소 (가입 양식: 사업장 주소 * - 주소 API로 입력)
	// Entitiy: business_zip_code, business_road_address, business_detail_address
	@NotBlank(message = "사업장 우편번호는 필수입니다.")
	@Size(max = 10, message = "사업장 우편번호는 최대 10자까지 입력 가능합니다.")
	private String businessZipCode; // 우편번호

	@NotBlank(message = "사업장 도로명 주소는 필수입니다.")
	@Size(max = 255, message = "사업장 도로명 주소는 최대 255자까지 입력 가능합니다.")
	private String businessRoadAddress; // 도로명 주소

	@NotBlank(message = "사업장 상세 주소는 필수입니다.")
	@Size(max = 255, message = "사업장 상세 주소는 최대 255자까지 입력 가능합니다.")
	private String businessDetailAddress; // 상세 주소 (사용자 직접 입력)

	// 대표자명 (가입 양식: 대표자명 *)
	// Entitiy: ceo_name
	@NotBlank(message = "대표자명은 필수 입력 항목입니다.")
	@Size(max = 50, message = "대표자명은 최대 50자까지 입력 가능합니다.")
	private String ceoName;

	// 업무 담당자명 (가입 양식: 업무 담당자명)
	// Entitiy: contact_person_name
	@NotBlank(message = "업무 담당자명은 필수 입력 항목입니다.")
	@Size(max = 50, message = "업무 담당자명은 최대 50자까지 입력 가능합니다.")
	private String contactPersonName;

	// 사업장 번호 (가입 양식: 사업장 번호 (선택))
	// Entitiy: business_workplace_number
	@Size(max = 50, message = "사업장 번호는 최대 50자까지 입력 가능합니다.") // 선택 사항이므로 @NotBlank 없음
	private String businessWorkplaceNumber;

	// 농장 주소 (Entitiy: farm_zip_code, farm_road_address, farm_detail_address)
	// - 필수 여부는 비즈니스 정책에 따름. 가입 시 필수가 아니라면 @NotBlank/NotNull 제거.
	// 현재 엔티티에서 nullable 여부가 명시되어 있지 않으므로, DTO에서는 @NotBlank를 일단 제거했습니다.
	// (필요 시 주석 풀고 사용)
	@Size(max = 10, message = "농장 우편번호는 최대 10자까지 입력 가능합니다.")
	private String farmZipCode;
	@Size(max = 255, message = "농장 도로명 주소는 최대 255자까지 입력 가능합니다.")
	private String farmRoadAddress;
	@Size(max = 255, message = "농장 상세 주소는 최대 255자까지 입력 가능합니다.")
	private String farmDetailAddress;

}
