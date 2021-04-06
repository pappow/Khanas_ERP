package com.asl.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import com.asl.enums.ReportMenu;
import com.asl.mapper.ProfileMapper;
import com.asl.model.MenuProfile;
import com.asl.model.ReportProfile;
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
		if(profile == null || StringUtils.isBlank(profile.getProfilecode())) return 0;

		profile.setProfilecode(modifiedProfileCode(profile.getProfilecode()));
		profile.setZid(sessionManager.getBusinessId());
		return profileMapper.save(profile);
	}

	@Override
	public long update(Profile profile) {
		if(profile == null || StringUtils.isBlank(profile.getProfilecode())) return 0;

		profile.setProfilecode(modifiedProfileCode(profile.getProfilecode()));
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
	public Profile findByProfilecode(String profilecode) {
		if(StringUtils.isBlank(profilecode)) return null;
		return profileMapper.findByProfilecode(profilecode, sessionManager.getBusinessId());
	}

	@Override
	public Profile findByProfilecodeAndProfiletype(String profileCode, ProfileType profileType) {
		if(StringUtils.isBlank(profileCode) || profileType == null) return null;
		return profileMapper.findByProfileCodeAndProfileType(profileCode, profileType, sessionManager.getBusinessId());
	}

	@Override
	public List<Profile> getAllProfiles() {
		List<Profile> list = profileMapper.getAllProfiles(null, sessionManager.getBusinessId());
		return list != null ? list : Collections.emptyList();
	}

	@Override
	public List<Profile> getAllProfilesByProfiletype(ProfileType profileType) {
		if(profileType == null) return Collections.emptyList();
		List<Profile> list = profileMapper.getAllProfiles(profileType, sessionManager.getBusinessId());
		return list != null ? list : Collections.emptyList();
	}

	@Override
	public MenuProfile getLoggedInUserMenuProfile() {
		ProfileAllocation pa = paService.findByZemail(sessionManager.getLoggedInUserDetails().getUsername());
		if(pa == null || StringUtils.isBlank(pa.getMenuprofilecode())) {
			return getDefaultMenuProfile();
		}
		return getMenuProfileByProfilecode(pa.getMenuprofilecode());
	}

	@Override
	public MenuProfile getMenuProfileByProfilecode(String profilecode) {
		if(StringUtils.isBlank(profilecode)) return getDefaultMenuProfile();

		Profile profile = findByProfilecode(profilecode);

		List<ProfileLine> profileLines = new ArrayList<>();

		// Generate profile lines from enum first
		for(com.asl.enums.MenuProfile item : com.asl.enums.MenuProfile.values()) {
			ProfileLine pl = new ProfileLine(item);
			pl.setProfilecode(profilecode);
			profileLines.add(pl);
		}

		// All datalist profile lines
		List<DataList> proxyProfileLines = listService.getList("PROFILE", null, null, ProfileType.M.getCode());
		if(proxyProfileLines != null && !proxyProfileLines.isEmpty()) {
			for(DataList dl : proxyProfileLines) {
				ProfileLine pl = new ProfileLine(dl);
				pl.setProfilecode(profile.getProfilecode());
				profileLines.add(pl);
			}
		}

		List<ProfileLine> originalProfileLines = profileLineService.getAllByProfilecodeAndProfiletype(profile.getProfilecode(), profile.getProfiletype());
		profileLines.stream().forEach(proxy -> {
			originalProfileLines.stream().forEach(original -> {
				if(proxy.getProfilelinecode().equalsIgnoreCase(original.getProfilelinecode())) {
					proxy.setProfilelineid(original.getProfilelineid());
					proxy.setProfilelinecode(original.getProfilelinecode());
					proxy.setProfilecode(original.getProfilecode());
					proxy.setProfiletype(original.getProfiletype());
					proxy.setEnabled(original.isEnabled());
					proxy.setDisplay(original.isDisplay());
					proxy.setRequired(original.isRequired());
					proxy.setSeqn(original.getSeqn());
					if(StringUtils.isNotBlank(original.getScreenprompt())) proxy.setScreenprompt(original.getScreenprompt());
				}
			});
		});
		MenuProfile rp = new MenuProfile();
		profileLines.stream().forEach(rp::setProfileLine);
		rp.getProfileLines().sort(Comparator.comparing(ProfileLine::getProfilelinecode));

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
				if(pl.getProfilecode().equalsIgnoreCase(enumpl.getProfilecode())) {
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

	@Override
	public ReportProfile getLoggedInUserReportProfile() {
		ProfileAllocation pa = paService.findByZemail(sessionManager.getLoggedInUserDetails().getUsername());
		if(pa == null || StringUtils.isBlank(pa.getReportprofilecode())) {
			log.debug("===> User \"{}\", don't have any specifc report profile", sessionManager.getLoggedInUserDetails().getUsername());
			return getDefaultReportProfile();
		}

		return getReportProfileByProfilecode(pa.getReportprofilecode());
	}

	@Override
	public ReportProfile getReportProfileByProfilecode(String profilecode) {
		if(StringUtils.isBlank(profilecode)) return getDefaultReportProfile();

		Profile profile = findByProfilecode(profilecode);

		List<ProfileLine> profileLines = new ArrayList<>();

		// All datalist profile lines
		List<DataList> proxyProfileLines = listService.getList("PROFILE", null, null, ProfileType.R.getCode());
		if(proxyProfileLines != null && !proxyProfileLines.isEmpty()) {
			for(DataList dl : proxyProfileLines) {
				ProfileLine pl = new ProfileLine(dl);
				pl.setProfilecode(profile.getProfilecode());
				profileLines.add(pl);
			}
		}

		List<ProfileLine> originalProfileLines = profileLineService.getAllByProfilecodeAndProfiletype(profile.getProfilecode(), profile.getProfiletype());
		profileLines.stream().forEach(proxy -> {
			originalProfileLines.stream().forEach(original -> {
				if(proxy.getProfilecode().equalsIgnoreCase(original.getProfilecode())) {
					proxy.setProfilelineid(original.getProfilelineid());
					proxy.setProfilecode(original.getProfilecode());
					proxy.setProfiletype(original.getProfiletype());
					proxy.setEnabled(original.isEnabled());
					proxy.setDisplay(original.isDisplay());
					proxy.setRequired(original.isRequired());
					proxy.setSeqn(original.getSeqn());
					if(StringUtils.isNotBlank(original.getScreenprompt())) proxy.setScreenprompt(original.getScreenprompt());
				}
			});
		});
		ReportProfile rp = new ReportProfile();
		profileLines.stream().forEach(rp::setProfileLine);

		return rp;
	}

	@Override
	public ReportProfile getDefaultReportProfile() {
		List<ProfileLine> profileLines = new ArrayList<>();
		List<DataList> proxyProfileLines = listService.getList("PROFILE", null, null, ProfileType.R.getCode());
		if(proxyProfileLines != null && !proxyProfileLines.isEmpty()) {
			for(DataList dl : proxyProfileLines) {
				ProfileLine pl = new ProfileLine(dl);
				profileLines.add(pl);
			}
		}

		// override enums with list if list exist
		List<ProfileLine> finalprofileLines = new ArrayList<>();
		EnumSet.allOf(ReportMenu.class).forEach(rm -> {
			ProfileLine enumpl = new ProfileLine(rm);
			profileLines.stream().forEach(pl -> {
				if(pl.getProfilecode().equalsIgnoreCase(enumpl.getProfilecode())) {
					BeanUtils.copyProperties(pl, enumpl);
				}
			});
			finalprofileLines.add(enumpl);
		});

		ReportProfile rp = new ReportProfile();
		finalprofileLines.stream().forEach(rp::setProfileLine);
		log.debug("Default report profile : {}", rp);
		return rp;
	}
}
