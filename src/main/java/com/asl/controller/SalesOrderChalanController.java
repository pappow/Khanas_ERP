package com.asl.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.Oporddetail;
import com.asl.entity.Opordheader;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.OpordService;

/**
 * @author Zubayer Ahamed
 * @since Mar 9, 2021
 */
@Controller
@RequestMapping("/salesninvoice/salesorderchalan")
public class SalesOrderChalanController extends ASLAbstractController {

	@Autowired private OpordService opordService;

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@GetMapping
	public String loadSalesOrderPage(Model model) {
		Opordheader chalan = opordService.findOpordHeaderByXtypetrnAndXtrnAndXdate(TransactionCodeType.CHALAN_NUMBER.getCode(), TransactionCodeType.CHALAN_NUMBER.getdefaultCode(), new Date());
		List<Oporddetail> chalanDetails = new ArrayList<>();
		if(chalan != null) {
			chalanDetails = opordService.findOporddetailByXordernum(chalan.getXordernum());
			if(chalanDetails == null) chalanDetails = new ArrayList<>();
		}
		if(chalan == null) chalan = new Opordheader();
		model.addAttribute("chalan", chalan);
		model.addAttribute("chalanDetails", chalanDetails);
		return "pages/salesninvoice/salesorderchalan/salesorderchalan";
	}

	@GetMapping("/query")
	public String reloadTableWithData(@RequestParam String date, Model model) throws ParseException {
		Opordheader chalan = opordService.findOpordHeaderByXtypetrnAndXtrnAndXdate(TransactionCodeType.CHALAN_NUMBER.getCode(), TransactionCodeType.CHALAN_NUMBER.getdefaultCode(), sdf.parse(date));
		List<Oporddetail> chalanDetails = new ArrayList<>();
		if(chalan != null) {
			chalanDetails = opordService.findOporddetailByXordernum(chalan.getXordernum());
			if(chalanDetails == null) chalanDetails = new ArrayList<>();
		}
		if(chalan == null) chalan = new Opordheader();
		model.addAttribute("chalan", chalan);
		model.addAttribute("chalanDetails", chalanDetails);
		return "pages/salesninvoice/salesorderchalan/salesorderchalan::salesorderchalansection";
	}

	@PostMapping("/query")
	public @ResponseBody Map<String, Object> queryForChalanDetails(Date xdate, Model model){
		responseHelper.setReloadSectionIdWithUrl("salesorderchalansection", "/salesninvoice/salesorderchalan/query?date=" + sdf.format(xdate));
		responseHelper.setStatus(ResponseStatus.SUCCESS);
		return responseHelper.getResponse();
	}
}
