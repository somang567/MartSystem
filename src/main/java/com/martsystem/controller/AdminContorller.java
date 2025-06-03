package com.martsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminContorller {
	@GetMapping("/login")
	public String Login(){
		return "admin/login";
	}


	@GetMapping("/register")
	public String register(){
		return "admin/userTypeSignup";
	}

	@GetMapping("/register/signupAgree")
	public String signUpAgree(){
		return "admin/signup";
	}

	@GetMapping("/register/userInfoPersonal")
	public String userInfoPersonal(){
		return "admin/userInfoPersonal";
	}
}
