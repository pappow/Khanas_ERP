package com.asl.controller;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.Arhed;
import com.asl.enums.CodeType;
import com.asl.enums.TransactionCodeType;
import com.asl.service.ArhedService;
import com.asl.service.XcodesService;
import com.asl.service.XtrnService;

@Controller
@RequestMapping("/purchasing/supplierpayment")
public class SupplierPaymentController extends ASLAbstractController {
	
	@Autowired
	private ArhedService arhedService;
	@Autowired
	private XtrnService xtrnService;
	@Autowired
	private XcodesService xcodeService;
	
	
	@GetMapping
	public String loadSupplierPaymentPage(Model model) {
		
		model.addAttribute("arhed", getDefaultArhed());
		
		model.addAttribute("allArhed", arhedService.getAllArheds());
		
		return "pages/supplierpayment/arhed/arhed";
	}
	
	@GetMapping("/{xvoucher}")
	public String loadSupplierPaymentPage(@PathVariable String xvoucher, Model model) {
		
		Arhed data = arhedService.findArhedByXvoucher(xvoucher);
		if(data == null) data = getDefaultArhed();

		model.addAttribute("arhed", data);
		model.addAttribute("allArhed", new ArrayList<Arhed>());
		model.addAttribute("arhedprefix", xtrnService.findByXtypetrn(TransactionCodeType.INVENTORY_NUMBER.getCode()));
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.WAREHOUSE.getCode()));
		
		return "pages/purchasing/pogrn/pogrn";
	}
	
	private Arhed getDefaultArhed() {
		Arhed arhed = new Arhed();
		//arhed.setXtype(TransactionCodeType.GRN_NUMBER.getCode());
		//arhed.setXtotamt(BigDecimal.ZERO);
		return arhed;
	}
	
	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Arhed arhed, BindingResult bindingResult){
		
		//responseHelper.setStatus(ResponseStatus.ERROR);
		return null;
	}
	
	@PostMapping("/archive/{xvoucher}")
	public @ResponseBody Map<String, Object> archive(@PathVariable String xvoucher){
		return doArchiveOrRestore(xvoucher, true);
	}

	@PostMapping("/restore/{xvoucher}")
	public @ResponseBody Map<String, Object> restore(@PathVariable String xvoucher){
		return doArchiveOrRestore(xvoucher, false);
	}

	public Map<String, Object> doArchiveOrRestore(String xvoucher, boolean archive){
		
		return null;
	}


}
