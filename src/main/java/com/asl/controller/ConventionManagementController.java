package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/conventionmanagement")
public class ConventionManagementController extends ASLAbstractController {
	
	@GetMapping
	public String loadConventionManagementMenuPage(Model model) {
		return "pages/conventionmanagement/conventionmanagement";
	}


}
