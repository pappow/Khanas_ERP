package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/shiftsettings")
public class ShiftSettingsController extends ASLAbstractController{
	
	@GetMapping
public String loadShiftSettingsPage() {
	
	
	return"pages/shiftsettings";
}
}
