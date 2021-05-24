package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/servicecharge")
public class ServiceChargeContoller extends ASLAbstractController{
	
	
	@GetMapping
	public String loadServiceChargePage() {
		
		return"pages/servicecharge";
	}

}
