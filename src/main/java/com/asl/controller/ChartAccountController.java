package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account/chartaccount")
public class ChartAccountController extends ASLAbstractController{
	
	@GetMapping
	public String loadChartAccountrPage() {
		return "pages/account/chartaccount/chartaccount";
	}
}
