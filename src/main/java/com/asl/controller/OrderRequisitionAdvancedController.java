package com.asl.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.Caitem;
import com.asl.entity.Oporddetail;
import com.asl.entity.PoordDetail;
import com.asl.entity.PoordHeader;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.CaitemService;
import com.asl.service.PoordService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Jun 7, 2021
 */
@Slf4j
@Controller
@RequestMapping("/procurement/requisition")
public class OrderRequisitionAdvancedController extends ASLAbstractController {

	@Autowired private CaitemService caitemService;
	@Autowired private PoordService poordService;

	@GetMapping
	public String loadOrderRequisitionAdvancedpage(Model model) {
		PoordHeader poordheader = new PoordHeader();
		poordheader.setXpornum("NEW");
		model.addAttribute("poordheader", poordheader);
		model.addAttribute("items", caitemService.getAllItemsWithoutRawMaterials());
		return "pages/procurement/requisition/requisition";
	}

	@GetMapping("/{xpornum}")
	public String loadOrderRequisitionAdvancedpage(@PathVariable String xpornum, Model model) {
		PoordHeader ph = poordService.findPoordHeaderByXpornum(xpornum);
		if(ph == null) return "redirect:/procurement/requisition";

		model.addAttribute("poordheader", ph);

		List<PoordDetail> details = poordService.findPoorddetailByXpornum(xpornum);
		List<Caitem> caitems = caitemService.getAllItemsWithoutRawMaterials();
		if(details != null && !details.isEmpty()) {
			for(Caitem c : caitems) {
				for(PoordDetail d : details) {
					if(d.getXitem().equalsIgnoreCase(c.getXcatitem())) {
						BeanUtils.copyProperties(d, c);
					}
				}
			}
		}

		model.addAttribute("items", caitems);
		return "pages/procurement/requisition/requisition";
	}

	@PostMapping(value = "/save", headers="Accept=application/json")
	public @ResponseBody Map<String, Object> save(@RequestBody String json) throws UnsupportedEncodingException{

		
		PoordHeader poh = new PoordHeader();
		Collection<PoordDetail> itemdetails = null;
		ObjectMapper obm = new ObjectMapper();
		try {
			poh = obm.readValue(json, PoordHeader.class);
			JsonNode rootNode = obm.readTree(json);

			JsonNode itemsNode = rootNode.get("items");
			TypeFactory typeFactory = obm.getTypeFactory();
			CollectionType cType = typeFactory.constructCollectionType(List.class, PoordDetail.class);
			itemdetails = obm.readValue(itemsNode.toString(), cType);
		} catch (JsonProcessingException e) {
			log.error(ERROR, e.getMessage(), e);
		}

		List<PoordDetail> details = itemdetails.stream().filter(i -> i.getXqtyord() != null && !i.getXqtyord().equals(BigDecimal.ZERO)).collect(Collectors.toList());
		if(details == null || details.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Please add quantity atleast one product");
			return responseHelper.getResponse();
		}

		if("NEW".equalsIgnoreCase(poh.getXpornum())) {
			PoordHeader poordHeader = new PoordHeader();
			poordHeader.setXtypetrn(TransactionCodeType.REQUISITION_ORDER.getCode());
			poordHeader.setXtrn(TransactionCodeType.REQUISITION_ORDER.getdefaultCode());
			poordHeader.setXdate(new Date());
			poordHeader.setXstatuspor("Open");
			
			long count = poordService.save(poordHeader);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't create Order Requisition");
				return responseHelper.getResponse();
			}

			// Create all details
			details.stream().forEach(d -> {
				d.setXpornum(poordHeader.getXpornum());
				if(d.getXrate() == null) d.setXrate(BigDecimal.ZERO);
				d.setXlineamt(d.getXqtyord().multiply(d.getXrate()));
			});

			long countd = poordService.saveDetail(details);
			if(countd == 0) {
				responseHelper.setErrorStatusAndMessage("Can't Add item details");
				return responseHelper.getResponse();
			}

			responseHelper.setSuccessStatusAndMessage("Requisition created successfully");
			responseHelper.setRedirectUrl("/procurement/requisition/" + poordHeader.getXpornum());
			return responseHelper.getResponse();
		} else {
			
		}
		
		
		
		
		
		responseHelper.setStatus(ResponseStatus.ERROR);
		
		return responseHelper.getResponse();
	}
}

