package com.asl.controller;

import java.util.Calendar;
import java.util.Date;
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

import com.asl.entity.Acdef;
import com.asl.entity.Acheader;
import com.asl.enums.TransactionCodeType;
import com.asl.service.AcService;
import com.asl.service.AcdefService;

@Controller
@RequestMapping("/account/voucher")
public class AccountVoucherController extends ASLAbstractController{

	@Autowired private AcService acService;
	@Autowired private AcdefService acdefService;

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

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Acheader acheader, BindingResult bindingResult){
		// validate
		if(acheader.getXdate() == null) {
			responseHelper.setErrorStatusAndMessage("Voucher date required");
			return responseHelper.getResponse();
		}

		// set year and date based on xdate   
		Acdef acdef = acdefService.find();
		if(acdef == null) {
			responseHelper.setErrorStatusAndMessage("Account default is not set in this sytem");
			return responseHelper.getResponse();
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(acheader.getXdate());

		

		// if existing
		if(StringUtils.isNotBlank(acheader.getXvoucher())) {
			
		}

		// if new
		
		
		
		return responseHelper.getResponse();
	}

}
