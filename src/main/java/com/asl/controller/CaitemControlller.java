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

import com.asl.entity.Caitem;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.service.CaitemService;
import com.asl.service.XcodesService;

@Controller
@RequestMapping("/mastersetup/caitem")
public class CaitemControlller extends ASLAbstractController {
	
	@Autowired private XcodesService xcodeService;
	@Autowired private CaitemService caitemService;
	
	
	@GetMapping
	public String loadCaitemPage(Model model) {
		
		//model.addAttribute("xcodesTypes", listService.getList("CODE_TYPE", "ITEM_CODE"));
		//model.addAttribute(attributeValue)
		
		model.addAttribute("caitem", new Caitem());
		model.addAttribute("xitemCategories", xcodeService.findByXtype(CodeType.ITEM_CATEGORY.getCode()));
		model.addAttribute("xitemGroups", xcodeService.findByXtype(CodeType.ITEM_GROUP.getCode()));
		model.addAttribute("xunitPurs", xcodeService.findByXtype(CodeType.PURCHASE_UNIT.getCode()));
		model.addAttribute("xunitSels", xcodeService.findByXtype(CodeType.SELLING_UNIT.getCode()));
		model.addAttribute("allCaitems", caitemService.getAllCaitems());
		return "pages/mastersetup/caitem/caitem";
	}
	
	@GetMapping("/{xitem}")
	public String loadCaitemPage(@PathVariable String xitem, Model model) {
				
		Caitem data = caitemService.findByXitem(xitem); 
		if(data == null)
			return "redirect:/mastersetup/caitem";
		
		model.addAttribute("caitem", data);
		model.addAttribute("xitemCategories", xcodeService.findByXtype(CodeType.ITEM_CATEGORY.getCode()));
		model.addAttribute("xitemGroups", xcodeService.findByXtype(CodeType.ITEM_GROUP.getCode()));
		model.addAttribute("xunitPurs", xcodeService.findByXtype(CodeType.PURCHASE_UNIT.getCode()));
		model.addAttribute("xunitSels", xcodeService.findByXtype(CodeType.SELLING_UNIT.getCode()));
		model.addAttribute("allCaitems", caitemService.getAllCaitems());
		 
		return "pages/mastersetup/caitem/caitem";
	}
	
	
	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Caitem caitem, BindingResult bindingResult){
		
		if(caitem == null || StringUtils.isBlank(caitem.getXitem())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// Modify xtrn code
		//xcodes.setXcode(xcodes.getXcode().trim());

		// Validate caitem

		// if existing record
		Caitem existCaitem = caitemService.findByXitem(caitem.getXitem());
		
		if(existCaitem != null) {
			BeanUtils.copyProperties(caitem, existCaitem, "xitem");
			long count = caitemService.update(existCaitem);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Item Master updated successfully");
			responseHelper.setRedirectUrl("/mastersetup/caitem/" + caitem.getXitem());
			return responseHelper.getResponse();
		}

		// If new caitem
		long count = caitemService.save(caitem);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Item Master saved successfully");
		responseHelper.setRedirectUrl("/mastersetup/caitem/" + caitem.getXitem());
		return responseHelper.getResponse();
	}
	

	@PostMapping("/archive/{xitem}")
	public @ResponseBody Map<String, Object> archive(@PathVariable String xitem){
		return doArchiveOrRestore(xitem, true);
	}

	@PostMapping("/restore/{xitem}")
	public @ResponseBody Map<String, Object> restore(@PathVariable String xitem){
		return doArchiveOrRestore(xitem, false);
	}
	
	public Map<String, Object> doArchiveOrRestore(String xitem, boolean archive){
		Caitem caitem = caitemService.findByXitem(xitem);
		if(caitem == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		caitem.setZactive(archive ? Boolean.FALSE : Boolean.TRUE);
		long count = caitemService.update(caitem);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Item Master updated successfully");
		responseHelper.setRedirectUrl("/mastersetup/caitem/" + xitem);
		return responseHelper.getResponse();
	}


}
