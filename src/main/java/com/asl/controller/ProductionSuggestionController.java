package com.asl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.asl.service.ProductionSuggestionService;

/**
 * @author Zubayer Ahamed
 * @since Mar 10, 2021
 */
@Controller
@RequestMapping("/production/suggestion")
public class ProductionSuggestionController extends ASLAbstractController {

	@Autowired private ProductionSuggestionService productionSuggestionService;

	@GetMapping
	public String loadSuggestion(Model model) {
		model.addAttribute("suggestions", productionSuggestionService.getProductionSuggestion());
		return "pages/production/suggestion/suggestion";
	}

	@GetMapping("/create/{xordernum}")
	public String createSuggestionAndLoadSuggestionPage(@PathVariable String xordernum, Model model) {
		productionSuggestionService.createSuggestion(xordernum);
		return "redirect:/production/suggestion";
	}
}
