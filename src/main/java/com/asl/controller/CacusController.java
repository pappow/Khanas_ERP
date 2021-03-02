package com.asl.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.Cacus;
import com.asl.entity.Xcodes;
import com.asl.entity.Xtrn;
import com.asl.enums.CacusType;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.CacusService;
import com.asl.service.XcodesService;
import com.asl.service.XtrnService;

/**
 * @author Zubayer Ahamed
 * @since Feb 28, 2021
 */
@Controller
@RequestMapping("/mastersetup/cacus")
public class CacusController extends ASLAbstractController {

	@Autowired private XtrnService xtrnService;
	@Autowired private XcodesService xcodesService;
	@Autowired private CacusService cacusService;

	@GetMapping
	public String loadCacusPage(Model model, @RequestParam(required = false) String cacusType) {
		if(StringUtils.isBlank(cacusType)) return "redirect:/mastersetup/partymaster";
		model.addAttribute("cacusType", cacusType);
		model.addAttribute("cacus", getDefaultCacus(cacusType));
		setOtherDefaultData(cacusType, model);
		return "pages/mastersetup/cacus/cacus";
	}

	@GetMapping("/{xcus}")
	public String loadCacusPage(@PathVariable String xcus, @RequestParam(required = false) String cacusType, Model model) {
		if(StringUtils.isBlank(cacusType)) return "redirect:/mastersetup/partymaster";

		Cacus cacus = cacusService.findByXcus(xcus);
		if(cacus == null) cacus = getDefaultCacus(cacusType);

		model.addAttribute("cacusType", cacusType);
		model.addAttribute("cacus", cacus);
		setOtherDefaultData(cacusType, model);
		return "pages/mastersetup/cacus/cacus";
	}

	private Cacus getDefaultCacus(String cacusType) {
		Cacus cacus = new Cacus();
		cacus.setXcrlimit(BigDecimal.ZERO);
		cacus.setXtype("SUP".equalsIgnoreCase(cacusType) ? TransactionCodeType.SUPPLIER_NUMBER.getCode() : TransactionCodeType.CUSTOMER_NUMBER.getCode());
		return cacus;
	}

	private void setOtherDefaultData(String cacusType, Model model) {
		List<Xcodes> supplierStatus = new ArrayList<>();
		List<Xcodes> customerStatus = new ArrayList<>();

		List<Xcodes> supplierGroups = new ArrayList<>();
		List<Xcodes> customerGroups = new ArrayList<>();

		List<Xtrn> supplierTypes = new ArrayList<>();
		List<Xtrn> customerTypes = new ArrayList<>();

		List<Cacus> suppliersList = new ArrayList<>();
		List<Cacus> customersList = new ArrayList<>();

		if(CacusType.SUP.name().equalsIgnoreCase(cacusType)) {
			supplierStatus.addAll(xcodesService.findByXtype(CodeType.SUPPLIER_STATUS.getCode()));
			supplierGroups.addAll(xcodesService.findByXtype(CodeType.SUPPLIER_GROUP.getCode()));
			supplierTypes.addAll(xtrnService.findByXtypetrn(TransactionCodeType.SUPPLIER_NUMBER.getCode()));
			suppliersList.addAll(cacusService.findByXtype(TransactionCodeType.SUPPLIER_NUMBER.getCode()));
		} else if (CacusType.CUS.name().equalsIgnoreCase(cacusType)) {
			customerStatus.addAll(xcodesService.findByXtype(CodeType.CUSTOMER_STATUS.getCode()));
			customerGroups.addAll(xcodesService.findByXtype(CodeType.CUSTOMER_GROUP.getCode()));
			customerTypes.addAll(xtrnService.findByXtypetrn(TransactionCodeType.CUSTOMER_NUMBER.getCode()));
			customersList.addAll(cacusService.findByXtype(TransactionCodeType.CUSTOMER_NUMBER.getCode()));
		} else {
			supplierStatus.addAll(xcodesService.findByXtype(CodeType.SUPPLIER_STATUS.getCode()));
			customerStatus.addAll(xcodesService.findByXtype(CodeType.CUSTOMER_STATUS.getCode()));
			customerGroups.addAll(xcodesService.findByXtype(CodeType.CUSTOMER_GROUP.getCode()));
			supplierGroups.addAll(xcodesService.findByXtype(CodeType.SUPPLIER_GROUP.getCode()));
			supplierTypes.addAll(xtrnService.findByXtypetrn(TransactionCodeType.SUPPLIER_NUMBER.getCode()));
			customerTypes.addAll(xtrnService.findByXtypetrn(TransactionCodeType.CUSTOMER_NUMBER.getCode()));
			suppliersList.addAll(cacusService.findByXtype(TransactionCodeType.SUPPLIER_NUMBER.getCode()));
			customersList.addAll(cacusService.findByXtype(TransactionCodeType.CUSTOMER_NUMBER.getCode()));
		}
		model.addAttribute("supplierStatus", supplierStatus);
		model.addAttribute("customerStatus", customerStatus);
		model.addAttribute("supplierGroups", supplierGroups);
		model.addAttribute("customerGroups", customerGroups);
		model.addAttribute("supplierTypes", supplierTypes);
		model.addAttribute("customerTypes", customerTypes);
		model.addAttribute("suppliersList", suppliersList);
		model.addAttribute("customersList", customersList);
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Cacus cacus, String cacusType, BindingResult bindingResult){
		if(cacus == null || StringUtils.isBlank(cacus.getXtype()) || StringUtils.isBlank(cacus.getXcustype())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		
		// if existing
		Cacus existingCacus = cacusService.findByXcus(cacus.getXcus());
		if(existingCacus != null) {
			BeanUtils.copyProperties(cacus, existingCacus, "xtype", "xcustype", "xcus");
			long count = cacusService.update(existingCacus);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Data updated successfully");
			responseHelper.setRedirectUrl("/mastersetup/cacus/" + cacus.getXcus() + "?cacusType=" + cacusType);
			return responseHelper.getResponse();
		}

		// If new
		long count = cacusService.save(cacus);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Data saved successfully");
		responseHelper.setRedirectUrl("/mastersetup/cacus?cacusType=" + cacusType);
		return responseHelper.getResponse();
	}

	@PostMapping("/archive/{xcus}/{cacusType}")
	public @ResponseBody Map<String, Object> archive(@PathVariable String xcus, @PathVariable String cacusType){
		return doArchiveOrRestore(xcus, cacusType, true);
	}

	@PostMapping("/restore/{xcus}/{cacusType}")
	public @ResponseBody Map<String, Object> restore(@PathVariable String xcus, @PathVariable String cacusType){
		return doArchiveOrRestore(xcus, cacusType, false);
	}

	public Map<String, Object> doArchiveOrRestore(String xcus, String cacusType, boolean archive){
		Cacus cacus = cacusService.findByXcus(xcus);
		if(cacus == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		cacus.setZactive(archive ? Boolean.FALSE : Boolean.TRUE);
		long count = cacusService.update(cacus);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		responseHelper.setRedirectUrl("/mastersetup/cacus/" + cacus.getXcus() + "?cacusType=" + cacusType);
		return responseHelper.getResponse();
	}
}
