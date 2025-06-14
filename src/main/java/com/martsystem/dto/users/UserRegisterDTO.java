package com.martsystem.dto.users;

import com.martsystem.constant.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class UserRegisterDTO {
	// 가입 양식: 아이디 (이메일) *
	@NotBlank(message = "이메일은 필수 입력 항목입니다.")
	@Email(message = "유효한 이메일 형식이 아닙니다.")
	private String email;

	// 가입 양식: 비밀번호 *
	@NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,16}$",
			message = "비밀번호는 8~16자, 영문 대소문자, 숫자, 특수문자를 포함해야 합니다.")
	private String password;

	// 가입 양식: 비밀번호 확인 * (프론트엔드에서 일치 여부 확인 후 백엔드로 전달)
	// 엔티티에는 저장되지 않지만, DTO에서는 유효성 검사를 위해 필요합니다.
	@NotBlank(message = "비밀번호 확인은 필수 입력 항목입니다.")
	private String passwordConfirm; // 비밀번호 일치 여부 검증용

	// 가입 양식: 사용자 닉네임 *
	@NotBlank(message = "닉네임은 필수 입력 항목입니다.")
	private String nickname;

	// 가입 양식: 전화번호 *
	@NotBlank(message = "전화번호는 필수 입력 항목입니다.")
	@Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다. (예: 010-1234-5678)")
	private String phoneNumber;

	// 사용자 역할 (클라이언트가 선택하는 회원 유형)
	@NotNull(message = "회원 유형은 필수 선택 항목입니다.")
	private UserRole role; // PRODUCER, MART_OWNER, DISTRIBUTOR, USER 등

	// 가입 양식: 서비스 이용 약관 동의 *
	@NotNull(message = "서비스 이용 약관 동의는 필수입니다.")
	private Boolean agreedToTerms;

	// 가입 양식: 개인정보 처리 방침 동의 *
	@NotNull(message = "개인정보 처리 방침 동의는 필수입니다.")
	private Boolean agreedToPrivacy;

	// 가입 양식: 광고성 정보수신 동의 (선택)
	// 기본값을 false로 설정하거나, null을 허용할 수 있습니다.
	private Boolean agreedToMarketing = false; // 초기값은 false로 설정
}
