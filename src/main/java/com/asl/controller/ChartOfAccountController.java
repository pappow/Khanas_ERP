package com.asl.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.AccountGroup;
import com.asl.entity.Acgroup;
import com.asl.entity.Acmst;
import com.asl.service.AccountGroupService;
import com.asl.service.AcmstService;

@Controller
@RequestMapping("/account/coa")
public class ChartOfAccountController extends ASLAbstractController {

	@Autowired private AcmstService acmstService;
	@Autowired private AccountGroupService accountGroupService;

	@GetMapping
	public String loadChartAccountrPage(Model model) {
		model.addAttribute("acmst", getDefaultAcmst());
		model.addAttribute("acmstlist", acmstService.getAllAcmst());
		return "pages/account/chartofaccount/chartofaccount";
	}

	private Acmst getDefaultAcmst() {
		Acmst acmst = new Acmst();
		acmst.setXaccusage("Ledger");
		return acmst;
	}

	@GetMapping("/{xacc}")
	public String loadChartAccountrPage(@PathVariable String xacc, Model model) {
		
		
		
		return "pages/account/chartofaccount/chartofaccount";
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Acmst acmst, BindingResult bindingResult){

		// validation
		if(StringUtils.isBlank(acmst.getXdesc())) {
			responseHelper.setErrorStatusAndMessage("Account name required");
			return responseHelper.getResponse();
		}
		if(StringUtils.isBlank(acmst.getXgroup())) {
			responseHelper.setErrorStatusAndMessage("Please select a account group");
			return responseHelper.getResponse();
		}
		
		
		// if existing
		
		
		
		// if new
		AccountGroup acgroup = accountGroupService.findByCode(acmst.getXgroup());
		if(acgroup == null) {
			responseHelper.setErrorStatusAndMessage("Account group not found in this system");
			return responseHelper.getResponse();
		}

		acmst.setXacctype(acgroup.getXagtype());
		long count = acmstService.save(acmst);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't save this account");
		}

		return responseHelper.getResponse();
	}

	

}
