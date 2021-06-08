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

import com.asl.entity.LandInfo;
import com.asl.entity.LandPerson;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.LandInfoService;


@Controller
@RequestMapping("/landinfo")
public class LandInfoController extends ASLAbstractController {
	
	@Autowired private LandInfoService landInfoService;
	
	@GetMapping
	public String loadLandInfoPage(Model model) {
		
		model.addAttribute("info", getDefaultLandinfo());
		model.addAttribute("allInfos", landInfoService.getAllLandInfo());
		model.addAttribute("prefixes", xtrnService.findByXtypetrn(TransactionCodeType.LAND_ID.getCode(), Boolean.TRUE));
		model.addAttribute("statusTypes", xcodesService.findByXtype(CodeType.STATUS_TYPE.getCode(), Boolean.TRUE));
		return "pages/land/landinfo";
	}
	
	private LandInfo getDefaultLandinfo() {
		LandInfo lf  = new LandInfo();
		lf.setXtypetrn(TransactionCodeType.LAND_ID.getCode());
		lf.setXtrn(TransactionCodeType.LAND_ID.getdefaultCode());
		return lf;
	}
	
	@GetMapping("/{xland}")
	public String loadInfoPage(@PathVariable String xland, Model model) {
		LandInfo landInfo = landInfoService.findByLandInfo(xland);
		if (landInfo == null) return "redirect:/landinfo";

		
		model.addAttribute("info", landInfo);
		model.addAttribute("allInfos", landInfoService.getAllLandInfo());
		model.addAttribute("prefixes", xtrnService.findByXtypetrn(TransactionCodeType.LAND_ID.getCode(), Boolean.TRUE));
		model.addAttribute("statusTypes", xcodesService.findByXtype(CodeType.STATUS_TYPE.getCode(), Boolean.TRUE));
		return "pages/land/landinfo";
	}
	
	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(LandInfo landInfo, BindingResult bindingResult) {
		if (landInfo == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// if existing
		if(StringUtils.isNotBlank(landInfo.getXland())) {
			LandInfo xlp = landInfoService.findByLandInfo(landInfo.getXland());
			BeanUtils.copyProperties(landInfo, xlp,"xtypetrn","xtrn");
			long count = landInfoService.update(xlp);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update land info");
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Land info updated successfully");
			responseHelper.setRedirectUrl("/landinfo/" + xlp.getXland());
			return responseHelper.getResponse();
		}

		// if new
		long count = landInfoService.save(landInfo);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't save land info");
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Land info saved successfully");
		responseHelper.setRedirectUrl("/landinfo/" + landInfo.getXland());
		return responseHelper.getResponse();
	}


}
