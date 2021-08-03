package com.asl.controller;

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
import com.asl.service.XtrnService;

@Controller
@RequestMapping("/salesninvoice/customeradjustment")
public class CustomerAdjustmentController extends ASLAbstractController{

	@Autowired
	private ArhedService arhedService;
	@Autowired
	private XtrnService xtrnService;
	@Autowired
	private XcodesService xcodeService;
	

	@GetMapping
	public String loadCustomerAdjustmentPage(Model model) {
		
		model.addAttribute("arhed", getDefaultArhed());		
		model.addAttribute("allArhed", arhedService.getAllAdars());
		model.addAttribute("arhedprefix", xtrnService.findByXtypetrn(TransactionCodeType.ACCOUNT_ADAR.getCode()));
		model.addAttribute("objtypeList", xcodeService.findByXtype(CodeType.OBJECT_TYPE.getCode()));
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.STORE.getCode()));
		model.addAttribute("paymenttypeList", xcodeService.findByXtype(CodeType.PAYMENT_TYPE.getCode()));
		model.addAttribute("chequeStatusList", xcodeService.findByXtype(CodeType.CHEQUE_STATUS.getCode()));
		model.addAttribute("bankstatusList", xcodeService.findByXtype(CodeType.BANK_STATUS.getCode()));
		model.addAttribute("jvstatusList", xcodeService.findByXtype(CodeType.JOURNAL_VOUCHER_STATUS.getCode()));
		if(isBoshila()) {
			return "pages/land/customeradjustment/arhed";
		}
		return "pages/salesninvoice/customeradjustment/arhed";
	}
	
	@GetMapping("/{xvoucher}")
	public String loadCustomerAdjustmentPage(@PathVariable String xvoucher, Model model) {
		
		Arhed data = arhedService.findArhedByXvoucher(xvoucher);
		if(data == null) data = getDefaultArhed();

		model.addAttribute("arhed", data);
		model.addAttribute("allArhed", arhedService.getAllAdars());
		model.addAttribute("arhedprefix", xtrnService.findByXtypetrn(TransactionCodeType.ACCOUNT_ADAR.getCode()));
		model.addAttribute("objtypeList", xcodeService.findByXcode(CodeType.OBJECT_TYPE.getCode()));
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.STORE.getCode()));
		model.addAttribute("paymenttypeLiist", xcodeService.findByXtype(CodeType.PAYMENT_TYPE.getCode()));
		model.addAttribute("chequeStatusList", xcodeService.findByXtype(CodeType.CHEQUE_STATUS.getCode()));
		model.addAttribute("bankstatusList", xcodeService.findByXtype(CodeType.BANK_STATUS.getCode()));
		model.addAttribute("jvstatusList", xcodeService.findByXtype(CodeType.JOURNAL_VOUCHER_STATUS.getCode()));
		if(isBoshila()) {
			return "pages/land/customeradjustment/arhed";
		}
		return "pages/salesninvoice/customeradjustment/arhed";
	}
	
	private Arhed getDefaultArhed() {
		Arhed arhed = new Arhed();
		arhed.setXtypeobj("Credit");
		arhed.setXtype(TransactionCodeType.ACCOUNT_ADAR.getCode());
		arhed.setXtypetrn(TransactionCodeType.ACCOUNT_ADAR.getCode());
		arhed.setXtrn(TransactionCodeType.ACCOUNT_ADAR.getdefaultCode());
		//arhed.setXtotamt(BigDecimal.ZERO);
		return arhed;
	}
	
	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Arhed arhed, BindingResult bindingResult){
		if((arhed == null || StringUtils.isBlank(arhed.getXtype())) || StringUtils.isBlank(arhed.getXtrntype())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		// Validate
		
		if("debit".equalsIgnoreCase(arhed.getXtypeobj())) {
			arhed.setXsign(+1);
		}else {
			arhed.setXsign(-1);
		}
		arhed.setXtype(TransactionCodeType.ACCOUNT_ADAR.getCode());

		// if existing record
		Arhed existArhed = arhedService.findArhedByXvoucher(arhed.getXvoucher());
		if(existArhed != null) {
			BeanUtils.copyProperties(arhed, existArhed, "xvoucher", "xtype", "xdate");
			long count = arhedService.update(existArhed);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Adjustment updated successfully");
			responseHelper.setRedirectUrl("/salesninvoice/customeradjustment/" + arhed.getXvoucher());
			return responseHelper.getResponse();
		}

		// If new
		long count = arhedService.save(arhed);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Adjustment created successfully");
		responseHelper.setRedirectUrl("/salesninvoice/customeradjustment/" + arhed.getXvoucher());
		return responseHelper.getResponse();
	}
	
	@PostMapping("/archive/{xvoucher}")
	public @ResponseBody Map<String, Object> archive(@PathVariable String xvoucher){
		return doArchiveOrRestore(xvoucher, true);
	}

	@PostMapping("/restore/{xvoucher}")
	public @ResponseBody Map<String, Object> restore(@PathVariable String xvoucher){
		return doArchiveOrRestore(xvoucher, false);
	}

	public Map<String, Object> doArchiveOrRestore(String xvoucher, boolean archive){
		Arhed arhed = arhedService.findArhedByXvoucher(xvoucher);
		if(arhed == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		arhed.setZactive(archive ? Boolean.FALSE : Boolean.TRUE);
		long count = arhedService.update(arhed);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Entry updated successfully");
		responseHelper.setRedirectUrl("/salesninvoice/customeradjustment/" + arhed.getXvoucher());
		return responseHelper.getResponse();
	}


}
