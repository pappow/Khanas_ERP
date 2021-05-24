package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/printsettings")
public class PrintSettingsController extends ASLAbstractController{
	
	@GetMapping
	public String loadPrintSettingsPage() {
		return "pages/printsettings";
		
	}

}
