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
@RequestMapping("/mastersetup/itemmaster")
public class ItemMasterController extends ASLAbstractController {

	@GetMapping
	public String loadItemMasterpage(Model model) {
		return "pages/mastersetup/itemmaster/itemmaster";
	}

}
