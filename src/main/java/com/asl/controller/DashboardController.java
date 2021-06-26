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
		if(isKhanas()) {
			return "pages/dashboard/dashboard-khanas";
		} else if (isTCC()) {
			return "pages/dashboard/dashboard-tcc";
		} else if (isBoshila()) {
			return "pages/dashboard/dashboard-boshila";
		}

		return "pages/dashboard/dashboard";
	}
}
