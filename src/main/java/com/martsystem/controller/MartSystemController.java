package com.martsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.RequiredArgsConstructor;

import java.lang.module.FindException;

@Controller
@RequiredArgsConstructor
public class MartSystemController {
	@GetMapping("/test")
	public String test(){
		return "index";
	}
}