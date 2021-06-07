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
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.LandPersonService;
import com.asl.service.XtrnService;

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

	@GetMapping("/{xperson}/education/{xrow}/show")
	public String loadEducationModal(@PathVariable String xperson, @PathVariable String xrow) {
		if("new".equalsIgnoreCase(xrow)) {
			
			
		} else {
			
		}
		
		return "pages/land/educationmodal::educationmodal";
	}
	

}
