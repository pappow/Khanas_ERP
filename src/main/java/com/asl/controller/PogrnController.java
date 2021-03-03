package com.asl.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
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

import com.asl.entity.PogrnDetail;
import com.asl.entity.PogrnHeader;
import com.asl.entity.PoordDetail;
import com.asl.entity.PoordHeader;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.PogrnService;
import com.asl.service.XcodesService;

@Controller
@RequestMapping("/purchasing/pogrn")
public class PogrnController extends ASLAbstractController {
	
	@Autowired
	private PogrnService pogrnService;
	@Autowired
	private XcodesService xcodeService;
	
	@GetMapping
	public String loadGRNPage(Model model) {
		
		model.addAttribute("pogrnheader", getDefaultPogrnHeader());
		//model.addAttribute("grnprefix", xtrnService.findByXtypetrn(TransactionCodeType.PURCHASE_ORDER.getCode()));
		model.addAttribute("allPogrnHeader", pogrnService.getAllPogrnHeaders());
		//model.addAttribute("allPogrnHeader", new ArrayList<PogrnHeader>());
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.WAREHOUSE.getCode()));
		model.addAttribute("postatusList", xcodeService.findByXtype(CodeType.PURCHASE_ORDER_STATUS.getCode()));
		model.addAttribute("grnStatusList", xcodeService.findByXtype(CodeType.GRN_STATUS.getCode()));
		
		return "pages/purchasing/pogrn/pogrn";
	}
	
	@GetMapping("/{xgrnnum}")
	public String loadGRNPage(@PathVariable String xgrnnum, Model model) {
		
		PogrnHeader data = pogrnService.findPogrnHeaderByXgrnnum(xgrnnum); 
		if(data == null) data = getDefaultPogrnHeader();

		model.addAttribute("pogrnheader", data);
		//model.addAttribute("grnprefix", xtrnService.findByXtypetrn(TransactionCodeType.PURCHASE_ORDER.getCode()));
		model.addAttribute("allPogrnHeader", pogrnService.getAllPogrnHeaders());
		//model.addAttribute("allPogrnHeader", new ArrayList<PogrnHeader>());
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.WAREHOUSE.getCode()));
		model.addAttribute("postatusList", xcodeService.findByXtype(CodeType.PURCHASE_ORDER_STATUS.getCode()));
		model.addAttribute("grnStatusList", xcodeService.findByXtype(CodeType.GRN_STATUS.getCode()));
		model.addAttribute("pogrnDetailsList", pogrnService.findPogrnDetailByXgrnnum(xgrnnum));
		
		return "pages/purchasing/pogrn/pogrn";
	}
	
	private PogrnHeader getDefaultPogrnHeader() {
		PogrnHeader pogrn = new PogrnHeader();
		pogrn.setXtype(TransactionCodeType.PURCHASE_ORDER.getCode());
		pogrn.setXtotamt(BigDecimal.ZERO);
		return pogrn;
	}
	
	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(PogrnHeader pogrnHeader, BindingResult bindingResult){
		
		
		if((pogrnHeader == null || StringUtils.isBlank(pogrnHeader.getXtype()))) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		// Validate

		// if existing record
		
		PogrnHeader existPogrnHeader = pogrnService.findPogrnHeaderByXgrnnum(pogrnHeader.getXgrnnum());
		if(existPogrnHeader != null) {
			BeanUtils.copyProperties(pogrnHeader, existPogrnHeader, "xgrnnum", "xtype", "xdate", "xtotamt");
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
		responseHelper.setRedirectUrl("/purchasing/poord/" + pogrnHeader.getXgrnnum());
		return responseHelper.getResponse();
		
	}

	
	@GetMapping("/pogrndetail/{xgrnnum}")
	public String reloadPogrnDetailTable(@PathVariable String xgrnnum, Model model) {
		List<PogrnDetail> detailList = pogrnService.findPogrnDetailByXgrnnum(xgrnnum);
		model.addAttribute("pogrnDetailsList", detailList);
		PogrnHeader header = new PogrnHeader();
		header.setXgrnnum(xgrnnum);
		model.addAttribute("pogrnheader", header);
		return "pages/purchasing/pogrn/pogrn::pogrndetailtable";
	}
	
	@PostMapping("/pogrndetail/save")
	public @ResponseBody Map<String, Object> savePogrndetail(PogrnDetail poordDetail){
		/*
		if(poordDetail == null || StringUtils.isBlank(poordDetail.getXpornum())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// modify line amount
		poordDetail.setXlineamt(poordDetail.getXqtyord().multiply(poordDetail.getXrate().setScale(2, RoundingMode.DOWN)));

		// if existing
		PoordDetail existDetail = poordService.findPoorddetailByXportNumAndXrow(poordDetail.getXpornum(), poordDetail.getXrow());
		if(existDetail != null) {
			BeanUtils.copyProperties(poordDetail, existDetail, "xpornum", "xrow");
			long count = poordService.updateDetail(existDetail);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setRedirectUrl("/purchasing/poord/" +  poordDetail.getXpornum());
			responseHelper.setSuccessStatusAndMessage("Order detail updated successfully");
			return responseHelper.getResponse();
		}

		// if new detail
		long count = poordService.saveDetail(poordDetail);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setRedirectUrl("/purchasing/poord/" +  poordDetail.getXpornum());
		responseHelper.setSuccessStatusAndMessage("Order detail saved successfully");
		
		return responseHelper.getResponse();
		*/
		return null;
	}

}
