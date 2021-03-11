package com.asl.controller;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.asl.entity.Opordheader;
import com.asl.enums.TransactionCodeType;
import com.asl.service.OpordService;
import com.asl.service.ProductionSuggestionService;

/**
 * @author Zubayer Ahamed
 * @since Mar 10, 2021
 */
@Controller
@RequestMapping("/production/suggestion")
public class ProductionSuggestionController extends ASLAbstractController {

	@Autowired private OpordService opordService;
	@Autowired private ProductionSuggestionService productionSuggestionService;

	@GetMapping
	public String loadSuggestion(@RequestParam(required = false) String xordernum, Model model) {
		Opordheader chalan = null;
		if(StringUtils.isBlank(xordernum)) {
			chalan = opordService.findOpordHeaderByXtypetrnAndXtrnAndXdate(TransactionCodeType.CHALAN_NUMBER.getCode(), TransactionCodeType.CHALAN_NUMBER.getdefaultCode(), new Date());
		} else {
			chalan = opordService.findOpordHeaderByXordernum(xordernum);
		}
		model.addAttribute("suggestions", productionSuggestionService.getProductionSuggestion(xordernum, chalan.getXdate()));
		return "pages/production/suggestion/suggestion";
	}

	@GetMapping("/create/{xordernum}")
	public String createSuggestionAndLoadSuggestionPage(@PathVariable String xordernum, Model model) {
		Opordheader opordHeader = opordService.findOpordHeaderByXordernum(xordernum);
		if(opordHeader == null) return "redirect:/production/suggestion";

		// delete suggestion table where xordernum
		long count = productionSuggestionService.deleteSuggestion(xordernum, opordHeader.getXdate());
		//if(count == 0) return "redirect:/production/suggestion";

		productionSuggestionService.createSuggestion(xordernum);
		return "redirect:/production/suggestion?xordernum=" + xordernum;
	}
}
