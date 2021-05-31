package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Zubayer Ahamed
 * @since May 29, 2021
 */
@Controller
@RequestMapping("/pos")
public class POSController extends ASLAbstractController {

	@GetMapping
	public String loadPOSPage(Model model) {
		
		return "pages/pos/pos";
	}
}
