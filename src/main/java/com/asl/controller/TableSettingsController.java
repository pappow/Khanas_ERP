package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tablesettings")
public class TableSettingsController extends ASLAbstractController{
	@GetMapping
	public String loadTableSettingsPage() {
		
		
		return"pages/tablesettings";
	}

}
