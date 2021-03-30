package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Profile;
import com.asl.enums.ProfileType;
import com.asl.model.MenuProfile;

/**
 * @author Zubayer Ahamed
 * @since Feb 17, 2021
 */
@Mapper
public interface ProfileMapper {

	long save(Profile profile);

	long update(Profile profile);

	Profile findByProfilecode(String profilecode);

	Profile findByProfileCodeAndProfileType(String profileCode, ProfileType profileType, String zid);

	List<Profile> getAllProfiles(String zid);

	List<Profile> getAllProfiles(ProfileType profileType, String zid);

	MenuProfile getMenuProfileById(Long profileId, String zid);

	String modifiedProfileCode(String pc);
}
