package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.asl.entity.Xroles;;

@Controller
@RequestMapping("/role")
public class RoleController extends ASLAbstractController {

	@GetMapping
	public String loadRolePage(Model model) {
		Xroles xroles = new Xroles();
		xroles.setXmaxdisc(1);
		xroles.setXmaxdiscf(1);
		model.addAttribute("roles", xroles);

		return "pages/role";
	}
}