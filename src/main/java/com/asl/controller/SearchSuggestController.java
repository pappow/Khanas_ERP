package com.asl.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.Cacus;
import com.asl.entity.Caitem;
import com.asl.enums.TransactionCodeType;
import com.asl.model.SearchSuggestResult;
import com.asl.service.CacusService;
import com.asl.service.CaitemService;

/**
 * @author Zubayer Ahamed
 * @since Mar 2, 2021
 */
@Controller
@RequestMapping("/search")
public class SearchSuggestController extends ASLAbstractController {

	@Autowired private CacusService cacusService;
	@Autowired private CaitemService caitemService;

	@GetMapping("/supplier/{hint}")
	public @ResponseBody List<SearchSuggestResult> getSuppliers(@PathVariable String hint){
		List<Cacus> cacusList = cacusService.findByXtype(TransactionCodeType.SUPPLIER_NUMBER.getCode());
		List<SearchSuggestResult> list = new ArrayList<>();
		cacusList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXcus(), c.getXcus())));
		return list;
	}

	@GetMapping("/caitem/{hint}")
	public @ResponseBody List<SearchSuggestResult> getCaitems(@PathVariable String hint){
		List<Caitem> cacusList = caitemService.getAllCaitems();
		List<SearchSuggestResult> list = new ArrayList<>();
		cacusList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXitem(), c.getXitem())));
		return list;
	}
}
