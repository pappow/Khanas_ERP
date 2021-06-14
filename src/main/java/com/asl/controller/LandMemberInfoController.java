package com.asl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.asl.entity.LandMemberInfo;
import com.asl.enums.CodeType;
import com.asl.enums.TransactionCodeType;
import com.asl.service.LandMemberInfoService;

@Controller
@RequestMapping("/landmemberinfo")
public class LandMemberInfoController extends ASLAbstractController{
	
	@Autowired private LandMemberInfoService landMemberInfoService;
	
	@GetMapping
	public String loadLandMemberInfoPage(Model model) {
		
		model.addAttribute("landmember", getDefaultLandmemberinfo());
		model.addAttribute("allLandMembers", landMemberInfoService.getAllLandMemberInfo());
		model.addAttribute("prefixes", xtrnService.findByXtypetrn(TransactionCodeType.LANDMEMBER_ID.getCode(), Boolean.TRUE));
		model.addAttribute("designationTypes", xcodesService.findByXtype(CodeType.MEMBER_DESIGNATION.getCode(), Boolean.TRUE));
		return "pages/land/landmemberinfo";
	}
	
	private LandMemberInfo getDefaultLandmemberinfo() {
		LandMemberInfo lm  = new LandMemberInfo();
		lm.setXtypetrn(TransactionCodeType.LANDMEMBER_ID.getCode());
		lm.setXtrn(TransactionCodeType.LANDMEMBER_ID.getdefaultCode());
		return lm;
	}
	

	@GetMapping("/{xmember}")
	public String loadMemberInfoPage(@PathVariable String xmember, Model model) {
		LandMemberInfo landMemberInfo = landMemberInfoService.findByLandMemberInfo(xmember);
		if (landMemberInfo == null) return "redirect:/landmemberinfo";
		
		model.addAttribute("landmember", landMemberInfo);
		model.addAttribute("allLandMembers", landMemberInfoService.getAllLandMemberInfo());
		model.addAttribute("prefixes", xtrnService.findByXtypetrn(TransactionCodeType.LANDMEMBER_ID.getCode(), Boolean.TRUE));
		model.addAttribute("designationTypes", xcodesService.findByXtype(CodeType.MEMBER_DESIGNATION.getCode(), Boolean.TRUE));;
		return "pages/land/landsurveyor";
	}
	

}
