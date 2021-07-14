package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.asl.enums.CodeType;
import com.asl.enums.TransactionCodeType;

@Controller
@RequestMapping("/receivable/moneyreceipt")
public class AccountsMoneyReceiptController extends ASLAbstractController{
	
	@GetMapping
	public String loadMoneyReceiptPage(Model model) {

		
		return "pages/accounts/receivable/moneyreceipt";
	}

}
