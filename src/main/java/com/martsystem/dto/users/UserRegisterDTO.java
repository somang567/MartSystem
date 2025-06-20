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
	// 정규식 수정: 숫자만 10~11자리 허용 (예: 01012345678)
	@Pattern(regexp = "^\\d{10,11}$", message = "전화번호는 10~11자리의 숫자만 입력해주세요. (예: 01012345678)")
	private String phoneNumber;


	@NotNull(message = "회원 유형은 필수 선택 항목입니다.")
	private UserRole role;

	@NotNull(message = "서비스 이용 약관 동의는 필수입니다.")
	private Boolean agreedToTerms;

	@NotNull(message = "개인정보 처리 방침 동의는 필수입니다.")
	private Boolean agreedToPrivacy;

	private Boolean agreedToMarketing = false;

	// 비밀번호 찾기 질문/답변 필드 추가
	@NotBlank(message = "비밀번호 분실 시 확인 질문은 필수 항목입니다.")
	private String passwordHintQuestion; // HTML의 id와 일치하도록 명명
	@NotBlank(message = "비밀번호 분실 시 확인 답변은 필수 항목입니다.")
	private String passwordHintAnswer;   // HTML의 id와 일치하도록 명명


	// ========= 사업자 회원 (PRODUCER, MART, DISTRIBUTOR) 공통 필드 =========
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