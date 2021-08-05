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
import com.asl.entity.LandInfo;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.ArhedService;
import com.asl.service.VataitService;
import com.asl.service.XcodesService;

@Controller
@RequestMapping("/salesninvoice/moneyreceipt")
public class ACCMoneyReceiptController extends ASLAbstractController{

	@Autowired private ArhedService arhedService;
	@Autowired private XcodesService xcodeService;	
	@Autowired private VataitService vataitService;

	@GetMapping
	public String loadMoneyReceiptPage(Model model) {

		model.addAttribute("arhed", getDefaultArhed());
		model.addAttribute("arhedprefix", xtrnService.findByXtypetrnAndXtrn(TransactionCodeType.MONEY_RECEIPTS.getCode(),TransactionCodeType.MONEY_RECEIPTS.getdefaultCode(), Boolean.TRUE));
		model.addAttribute("paymenttypeList", xcodeService.findByXtype(CodeType.PAYMENT_TYPE.getCode(), Boolean.TRUE));
		model.addAttribute("allArhed", arhedService.getAllArhedByXtype(TransactionCodeType.MONEY_RECEIPTS.getdefaultCode()));
		if(isBoshila()) {
			return "pages/accounts/moneyreceipt";
		}
		return "pages/salesninvoice/moneyreceipt/arhed";
	}

	@GetMapping("/{xvoucher}")
	public String loadMoneyReceiptPage(@PathVariable String xvoucher, Model model) {
		Arhed data = arhedService.findArhedByXvoucher(xvoucher);
		if(data == null) data = getDefaultArhed();

		
		model.addAttribute("arhed", data);
		model.addAttribute("arhedprefix", xtrnService.findByXtypetrn(TransactionCodeType.MONEY_RECEIPTS.getCode()));
		model.addAttribute("paymenttypeList", xcodeService.findByXtype(CodeType.PAYMENT_TYPE.getCode()));
		model.addAttribute("allArhed", arhedService.getAllArhedByXtype(TransactionCodeType.MONEY_RECEIPTS.getdefaultCode()));
		if(isBoshila()) {
			return "pages/accounts/moneyreceipt";
		}
		return "pages/salesninvoice/moneyreceipt/arhed";
	}

	private Arhed getDefaultArhed() {
		Arhed arhed = new Arhed();

		arhed.setXtype(TransactionCodeType.MONEY_RECEIPTS.getCode());
		arhed.setXtypetrn(TransactionCodeType.MONEY_RECEIPTS.getCode());
		arhed.setXtrntype(TransactionCodeType.MONEY_RECEIPTS.getCode());
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
		arhed.setXsign(-1);
		arhed.setXtypetrn("Sale");
		arhed.setXpaymentterm("Credit");
		arhed.setXwh("01");
		arhed.setXstatusbnk("Open");
		arhed.setXbase(arhed.getXprime());

		// if existing record
		if(StringUtils.isNotBlank(arhed.getXvoucher())) {
			Arhed existArhed = arhedService.findArhedByXvoucher(arhed.getXvoucher());
			BeanUtils.copyProperties(arhed, existArhed, "xvoucher", "xtype");
			long count = arhedService.update(existArhed);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Money Receipt updated successfully");
			responseHelper.setRedirectUrl("/salesninvoice/moneyreceipt/" + arhed.getXvoucher());
			return responseHelper.getResponse();
		}

		// If new
		long count = arhedService.save(arhed);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Money Receipt created successfully");
		responseHelper.setRedirectUrl("/salesninvoice/moneyreceipt/" + arhed.getXvoucher());
		return responseHelper.getResponse();
	}

	
	@PostMapping("/delete/{xvoucher}")
	public @ResponseBody Map<String, Object> delete(@PathVariable String xvoucher, Model model){
		Arhed voucher = arhedService.findArhedByXvoucher(xvoucher);
		if(voucher == null) {
			responseHelper.setErrorStatusAndMessage("Money Receipt not found in this system");
			return responseHelper.getResponse();
		}

		long count = arhedService.deleteVoucher(xvoucher);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't delete Money Receipt");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Money Receipt deleted successfully");
		responseHelper.setRedirectUrl("/salesninvoice/moneyreceipt");
		return responseHelper.getResponse();
	}
	
	@PostMapping("/confirm/{xvoucher}")
	public @ResponseBody Map<String, Object> confirmMoneyReceipt(@PathVariable String xvoucher){
		Arhed voucher = arhedService.findArhedByXvoucher(xvoucher);
		if(voucher == null) {
			responseHelper.setErrorStatusAndMessage("Money Receipt not found in this system");
			return responseHelper.getResponse();
		}

		voucher.setXstatus("Confirmed");
		long count = arhedService.update(voucher);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't confirm Money Receipt");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Money Receipt confirmed successfully");
		responseHelper.setRedirectUrl("/salesninvoice/moneyreceipt/" + voucher.getXvoucher());
		return responseHelper.getResponse();
	}
}
