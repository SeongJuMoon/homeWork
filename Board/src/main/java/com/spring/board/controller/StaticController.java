package com.spring.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StaticController {

	@RequestMapping("/")
	public String home() {
		System.out.println("home file is exist??!");
		return "home";
	}
}
