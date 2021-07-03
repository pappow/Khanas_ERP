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
import com.asl.service.XcodesService;

@Controller
@RequestMapping("/purchasing/supplierpayment")
public class SupplierPaymentController extends ASLAbstractController {

	@Autowired private ArhedService arhedService;
	@Autowired private XcodesService xcodeService;

	@GetMapping
	public String loadSupplierPaymentPage(Model model) {

		model.addAttribute("arhed", getDefaultArhed());
		model.addAttribute("allArhed", arhedService.getAllArhedByXtrnarhedAndXtype(TransactionCodeType.ACCOUNT_PAYMENT.getdefaultCode(), TransactionCodeType.ACCOUNT_PAYMENT.getCode()));
		model.addAttribute("arhedprefix", xtrnService.findByXtypetrn(TransactionCodeType.ACCOUNT_PAYMENT.getCode()));
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.WAREHOUSE.getCode()));
		model.addAttribute("paymenttypeList", xcodeService.findByXtype(CodeType.PAYMENT_TYPE.getCode()));
		model.addAttribute("chequeStatusList", xcodeService.findByXtype(CodeType.CHEQUE_STATUS.getCode()));
		model.addAttribute("bankstatusList", xcodeService.findByXtype(CodeType.BANK_STATUS.getCode()));
		model.addAttribute("jvstatusList", xcodeService.findByXtype(CodeType.JOURNAL_VOUCHER_STATUS.getCode()));
//		if(isBoshila()) {
//			return "pages/land/supplierpayment/arhed";
//		}
		return "pages/supplierpayment/arhed/arhed";
	}

	@GetMapping("/{xvoucher}")
	public String loadSupplierPaymentPage(@PathVariable String xvoucher, Model model) {
		Arhed data = arhedService.findArhedByXvoucher(xvoucher);
		if(data == null) data = getDefaultArhed();

		model.addAttribute("arhed", data);
		model.addAttribute("allArhed", arhedService.getAllArhedByXtrnarhedAndXtype(TransactionCodeType.ACCOUNT_PAYMENT.getdefaultCode(), TransactionCodeType.ACCOUNT_PAYMENT.getCode()));
		model.addAttribute("arhedprefix", xtrnService.findByXtypetrn(TransactionCodeType.ACCOUNT_PAYMENT.getCode()));
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.WAREHOUSE.getCode()));
		model.addAttribute("paymenttypeList", xcodeService.findByXtype(CodeType.PAYMENT_TYPE.getCode()));
		model.addAttribute("chequeStatusList", xcodeService.findByXtype(CodeType.CHEQUE_STATUS.getCode()));
		model.addAttribute("bankstatusList", xcodeService.findByXtype(CodeType.BANK_STATUS.getCode()));
		model.addAttribute("jvstatusList", xcodeService.findByXtype(CodeType.JOURNAL_VOUCHER_STATUS.getCode()));
//		if(isBoshila()) {
//			return "pages/land/supplierpayment/arhed";
//		}
		return "pages/supplierpayment/arhed/arhed";
	}
	
	private Arhed getDefaultArhed() {
		Arhed arhed = new Arhed();
		arhed.setXdate(new Date());
		arhed.setXprime(BigDecimal.ZERO);
		arhed.setXbalprime(BigDecimal.ZERO);
		arhed.setXvatamt(BigDecimal.ZERO);
		arhed.setXaitamt(BigDecimal.ZERO);
		arhed.setXdiscprime(BigDecimal.ZERO);
		arhed.setXbase(BigDecimal.ZERO);
		arhed.setXvatait("No Vat");
		arhed.setXpaymentterm("Credit");

		arhed.setXtypetrn("Account Payment");
		arhed.setXstatus("Open");
		arhed.setXstatusjv("Open");
		arhed.setXtype(TransactionCodeType.ACCOUNT_PAYMENT.getCode());
		arhed.setXtrnarhed(TransactionCodeType.ACCOUNT_PAYMENT.getdefaultCode());
		return arhed;
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Arhed arhed, BindingResult bindingResult){
		if((arhed == null || StringUtils.isBlank(arhed.getXtrnarhed())) && StringUtils.isBlank(arhed.getXvoucher())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// Validate
		if(StringUtils.isBlank(arhed.getXcus())) {
			responseHelper.setErrorStatusAndMessage("Supplier required!");
			return responseHelper.getResponse();
		}
		if(arhed.getXprime().compareTo(BigDecimal.ZERO) == -1 || arhed.getXprime().equals(BigDecimal.ZERO)) {
			responseHelper.setErrorStatusAndMessage("Invalid amount");
			return responseHelper.getResponse();
		}

		//Modify transaction codes for arhed
		arhed.setXsign(+1);
		arhed.setXtype(TransactionCodeType.ACCOUNT_PAYMENT.getCode());
		arhed.setXtrnarhed(TransactionCodeType.ACCOUNT_PAYMENT.getdefaultCode());
		arhed.setXtrn(TransactionCodeType.ACCOUNT_PAYMENT.getdefaultCode());

		// if existing record
		Arhed existArhed = arhedService.findArhedByXvoucher(arhed.getXvoucher());
		if(existArhed != null) {
			BeanUtils.copyProperties(arhed, existArhed, "xvoucher", "xtype", "xdate","xtrnarhed");
			long count = arhedService.update(existArhed);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Supplier payment updated successfully");
			responseHelper.setRedirectUrl("/purchasing/supplierpayment/" + arhed.getXvoucher());
			return responseHelper.getResponse();
		}

		// If new
		long count = arhedService.save(arhed);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Supplier payment created successfully");
		responseHelper.setRedirectUrl("/purchasing/supplierpayment/" + arhed.getXvoucher());
		return responseHelper.getResponse();
	}

	@PostMapping("/archive/{xvoucher}")
	public @ResponseBody Map<String, Object> archive(@PathVariable String xvoucher){
		return doArchiveOrRestore(xvoucher, true);
	}

	public Map<String, Object> doArchiveOrRestore(String xvoucher, boolean archive){
		Arhed voucher = arhedService.findArhedByXvoucher(xvoucher);
		if(voucher == null) {
			responseHelper.setErrorStatusAndMessage("Voucher not found in this system");
			return responseHelper.getResponse();
		}

		long count = arhedService.deleteVoucher(xvoucher);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't delete voucher");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Voucher deleted successfully");
		responseHelper.setRedirectUrl("/purchasing/supplierpayment");
		return responseHelper.getResponse();
	}

	@PostMapping("/confirm/{xvoucher}")
	public @ResponseBody Map<String, Object> confirmMoneyReceipt(@PathVariable String xvoucher){
		Arhed voucher = arhedService.findArhedByXvoucher(xvoucher);
		if(voucher == null) {
			responseHelper.setErrorStatusAndMessage("Voucher not found in this system");
			return responseHelper.getResponse();
		}

		voucher.setXstatus("Confirmed");
		long count = arhedService.update(voucher);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't confirm voucher");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Voucher confirmed successfully");
		responseHelper.setRedirectUrl("/purchasing/supplierpayment/" + voucher.getXvoucher());
		return responseHelper.getResponse();
	}

}
