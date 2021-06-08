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

import com.asl.entity.LandExperience;
import com.asl.entity.LandPerson;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.LandExperienceService;
import com.asl.service.XcodesService;

@Controller
@RequestMapping("/landexperience")
public class LandExperienceController extends ASLAbstractController{
	
	@Autowired private LandExperienceService landExperienceService;
	@Autowired private XcodesService xcodesService;
	
	@GetMapping
	public String loadLandExperiencePage(Model model) {
		model.addAttribute("education", getDefaultLandExperience());
		model.addAttribute("allExperiencePerson", landExperienceService.getAllLandExperience());
		model.addAttribute("ett", xcodesService.findByXcode(CodeType.EXPERIENCE_TRANSACTION_TYPE.getCode(), Boolean.TRUE));
		model.addAttribute("et", xcodesService.findByXcode(CodeType.EXPERIENCE_TYPE.getCode(), Boolean.TRUE));
		return "pages/landexperience/landexperience";
	}
	
	private LandExperience getDefaultLandExperience() {
		LandExperience lpe  = new LandExperience();
		lpe.setXdesignation("Developer");
		lpe.setXduration(50);
		lpe.setXname("Abu Bakkar Siddik");
		lpe.setXnote("Hi...");
		lpe.setXtypetrn(CodeType.EXPERIENCE_TRANSACTION_TYPE.getCode());
		lpe.setXtype(CodeType.EXPERIENCE_TYPE.getCode());
		return lpe;
	}
	
	@GetMapping("/{xperson}")
	public String loadRolePage(@PathVariable String xperson, Model model) {
		LandExperience landPersonex = landExperienceService.findByLandExperiencePerson(xperson);
		if (landPersonex == null) return "redirect:/landperson";

		//landPerson.setNewdata(false);
		model.addAttribute("education", landPersonex);
		model.addAttribute("allExperiencePerson", landExperienceService.getAllLandExperience());
		model.addAttribute("ett", xcodesService.findByXcode(CodeType.EXPERIENCE_TRANSACTION_TYPE.getCode(), Boolean.TRUE));
		model.addAttribute("et", xcodesService.findByXcode(CodeType.EXPERIENCE_TYPE.getCode(), Boolean.TRUE));
		return "pages/landexperience/landexperience";
	}
	
	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(LandExperience landExperience, BindingResult bindingResult) {
		if (landExperience == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// if existing
		if(StringUtils.isNotBlank(landExperience.getXperson())) {
			LandExperience lpe = landExperienceService.findByLandExperiencePerson(landExperience.getXperson());
			BeanUtils.copyProperties(landExperience, lpe,"xperson");
			long count = landExperienceService.update(lpe);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update person Experience info");
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Person Experience info updated successfully");
			responseHelper.setRedirectUrl("/landexperience/" + lpe.getXperson());
			return responseHelper.getResponse();
		}

		// if new
		long count = landExperienceService.save(landExperience);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't save person Experience info");
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Person Experience info saved successfully");
		responseHelper.setRedirectUrl("/landexperience/" + landExperience.getXperson());
		return responseHelper.getResponse();
	}
	
}
