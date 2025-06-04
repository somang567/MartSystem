package com.martsystem.controller;

import com.martsystem.constant.UserRole;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
	@GetMapping("/login")
	public String Login(){
		return "admin/login";
	}


	@GetMapping("/register")
	public String register(){
		return "admin/userTypeSignup";
	}

	@GetMapping("/register/signupAgree")
	public String signUpAgree(@RequestParam("type") UserRole userRole){

		if(userRole== UserRole.PRODUCER){
			System.out.println("생산자 회원 가입 정보입력페이지로 이동합니다."  + userRole.getDescription());
		}
		return "admin/signupAgree";
	}

	@GetMapping("/register/userInfoPersonal")
	public String userInfoPersonal(){
		return "admin/userInfoPersonal";
	}

	@GetMapping("/register/registerSuccess")
	public String RegisterSuccess(){
		return "admin/registerSuccess";
	}
}
