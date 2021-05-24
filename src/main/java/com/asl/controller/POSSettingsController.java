package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/possettings")
public class POSSettingsController extends ASLAbstractController{

	
	@GetMapping
	public String loadPage() { 
		
		return "pages/possettings";
}
}
