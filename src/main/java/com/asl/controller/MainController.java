package com.asl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.asl.service.ListService;

/**
 * @author Zubayer Ahamed
 * @since Feb 16, 2021
 */
@Controller
@RequestMapping("/")
public class MainController {

	@Autowired private ListService listService;

	@GetMapping
	public String loadHomePage(Model model) {
		
		listService.findAllListHead().stream().forEach(r -> {
			System.out.println(r.toString());
		});
		
		return "index";
	}
}
