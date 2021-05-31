package com.asl.controller;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.Zbusiness;
import com.asl.enums.ResponseStatus;
import com.asl.service.ZbusinessService;
import com.asl.enums.ProfileType;

@Controller
@RequestMapping("/posbusiness")
public class POSBusinessController extends ASLAbstractController{
	@Autowired private ZbusinessService zbusinessService;
	
	@GetMapping
	public String loadPOSBusinessPage(Model model) {
		
		Zbusiness zbusiness = new Zbusiness();
		zbusiness.setXdine(ProfileType.C);
		model.addAttribute("business", zbusiness);		
		model.addAttribute("allBranchBusiness", zbusinessService.getAllBranchBusiness());

		return"pages/posbusiness";
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
		
		// If new
		long count = zbusinessService.save(zbusiness);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Data saved successfully");
		responseHelper.setRedirectUrl("/pages/posbusiness");
		return responseHelper.getResponse();
		
	}


}
