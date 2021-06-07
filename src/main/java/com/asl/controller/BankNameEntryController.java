package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account/banknameentry")
public class BankNameEntryController extends ASLAbstractController{
	
	@GetMapping
	public String LoadBankNameEntryPage(Model model) {
		
		return "pages/account/banknameentry/banknameentry";
	}

}
