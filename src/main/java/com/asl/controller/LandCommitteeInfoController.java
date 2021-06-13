package com.asl.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

import com.asl.entity.LandCommitteeInfo;
import com.asl.entity.LandSurveyor;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.LandCommitteeInfoService;


@Controller
@RequestMapping("/landcommitteeinfo")
public class LandCommitteeInfoController extends ASLAbstractController{
	
	@Autowired private LandCommitteeInfoService  landCommitteeInfoService ;
	@GetMapping
	public String loadLandCommitteeInfoPage(Model model) {
		
		model.addAttribute("committee", getDefaultLandCommitteeinfo());
		model.addAttribute("allCommitteeInfos", landCommitteeInfoService.getAllLandCommitteeInfo());
		model.addAttribute("prefixes", xtrnService.findByXtypetrn(TransactionCodeType.COMMITTEEINFO_ID.getCode(), Boolean.TRUE));
		return "pages/land/landcommitteeinfo";
	}
	
	private LandCommitteeInfo getDefaultLandCommitteeinfo() {
		LandCommitteeInfo lf  = new LandCommitteeInfo();
		lf.setXtypetrn(TransactionCodeType.COMMITTEEINFO_ID.getCode());
		lf.setXtrn(TransactionCodeType.COMMITTEEINFO_ID.getdefaultCode());
		
		return lf;
	}
	
	@GetMapping("/{xcommittee}")
	public String loadSurveyorPage(@PathVariable String xcommittee, Model model) {
		LandCommitteeInfo landCommitteeInfo = landCommitteeInfoService.findByLandCommitteeInfo(xcommittee);
		if (landCommitteeInfo == null) return "redirect:/landsurveyor";
		
		model.addAttribute("committee", landCommitteeInfo);
		model.addAttribute("allCommitteeInfos", landCommitteeInfoService.getAllLandCommitteeInfo());
		model.addAttribute("prefixes", xtrnService.findByXtypetrn(TransactionCodeType.COMMITTEEINFO_ID.getCode(), Boolean.TRUE));
		return "pages/land/landcommitteeinfo";
	}
	
	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(LandCommitteeInfo landCommitteeInfo, BindingResult bindingResult) {
		if (landCommitteeInfo == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

	// if existing
	if(StringUtils.isNotBlank(landCommitteeInfo.getXcommittee())) {
		LandCommitteeInfo xlp = landCommitteeInfoService.findByLandCommitteeInfo(landCommitteeInfo.getXcommittee());
		BeanUtils.copyProperties(landCommitteeInfo, xlp,"xtypetrn","xtrn");
	long count = landCommitteeInfoService.update(xlp);
	if(count == 0) {
		responseHelper.setErrorStatusAndMessage("Can't update committee info");
		return responseHelper.getResponse();
	}
	responseHelper.setSuccessStatusAndMessage("Committee info updated successfully");
	responseHelper.setRedirectUrl("/landcommitteeinfo/" + xlp.getXcommittee());
	return responseHelper.getResponse();
	}
		
		// if new
		long count = landCommitteeInfoService.save(landCommitteeInfo);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't save committee info");
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Committee info saved successfully");
		responseHelper.setRedirectUrl("/landcommitteeinfo/" + landCommitteeInfo.getXcommittee());
			return responseHelper.getResponse();
		}
	
	

}
