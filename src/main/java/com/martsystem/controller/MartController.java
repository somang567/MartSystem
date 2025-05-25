package com.martsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MartController {
	@GetMapping("/  ")
	public String test(){
		return "index";
	}

	@GetMapping("/mart")
	public String testMart(){
		return "mart/martHome";
	}
}