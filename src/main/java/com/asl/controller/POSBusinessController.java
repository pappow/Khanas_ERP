package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/posbusiness")
public class POSBusinessController extends ASLAbstractController{
	
	@GetMapping
	public String loadPOSBusinessPage() {
		
		return"pages/posbusiness";
	}

}
