package com.asl.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import com.asl.entity.PoordDetail;
import com.asl.entity.PoordHeader;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.model.ServiceException;
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
		model.addAttribute("items", caitemService.getAllRequisitionItems());
		return "pages/procurement/requisition/requisition";
	}

	@GetMapping("/{xpornum}")
	public String loadOrderRequisitionAdvancedpage(@PathVariable String xpornum, Model model) {
		PoordHeader ph = poordService.findPoordHeaderByXpornum(xpornum);
		if(ph == null) return "redirect:/procurement/requisition";

		model.addAttribute("poordheader", ph);

		List<PoordDetail> details = poordService.findPoorddetailByXpornum(xpornum);
		List<Caitem> caitems = caitemService.getAllRequisitionItems();
		if(details != null && !details.isEmpty()) {
			for(Caitem c : caitems) {
				for(PoordDetail d : details) {
					if(d.getXitem().equalsIgnoreCase(c.getXitem())) {
						//BeanUtils.copyProperties(d, c);
						c.setXqtyord(d.getXqtyord());
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
			responseHelper.setErrorStatusAndMessage(e.getMessage());
			return responseHelper.getResponse();
		}

		if(itemdetails == null || itemdetails.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Please add quantity atleast one product");
			return responseHelper.getResponse();
		}

		List<PoordDetail> details = itemdetails.stream().filter(i -> i.getXqtyord() != null && !i.getXqtyord().equals(BigDecimal.ZERO)).collect(Collectors.toList());
		if(details == null || details.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Please add quantity atleast one product");
			return responseHelper.getResponse();
		}

		if("NEW".equalsIgnoreCase(poh.getXpornum())) {
			PoordHeader poordHeader = new PoordHeader();
			poordHeader.setXtype(TransactionCodeType.REQUISITION_ORDER.getCode());
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
			int i = 1;
			for(PoordDetail d : details) {
				d.setXrow(i);
				d.setXpornum(poordHeader.getXpornum());
				if(d.getXrate() == null) d.setXrate(BigDecimal.ZERO);
				d.setXlineamt(d.getXqtyord().multiply(d.getXrate()));
				i++;
			}

			long countd = 0;
			try {
				countd = poordService.saveDetail(details);
			} catch (ServiceException e) {
				log.error(ERROR, e.getMessage(), e);
				responseHelper.setErrorStatusAndMessage(e.getMessage());
				return responseHelper.getResponse();
			}
			if(countd == 0) {
				responseHelper.setErrorStatusAndMessage("Can't Add item details");
				return responseHelper.getResponse();
			}

			responseHelper.setSuccessStatusAndMessage("Requisition created successfully");
			responseHelper.setRedirectUrl("/procurement/requisition/" + poordHeader.getXpornum());
			return responseHelper.getResponse();
		} 


		// Archive all details first
		if(!poordService.findPoorddetailByXpornum(poh.getXpornum()).isEmpty()) {
			long count = poordService.deleteDetailByXpornum(poh.getXpornum());
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update requisition details");
				return responseHelper.getResponse();
			}
		}

		// Create all details
		int i = 1;
		for(PoordDetail d : details) {
			d.setXrow(i);
			d.setXpornum(poh.getXpornum());
			if(d.getXrate() == null) d.setXrate(BigDecimal.ZERO);
			d.setXlineamt(d.getXqtyord().multiply(d.getXrate()));
			i++;
		}

		long countd = 0;
		try {
			countd = poordService.saveDetail(details);
		} catch (ServiceException e) {
			log.error(ERROR, e.getMessage(), e);
			responseHelper.setErrorStatusAndMessage(e.getMessage());
			return responseHelper.getResponse();
		}
		if(countd == 0) {
			responseHelper.setErrorStatusAndMessage("Can't Add item details");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Requisition created successfully");
		responseHelper.setRedirectUrl("/procurement/requisition/" + poh.getXpornum());
		return responseHelper.getResponse();
	}

	@GetMapping("/allrequisition")
	public String loadAllReqListPage(Model model) {
		model.addAttribute("reqs", poordService.getPoordHeadersByXtype(TransactionCodeType.REQUISITION_ORDER.getCode()));
		return "pages/procurement/requisition/requisitionslist";
	}

	@PostMapping("/archive/{xpornum}")
	public @ResponseBody Map<String, Object> archive(@PathVariable String xpornum){
		return doArchiveOrRestore(xpornum, true);
	}

	@PostMapping("/restore/{xpornum}")
	public @ResponseBody Map<String, Object> restore(@PathVariable String xpornum){
		return doArchiveOrRestore(xpornum, false);
	}

	public Map<String, Object> doArchiveOrRestore(String xpornum, boolean archive){
		PoordHeader poordHeader = poordService.findPoordHeaderByXpornum(xpornum);
		if(poordHeader == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// archive all details
		if(archive && poordService.countOfRequisitionDetailsByXpornum(xpornum) > 0) {
			long count2 = poordService.archiveAllPoordDetailByXpornum(xpornum);
			if(count2 == 0) {
				responseHelper.setErrorStatusAndMessage("Can't archive details");
				return responseHelper.getResponse();
			}
		}

		poordHeader.setZactive(archive ? Boolean.FALSE : Boolean.TRUE);
		long count = poordService.update(poordHeader);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Requisition order deleted successfully");
		responseHelper.setRedirectUrl("/procurement/requisition/" + poordHeader.getXpornum());
		return responseHelper.getResponse();
	}
}

