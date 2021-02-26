package com.asl.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asl.entity.DataList;
import com.asl.entity.Profile;
import com.asl.entity.ProfileAllocation;
import com.asl.entity.ProfileLine;
import com.asl.enums.ProfileType;
import com.asl.mapper.ProfileMapper;
import com.asl.model.MenuProfile;
import com.asl.service.ListService;
import com.asl.service.ProfileAllocationService;
import com.asl.service.ProfileLineService;
import com.asl.service.ProfileService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Dec 1, 2020
 */
@Slf4j
@Service
public class ProfileServiceImpl extends AbstractGenericService implements ProfileService {

	@Autowired private ProfileMapper profileMapper;
	@Autowired private ProfileAllocationService paService;
	@Autowired private ProfileLineService profileLineService;
	@Autowired private ListService listService;

	@Override
	public long save(Profile profile) {
		if(profile == null || StringUtils.isBlank(profile.getProfileCode())) return 0;

		profile.setProfileCode(modifiedProfileCode(profile.getProfileCode()));
		profile.setZid(sessionManager.getBusinessId());
		return profileMapper.save(profile);
	}

	@Override
	public long update(Profile profile) {
		if(profile == null || StringUtils.isBlank(profile.getProfileCode())) return 0;

		profile.setProfileCode(modifiedProfileCode(profile.getProfileCode()));
		profile.setZid(sessionManager.getBusinessId());
		return profileMapper.update(profile);
	}

	@Override
	public String modifiedProfileCode(String pc) {
		if(StringUtils.isBlank(pc)) return "";
		pc = pc.trim();
		pc = pc.toUpperCase();
		pc = pc.replace(" ", "_");
		return pc;
	}

	@Override
	public Profile findById(Long profileId) {
		if(profileId == null) return null;
		return profileMapper.findByProfileId(profileId);
	}

	@Override
	public Profile findByProfileCodeAndProfileType(String profileCode, ProfileType profileType) {
		if(StringUtils.isBlank(profileCode) || profileType == null) return null;
		return profileMapper.findByProfileCodeAndProfileType(profileCode, profileType, sessionManager.getBusinessId());
	}

	@Override
	public List<Profile> getAllProfiles() {
		List<Profile> list = profileMapper.getAllProfiles(sessionManager.getBusinessId());
		return list != null ? list : Collections.emptyList();
	}

	@Override
	public List<Profile> getAllProfiles(ProfileType profileType) {
		if(profileType == null) return Collections.emptyList();
		List<Profile> list = profileMapper.getAllProfiles(profileType, sessionManager.getBusinessId());
		return list != null ? list : Collections.emptyList();
	}

	@Override
	public MenuProfile getLoggedInUserMenuProfile() {
		ProfileAllocation pa = paService.findByUsername(sessionManager.getLoggedInUserDetails().getUsername());
		if(pa == null || pa.getMenuProfileId() == null) {
			return getDefaultMenuProfile();
		}
		return getMenuProfileById(pa.getMenuProfileId());
	}

	

	@Override
	public MenuProfile getMenuProfileById(Long profileId) {
		if(profileId == null) return getDefaultMenuProfile();

		Profile profile = findById(profileId);

		List<ProfileLine> profileLines = new ArrayList<>();

		// All datalist profile lines
		List<DataList> proxyProfileLines = listService.getList("PROFILE", null, null, ProfileType.M.getCode());
		if(proxyProfileLines != null && !proxyProfileLines.isEmpty()) {
			for(DataList dl : proxyProfileLines) {
				ProfileLine pl = new ProfileLine(dl);
				pl.setProfileId(profile.getProfileId());
				profileLines.add(pl);
			}
		}

		List<ProfileLine> originalProfileLines = profileLineService.getAllByProfileIdAndProfileType(profile.getProfileId(), profile.getProfileType());
		profileLines.stream().forEach(proxy -> {
			originalProfileLines.stream().forEach(original -> {
				if(proxy.getProfileCode().equalsIgnoreCase(original.getProfileCode())) {
					proxy.setProfileLineId(original.getProfileLineId());
					proxy.setProfileId(original.getProfileId());
					proxy.setProfileCode(original.getProfileCode());
					proxy.setProfileType(original.getProfileType());
					proxy.setEnabled(original.isEnabled());
					proxy.setDisplay(original.isDisplay());
					proxy.setRequired(original.isRequired());
					proxy.setSeqn(original.getSeqn());
					if(StringUtils.isNotBlank(original.getScreenPrompt())) proxy.setScreenPrompt(original.getScreenPrompt());
				}
			});
		});
		MenuProfile rp = new MenuProfile();
		profileLines.stream().forEach(rp::setProfileLine);

		return rp;
	}

	@Override
	public MenuProfile getDefaultMenuProfile() {
		List<ProfileLine> profileLines = new ArrayList<>();
		List<DataList> proxyProfileLines = listService.getList("PROFILE", null, null, ProfileType.M.getCode());
		if(proxyProfileLines != null && !proxyProfileLines.isEmpty()) {
			for(DataList dl : proxyProfileLines) {
				ProfileLine pl = new ProfileLine(dl);
				profileLines.add(pl);
			}
		}

		// override enums with list if list exist
		List<ProfileLine> finalprofileLines = new ArrayList<>();
		EnumSet.allOf(com.asl.enums.MenuProfile.class).forEach(rm -> {
			ProfileLine enumpl = new ProfileLine(rm);
			profileLines.stream().forEach(pl -> {
				if(pl.getProfileCode().equalsIgnoreCase(enumpl.getProfileCode())) {
					BeanUtils.copyProperties(pl, enumpl);
				}
			});
			finalprofileLines.add(enumpl);
		});

		MenuProfile mp = new MenuProfile();
		finalprofileLines.stream().forEach(mp::setProfileLine);
		log.debug("Default menu profile : {}", mp);
		return mp;
	}

}
