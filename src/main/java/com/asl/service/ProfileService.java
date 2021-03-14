package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.Profile;
import com.asl.enums.ProfileType;
import com.asl.model.MenuProfile;
import com.asl.model.ReportProfile;

/**
 * @author Zubayer Ahamed
 * @since Dec 1, 2020
 */
@Component
public interface ProfileService {

	long save(Profile profile);
	long update(Profile profile);
	Profile findById(Long profileId);
	Profile findByProfileCodeAndProfileType(String profileCode, ProfileType profileType);
	List<Profile> getAllProfiles();
	List<Profile> getAllProfiles(ProfileType profileType);

	MenuProfile getLoggedInUserMenuProfile();
	MenuProfile getMenuProfileById(Long profileId);
	MenuProfile getDefaultMenuProfile();

	ReportProfile getLoggedInUserReportProfile();
	ReportProfile getReportProfileById(Long profileId);
	ReportProfile getDefaultReportProfile();

	String modifiedProfileCode(String pc);
}
