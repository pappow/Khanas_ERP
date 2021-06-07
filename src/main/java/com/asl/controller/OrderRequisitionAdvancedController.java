package com.asl.controller;

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
import com.asl.enums.ResponseStatus;
import com.asl.service.CaitemService;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Jun 7, 2021
 */
@Controller
@RequestMapping("/procurement/requisition")
public class OrderRequisitionAdvancedController extends ASLAbstractController {

	@Autowired private CaitemService caitemService;

	@GetMapping
	public String loadOrderRequisitionAdvancedpage(Model model) {
		model.addAttribute("items", caitemService.getAllItemsWithoutRawMaterials());
		return "pages/procurement/requisition/requisition";
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(List<Oporddetail> details){
		
		System.out.println(details.size());
		
		responseHelper.setStatus(ResponseStatus.ERROR);
		
		return responseHelper.getResponse();
	}
}

