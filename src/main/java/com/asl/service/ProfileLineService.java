package com.asl.service;

import java.util.List;

import com.asl.entity.ProfileLine;
import com.asl.enums.ProfileType;

/**
 * @author Zubayer Ahamed
 * @since Dec 3, 2020
 */
public interface ProfileLineService {

	long save(ProfileLine profileLine);
	long update(ProfileLine profileLine);
	ProfileLine findById(Long profileLineId);
	List<ProfileLine> getAllByProfileIdAndProfileType(Long profileId, ProfileType profileType);
}
