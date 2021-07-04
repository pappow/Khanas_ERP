package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account/coa")
public class ChartOfAccountController extends ASLAbstractController{

	@GetMapping
	public String loadChartAccountrPage(Model model) {
		return "pages/account/chartofaccount/chartofaccount";
	}
}
