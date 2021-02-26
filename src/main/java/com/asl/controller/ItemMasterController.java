package com.asl.controller;

import java.util.Map;

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

import com.asl.entity.Xcodes;
import com.asl.enums.ResponseStatus;
import com.asl.service.ListService;
import com.asl.service.XcodesService;

/**
 * @author Zubayer Ahamed
 * @since Feb 24, 2021
 */
@Controller
@RequestMapping("/mastersetup/itemmaster")
public class ItemMasterController extends ASLAbstractController {

	@Autowired private ListService listService;
	@Autowired private XcodesService xcodesService;

	@GetMapping
	public String loadItemMasterpage(Model model) {
		return "pages/mastersetup/itemmaster/itemmaster";
	}

	@GetMapping("/code")
	public String loadItemCodePage(Model model) {
		model.addAttribute("codeTypes", listService.getList("CODE_TYPE", "ITEM_CODE"));
		model.addAttribute("xcodes", new Xcodes());
		model.addAttribute("xcodesList", xcodesService.getAllXcodes());
		return "pages/mastersetup/itemmaster/code";
	}

	@GetMapping("/code/{xtype}/{xcode}")
	public String loadItemCodePage(@PathVariable String xtype, @PathVariable String xcode, Model model) {
		Xcodes xcodes = xcodesService.findByXtypesAndXcodes(xtype, xcode);
		if(xcodes == null) return "redirect:/mastersetup/itemmaster/code";

		model.addAttribute("codeTypes", listService.getList("CODE_TYPE", "ITEM_CODE"));
		model.addAttribute("xcodes", xcodes);
		model.addAttribute("xcodesList", xcodesService.getAllXcodes());
		return "pages/mastersetup/itemmaster/code";
	}

	@PostMapping("/code/save")
	public @ResponseBody Map<String, Object> saveItemCode(Xcodes xcodes, BindingResult bindingResult){
		if(xcodes == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// modify xcodes
		
		// validate xcodes

		// If existing xcodes
		Xcodes existXcodes = xcodesService.findByXtypesAndXcodes(xcodes.getXtype(), xcodes.getXcode());
		if(existXcodes != null) {
			BeanUtils.copyProperties(xcodes, existXcodes, "xcode");
			long count = xcodesService.update(existXcodes);
			if(count != 1) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Code updated successfully");
			responseHelper.setRedirectUrl("/mastersetup/itemmaster/code/" + xcodes.getXtype() + "/" + xcodes.getXcode());
			return responseHelper.getResponse();
		}

		// if new xcodes
		long count = xcodesService.save(xcodes);
		if(count != 1) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Code saved successfully");
		responseHelper.setRedirectUrl("/mastersetup/itemmaster/code/" + xcodes.getXtype() + "/" + xcodes.getXcode());
		return responseHelper.getResponse();
	}

	@GetMapping("/code/paging/jump/{direction}")
	public String itemCodePaging(@PathVariable("direction") String direction, Model model) {
		String sort = "ASC";
		if("bottom".equalsIgnoreCase(direction)) sort = "DESC";
		Xcodes xcodes = xcodesService.getSeilingRecord(sort);
		if(xcodes == null) xcodes = new Xcodes();
		model.addAttribute("codeTypes", listService.getList("CODE_TYPE", "ITEM_CODE"));
		model.addAttribute("xcodes", xcodes);
		model.addAttribute("xcodesList", xcodesService.getAllXcodes());
		return "pages/mastersetup/itemmaster/code";
	}
}
