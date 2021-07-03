package com.asl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.asl.entity.AccountGroup;
import com.asl.enums.AccountType;
import com.asl.service.AccountGroupService;

@Controller
@RequestMapping("/acgroup")
public class AccountGroupController extends ASLAbstractController {

	@Autowired private AccountGroupService agService;

	@GetMapping
	public String LoadAccountGroupPage(@RequestParam int level,  Model model){
		model.addAttribute("acgroup", getDefaultAccountGroup());
		return "pages/accountgroup/accountgroup";
	}

	private AccountGroup getDefaultAccountGroup() {
		AccountGroup ag = new AccountGroup();
		ag.setXagtype(AccountType.ASSET.getCode());
		ag.setXaglevel(1);
		return ag;
	}
}
