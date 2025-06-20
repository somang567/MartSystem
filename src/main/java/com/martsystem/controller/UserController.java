package com.martsystem.controller;

import com.martsystem.constant.UserRole;
import com.martsystem.dto.users.UserRegisterDTO;
import com.martsystem.service.UserService;

import jakarta.validation.Valid; // @Valid 임포트
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping("/login")
	public String login() {
		return "user/login";
	}

	@GetMapping("/register")
	public String register() {
		return "user/userTypeSignup";
	}

	@PostMapping("/register/signupAgree")
	public String signUpAgreeProcess(
			@RequestParam("userRoleType") UserRole userRole,
			@RequestParam("termsOfService") boolean termsOfService,
			@RequestParam("privacyPolicy") boolean privacyPolicy,
			@RequestParam(value = "marketingConsent", defaultValue = "false") boolean marketingConsent,
			RedirectAttributes redirectAttributes) {

		if (!termsOfService || !privacyPolicy) {
			redirectAttributes.addFlashAttribute("errorMessage", "필수 약관에 모두 동의해야 다음 단계로 진행할 수 있습니다.");
			// 약관 동의 페이지로 다시 돌아갈 때 userRoleType을 쿼리 파라미터로 넘겨줌
			return "redirect:/register/signupAgree?type=" + userRole.name();
		}

		// Flash Attributes에 다음 페이지에서 필요한 정보를 담아 전달
		redirectAttributes.addFlashAttribute("userRoleType", userRole);
		redirectAttributes.addFlashAttribute("agreedToTerms", termsOfService);
		redirectAttributes.addFlashAttribute("agreedToPrivacy", privacyPolicy);
		redirectAttributes.addFlashAttribute("agreedToMarketing", marketingConsent);

		// 개인정보 입력 페이지로 리다이렉트
		return "redirect:/register/userInfoPersonal";
	}

	@GetMapping("/register/signupAgree")
	public String showSignupAgreePage(@RequestParam("type") UserRole userRole, Model model) {
		model.addAttribute("userRoleType", userRole);
		return "user/signupAgree";
	}

	// 3단계: 개인정보 입력 페이지 (GET)
	// 폼이 처음 로드되거나 유효성 검사 실패 후 리다이렉트될 때 호출됩니다.
	@GetMapping("/register/userInfoPersonal")
	public String userInfoPersonal(Model model) {

		// RedirectAttributes에서 넘어온 Flash Attribute를 가져옴
		// Flash Attribute는 한 번 사용하면 소멸되므로, 여기서 가져와서 다시 Model에 추가해야 합니다.
		UserRole userRole = (UserRole) model.asMap().get("userRoleType");
		Boolean agreedToTerms = (Boolean) model.asMap().getOrDefault("agreedToTerms", false);
		Boolean agreedToPrivacy = (Boolean) model.asMap().getOrDefault("agreedToPrivacy", false);
		Boolean agreedToMarketing = (Boolean) model.asMap().getOrDefault("agreedToMarketing", false);

		// userRole이 없으면 잘못된 접근이므로 첫 단계로 리다이렉트
		if (userRole == null) {
			return "redirect:/register";
		}

		// registrationDto가 이미 모델에 있다면 (예: 유효성 검사 실패 후 리다이렉트될 때), 그 DTO를 사용합니다.
		// BindingResult가 함께 넘어올 때도 이 방식으로 DTO가 모델에 다시 추가됩니다.
		UserRegisterDTO registrationDto = (UserRegisterDTO) model.asMap().get("registrationDto");
		if (registrationDto == null) {
			// 처음 폼을 로드하는 경우, 새로운 DTO를 생성하고 초기값 설정
			registrationDto = new UserRegisterDTO();
			registrationDto.setRole(userRole); // 역할 설정
			registrationDto.setAgreedToTerms(agreedToTerms); // 약관 동의 상태 설정
			registrationDto.setAgreedToPrivacy(agreedToPrivacy);
			registrationDto.setAgreedToMarketing(agreedToMarketing);
		} else {
			// 유효성 검사 실패 등으로 기존 DTO가 있다면, 약관 동의 상태를 다시 설정해줄 필요가 있을 수 있음
			// 그러나 th:hidden 필드로 관리하고 있으므로 폼 제출 시 자동으로 바인딩됩니다.
			// 여기서는 role만 다시 설정 (혹시 DTO에 role이 제대로 바인딩되지 않았을 경우를 대비)
			registrationDto.setRole(userRole);
		}

		// Model에 DTO와 기타 필요한 속성들을 추가하여 뷰로 전달
		model.addAttribute("registrationDto", registrationDto);
		model.addAttribute("userRoleType", userRole); // Thymeleaf에서 참조할 수 있도록 다시 추가
		model.addAttribute("agreedToTerms", agreedToTerms); // Hidden 필드에 사용될 값도 다시 추가
		model.addAttribute("agreedToPrivacy", agreedToPrivacy);
		model.addAttribute("agreedToMarketing", agreedToMarketing);

		return "user/userInfoPersonal"; // 뷰 이름 반환
	}

	// 4단계: 최종 회원가입 처리 (POST)
	@PostMapping("/register/complete")
	public String registerComplete(
			@Valid UserRegisterDTO dto, // @Valid 어노테이션으로 DTO 유효성 검사
			BindingResult bindingResult, // 유효성 검사 결과
			Model model, // 폼 실패 시 데이터 유지를 위해 RedirectAttributes 대신 Model 사용
			RedirectAttributes redirectAttributes) { // 성공 시 리다이렉트용

		// 유효성 검사 (Spring Validator가 처리)
		if (bindingResult.hasErrors()) {
			System.out.println("---- 서버 측 유효성 검사 오류 ----");
			bindingResult.getAllErrors().forEach(error -> {
				if (error instanceof FieldError) {
					FieldError fieldError = (FieldError) error;
					System.out.println("  Field: " + fieldError.getField() +
							", RejectedValue: '" + fieldError.getRejectedValue() + "'" +
							", Message: " + fieldError.getDefaultMessage());
				} else {
					System.out.println("  Global Error: " + error.getDefaultMessage());
				}
			});
			System.out.println("--- 서버 측 유효성 검사 오류 상세 끝 ---");


			model.addAttribute("registrationDto", dto);
			// bindingResult는 model.addAttribute("registrationDto", dto) 시 자동으로 함께 모델에 추가됩니다.
			// 따라서 "org.springframework.validation.BindingResult.registrationDto"를 수동으로 추가할 필요 없음.

			// Flash Attribute로 넘어왔던 약관 동의 및 역할 정보를 다시 Model에 추가 (뷰에서 사용할 수 있도록)
			model.addAttribute("userRoleType", dto.getRole());
			model.addAttribute("agreedToTerms", dto.getAgreedToTerms());
			model.addAttribute("agreedToPrivacy", dto.getAgreedToPrivacy());
			model.addAttribute("agreedToMarketing", dto.getAgreedToMarketing());

			// 에러 메시지 (일반적인 안내 메시지)
			model.addAttribute("errorMessage", "입력 내용을 다시 확인해주세요.");

			return "user/userInfoPersonal"; // 직접 뷰 반환 (리다이렉트 아님)
		}

		try {
			userService.registerUser(dto); // 서비스 호출

		} catch (IllegalArgumentException e) {
			// 서비스 레이어에서 발생한 비즈니스 로직 예외 처리 (예: 이메일 중복, 비밀번호 불일치 등)
			model.addAttribute("errorMessage", e.getMessage());
			model.addAttribute("registrationDto", dto); // 사용자 입력 데이터 다시 전달
			model.addAttribute("userRoleType", dto.getRole());
			model.addAttribute("agreedToTerms", dto.getAgreedToTerms());
			model.addAttribute("agreedToPrivacy", dto.getAgreedToPrivacy());
			model.addAttribute("agreedToMarketing", dto.getAgreedToMarketing());

			return "user/userInfoPersonal"; // 직접 뷰 반환 (리다이렉트 아님)
		}

		// 모든 처리 성공 시 회원가입 완료 페이지로 리다이렉트
		return "redirect:/register/registerSuccess";
	}

	@GetMapping("/register/registerSuccess")
	public String registerSuccess() {
		return "user/registerSuccess";
	}
}