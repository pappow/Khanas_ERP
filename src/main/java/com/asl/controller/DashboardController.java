package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Zubayer Ahamed
 * @since Jan 5, 2021
 */
@Controller
@RequestMapping("/dashboard")
public class DashboardController extends ASLAbstractController {

	@GetMapping
	public String loadDashboard(Model model) {
		return "pages/dashboard/dashboard";
	}
}
