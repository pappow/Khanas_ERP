package com.asl.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.asl.entity.Acheader;
import com.asl.enums.TransactionCodeType;
import com.asl.service.AcService;

@Controller
@RequestMapping("/account/voucher")
public class AccountVoucherController extends ASLAbstractController{

	@Autowired private AcService acService;

	@GetMapping
	public String loadAccountVoucherPage(Model model) {

		model.addAttribute("acheader", getDefaultAcheader());
		model.addAttribute("voucherprefix", xtrnService.findByXtypetrn(TransactionCodeType.GL_VOUCHER.getCode(), Boolean.TRUE));

		return"pages/account/voucher/voucher";
	}

	private Acheader getDefaultAcheader() {
		Acheader acheader = new Acheader();
		acheader.setXtypetrn(TransactionCodeType.GL_VOUCHER.getCode());
		acheader.setXtrn(TransactionCodeType.GL_VOUCHER.getdefaultCode());
		acheader.setXstatusjv("Balanced");
		acheader.setXdate(new Date());
		return acheader;
	}

	

}
