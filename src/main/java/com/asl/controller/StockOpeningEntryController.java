package com.asl.controller;

import java.util.Map;

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

import com.asl.entity.Imtrn;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.ImtrnService;
import com.asl.service.XcodesService;
import com.asl.service.XtrnService;

@Controller
@RequestMapping("/inventory/openingentry")
public class StockOpeningEntryController extends ASLAbstractController {
	
	@Autowired
	private ImtrnService imtrnService;
	@Autowired
	private XtrnService xtrnService;
	@Autowired
	private XcodesService xcodeService;
	
	@GetMapping
	public String loadStockOpeningEntryPage(Model model) {
		
		model.addAttribute("imtrn", getDefaultImtrn());
		model.addAttribute("allImtrn", imtrnService.getAllImtrn());
		model.addAttribute("imtrnprefix", xtrnService.findByXtypetrn(TransactionCodeType.INVENTORY_NUMBER.getCode()));
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.WAREHOUSE.getCode()));
		//model.addAttribute("allArhed", arhedService.getAllObaps());
		//model.addAttribute("arhedprefix", xtrnService.findByXtypetrn(TransactionCodeType.ACCOUNT_OBAP.getCode()));
		//model.addAttribute("paymentmodeList", xcodeService.findByXtype(CodeType.PAYMENT_MODE.getCode()));
		//model.addAttribute("paymenttypeList", xcodeService.findByXtype(CodeType.PAYMENT_TYPE.getCode()));
		//model.addAttribute("chequeStatusList", xcodeService.findByXtype(CodeType.CHEQUE_STATUS.getCode()));
		//model.addAttribute("bankstatusList", xcodeService.findByXtype(CodeType.BANK_STATUS.getCode()));
		//model.addAttribute("jvstatusList", xcodeService.findByXtype(CodeType.JOURNAL_VOUCHER_STATUS.getCode()));
		
		return "pages/inventory/openingentry/imtrn";
	}
	
	@GetMapping("/{ximtrnnum}")
	public String loadStockOpeningEntryPage(@PathVariable String ximtrnnum, Model model) {
		
		Imtrn data = imtrnService.findImtrnByXimtrnnum(ximtrnnum);
		if(data == null) data = getDefaultImtrn();

		model.addAttribute("imtrn", data);
		model.addAttribute("allImtrn", imtrnService.getAllImtrn());
		model.addAttribute("imtrnprefix", xtrnService.findByXtypetrn(TransactionCodeType.INVENTORY_NUMBER.getCode()));
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.WAREHOUSE.getCode()));
		//model.addAttribute("arhedprefix", xtrnService.findByXtypetrn(TransactionCodeType.ACCOUNT_OBAP.getCode()));
		//model.addAttribute("paymentmodeList", xcodeService.findByXtype(CodeType.PAYMENT_MODE.getCode()));
		//model.addAttribute("paymenttypeList", xcodeService.findByXtype(CodeType.PAYMENT_TYPE.getCode()));
		//model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.WAREHOUSE.getCode()));
		//model.addAttribute("chequeStatusList", xcodeService.findByXtype(CodeType.CHEQUE_STATUS.getCode()));
		//model.addAttribute("bankstatusList", xcodeService.findByXtype(CodeType.BANK_STATUS.getCode()));
		model.addAttribute("jvstatusList", xcodeService.findByXtype(CodeType.JOURNAL_VOUCHER_STATUS.getCode()));
		
		return "pages/inventory/openingentry/imtrn";
	}
	
	private Imtrn getDefaultImtrn() {
		Imtrn imtrn = new Imtrn();
		//arhed.setXtype(TransactionCodeType.GRN_NUMBER.getCode());
		//arhed.setXprime(BigDecimal.ZERO);
		return imtrn;
	}
	
	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Imtrn imtrn, BindingResult bindingResult){
		if(imtrn == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		// Validate
		
		//Modify transaction codes for imtrn
		
		/*
		if("Advance".equalsIgnoreCase(arhed.getXtyperec()) && BigDecimal.ZERO.compareTo(arhed.getXprime()) == -1)
			arhed.setXsign(+1);
		else if("Due".equalsIgnoreCase(arhed.getXtyperec()) &&  BigDecimal.ZERO.compareTo(arhed.getXbalprime()) == -1)
			arhed.setXsign(-1);
		arhed.setXtype(TransactionCodeType.ACCOUNT_OBAP.getCode());
		arhed.setXtrnarhed(TransactionCodeType.ACCOUNT_OBAP.getdefaultCode());
		arhed.setXtypetrn("AP Transaction");
		arhed.setXstatusjv("Confirmed");
		
		 */
		// if existing record
		Imtrn existImtrn = imtrnService.findImtrnByXimtrnnum(imtrn.getXimtrnnum());
		if(existImtrn != null) {
			BeanUtils.copyProperties(imtrn, existImtrn);
			long count = imtrnService.update(existImtrn);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Opening Entry updated successfully");
			responseHelper.setRedirectUrl("/inventory/openingentry/" + imtrn.getXimtrnnum());
			return responseHelper.getResponse();
		}

		// If new
		imtrn.setXsign(+1);
		imtrn.setXtype(TransactionCodeType.INVENTORY_NUMBER.getCode());
		imtrn.setXtrnimtrn(TransactionCodeType.INVENTORY_NUMBER.getdefaultCode());
		
		long count = imtrnService.save(imtrn);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Opening Entry created successfully");
		responseHelper.setRedirectUrl("/inventory/openingentry/" + imtrn.getXimtrnnum());
		return responseHelper.getResponse();
	}

}
