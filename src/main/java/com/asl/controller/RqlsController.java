package com.asl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.asl.service.RequisitionListService;

@Controller
@RequestMapping("/purchasing/rqls")
public class RqlsController extends ASLAbstractController {

	@Autowired private RequisitionListService requisitionListService;

	@GetMapping
	public String loadRqlsPage(Model model) {
		model.addAttribute("rqlsList", requisitionListService.getAllBranchesRequisitionList());
		return "pages/purchasing/rqls/rqls";
	}

}
