package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.asl.entity.Moheader;
import com.asl.enums.TransactionCodeType;

/**
 * @author Zubayer Ahamed
 * @since Mar 18, 2021
 */
@Controller
@RequestMapping("/production/batch")
public class ProductionProcessBatchController extends ASLAbstractController {

	

	@GetMapping
	public String loadProductionBatchPage(Model model) {
		model.addAttribute("batch", getDefaultMoHeader());
		model.addAttribute("batchPrefix", xtrnService.findByXtrn(TransactionCodeType.BATCH_NUMBER.getCode(), Boolean.TRUE));
		return "pages/production/batch/batch";
	}

	private Moheader getDefaultMoHeader() {
		Moheader mh = new Moheader();
		mh.setXtypetrn(TransactionCodeType.BATCH_NUMBER.getCode());
		mh.setXtrn(TransactionCodeType.BATCH_NUMBER.getdefaultCode());
		mh.setXstatusmor("Open");
		return mh;
	}
}
