package com.asl.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.OpordService;

/**
 * @author Zubayer Ahamed
 * @since Mar 9, 2021
 */
@Controller
@RequestMapping("/salesninvoice/salesorder")
public class SalesOrderController extends ASLAbstractController {

	@Autowired private OpordService opordService;

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@GetMapping
	public String loadSalesOrderPage(Model model) {
		model.addAttribute("salesOrders", opordService.findAllOpordHeaderByXtypetrnAndXtrnAndXdate(TransactionCodeType.SALES_ORDER.getCode(), "SO-", new Date()));
		return "pages/salesninvoice/salesorders/salesorders";
	}

	@GetMapping("/query")
	public String reloadTableWithData(@RequestParam String date, Model model) throws ParseException {
		model.addAttribute("salesOrders", opordService.findAllOpordHeaderByXtypetrnAndXtrnAndXdate(TransactionCodeType.SALES_ORDER.getCode(), "SO-", sdf.parse(date)));
		return "pages/salesninvoice/salesorders/salesorders::salesordertable";
	}

	@PostMapping("/query")
	public @ResponseBody Map<String, Object> queryForrequistionDetails(Date xdate, Model model){
		responseHelper.setReloadSectionIdWithUrl("salesordertable", "/salesninvoice/salesorder/query?date=" + sdf.format(xdate));
		responseHelper.setStatus(ResponseStatus.SUCCESS);
		return responseHelper.getResponse();
	}
}
