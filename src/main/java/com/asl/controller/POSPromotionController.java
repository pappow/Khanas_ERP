package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/promotion")
public class POSPromotionController extends ASLAbstractController{

	
	@GetMapping
	public String loadPromotionPage() {
		
		return"pages/promotion";
	}
}
