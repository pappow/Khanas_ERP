package com.asl.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.asl.entity.Pocrnheader;
import com.asl.entity.PogrnHeader;
import com.asl.enums.CodeType;
import com.asl.enums.TransactionCodeType;
import com.asl.service.PocrnService;
import com.asl.service.XcodesService;
import com.asl.service.XtrnService;

@Controller
@RequestMapping("/procurement/grnreturn")
public class GrnReturnController extends ASLAbstractController {
	
	@Autowired
	private XcodesService xcodeService;
	@Autowired
	private XtrnService xtrnService;
	@Autowired
	private PocrnService pocrnService;
	
	@GetMapping
	public String loadGrnReturnPage(Model model) {
		
		model.addAttribute("pocrnheader", getDefaultPocrnHeader());
		model.addAttribute("crnprefix", xtrnService.findByXtypetrn(TransactionCodeType.GRN_NUMBER.getCode()));
		model.addAttribute("allPocrnHeader", pocrnService.getAllPocrnheader());
		//model.addAttribute("allPogrnHeader", new ArrayList<PogrnHeader>());
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.WAREHOUSE.getCode()));
		model.addAttribute("grnStatusList", xcodeService.findByXtype(CodeType.GRN_STATUS.getCode()));
		model.addAttribute("paymenttypeList", xcodeService.findByXtype(CodeType.PAYMENT_TYPE.getCode()));
		model.addAttribute("jvstatusList", xcodeService.findByXtype(CodeType.JOURNAL_VOUCHER_STATUS.getCode()));
		
		return "pages/procurement/grnreturn/pocrn";
	}
	
	@GetMapping("/{xcrnnum}")
	public String loadGrnReturnPage(@PathVariable String xcrnnum, Model model) {
		
		Pocrnheader data = pocrnService.findPocrnHeaderByXcrnnum(xcrnnum); 
		if(data == null) data = getDefaultPocrnHeader();

		model.addAttribute("pogrnheader", data);
		model.addAttribute("crnprefix", xtrnService.findByXtypetrn(TransactionCodeType.GRN_NUMBER.getCode()));
		model.addAttribute("allPocrnHeader", pocrnService.getAllPocrnheader());
		//model.addAttribute("allPogrnHeader", new ArrayList<PogrnHeader>());
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.WAREHOUSE.getCode()));
		model.addAttribute("grnStatusList", xcodeService.findByXtype(CodeType.GRN_STATUS.getCode()));
		model.addAttribute("paymenttypeList", xcodeService.findByXtype(CodeType.PAYMENT_TYPE.getCode()));
		model.addAttribute("jvstatusList", xcodeService.findByXtype(CodeType.JOURNAL_VOUCHER_STATUS.getCode()));
		model.addAttribute("pocrnDetailsList", pocrnService.findPocrnDetailByXcrnnum(xcrnnum));
		
		return "pages/procurement/grnreturn/pocrn";
	}
	
	private Pocrnheader getDefaultPocrnHeader() {
		Pocrnheader pocrn = new Pocrnheader();
		//pogrn.setXtype(TransactionCodeType.GRN_NUMBER.getCode());
		//pogrn.setXtypetrn("Purchase");
		//pogrn.setXtotamt(BigDecimal.ZERO);
		return pocrn;
	}

}
