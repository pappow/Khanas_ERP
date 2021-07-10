package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.asl.entity.Acheader;
import com.asl.enums.TransactionCodeType;

@Controller
@RequestMapping("/account/voucher")
public class AccountVoucherController extends ASLAbstractController{
	
	@GetMapping
	public String loadAccountVoucherPage(Model model) {
		//return "pages/account/voucher/voucher";
		
		
		
		
		
		
		return"pages/accounts/voucherheader";
	}

	private Acheader getDefaultAcheader() {
		Acheader acheader = new Acheader();
		acheader.setXtypetrn(TransactionCodeType.GL_VOUCHER.getCode());
		acheader.setXtrn(TransactionCodeType.GL_VOUCHER.getdefaultCode());
		acheader.setXstatus("Balanced");
		return acheader;
	}

	

}
