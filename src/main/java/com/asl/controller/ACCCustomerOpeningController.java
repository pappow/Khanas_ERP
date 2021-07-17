package com.asl.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.Arhed;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.ArhedService;
import com.asl.service.VataitService;
import com.asl.service.XcodesService;

@Controller
@RequestMapping("/cusopening")
public class ACCCustomerOpeningController extends ASLAbstractController{
	
	@Autowired private ArhedService arhedService;
	@Autowired private XcodesService xcodeService;	
	@Autowired private VataitService vataitService;

	@GetMapping
	public String loadCusOpeningPage(Model model) {

		model.addAttribute("arhed", getDefaultArhed());
		model.addAttribute("arhedprefix", xtrnService.findByXtypetrn(TransactionCodeType.ACCOUNT_OBAR.getCode()));
		model.addAttribute("paymentModeList", xcodeService.findByXtype(CodeType.PAYMENT_MODE.getCode()));
		model.addAttribute("allArhed", arhedService.getAllObars());
		
		return "pages/salesninvoice/customeropen/cusopening";
	}

	@GetMapping("/{xvoucher}")
	public String loadCustomerOpeningPage(@PathVariable String xvoucher, Model model) {
		Arhed data = arhedService.findArhedByXvoucher(xvoucher);
		if(data == null) data = getDefaultArhed();

		data.setXtypetrn(TransactionCodeType.ACCOUNT_OBAR.getCode());
		model.addAttribute("arhed", data);
		model.addAttribute("arhedprefix", xtrnService.findByXtypetrn(TransactionCodeType.ACCOUNT_OBAR.getCode()));
		model.addAttribute("paymentModeList", xcodeService.findByXtype(CodeType.PAYMENT_MODE.getCode()));
		model.addAttribute("allArhed", arhedService.getAllObars());
		
		return "pages/salesninvoice/customeropen/cusopening";
	}

	private Arhed getDefaultArhed() {
		Arhed arhed = new Arhed();

		arhed.setXtrntype(TransactionCodeType.ACCOUNT_OBAR.getCode());
		arhed.setXdate(new Date());
		arhed.setXprime(BigDecimal.ZERO);
		arhed.setXstatus("Open");
		arhed.setXstatusjv("Open");
		
		

		return arhed;
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Arhed arhed, BindingResult bindingResult){
		// Validate
		if(StringUtils.isBlank(arhed.getXcus())) {
			responseHelper.setErrorStatusAndMessage("Customer required");
			return responseHelper.getResponse();
		}
		if(arhed.getXprime().compareTo(BigDecimal.ZERO) == -1 || arhed.getXprime().equals(BigDecimal.ZERO)) {
			responseHelper.setErrorStatusAndMessage("Invalid amount");
			return responseHelper.getResponse();
		}

		//Modify transaction codes for arhed
		
		arhed.setXtypetrn("Sale");
		arhed.setXstatusjv("Open");
		arhed.setXbase(arhed.getXprime());
		if (arhed.getXtyperec().equals("Normal")){
			arhed.setXsign(1);
		}
		else {arhed.setXsign(-1);}
		
		// if existing record
		if(StringUtils.isNotBlank(arhed.getXvoucher())) {
			Arhed existArhed = arhedService.findArhedByXvoucher(arhed.getXvoucher());
			BeanUtils.copyProperties(arhed, existArhed, "xvoucher", "xtype");
			long count = arhedService.update(existArhed);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Customer Opening updated successfully");
			responseHelper.setRedirectUrl("/cusopening/" + arhed.getXvoucher());
			return responseHelper.getResponse();
		}

		// If new
		long count = arhedService.save(arhed);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Customer Opening created successfully");
		responseHelper.setRedirectUrl("/cusopening/" + arhed.getXvoucher());
		return responseHelper.getResponse();
	}

	
	@PostMapping("/delete/{xvoucher}")
	public @ResponseBody Map<String, Object> delete(@PathVariable String xvoucher, Model model){
		Arhed voucher = arhedService.findArhedByXvoucher(xvoucher);
		if(voucher == null) {
			responseHelper.setErrorStatusAndMessage("Customer Opening not found in this system");
			return responseHelper.getResponse();
		}

		long count = arhedService.deleteVoucher(xvoucher);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't delete Supplier Payment voucher");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Customer Opening deleted successfully");
		responseHelper.setRedirectUrl("/cusopening");
		return responseHelper.getResponse();
	}
	
	@PostMapping("/confirm/{xvoucher}")
	public @ResponseBody Map<String, Object> confirmMoneyReceipt(@PathVariable String xvoucher){
		Arhed voucher = arhedService.findArhedByXvoucher(xvoucher);
		if(voucher == null) {
			responseHelper.setErrorStatusAndMessage("Customer Opening not found in this system");
			return responseHelper.getResponse();
		}

		voucher.setXstatus("Confirmed");
		long count = arhedService.update(voucher);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't confirm Customer Opening");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Customer Opening confirmed successfully");
		responseHelper.setRedirectUrl("/cusopening/" + voucher.getXvoucher());
		return responseHelper.getResponse();
	}

}
