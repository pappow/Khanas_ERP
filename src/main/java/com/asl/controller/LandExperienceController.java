package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/landexperience")
public class LandExperienceController extends ASLAbstractController{
	
	@GetMapping
	public String loadLandExperiencePage() {
		
		return "pages/landexperience";
	}

}
