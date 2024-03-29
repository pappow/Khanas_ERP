package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Zubayer Ahamed
 * @since Feb 24, 2021
 */
@Controller
@RequestMapping("mastersetup/cap")
public class CodeAndParametersController extends ASLAbstractController {

	@GetMapping
	public String loadCodeAndParametersPage(Model model) {
		
		return "pages/mastersetup/cap/cap";
	}
}
