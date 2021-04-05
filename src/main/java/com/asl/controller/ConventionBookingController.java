package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/conventionmanagement/booking")
public class ConventionBookingController extends ASLAbstractController {
	
	@GetMapping
	public String loadBookingPage(Model model) {
		
		return "pages/conventionmanagement/booking/booking";
	}

	@GetMapping("/{xunknown}")
	public String loadBookingPage(@PathVariable String xunknown, Model model) {
		

		return "pages/conventionmanagement/booking/booking";
	}

}
