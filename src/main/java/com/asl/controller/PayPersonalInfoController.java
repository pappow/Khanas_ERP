package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/paypersonal")
public class PayPersonalInfoController extends ASLAbstractController{
	
	@GetMapping
	public String loadPayPersonalInfoPage() {
		
		return "pages/hr/paypersonal";
	}

}
