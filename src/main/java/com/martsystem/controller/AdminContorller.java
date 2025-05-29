package com.martsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminContorller {
	@GetMapping("/login")
	public String Login(){
		return "admin/login";
	}

	@GetMapping("/signup")
	public String signUp(){
		return "admin/signup";
	}
}
