package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account/default")
public class AccountDefaultController extends ASLAbstractController {
	
	@GetMapping
	public String LoadAccountDefaultPage(Model model) {
		return "pages/account/accountdefault/accountdefault";
	}

}
