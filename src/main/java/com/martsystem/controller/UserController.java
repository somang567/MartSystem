package com.martsystem.controller;

import com.martsystem.constant.UserRole;
import com.martsystem.dto.users.DistributorRegisterDTO;
import com.martsystem.dto.users.MartRegisterDTO;
import com.martsystem.dto.users.ProducerRegisterDTO;
import com.martsystem.dto.users.UserRegisterDTO;
import com.martsystem.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;

@Controller
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	// 로그인 페이지 (GET)
	@GetMapping("/login")
	public String login() {
		return "user/login"; // <--- 경로 수정: admin -> user
	}

	// 1단계: 가입 유형 선택 페이지 (GET)
	@GetMapping("/register")
	public String register() {
		return "user/userTypeSignup"; // <--- 경로 수정: admin -> user
	}

	// 2단계: 약관 동의 페이지 (POST)
	@PostMapping("/register/signupAgree")
	public String signUpAgreeProcess(
			@RequestParam("userRoleType") UserRole userRole,
			@RequestParam("termsOfService") boolean termsOfService,
			@RequestParam("privacyPolicy") boolean privacyPolicy,
			@RequestParam(value = "marketingConsent", defaultValue = "false") boolean marketingConsent,
			RedirectAttributes redirectAttributes) {

		if (!termsOfService || !privacyPolicy) {
			redirectAttributes.addFlashAttribute("errorMessage", "필수 약관에 모두 동의해야 다음 단계로 진행할 수 있습니다.");
			return "redirect:/register/signupAgree?type=" + userRole.name();
		}

		redirectAttributes.addFlashAttribute("userRoleType", userRole);
		redirectAttributes.addFlashAttribute("agreedToTerms", termsOfService);
		redirectAttributes.addFlashAttribute("agreedToPrivacy", privacyPolicy);
		redirectAttributes.addFlashAttribute("agreedToMarketing", marketingConsent);

		return "redirect:/register/userInfoPersonal";
	}

	// 2단계: 약관 동의 페이지 (GET)
	@GetMapping("/register/signupAgree")
	public String showSignupAgreePage(@RequestParam("type") UserRole userRole, Model model) {
		model.addAttribute("userRoleType", userRole);
		System.out.println("회원 가입 유형 선택됨: " + userRole.getDescription());
		return "user/signupAgree"; // <--- 경로 수정: admin -> user
	}


	// 3단계: 개인정보 입력 페이지 (GET)
	@GetMapping("/register/userInfoPersonal")
	public String userInfoPersonal(
			@RequestParam(value = "userRoleType", required = false) UserRole userRole,
			Model model) {

		UserRole sessionRole = (UserRole) model.asMap().get("userRoleType");
		if (sessionRole != null) {
			userRole = sessionRole;
		}

		if (userRole == null) {
			return "redirect:/register";
		}

		model.addAttribute("agreedToTerms", model.asMap().getOrDefault("agreedToTerms", false));
		model.addAttribute("agreedToPrivacy", model.asMap().getOrDefault("agreedToPrivacy", false));
		model.addAttribute("agreedToMarketing", model.asMap().getOrDefault("agreedToMarketing", false));

		if (userRole == UserRole.PRODUCER) {
			model.addAttribute("registrationDto", new ProducerRegisterDTO());
		} else if (userRole == UserRole.MART) {
			model.addAttribute("registrationDto", new MartRegisterDTO());
		} else if (userRole == UserRole.DISTRIBUTOR) {
			model.addAttribute("registrationDto", new DistributorRegisterDTO());
		} else {
			return "redirect:/register";
		}

		model.addAttribute("userRoleType", userRole);

		return "user/userInfoPersonal"; // <--- 경로 수정: admin -> user
	}

	// 4단계: 최종 회원가입 처리 (POST)
	@PostMapping("/register/complete")
	public String registerComplete(
			@Valid UserRegisterDTO dto,
			BindingResult bindingResult,
			RedirectAttributes redirectAttributes,
			@RequestParam("userRoleType") UserRole userRole) {

		UserRegisterDTO actualDto; // 실제 유저들의 입력 데이터를 넘겨줄 DTO

		if (userRole == UserRole.PRODUCER) {
			actualDto = (ProducerRegisterDTO) dto;
		} else if (userRole == UserRole.MART) {
			actualDto = (MartRegisterDTO) dto;
		} else if (userRole == UserRole.DISTRIBUTOR) {
			actualDto = (DistributorRegisterDTO) dto;
		} else {
			redirectAttributes.addFlashAttribute("errorMessage", "유효하지 않은 회원 유형입니다.");
			return "redirect:/register";
		}

		actualDto.setRole(userRole);

		if (bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registrationDto", bindingResult);
			redirectAttributes.addFlashAttribute("registrationDto", actualDto);
			redirectAttributes.addFlashAttribute("userRoleType", userRole);
			redirectAttributes.addFlashAttribute("errorMessage", "입력 내용을 다시 확인해주세요.");
			return "redirect:/register/userInfoPersonal";
		}

		try {
			userService.registerUser(actualDto);

		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			redirectAttributes.addFlashAttribute("registrationDto", actualDto);
			redirectAttributes.addFlashAttribute("userRoleType", userRole);
			return "redirect:/register/userInfoPersonal";
		}

		return "redirect:/register/registerSuccess";
	}

	// 회원가입 완료 페이지 (GET)
	@GetMapping("/register/registerSuccess")
	public String registerSuccess() {
		return "user/registerSuccess"; // <--- 경로 수정: admin -> user
	}
}