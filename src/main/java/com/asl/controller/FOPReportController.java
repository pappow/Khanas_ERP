package com.asl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.asl.enums.CodeType;
import com.asl.service.ImstockService;
import com.asl.service.XcodesService;

/**
 * @author Zubayer Ahamed
 * @since Mar 31, 2021
 */
@Controller
@RequestMapping("/report2")
public class FOPReportController extends ASLAbstractController{

	@Autowired private ImstockService imstockService;
	@Autowired private XcodesService xcodesService;

	@GetMapping("/STOCKL")
	public String loadStocklPage(Model model) {
		model.addAttribute("xwh", xcodesService.findByXtype(CodeType.WAREHOUSE.getCode(), Boolean.TRUE));
//		model.addAttribute("imstocks", imstockService.search(xwh, xitem));
		return "pages/report/report2/stockl";
	}
}
