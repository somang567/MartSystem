package com.martsystem.dto.users;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MartRegisterDTO extends UserRegisterDTO {
	@NotBlank(message = "사업자등록번호는 필수 입력 항목입니다.")
	@Size(max = 20, message = "사업자등록번호는 최대 20자까지 입력 가능합니다.") // 엔티티 길이와 맞춤
	private String businessRegistrationNumber;

	// 업체명 (가입 양식: 업체명 *)
	// Entitiy: company_name
	@NotBlank(message = "업체명은 필수 입력 항목입니다.")
	@Size(max = 100, message = "업체명은 최대 100자까지 입력 가능합니다.")
	private String companyName;

	// 사업장 번호 (가입 양식: 사업장 번호 (선택))
	// Entitiy: business_workplace_number
	@Size(max = 50, message = "사업장 번호는 최대 50자까지 입력 가능합니다.") // 선택 사항이므로 @NotBlank 없음
	private String businessWorkplaceNumber;

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

	// 업무 담당자명 (가입 양식: 업무 담당자명)
	// Entitiy: contact_person_name
	@NotBlank(message = "업무 담당자명은 필수 입력 항목입니다.")
	@Size(max = 50, message = "업무 담당자명은 최대 50자까지 입력 가능합니다.")
	private String contactPersonName;

	// 마트 대표 전화번호 (Entitiy: mart_phone_number)
	@Size(max = 20, message = "마트 전화번호는 최대 20자까지 입력 가능합니다.")
	@Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "마트 전화번호 형식이 올바르지 않습니다. (예: 02-1234-5678)")
	private String martPhoneNumber;
}
