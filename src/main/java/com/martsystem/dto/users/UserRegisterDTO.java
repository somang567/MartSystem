package com.martsystem.dto.users;

import com.martsystem.constant.UserRole;
// import com.martsystem.validator.DistributorDetailsRequired; // 유통사 전용 유효성 검사를 사용한다면 유지

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank; // @NotBlank 임포트 유지 (다른 필드에 사용되므로)
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
// @DistributorDetailsRequired // 특정 역할에만 필요한 필드를 검증할 커스텀 어노테이션 (필요시 사용)
public class UserRegisterDTO {

	// 공통 필드
	@NotBlank(message = "이메일은 필수 입력 항목입니다.")
	@Email(message = "유효한 이메일 형식이 아닙니다.")
	private String email;

	@NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
	@Pattern(
			regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,16}$",
			message = "비밀번호는 8~16자, 영문, 숫자, 특수문자를 포함해야 합니다."
	)
	private String password;

	@NotBlank(message = "비밀번호 확인은 필수 입력 항목입니다.")
	private String passwordConfirm;

	@NotBlank(message = "닉네임은 필수 입력 항목입니다.")
	private String nickname;

	@NotBlank(message = "전화번호는 필수 입력 항목입니다.")
	@Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다.")
	private String phoneNumber;

	@NotNull(message = "회원 유형은 필수 선택 항목입니다.")
	private UserRole role;

	@NotNull(message = "서비스 이용 약관 동의는 필수입니다.")
	private Boolean agreedToTerms;

	@NotNull(message = "개인정보 처리 방침 동의는 필수입니다.")
	private Boolean agreedToPrivacy;

	// 선택 마케팅 수신여부
	private Boolean agreedToMarketing = false;

	// ========= 사업자 회원 (PRODUCER, MART, DISTRIBUTOR) 공통 필드 =========
	// HTML에서 카카오 API를 통해 채워지는 필드이므로 @NotBlank 제거
	private String businessRegistrationNumber;
	private String businessZipCode;
	private String businessRoadAddress;
	private String businessDetailAddress;

	@NotBlank(message = "업체명은 필수 입력 항목입니다.")
	private String companyName;

	@NotBlank(message = "대표자명은 필수 입력 항목입니다.")
	private String ceoName;

	@NotBlank(message = "업무담당자명은 필수 입력 항목입니다.")
	private String contactPersonName;
}