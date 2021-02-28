package com.asl.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.asl.entity.Cacus;
import com.asl.entity.Xtrn;
import com.asl.enums.CacusType;
import com.asl.enums.TransactionCodeType;
import com.asl.service.XtrnService;

/**
 * @author Zubayer Ahamed
 * @since Feb 28, 2021
 */
@Controller
@RequestMapping("/mastersetup/cacus")
public class CacusController extends ASLAbstractController {

	@Autowired private XtrnService xtrnService;

	@GetMapping
	public String loadCacusPage(Model model, @RequestParam(required = false) String cacusType) {
		model.addAttribute("cacusType", cacusType);
		model.addAttribute("cacus", new Cacus());
		setTransactionCodeTypes(cacusType, model);
		return "pages/mastersetup/cacus/cacus";
	}

	private void setTransactionCodeTypes(String cacusType, Model model) {
		List<Xtrn> supplierTypes = new ArrayList<>();
		List<Xtrn> customerTypes = new ArrayList<>();
		if(CacusType.SUP.name().equalsIgnoreCase(cacusType)) {
			supplierTypes.addAll(xtrnService.findByXtypetrn(TransactionCodeType.SUPPLIER_NUMBER.getCode()));
		} else if (CacusType.CUS.name().equalsIgnoreCase(cacusType)) {
			customerTypes.addAll(xtrnService.findByXtypetrn(TransactionCodeType.CUSTOMER_NUMBER.getCode()));
		} else {
			supplierTypes.addAll(xtrnService.findByXtypetrn(TransactionCodeType.SUPPLIER_NUMBER.getCode()));
			customerTypes.addAll(xtrnService.findByXtypetrn(TransactionCodeType.CUSTOMER_NUMBER.getCode()));
		}
		model.addAttribute("supplierTypes", supplierTypes);
		model.addAttribute("customerTypes", customerTypes);
	}
}
