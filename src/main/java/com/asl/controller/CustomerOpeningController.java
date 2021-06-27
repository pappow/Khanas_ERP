package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/customeropening")
public class CustomerOpeningController extends ASLAbstractController{
	
	@GetMapping
	public String loadCustomerOpeningPage() {
		
		return"pages/accounts/customeropening";
	}

}
