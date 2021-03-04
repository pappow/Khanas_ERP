package com.asl.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.asl.service.RequisitionListService;

@Controller
@RequestMapping("/purchasing/bqls")
public class BranchesRequisitionsController extends ASLAbstractController {

	@Autowired private RequisitionListService requisitionListService;

	@GetMapping
	public String loadBqls(Model model) {
		model.addAttribute("bqlsList", requisitionListService.getAllBranchesRequisitions(new Date()));
		return "pages/purchasing/branchesrequisitions/bqls";
	}

	@GetMapping("/details")
	public String loadRqlsDetails(Model model) {
		model.addAttribute("bqlsDetailsList", requisitionListService.getAllBranchesRequisitionDetails(new Date()));
		return "pages/purchasing/branchesrequisitions/bqlsdetail";
	}

}
