package com.asl.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		List<Cacus> cacusList = cacusService.searchCacus(TransactionCodeType.SUPPLIER_NUMBER.getCode(), hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		cacusList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXcus(), c.getXcus() + " - " + c.getXorg())));
		return list;
	}

	@GetMapping("/caitem/{hint}")
	public @ResponseBody List<SearchSuggestResult> getCaitems(@PathVariable String hint){
		List<Caitem> caitemList = caitemService.searchCaitem(hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		caitemList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXitem(), c.getXitem() + " - " + c.getXdesc())));
		return list;
	}

	@GetMapping("/caitem/central/{hint}")
	public @ResponseBody List<SearchSuggestResult> getCentralCaitems(@PathVariable String hint){
		List<Caitem> caitemList = caitemService.searchCentralCaitem(hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		caitemList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXitem(), c.getXitem() + " - " + c.getXdesc())));
		return list;
	}
}
