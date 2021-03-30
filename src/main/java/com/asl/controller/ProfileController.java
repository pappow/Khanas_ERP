
package com.asl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.asl.entity.Profile;
import com.asl.enums.ProfileType;
import com.asl.service.ProfileService;

@Controller
@RequestMapping("/system/profile")
public class ProfileController extends ASLAbstractController {

	@Autowired private ProfileService profileService;

	@GetMapping
	public String loadProfilePage(Model model) {
		model.addAttribute("profile", getDefaultProfile());
		return "pages/system/usersentry/profile/profile";
	}

	private Profile getDefaultProfile() {
		Profile profile = new Profile();
		profile.setProfiletype(ProfileType.U);
		return profile;
	}

	@GetMapping("/{profilecode}")
	public String loadProfilePageByProfileCode(@PathVariable String profilecode, Model model) {
		Profile profile = profileService.findByProfilecode(profilecode);
		if(profile == null) return "redirect:/system/profile";

		model.addAttribute("profile", profile);

		if(ProfileType.M.equals(profile.getProfiletype())) {
			model.addAttribute("menuprofile", profileService.getMenuProfileByProfilecode(profile.getProfilecode()));
		} else if (ProfileType.U.equals(profile.getProfiletype())) {
			// TODO: 
		} else if (ProfileType.R.equals(profile.getProfiletype())) {
			model.addAttribute("reportprofile", profileService.getReportProfileByProfilecode(profile.getProfilecode()));
		}

		return "pages/system/usersentry/profile/profile";
	}
	
}
