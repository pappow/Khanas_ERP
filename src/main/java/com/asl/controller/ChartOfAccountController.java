package com.asl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.asl.entity.Acmst;
import com.asl.service.AcmstService;

@Controller
@RequestMapping("/account/coa")
public class ChartOfAccountController extends ASLAbstractController {

	@Autowired private AcmstService acmstService;

	@GetMapping
	public String loadChartAccountrPage(Model model) {
		model.addAttribute("acmst", getDefaultAcmst());
		return "pages/account/chartofaccount/chartofaccount";
	}

	private Acmst getDefaultAcmst() {
		Acmst acmst = new Acmst();

		return acmst;
	}
}
