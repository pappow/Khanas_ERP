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

import com.asl.entity.Zbusiness;
import com.asl.enums.ResponseStatus;
import com.asl.service.ZbusinessService;
import com.asl.enums.ProfileType;

@Controller
@RequestMapping("/system/businessprof")
public class BusinessProfileController extends ASLAbstractController{
	@Autowired private ZbusinessService zbusinessService;
	
	@GetMapping
	public String loadBusinessPage(Model model) {
		
		Zbusiness zbusiness = new Zbusiness();
		zbusiness.setXdine(ProfileType.C);
		model.addAttribute("business", zbusinessService.findfromZid());
		model.addAttribute("allBranchBusiness", zbusinessService.getAllBranchBusiness());

		return"pages/businessProfile/business";
	}
	
	@GetMapping("/{zid}")
	public String loadbusinessPrifilePage(@PathVariable String zid, Model model) {
		Zbusiness zb = zbusinessService.findBById(zid);
		if(zb == null) return "redirect:/system/businessprof";
		model.addAttribute("business", zb);
		model.addAttribute("allbusiness", zbusinessService.findfromZid());
		model.addAttribute("allBranchBusiness", zbusinessService.getAllBranchBusiness());
		
		return "pages/businessProfile/business";
	}
	
	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Zbusiness zbusiness, BindingResult bindingResult){
		if(zbusiness == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		} 

		if(StringUtils.isBlank(zbusiness.getZid())) {
			responseHelper.setErrorStatusAndMessage("Business ID required");
			return responseHelper.getResponse();
		}
		
		// if existing
		if(StringUtils.isNotBlank(zbusiness.getZid())) {
			Zbusiness zb = zbusinessService.findBById(zbusiness.getZid());
			BeanUtils.copyProperties(zbusiness, zb,"zid");
			long count = zbusinessService.update(zb);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update Business info");
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Business info updated successfully");
			responseHelper.setRedirectUrl("/system/businessprof/" + zb.getZid());
			return responseHelper.getResponse();
		}

		// If new
		long count = zbusinessService.save(zbusiness);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Business info saved successfully");
		responseHelper.setRedirectUrl("/system/businessprof");
		return responseHelper.getResponse();
	}


}
