package com.asl.controller;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.Pdmst;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;

/**
 * @author Zubayer Ahamed
 * @since May 6, 2021
 */
@Controller
@RequestMapping("/system/staff")
public class StaffController extends ASLAbstractController {

	@GetMapping
	public String loadStappPage(Model model) {
		model.addAttribute("pdmst", getDefaultPdmst());

		model.addAttribute("prefix", xtrnService.findByXtypetrn(TransactionCodeType.EMPLOYEE_ID.getCode(), Boolean.TRUE));
		model.addAttribute("designations", xcodesService.findByXtype(CodeType.EMPLOYEE_DESIGNATION.getCode(), Boolean.TRUE));
		model.addAttribute("departments", xcodesService.findByXtype(CodeType.EMPLOYEE_DEPARTMENT.getCode(), Boolean.TRUE));
		model.addAttribute("empcategories", xcodesService.findByXtype(CodeType.EMPLOYEE_CATEGORY.getCode(), Boolean.TRUE));
		model.addAttribute("empstatus", xcodesService.findByXtype(CodeType.EMPLOYEE_STATUS.getCode(), Boolean.TRUE));
		model.addAttribute("emptypes", xcodesService.findByXtype(CodeType.EMPLOYEE_TYPE.getCode(), Boolean.TRUE));
		model.addAttribute("sex", xcodesService.findByXtype(CodeType.SEX.getCode(), Boolean.TRUE));
		model.addAttribute("religion", xcodesService.findByXtype(CodeType.RELIGION.getCode(), Boolean.TRUE));
		model.addAttribute("bloodgroup", xcodesService.findByXtype(CodeType.BLOOD_GROUP.getCode(), Boolean.TRUE));

		return "pages/system/usersentry/staff/staff";
	}

	private Pdmst getDefaultPdmst() {
		Pdmst pdmst = new Pdmst();
		pdmst.setXtypetrn(TransactionCodeType.EMPLOYEE_ID.getCode());
		pdmst.setXtrn(TransactionCodeType.EMPLOYEE_ID.getdefaultCode());
		pdmst.setXsex("Male");
		pdmst.setXempstatus("Normal");
		return pdmst;
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Pdmst pdmst, BindingResult bindingResult){
		if(pdmst == null || StringUtils.isBlank(pdmst.getXtypetrn()) || StringUtils.isBlank(pdmst.getXtrn())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		
		
		
		
		responseHelper.setSuccessStatusAndMessage("Staff info saved successfully");
		responseHelper.setRedirectUrl("/system/staff");
		return responseHelper.getResponse();
	}
}
