package com.asl.controller;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.AccountGroup;
import com.asl.enums.AccountType;
import com.asl.service.AccountGroupService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/acgroup")
public class AccountGroupController extends ASLAbstractController {

	@Autowired private AccountGroupService agService;

	@GetMapping
	public String LoadAccountGroupPage(@RequestParam(required = false) int level, Model model){

		model.addAttribute("acgroup", getDefaultAccountGroup(level));
		return "pages/accountgroup/accountgroup";
	}

	private AccountGroup getDefaultAccountGroup(int level) {
		AccountGroup ag = new AccountGroup();
		ag.setXagtype(AccountType.ASSET.getCode());
		ag.setXaglevel(level == 0 ? 1 : level);
		return ag;
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(AccountGroup accountGroup){
		// validation
		if(StringUtils.isBlank(accountGroup.getXagname())) {
			responseHelper.setErrorStatusAndMessage("Group Name Required");
			return responseHelper.getResponse();
		}
		if(StringUtils.isBlank(accountGroup.getXagcode())) {
			responseHelper.setErrorStatusAndMessage("Group Code Required");
			return responseHelper.getResponse();
		}
		if(StringUtils.isBlank(accountGroup.getXagtype())) {
			responseHelper.setErrorStatusAndMessage("Group Type Required");
			return responseHelper.getResponse();
		}
		try {
			Long parsedValue = Long.valueOf(accountGroup.getXagcode());
		} catch (Exception e) {
			log.error(ERROR, e.getMessage(), e);
			responseHelper.setErrorStatusAndMessage(e.getMessage());
			return responseHelper.getResponse();
		}

		// if existing
		if(StringUtils.isNotBlank(accountGroup.getXagcode())) {
			
		}

		// if new
		long count = agService.save(accountGroup);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't save account group");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Account group created successfully");
		responseHelper.setRedirectUrl("/acgroup?level=" + accountGroup.getXaglevel());
		return responseHelper.getResponse();
	}
}
