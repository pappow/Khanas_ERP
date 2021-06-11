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
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.service.LandExperienceService;


@Controller
@RequestMapping("/landexperience")
public class LandExperienceController extends ASLAbstractController{
	
	@Autowired private LandExperienceService landExperienceService;
	
	@GetMapping
	public String loadLandExperiencePage(Model model) {
		model.addAttribute("education", getDefaultLandExperience());
		model.addAttribute("allExperiencePerson", landExperienceService.getAllLandExperience());
		model.addAttribute("ett", xcodesService.findByXtype(CodeType.EXPERIENCE_TRANSACTION_TYPE.getCode(), Boolean.TRUE));
		model.addAttribute("et", xcodesService.findByXtype(CodeType.EXPERIENCE_TYPE.getCode(), Boolean.TRUE));
		return "pages/landexperience/landexperience";
	}
	
	private LandExperience getDefaultLandExperience() {
		LandExperience lpe  = new LandExperience();
		lpe.setXdesignation("");
		lpe.setXduration(0);
		lpe.setXname("");
		lpe.setXnote("");
		lpe.setNewData(true);
		return lpe;
	}
	
	@GetMapping("/{xperson}")
	public String loadRolePage(@PathVariable String xperson, Model model) {
		LandExperience landPersonex = landExperienceService.findByLandExperiencePerson(xperson);
		if (landPersonex == null) return "redirect:/landperson";

		model.addAttribute("education", landPersonex);
		model.addAttribute("allExperiencePerson", landExperienceService.getAllLandExperience());
		model.addAttribute("ett", xcodesService.findByXtype(CodeType.EXPERIENCE_TRANSACTION_TYPE.getCode(), Boolean.TRUE));
		model.addAttribute("et", xcodesService.findByXtype(CodeType.EXPERIENCE_TYPE.getCode(), Boolean.TRUE));
		return "pages/landexperience/landexperience";
	}
	
	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(LandExperience landExperience, BindingResult bindingResult) {
		if (landExperience == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		
		// Validation
		if(StringUtils.isBlank(landExperience.getXdesignation())) {
			responseHelper.setErrorStatusAndMessage("Please Enter Your Designation");
			return responseHelper.getResponse();
		}
		
		LandExperience exist = landExperienceService.findByLandExperiencePerson(landExperience.getXperson());

		// if existing
		if(exist != null) {
			BeanUtils.copyProperties(landExperience, exist, "xperson");
			long count = landExperienceService.update(landExperience);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Person " +landExperience.getXperson()+" Data alredy Exit");
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Person Experience info updated successfully");
			responseHelper.setRedirectUrl("/landexperience/" + landExperience.getXperson());
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
	
	
	@PostMapping("/archive/{xperson}")
	public @ResponseBody Map<String, Object> archive(@PathVariable String xperson){
		return doArchiveOrRestore(xperson, true);
	}

	@PostMapping("/restore/{xperson}")
	public @ResponseBody Map<String, Object> restore(@PathVariable String xperson){
		return doArchiveOrRestore(xperson, false);
	}

	public Map<String, Object> doArchiveOrRestore(String xperson, boolean archive){
		LandExperience lpe = landExperienceService.findByLandExperiencePerson(xperson);
		if(lpe== null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		lpe.setZactive(archive ? Boolean.FALSE : Boolean.TRUE);
		long count = landExperienceService.update(lpe);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Person Experience Information updated successfully");
		responseHelper.setRedirectUrl("/landexperience/" + lpe.getXperson());
		return responseHelper.getResponse();
	}
	
	@PostMapping("{xperson}/{xrow}/delete")
	public @ResponseBody Map<String, Object> deletePersonDetail(@PathVariable String xperson, @PathVariable String xrow, Model model) {
		LandExperience lpe = landExperienceService.findByXpersonAndXrow(xperson, Integer.parseInt(xrow));
		if(lpe == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = landExperienceService.deleteDetail(lpe);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setRedirectUrl("/landexperience/" + lpe.getXperson());
		return responseHelper.getResponse();
	}

}
