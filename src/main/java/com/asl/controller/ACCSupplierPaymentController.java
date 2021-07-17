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
@RequestMapping("/purchasing/supplierpayment")
public class ACCSupplierPaymentController extends ASLAbstractController{
	
	@Autowired private ArhedService arhedService;
	@Autowired private XcodesService xcodeService;	
	@Autowired private VataitService vataitService;

	@GetMapping
	public String loadMoneyReceiptPage(Model model) {

		model.addAttribute("arhed", getDefaultArhed());
		model.addAttribute("arhedprefix", xtrnService.findByXtypetrn(TransactionCodeType.ACCOUNT_PAYMENT.getCode()));
		model.addAttribute("paymenttypeList", xcodeService.findByXtype(CodeType.PAYMENT_TYPE.getCode()));
		model.addAttribute("allArhed", arhedService.getAllArhedByXtrn(TransactionCodeType.ACCOUNT_PAYMENT.getdefaultCode()));
		
		return "pages/purchasing/supplierpayment/supplierpayment";
	}

	@GetMapping("/{xvoucher}")
	public String loadMoneyReceiptPage(@PathVariable String xvoucher, Model model) {
		Arhed data = arhedService.findArhedByXvoucher(xvoucher);
		if(data == null) data = getDefaultArhed();

		data.setXtypetrn(TransactionCodeType.ACCOUNT_PAYMENT.getCode());
		model.addAttribute("arhed", data);
		model.addAttribute("arhedprefix", xtrnService.findByXtypetrn(TransactionCodeType.ACCOUNT_PAYMENT.getCode()));
		model.addAttribute("paymenttypeList", xcodeService.findByXtype(CodeType.PAYMENT_TYPE.getCode()));
		model.addAttribute("allArhed", arhedService.getAllArhedByXtrn(TransactionCodeType.ACCOUNT_PAYMENT.getdefaultCode()));
		
		return "pages/purchasing/supplierpayment/supplierpayment";
	}

	private Arhed getDefaultArhed() {
		Arhed arhed = new Arhed();

		arhed.setXtrntype(TransactionCodeType.ACCOUNT_PAYMENT.getCode());
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
			responseHelper.setErrorStatusAndMessage("Supplier required");
			return responseHelper.getResponse();
		}
		if(arhed.getXprime().compareTo(BigDecimal.ZERO) == -1 || arhed.getXprime().equals(BigDecimal.ZERO)) {
			responseHelper.setErrorStatusAndMessage("Invalid amount");
			return responseHelper.getResponse();
		}

		//Modify transaction codes for arhed
		arhed.setXsign(1);
		arhed.setXtypetrn("Purchase");
		arhed.setXpaymentterm("Cash");
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
			responseHelper.setSuccessStatusAndMessage("Supplier Payment updated successfully");
			responseHelper.setRedirectUrl("/purchasing/supplierpayment/" + arhed.getXvoucher());
			return responseHelper.getResponse();
		}

		// If new
		long count = arhedService.save(arhed);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Supplier Payment created successfully");
		responseHelper.setRedirectUrl("/purchasing/supplierpayment/" + arhed.getXvoucher());
		return responseHelper.getResponse();
	}

	
	@PostMapping("/delete/{xvoucher}")
	public @ResponseBody Map<String, Object> delete(@PathVariable String xvoucher, Model model){
		Arhed voucher = arhedService.findArhedByXvoucher(xvoucher);
		if(voucher == null) {
			responseHelper.setErrorStatusAndMessage("Supplier Payment not found in this system");
			return responseHelper.getResponse();
		}

		long count = arhedService.deleteVoucher(xvoucher);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't delete Supplier Payment voucher");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Supplier Payment deleted successfully");
		responseHelper.setRedirectUrl("/purchasing/supplierpayment");
		return responseHelper.getResponse();
	}
	
	@PostMapping("/confirm/{xvoucher}")
	public @ResponseBody Map<String, Object> confirmMoneyReceipt(@PathVariable String xvoucher){
		Arhed voucher = arhedService.findArhedByXvoucher(xvoucher);
		if(voucher == null) {
			responseHelper.setErrorStatusAndMessage("Supplier Payment not found in this system");
			return responseHelper.getResponse();
		}

		voucher.setXstatus("Confirmed");
		long count = arhedService.update(voucher);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't confirm Supplier Payment");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Supplier Payment confirmed successfully");
		responseHelper.setRedirectUrl("/purchasing/supplierpayment/" + voucher.getXvoucher());
		return responseHelper.getResponse();
	}

}
