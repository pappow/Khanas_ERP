package com.asl.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asl.entity.ProfileLine;
import com.asl.enums.ProfileType;
import com.asl.mapper.ProfileLineMapper;
import com.asl.service.ProfileLineService;

/**
 * @author Zubayer Ahamed
 * @since Dec 3, 2020
 */
@Service
public class ProfileLineServiceImpl extends AbstractGenericService implements ProfileLineService {

	@Autowired private ProfileLineMapper profileLineMapper;

	@Override
	public long save(ProfileLine profileLine) {
		if(profileLine == null) return 0;

		profileLine.setZid(sessionManager.getBusinessId());
		return profileLineMapper.save(profileLine);
	}

	@Override
	public long update(ProfileLine profileLine) {
		if(profileLine == null) return 0;

		profileLine.setZid(sessionManager.getBusinessId());
		return profileLineMapper.save(profileLine);
	}

	@Override
	public ProfileLine findById(Long profileLineId) {
		if(profileLineId == null) return null;
		return profileLineMapper.findById(profileLineId);
	}

	@Override
	public List<ProfileLine> getAllByProfileIdAndProfileType(Long profileId, ProfileType profileType) {
		if(profileId == null || profileType == null) return null;

		List<ProfileLine> list = profileLineMapper.getAllByProfileIdAndProfileType(profileId, profileType, sessionManager.getBusinessId());
		return list == null ? Collections.emptyList() : list;
	}

}
