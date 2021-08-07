package com.asl.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

import com.asl.entity.Caitem;
import com.asl.entity.Imtrn;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.CaitemService;
import com.asl.service.ImtrnService;
import com.asl.service.XcodesService;
import com.asl.service.XtrnService;

@Controller
@RequestMapping("/inventory/issueentry")
public class DamageIssueEntryController extends ASLAbstractController {

	@Autowired private ImtrnService imtrnService;
	@Autowired private XtrnService xtrnService;
	@Autowired private XcodesService xcodeService;
	@Autowired private CaitemService caitemService;

	@GetMapping
	public String loadStockOpeningEntryPage(Model model) {
		model.addAttribute("imtrn", getDefaultImtrn());
		model.addAttribute("allImtrnlist", imtrnService.getAllImtrnlist());
		model.addAttribute("imtrnprefix", xtrnService.findByXtypetrnAndXtrn(TransactionCodeType.INVENTORY_TRANSACTION3.getCode(),TransactionCodeType.INVENTORY_TRANSACTION3.getdefaultCode(), Boolean.TRUE));
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.STORE.getCode(), Boolean.TRUE));
		return "pages/inventory/issueentry/imtrn";
	}

	@GetMapping("/{ximtrnnum}")
	public String loadStockOpeningEntryPage(@PathVariable String ximtrnnum, Model model) {
		Imtrn data = imtrnService.findImtrnByXimtrnnum(ximtrnnum);
		if(data == null) data = getDefaultImtrn();

		model.addAttribute("imtrn", data);
		model.addAttribute("allImtrnlist", imtrnService.getAllImtrnlist());
		model.addAttribute("imtrnprefix", xtrnService.findByXtypetrnAndXtrn(TransactionCodeType.INVENTORY_TRANSACTION3.getCode(),TransactionCodeType.INVENTORY_TRANSACTION3.getdefaultCode(), Boolean.TRUE));
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.STORE.getCode(), Boolean.TRUE));
		return "pages/inventory/issueentry/imtrn";
	}

	private Imtrn getDefaultImtrn() {
		Imtrn imtrn = new Imtrn();
		imtrn.setXtype(TransactionCodeType.INVENTORY_TRANSACTION3.getCode());
		imtrn.setXtrn(TransactionCodeType.INVENTORY_TRANSACTION3.getdefaultCode());
		imtrn.setXqty(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
		imtrn.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
		imtrn.setXval(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
		imtrn.setXunit("");
		imtrn.setXnote("Inventory Issue");
		return imtrn;	
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Imtrn imtrn, BindingResult bindingResult){
		if(imtrn == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// if item is empty
		if(StringUtils.isBlank(imtrn.getXitem())) {
			responseHelper.setErrorStatusAndMessage("Please select an item first");
			return responseHelper.getResponse();
		}

		if(imtrn.getXqty().compareTo(BigDecimal.ZERO) == -1) {
			responseHelper.setErrorStatusAndMessage("Please insert valid item quantity");
			return responseHelper.getResponse();
		}

		if(imtrn.getXqty().compareTo(BigDecimal.ZERO) == 0) {
			responseHelper.setErrorStatusAndMessage("Please insert Greater than 0 item quantity");
			return responseHelper.getResponse();
		}
		
		if(imtrn.getXrate() == null) imtrn.setXrate(BigDecimal.ZERO);
		if(imtrn.getXrate().compareTo(BigDecimal.ZERO) == -1) {
			responseHelper.setErrorStatusAndMessage("Please insert valid rate");
			return responseHelper.getResponse();
		}
		
		if(imtrn.getXrate() == null) imtrn.setXrate(BigDecimal.ZERO);
		if(imtrn.getXrate().compareTo(BigDecimal.ZERO) == 0) {
			responseHelper.setErrorStatusAndMessage("Please insert Greater than 0 item rate");
			return responseHelper.getResponse();
		}

		// if existing record
		Imtrn existImtrn = imtrnService.findImtrnByXimtrnnum(imtrn.getXimtrnnum());
		if(existImtrn != null) {
			BeanUtils.copyProperties(imtrn, existImtrn, "xtype","xtrn","xitem","xsign");
			long count = imtrnService.update(existImtrn);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Issue Entry updated successfully");
			responseHelper.setRedirectUrl("/inventory/issueentry/" + imtrn.getXimtrnnum());
			return responseHelper.getResponse();
		}

		// If new
		imtrn.setXsign(-1);
		long count = imtrnService.save(imtrn);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Issue Entry created successfully");
		responseHelper.setRedirectUrl("/inventory/issueentry/" + imtrn.getXimtrnnum());
		return responseHelper.getResponse();
	}

	@PostMapping("/archive/{ximtrnnum}")
	public @ResponseBody Map<String, Object> delete(@PathVariable String ximtrnnum){
		Imtrn imtrn = imtrnService.findImtrnByXimtrnnum(ximtrnnum);
		if(imtrn == null) {
			responseHelper.setErrorStatusAndMessage("Can't find Issue entry : " + ximtrnnum);
			return responseHelper.getResponse();
		}

		long count = imtrnService.deleteByXimtrnnum(ximtrnnum);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't delete Issue entry : " + ximtrnnum);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Issue Entry deleted successfully");
		responseHelper.setRedirectUrl("/inventory/issueentry/");
		return responseHelper.getResponse();
	}

	@GetMapping("/itemdetail/{xitem}")
	public @ResponseBody Caitem getCentralItemDetail(@PathVariable String xitem){
		return caitemService.findByXitem(xitem);
	}
}
