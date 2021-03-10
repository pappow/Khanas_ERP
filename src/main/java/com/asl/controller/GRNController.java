package com.asl.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
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

import com.asl.entity.Pogrnheader;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.PogrnService;
import com.asl.service.XcodesService;

@Controller
@RequestMapping("/purchasing/grn")
public class GRNController extends ASLAbstractController {
	
	@Autowired
	private PogrnService pogrnService;
	
	@Autowired
	private XcodesService xcodeService;
	
	@GetMapping
	public String loadGRNPage(Model model) {
		
		model.addAttribute("pogrnheader", getDefaultPogrnHeader());
		//model.addAttribute("grnprefix", xtrnService.findByXtypetrn(TransactionCodeType.PURCHASE_ORDER.getCode()));
		//model.addAttribute("allPogrnHeader", pogrnService.getAllPogrnHeaders());
		model.addAttribute("allPogrnHeader", new ArrayList<Pogrnheader>());
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.WAREHOUSE.getCode()));
		//model.addAttribute("postatusList", xcodeService.findByXtype(CodeType.PURCHASE_ORDER_STATUS.getCode()));
		model.addAttribute("grnStatusList", xcodeService.findByXtype(CodeType.GRN_STATUS.getCode()));
		
		return "pages/purchasing/grn/grn";
	}
	
	@GetMapping("/{xgrnnum}")
	public String loadGRNPage(@PathVariable String xgrnnum, Model model) {
		
		Pogrnheader data = pogrnService.findPogrnHeaderByXgrnnum(xgrnnum); 
		if(data == null) data = getDefaultPogrnHeader();

		model.addAttribute("pogrnheader", data);
		//model.addAttribute("grnprefix", xtrnService.findByXtypetrn(TransactionCodeType.PURCHASE_ORDER.getCode()));
		//model.addAttribute("allPogrnHeader", pogrnService.getAllPogrnHeaders());
		model.addAttribute("allPogrnHeader", new ArrayList<Pogrnheader>());
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.WAREHOUSE.getCode()));
		model.addAttribute("postatusList", xcodeService.findByXtype(CodeType.PURCHASE_ORDER_STATUS.getCode()));
		model.addAttribute("grnStatusList", xcodeService.findByXtype(CodeType.GRN_STATUS.getCode()));
		model.addAttribute("pogrnDetailsList", pogrnService.findPogrnDetailByXgrnnum(xgrnnum));
		
		return "pages/purchasing/grn/grn";
	}
	
	private Pogrnheader getDefaultPogrnHeader() {
		Pogrnheader pogrn = new Pogrnheader();
		pogrn.setXtype(TransactionCodeType.PURCHASE_ORDER.getCode());
		pogrn.setXtotamt(BigDecimal.ZERO);
		return pogrn;
	}
	
	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Pogrnheader pogrnHeader, BindingResult bindingResult){
		if((pogrnHeader == null || StringUtils.isBlank(pogrnHeader.getXtype()))) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		// Validate

		// if existing record
		Pogrnheader existPogrnHeader = pogrnService.findPogrnHeaderByXgrnnum(pogrnHeader.getXgrnnum());
		if(existPogrnHeader != null) {
			BeanUtils.copyProperties(pogrnHeader, existPogrnHeader, "xgrnnum");
			long count = pogrnService.update(existPogrnHeader);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("GRN updated successfully");
			responseHelper.setRedirectUrl("/purchasing/grn/" + pogrnHeader.getXgrnnum());
			return responseHelper.getResponse();
		}

		// If new
		long count = pogrnService.save(pogrnHeader);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("GRN created successfully");
		responseHelper.setRedirectUrl("/purchasing/grn/" + pogrnHeader.getXgrnnum());
		return responseHelper.getResponse();
	}
	
	@GetMapping("{xgrnnum}/pogrndetail/{xrow}/show")
	public String openPoordDetailModal(@PathVariable String xpornum, @PathVariable String xrow, Model model) {
		/*
		model.addAttribute("purchaseUnit", xcodeService.findByXtype(CodeType.PURCHASE_UNIT.getCode()));

		if("new".equalsIgnoreCase(xrow)) {
			PoordDetail poorddetail = new PoordDetail();
			poorddetail.setXpornum(xpornum);
			poorddetail.setXqtyord(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
			poorddetail.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			poorddetail.setXlineamt(poorddetail.getXqtyord().multiply(poorddetail.getXrate()));
			model.addAttribute("poorddetail", poorddetail);
		} else {
			PoordDetail poorddetail = poordService.findPoorddetailByXportNumAndXrow(xpornum, Integer.parseInt(xrow));
			if(poorddetail == null) {
				poorddetail = new PoordDetail();
				poorddetail.setXpornum(xpornum);
				poorddetail.setXqtyord(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
				poorddetail.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
				poorddetail.setXlineamt(poorddetail.getXqtyord().multiply(poorddetail.getXrate()));
			}
			model.addAttribute("poorddetail", poorddetail);
		}*/

		return "pages/purchasing/poord/poorddetailmodal::poorddetailmodal";
	}

}
