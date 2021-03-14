package com.asl.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
import com.asl.model.ProductionSuggestion;
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
		List<ProductionSuggestion> list = new ArrayList<>();
		if(chalan != null) {
			list = productionSuggestionService.getProductionSuggestion(chalan.getXordernum(), chalan.getXdate());
		}
		model.addAttribute("suggestions", list == null ? Collections.emptyList() : list);
		return "pages/production/suggestion/suggestion";
	}

	@GetMapping("/create/{xordernum}")
	public String createSuggestionAndLoadSuggestionPage(@PathVariable String xordernum, Model model) {
		Opordheader opordHeader = opordService.findOpordHeaderByXordernum(xordernum);
		if(opordHeader == null) return "redirect:/production/suggestion";

		// delete suggestion table where xordernum
		productionSuggestionService.deleteSuggestion(xordernum, opordHeader.getXdate());

		productionSuggestionService.createSuggestion(xordernum);
		return "redirect:/production/suggestion?xordernum=" + xordernum;
	}
}
