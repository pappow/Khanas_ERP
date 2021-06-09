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

import com.asl.entity.LandPerson;
import com.asl.entity.LandSurveyor;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;

import com.asl.service.LandSurveyorService;

@Controller
@RequestMapping("/landsurveyor")
public class LandSurveyorController extends ASLAbstractController{

	@Autowired private LandSurveyorService landSurveyorService;
	
	@GetMapping
	public String loadLandSurveyorPage(Model model) {
		
		model.addAttribute("surveyor", getDefaultLandsurveyor());
		model.addAttribute("allSurveyors", landSurveyorService.getAllLandSurveyor());
		model.addAttribute("prefixes", xtrnService.findByXtypetrn(TransactionCodeType.SURVEYOR_ID.getCode(), Boolean.TRUE));
		return "pages/land/landsurveyor";
	}
	
	private LandSurveyor getDefaultLandsurveyor() {
		LandSurveyor ls  = new LandSurveyor();
		ls.setXtypetrn(TransactionCodeType.SURVEYOR_ID.getCode());
		ls.setXtrn(TransactionCodeType.SURVEYOR_ID.getdefaultCode());
		return ls;
	}
	
	@GetMapping("/{xsurveyor}")
	public String loadSurveyorPage(@PathVariable String xsurveyor, Model model) {
		LandSurveyor landSurveyor = landSurveyorService.findByLandSurveyor(xsurveyor);
		if (landSurveyor == null) return "redirect:/landsurveyor";
		
		model.addAttribute("surveyor", landSurveyor);
		model.addAttribute("allSurveyors", landSurveyorService.getAllLandSurveyor());
		model.addAttribute("prefixes", xtrnService.findByXtypetrn(TransactionCodeType.SURVEYOR_ID.getCode(), Boolean.TRUE));
		return "pages/land/landsurveyor";
	}
	
	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(LandSurveyor landSurveyor, BindingResult bindingResult) {
		if (landSurveyor == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		
		// if existing
		if(StringUtils.isNotBlank(landSurveyor.getXsurveyor())) {
			LandSurveyor xlp = landSurveyorService.findByLandSurveyor(landSurveyor.getXsurveyor());
			BeanUtils.copyProperties(landSurveyor, xlp,"xtypetrn","xtrn");
			long count = landSurveyorService.update(xlp);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update surveyor info");
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Surveyor info updated successfully");
			responseHelper.setRedirectUrl("/landsurveyor/" + xlp.getXsurveyor());
			return responseHelper.getResponse();
		}
		// if new
		long count = landSurveyorService.save(landSurveyor);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't save surveyor info");
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Surveyor info saved successfully");
		responseHelper.setRedirectUrl("/landsurveyor/" + landSurveyor.getXsurveyor());
			return responseHelper.getResponse();
		}
	
	@PostMapping("/archive/{xsurveyor}")
	public @ResponseBody Map<String, Object> archive(@PathVariable String xsurveyor){
		return doArchiveOrRestore(xsurveyor, true);
	}

	@PostMapping("/restore/{xsurveyor}")
	public @ResponseBody Map<String, Object> restore(@PathVariable String xsurveyor){
		return doArchiveOrRestore(xsurveyor, false);
	}

	public Map<String, Object> doArchiveOrRestore(String xsurveyor, boolean archive){
		LandSurveyor lp = landSurveyorService.findByLandSurveyor(xsurveyor);
		if(lp == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		lp.setZactive(archive ? Boolean.FALSE : Boolean.TRUE);
		long count = landSurveyorService.update(lp);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Surveyor Information updated successfully");
		responseHelper.setRedirectUrl("/landsurveyor/" + lp.getXsurveyor());
		return responseHelper.getResponse();
	}

	
}
