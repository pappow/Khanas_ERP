package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account/voucher")
public class AccountVoucherController extends ASLAbstractController{
	
	@GetMapping
	public String loadAccountVoucherPage() {
		return "pages/account/voucher/voucher";
	}

}
