package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/landdocument")
public class LandDocumentController extends ASLAbstractController{
	
	@GetMapping
	public String loadLandDocumentPage(){
		return "pages/landdocument";
	}

}
