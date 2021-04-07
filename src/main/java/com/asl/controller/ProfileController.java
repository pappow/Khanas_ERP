
package com.asl.controller;

import java.util.ArrayList;
import java.util.HashMap;
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

import com.asl.entity.Profile;
import com.asl.entity.ProfileLine;
import com.asl.enums.MenuProfile;
import com.asl.enums.ProfileType;
import com.asl.enums.ReportMenu;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.model.ProfileLineWrapper;
import com.asl.model.ReportProfile;
import com.asl.service.ProfileLineService;
import com.asl.service.ProfileService;

@Controller
@RequestMapping("/system/profile")
public class ProfileController extends ASLAbstractController {

	@Autowired private ProfileService profileService;
	@Autowired private ProfileLineService profileLineService;

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

		Map<String, ProfileLineWrapper> profileLinesMap = new HashMap<>();
		if(ProfileType.M.equals(profile.getProfiletype())) {
			com.asl.model.MenuProfile mp = profileService.getMenuProfileByProfilecode(profile.getProfilecode());

			mp.getProfileLines().stream().forEach(pl -> {
				if(profileLinesMap.get(pl.getPgroup()) != null) {
					ProfileLineWrapper wrapper = profileLinesMap.get(pl.getPgroup());
					wrapper.getProfileLines().add(pl);
					if(wrapper.isAllchecked()) wrapper.setAllchecked(pl.isDisplay());
				} else {
					List<ProfileLine> list = new ArrayList<>();
					list.add(pl);
					ProfileLineWrapper wrapper = new ProfileLineWrapper();
					wrapper.getProfileLines().add(pl);
					if(wrapper.isAllchecked()) wrapper.setAllchecked(pl.isDisplay());
					profileLinesMap.put(pl.getPgroup(), wrapper);
				}
			});
		} else if (ProfileType.U.equals(profile.getProfiletype())) {
			// TODO: 
		} else if (ProfileType.R.equals(profile.getProfiletype())) {
			ReportProfile rp = profileService.getReportProfileByProfilecode(profile.getProfilecode());

			rp.getProfileLines().stream().forEach(pl -> {
				if(profileLinesMap.get(pl.getPgroup()) != null) {
					ProfileLineWrapper wrapper = profileLinesMap.get(pl.getPgroup());
					wrapper.getProfileLines().add(pl);
					if(wrapper.isAllchecked()) wrapper.setAllchecked(pl.isDisplay());
				} else {
					List<ProfileLine> list = new ArrayList<>();
					list.add(pl);
					ProfileLineWrapper wrapper = new ProfileLineWrapper();
					wrapper.getProfileLines().add(pl);
					if(wrapper.isAllchecked()) wrapper.setAllchecked(pl.isDisplay());
					profileLinesMap.put(pl.getPgroup(), wrapper);
				}
			});
			
		}

		model.addAttribute("plmap", profileLinesMap);
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
				responseHelper.setSuccessStatusAndMessage("Can't save profile : " + profile.getProfilecode());
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Profile saved successfully");
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

	@PostMapping("/archive/{profilecode}/{profiletype}")
	public @ResponseBody Map<String, Object> archive(@PathVariable String profilecode, @PathVariable ProfileType profiletype){
		return doArchiveOrRestore(profilecode, profiletype, true);
	}

	@PostMapping("/restore/{profilecode}/{profiletype}")
	public @ResponseBody Map<String, Object> restore(@PathVariable String profilecode, @PathVariable ProfileType profiletype){
		return doArchiveOrRestore(profilecode, profiletype, false);
	}

	private Map<String, Object> doArchiveOrRestore(String profilecode, ProfileType profiletype, boolean doArchive) {
		if(StringUtils.isBlank(profilecode)) {
			responseHelper.setErrorStatusAndMessage("Can't update profile");
			return responseHelper.getResponse();
		}

		Profile pr = profileService.findByProfilecodeAndProfiletype(profilecode, profiletype);
		if(pr == null) {
			responseHelper.setErrorStatusAndMessage("Profile not exist into system");
			return responseHelper.getResponse();
		}

		pr.setZactive(doArchive ? Boolean.FALSE : Boolean.TRUE);
		long count = profileService.update(pr);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't update profile");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Profile update successful");
		responseHelper.setRedirectUrl("/system/profile/" + pr.getProfilecode());
		return responseHelper.getResponse();
	}

	@PostMapping("/allprofilelines/update")
	public @ResponseBody Map<String, Object> updateAllProfileLine(ProfileLine profileLine){
		if(profileLine == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		if(ProfileType.M.equals(profileLine.getProfiletype())) {
			com.asl.model.MenuProfile mp = profileService.getMenuProfileByProfilecode(profileLine.getProfilecode());

			for(ProfileLine pl : mp.getProfileLines()) {
				if(!pl.getPgroup().equalsIgnoreCase(profileLine.getPgroup())) continue;
				if(StringUtils.isBlank(pl.getProfilelineid()) && pl.isDisplay() != profileLine.isDisplay()) {
					// create new
					pl.setPgroup(profileLine.getPgroup());
					pl.setDisplay(profileLine.isDisplay());
					profileLineService.save(pl);
				} else if(StringUtils.isNotBlank(pl.getProfilelineid()) && pl.isDisplay() != profileLine.isDisplay()){
					// update existing
					pl.setPgroup(profileLine.getPgroup());
					pl.setDisplay(profileLine.isDisplay());
					profileLineService.update(pl);
				}
			}
		} else if (ProfileType.U.equals(profileLine.getProfiletype())) {
			
		} else if (ProfileType.R.equals(profileLine.getProfiletype())) {
			ReportProfile rp = profileService.getReportProfileByProfilecode(profileLine.getProfilecode());

			for(ProfileLine pl : rp.getProfileLines()) {
				if(!pl.getPgroup().equalsIgnoreCase(profileLine.getPgroup())) continue;
				if(StringUtils.isBlank(pl.getProfilelineid()) && pl.isDisplay() != profileLine.isDisplay()) {
					// create new
					pl.setPgroup(profileLine.getPgroup());
					pl.setDisplay(profileLine.isDisplay());
					profileLineService.save(pl);
				} else if(StringUtils.isNotBlank(pl.getProfilelineid()) && pl.isDisplay() != profileLine.isDisplay()){
					// update existing
					pl.setPgroup(profileLine.getPgroup());
					pl.setDisplay(profileLine.isDisplay());
					profileLineService.update(pl);
				}
			}
		}

		responseHelper.setReloadSectionIdWithUrl("profilelinetable", "/system/profile/profilelines/" + profileLine.getProfilecode() + "/" + profileLine.getProfiletype());
		responseHelper.setSuccessStatusAndMessage("Profile Line updated successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/profileline/save")
	public @ResponseBody Map<String, Object> saveProfileLine(ProfileLine profileLine){
		if(profileLine == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// if new profile line
		if(StringUtils.isBlank(profileLine.getProfilelineid())) {
			profileLine.setXtypetrn(TransactionCodeType.PROFILE_LINE.getCode());
			profileLine.setXtrn(TransactionCodeType.PROFILE_LINE.getdefaultCode());
			long count = profileLineService.save(profileLine);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}

			responseHelper.setReloadSectionIdWithUrl("profilelinetable", "/system/profile/profilelines/" + profileLine.getProfilecode() + "/" + profileLine.getProfiletype());
			responseHelper.setSuccessStatusAndMessage("Profile Line updated successfully");
			return responseHelper.getResponse();
		}

		ProfileLine existProfileLine = profileLineService.findByProfilelinecodeAndProfilecodeAndProfiletype(profileLine.getProfilelinecode(), profileLine.getProfilecode(), profileLine.getProfiletype());
		if(existProfileLine == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		BeanUtils.copyProperties(profileLine, existProfileLine, "profilecode", "profiletype", "profilelineid", "profilelinecode", "xtypetrn", "xtrn");
		long count = profileLineService.update(existProfileLine);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setReloadSectionIdWithUrl("profilelinetable", "/system/profile/profilelines/" + profileLine.getProfilecode() + "/" + profileLine.getProfiletype());
		responseHelper.setSuccessStatusAndMessage("Profile Line updated successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/profilelines/{profilecode}/{profiletype}")
	public String reloadProfileLineTable(@PathVariable String profilecode, @PathVariable ProfileType profiletype, Model model) {
		Map<String, ProfileLineWrapper> profileLinesMap = new HashMap<>();
		if(ProfileType.M.equals(profiletype)) {
			com.asl.model.MenuProfile mp = profileService.getMenuProfileByProfilecode(profilecode);

			mp.getProfileLines().stream().forEach(pl -> {
				if(profileLinesMap.get(pl.getPgroup()) != null) {
					ProfileLineWrapper wrapper = profileLinesMap.get(pl.getPgroup());
					wrapper.getProfileLines().add(pl);
					if(wrapper.isAllchecked()) wrapper.setAllchecked(pl.isDisplay());
				} else {
					List<ProfileLine> list = new ArrayList<>();
					list.add(pl);
					ProfileLineWrapper wrapper = new ProfileLineWrapper();
					wrapper.getProfileLines().add(pl);
					if(wrapper.isAllchecked()) wrapper.setAllchecked(pl.isDisplay());
					profileLinesMap.put(pl.getPgroup(), wrapper);
				}
			});
			model.addAttribute("plmap", profileLinesMap);
		} else if (ProfileType.U.equals(profiletype)) {
			// TODO: 
		} else if (ProfileType.R.equals(profiletype)) {
			ReportProfile rp = profileService.getReportProfileByProfilecode(profilecode);

			rp.getProfileLines().stream().forEach(pl -> {
				if(profileLinesMap.get(pl.getPgroup()) != null) {
					ProfileLineWrapper wrapper = profileLinesMap.get(pl.getPgroup());
					wrapper.getProfileLines().add(pl);
					if(wrapper.isAllchecked()) wrapper.setAllchecked(pl.isDisplay());
				} else {
					List<ProfileLine> list = new ArrayList<>();
					list.add(pl);
					ProfileLineWrapper wrapper = new ProfileLineWrapper();
					wrapper.getProfileLines().add(pl);
					if(wrapper.isAllchecked()) wrapper.setAllchecked(pl.isDisplay());
					profileLinesMap.put(pl.getPgroup(), wrapper);
				}
			});
			
		}

		model.addAttribute("plmap", profileLinesMap);

		Profile p = new Profile();
		p.setProfilecode(profilecode);
		p.setProfiletype(profiletype);
		model.addAttribute("profile", p);

		return "pages/system/usersentry/profile/profile::profilelinetable";
	}

	@GetMapping("/profilelinemodal/{profilelinecode}/{profilecode}/{profiletype}/show")
	public String loadProfileLineModal(@PathVariable String profilelinecode, @PathVariable String profilecode, @PathVariable ProfileType profiletype, Model model) {
		ProfileLine pl = profileLineService.findByProfilelinecodeAndProfilecodeAndProfiletype(profilelinecode, profilecode, profiletype);
		if(pl == null) {
			if(ProfileType.M.equals(profiletype)) {
				pl = new ProfileLine(MenuProfile.valueOf(profilelinecode));
			} else if (ProfileType.U.equals(profiletype)) {
				
			} else if (ProfileType.R.equals(profiletype)) {
				pl = new ProfileLine(ReportMenu.valueOf(profilelinecode));
			}
		}

		model.addAttribute("profileline", pl);

		if(ProfileType.M.equals(profiletype)) {
			return "pages/system/usersentry/profile/menuprofilelinemodal::menuprofilelinemodal";
		} else if (ProfileType.R.equals(profiletype)) {
			return "pages/system/usersentry/profile/reportprofilelinemodal::reportprofilelinemodal";
		} else {
			return "pages/system/usersentry/profile/userprofilelinemodal::userprofilelinemodal";
		}
	}

}

