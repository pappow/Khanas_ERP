package com.asl.controller;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.LandPerson;
import com.asl.entity.LandEducation;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.LandPersonService;

@Controller
@RequestMapping("/landperson")
public class LandPersonController extends ASLAbstractController {

	@Autowired private LandPersonService landPersonService;

	@GetMapping
	public String loadLandPersonPage(Model model) {
		model.addAttribute("person", getDefaultLandperson());
		model.addAttribute("allPersons", landPersonService.getAllLandPerson());
		model.addAttribute("prefixes", xtrnService.findByXtypetrn(TransactionCodeType.PERSON_ID.getCode(), Boolean.TRUE));
		return "pages/land/landperson";
	}

	private LandPerson getDefaultLandperson() {
		LandPerson lp  = new LandPerson();
		lp.setXtypetrn(TransactionCodeType.PERSON_ID.getCode());
		lp.setXtrn(TransactionCodeType.PERSON_ID.getdefaultCode());
		return lp;
	}

	@GetMapping("/{xperson}")
	public String loadRolePage(@PathVariable String xperson, Model model) {
		LandPerson landPerson = landPersonService.findByLandPerson(xperson);
		if (landPerson == null) return "redirect:/landperson";

		//landPerson.setNewdata(false);
		model.addAttribute("person", landPerson);
		model.addAttribute("allPersons", landPersonService.getAllLandPerson());
		model.addAttribute("lpelist", landPersonService.findByPersonEducation(xperson));
		model.addAttribute("prefixes", xtrnService.findByXtypetrn(TransactionCodeType.PERSON_ID.getCode(), Boolean.TRUE));
		return "pages/land/landperson";
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(LandPerson landPerson, BindingResult bindingResult) {
		if (landPerson == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// if existing
		if(StringUtils.isNotBlank(landPerson.getXperson())) {
			LandPerson xlp = landPersonService.findByLandPerson(landPerson.getXperson());
			BeanUtils.copyProperties(landPerson, xlp,"xtypetrn","xtrn");
			long count = landPersonService.update(xlp);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update person info");
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Person info updated successfully");
			responseHelper.setRedirectUrl("/landperson/" + xlp.getXperson());
			return responseHelper.getResponse();
		}

		// if new
		long count = landPersonService.save(landPerson);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't save person info");
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Person info saved successfully");
		responseHelper.setRedirectUrl("/landperson/" + landPerson.getXperson());
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
		LandPerson lp = landPersonService.findByLandPerson(xperson);
		if(lp == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		lp.setZactive(archive ? Boolean.FALSE : Boolean.TRUE);
		long count = landPersonService.update(lp);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Person Information updated successfully");
		responseHelper.setRedirectUrl("/landperson/" + lp.getXperson());
		return responseHelper.getResponse();
	}

	
	

	@GetMapping("/{xperson}/education/{xrow}/show")
	public String loadEducationModal(@PathVariable String xperson, @PathVariable String xrow, Model model) {
		if("new".equalsIgnoreCase(xrow)) {
			LandEducation lpe = new LandEducation();
			lpe.setXyear("");
			lpe.setXexam("");
			lpe.setXresult("");
			lpe.setXperson(xperson);
			model.addAttribute("lpe", lpe);
		} 
		else {
			LandEducation lpe = landPersonService.findLandEducationdetailByXpersonAndXrow(xperson, Integer.parseInt(xrow));
			if(lpe==null) {
				lpe = new LandEducation();
				lpe.setXyear("");
				lpe.setXexam("");
				lpe.setXresult("");
			}
			model.addAttribute("lpe", lpe);
		}
		
		return "pages/land/educationmodal::educationmodal";
	}
	
	
	@PostMapping("/education/save")
	public @ResponseBody Map<String, Object> savePersonEducation(LandEducation landEducation){
		if(landEducation == null || StringUtils.isBlank(landEducation.getXperson())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// Validation
		if(StringUtils.isBlank(landEducation.getXyear())) {
			responseHelper.setErrorStatusAndMessage("Please select Your Year");
			return responseHelper.getResponse();
		}
		if(landEducation.getXmajor() == null || StringUtils.isBlank(landEducation.getXmajor())) {
			responseHelper.setErrorStatusAndMessage("Please Enter your Major");
			return responseHelper.getResponse();
		}
		if(StringUtils.isBlank(landEducation.getXresult())) {
			responseHelper.setErrorStatusAndMessage("Please Enter Your Result");
			return responseHelper.getResponse();
		}

		// Check person already exist in detail list
		if(landEducation.getXrow() == 0 &&  landPersonService.findLandEducationByXpersonAndXexam(landEducation.getXperson(), landEducation.getXexam())!= null) {
			responseHelper.setErrorStatusAndMessage("Person already added into detail list. Please add another one or update existing");
			return responseHelper.getResponse();
		}

		// if existing
		LandEducation existPerson = landPersonService.findLandEducationdetailByXpersonAndXrow(landEducation.getXperson(), landEducation.getXrow());
		if(existPerson != null) {
			BeanUtils.copyProperties(landEducation, existPerson, "xperson");
			long count = landPersonService.update(existPerson);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setReloadSectionIdWithUrl("educationtable", "/landperson/education/" + landEducation.getXperson());
			responseHelper.setSuccessStatusAndMessage("Person Education updated successfully");
			return responseHelper.getResponse();
		}

		// if new detail
		long count = landPersonService.save(landEducation);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setReloadSectionIdWithUrl("educationtable", "/landperson/education/" + landEducation.getXperson());
		responseHelper.setSuccessStatusAndMessage("Person Education saved successfully");
		return responseHelper.getResponse();
	}
	
	@GetMapping("/education/{xperson}")
	public String reloadEducationTable(@PathVariable String xperson, Model model) {
		List<LandEducation> educationList = landPersonService.findByPersonEducation(xperson);
		model.addAttribute("lpelist", educationList);
		model.addAttribute("person", landPersonService.findByLandPerson(xperson));
		return "pages/land/landperson::educationtable";
	}
	
	@PostMapping("{xperson}/education/{xrow}/delete")
	public @ResponseBody Map<String, Object> deletePersonDetail(@PathVariable String xperson, @PathVariable String xrow, Model model) {
		LandEducation lpe = landPersonService.findLandEducationdetailByXpersonAndXrow(xperson, Integer.parseInt(xrow));
		if(lpe == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = landPersonService.deleteDetail(lpe);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setReloadSectionIdWithUrl("educationtable", "/landperson/education/" + xperson);
		return responseHelper.getResponse();
	}

}
