package com.asl.controller;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.PoordHeader;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.service.PoordService;
import com.asl.service.XcodesService;

//Purchase Order Controller

@Controller
@RequestMapping("/purchasing/poord")
public class PoordController extends ASLAbstractController {
	
	@Autowired private XcodesService xcodeService;
	@Autowired private PoordService poordService;
	
	@GetMapping
	public String loadPoordPage(Model model) {
		
		model.addAttribute("poordheader", new PoordHeader());
		//model.addAttribute("xitemCategories", xcodeService.findByXtype(CodeType.ITEM_CATEGORY.getCode()));
		model.addAttribute("allPoordHeader", poordService.getAllPoordHeaders());
		//model.addAttribute("warehouses", xcodeService.findByXtype(CodeType..getCode()));
		
		return "pages/purchasing/poord/poord";
	}
	
	
	@GetMapping("/{xpornum}")
	public String loadPoordPage(@PathVariable String xpornum, Model model) {
				
		PoordHeader data = poordService.findPoordHeaderByXpornum(xpornum); 
		if(data == null)
			return "redirect:/purchasing/poord";
		 
		model.addAttribute("poordheader", data);
		model.addAttribute("allPoordHeader", poordService.getAllPoordHeaders());
		//model.addAttribute("", poordService.getPoordDetailsByXpornum(xpornum));
		return "pages/purchasing/poord/poord";
	}
	
	
	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(PoordHeader poordHeader, BindingResult bindingResult){
		
		if(poordHeader == null || StringUtils.isBlank(poordHeader.getXpornum())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// Validate

		// if existing record
		PoordHeader existPoordHeader = poordService.findPoordHeaderByXpornum(poordHeader.getXpornum());
		
		if(existPoordHeader != null) {
			BeanUtils.copyProperties(poordHeader, existPoordHeader, "xpornum");
			long count = poordService.update(existPoordHeader);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Purchase Order updated successfully");
			responseHelper.setRedirectUrl("/purchasing/poord/" + poordHeader.getXpornum());
			return responseHelper.getResponse();
		}

		// If new
		long count = poordService.save(poordHeader);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Purchase Order created successfully");
		responseHelper.setRedirectUrl("/purchasing/poord/" + poordHeader.getXpornum());
		return responseHelper.getResponse();
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

		poordHeader.setZactive(archive ? Boolean.FALSE : Boolean.TRUE);
		long count = poordService.update(poordHeader);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Purchase order updated successfully");
		responseHelper.setRedirectUrl("/purchasing/poord/" + poordHeader.getXpornum());
		return responseHelper.getResponse();
	}
	
}
