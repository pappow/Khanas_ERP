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

import com.asl.entity.LandMemberInfo;
import com.asl.entity.LandPerson;
import com.asl.entity.LandSurveyor;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
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
		return "pages/land/landmemberinfo";
	}
	
	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(LandMemberInfo landMemberInfo, BindingResult bindingResult) {
		if (landMemberInfo == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		
		// if existing
		if(StringUtils.isNotBlank(landMemberInfo.getXmember())) {
			LandMemberInfo xlp = landMemberInfoService.findByLandMemberInfo(landMemberInfo.getXmember());
			BeanUtils.copyProperties(landMemberInfo, xlp,"xtypetrn","xtrn");
			long count = landMemberInfoService.update(xlp);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update member info");
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Member info updated successfully");
			responseHelper.setRedirectUrl("/landmemberinfo/" + xlp.getXmember());
			return responseHelper.getResponse();
		}
		// if new
		long count = landMemberInfoService.save(landMemberInfo);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't save surveyor info");
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Member info saved successfully");
		responseHelper.setRedirectUrl("/landmemberinfo/" + landMemberInfo.getXmember());
			return responseHelper.getResponse();
		}

	@PostMapping("/delete/{xmember}")
	public @ResponseBody Map<String, Object> deleteMember(@PathVariable String xmember,  Model model) {
		LandMemberInfo lm = landMemberInfoService.findByLandMemberInfo(xmember);
		if(lm == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = landMemberInfoService.delete(lm);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setRedirectUrl("/landmemberinfo/" + xmember );
		return responseHelper.getResponse();
}

}
