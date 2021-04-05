
package com.asl.controller;

import java.util.Map;

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

import com.asl.entity.Profile;
import com.asl.enums.ProfileType;
import com.asl.enums.ResponseStatus;
import com.asl.service.ProfileService;

@Controller
@RequestMapping("/system/profile")
public class ProfileController extends ASLAbstractController {

	@Autowired private ProfileService profileService;

	@GetMapping
	public String loadProfilePage(Model model) {
		model.addAttribute("profile", getDefaultProfile());
		model.addAttribute("profiles", profileService.getAllProfiles());
		return "pages/system/usersentry/profile/profile";
	}

	private Profile getDefaultProfile() {
		Profile profile = new Profile();
		profile.setProfiletype(ProfileType.M);
		return profile;
	}

	@GetMapping("/{profilecode}")
	public String loadProfilePageByProfileCode(@PathVariable String profilecode, Model model) {
		Profile profile = profileService.findByProfilecode(profilecode);
		if(profile == null) return "redirect:/system/profile";

		if(ProfileType.M.equals(profile.getProfiletype())) {
			model.addAttribute("menuprofile", profileService.getMenuProfileByProfilecode(profile.getProfilecode()));
		} else if (ProfileType.U.equals(profile.getProfiletype())) {
			// TODO: 
		} else if (ProfileType.R.equals(profile.getProfiletype())) {
			model.addAttribute("reportprofile", profileService.getReportProfileByProfilecode(profile.getProfilecode()));
		}

		model.addAttribute("profile", profile);
		model.addAttribute("profiles", profileService.getAllProfiles());
		return "pages/system/usersentry/profile/profile";
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> saveProfile(Profile profile, BindingResult bindingResult, Model model){
		if(profile == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// Modify profile code first
		profile.setProfilecode(profileService.modifiedProfileCode(profile.getProfilecode()));

		// Validate profile data
		modelValidator.validateProfile(profile, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		// if new profile
		if("Y".equalsIgnoreCase(profile.getNewflag())) {
			long count = profileService.save(profile);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setErrorStatusAndMessage("Can't save profile : " + profile.getProfilecode());
			responseHelper.setRedirectUrl("/system/profile/" + profile.getProfilecode());
			return responseHelper.getResponse();
		}

		// if existing
		Profile existProfile = profileService.findByProfilecode(profile.getProfilecode());
		if(existProfile == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		BeanUtils.copyProperties(profile, existProfile, "profilecode", "profiletype");
		long count = profileService.update(existProfile);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't update profile : " + profile.getProfilecode());
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Profile saved successfully");
		responseHelper.setRedirectUrl("/system/profile/" + profile.getProfilecode());
		return responseHelper.getResponse();
	}
}
