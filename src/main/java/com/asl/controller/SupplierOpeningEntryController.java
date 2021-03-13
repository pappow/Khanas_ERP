package com.asl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.asl.entity.Arhed;
import com.asl.enums.CodeType;
import com.asl.enums.TransactionCodeType;
import com.asl.service.ArhedService;
import com.asl.service.XcodesService;
import com.asl.service.XtrnService;

@Controller
@RequestMapping("/purchasing/supopenentry")
public class SupplierOpeningEntryController extends ASLAbstractController{
	

	@Autowired
	private ArhedService arhedService;
	@Autowired
	private XtrnService xtrnService;
	@Autowired
	private XcodesService xcodeService;
	
	@GetMapping
	public String loadSupOpeningEntryPage(Model model) {
		
		model.addAttribute("arhed", getDefaultArhed());		
		model.addAttribute("allArhed", arhedService.getAllArheds());
		model.addAttribute("arhedprefix", xtrnService.findByXtypetrn(TransactionCodeType.VOUCHER_NUMBER.getCode()));
		model.addAttribute("paymentmodeList", xcodeService.findByXtype(CodeType.PAYMENT_MODE.getCode()));
		//model.addAttribute("paymenttypeList", xcodeService.findByXtype(CodeType.PAYMENT_TYPE.getCode()));
		//model.addAttribute("chequeStatusList", xcodeService.findByXtype(CodeType.CHEQUE_STATUS.getCode()));
		//model.addAttribute("bankstatusList", xcodeService.findByXtype(CodeType.BANK_STATUS.getCode()));
		model.addAttribute("jvstatusList", xcodeService.findByXtype(CodeType.JOURNAL_VOUCHER_STATUS.getCode()));
		
		return "pages/supplierpayment/arhed/arhed";
	}
	
	@GetMapping("/{xvoucher}")
	public String loadSupOpeningEntryPage(@PathVariable String xvoucher, Model model) {
		
		Arhed data = arhedService.findArhedByXvoucher(xvoucher);
		if(data == null) data = getDefaultArhed();

		model.addAttribute("arhed", data);
		model.addAttribute("allArhed", arhedService.getAllArheds());
		model.addAttribute("arhedprefix", xtrnService.findByXtypetrn(TransactionCodeType.VOUCHER_NUMBER.getCode()));
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.WAREHOUSE.getCode()));
		model.addAttribute("paymenttypeLiist", xcodeService.findByXtype(CodeType.PAYMENT_TYPE.getCode()));
		model.addAttribute("chequeStatusList", xcodeService.findByXtype(CodeType.CHEQUE_STATUS.getCode()));
		model.addAttribute("bankstatusList", xcodeService.findByXtype(CodeType.BANK_STATUS.getCode()));
		model.addAttribute("jvstatusList", xcodeService.findByXtype(CodeType.JOURNAL_VOUCHER_STATUS.getCode()));
		
		return "pages/supplierpayment/arhed/arhed";
	}
	
	private Arhed getDefaultArhed() {
		Arhed arhed = new Arhed();
		//arhed.setXtype(TransactionCodeType.GRN_NUMBER.getCode());
		//arhed.setXtotamt(BigDecimal.ZERO);
		return arhed;
	} 

}
