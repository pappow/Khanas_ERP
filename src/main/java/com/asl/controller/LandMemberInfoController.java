package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/landmemberinfo")
public class LandMemberInfoController extends ASLAbstractController{
	
	@GetMapping
	public String loadLandMemberInfoPage() {
		
		
		return "pages/land/landmemberinfo";
	}

}
