package com.asl.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asl.entity.ProfileAllocation;
import com.asl.mapper.ProfileAllocationMapper;
import com.asl.service.ProfileAllocationService;

/**
 * @author Zubayer Ahamed
 * @since Jan 5, 2021
 */
@Service
public class ProfileAllocationServiceImpl extends AbstractGenericService implements ProfileAllocationService {

	@Autowired private ProfileAllocationMapper profileAllocationMapper;

	@Override
	public long save(ProfileAllocation profileAllocation) {
		if(profileAllocation == null || StringUtils.isBlank(profileAllocation.getUsername())) return 0;

		profileAllocation.setZid(sessionManager.getBusinessId());
		return profileAllocationMapper.save(profileAllocation);
	}

	@Override
	public long update(ProfileAllocation profileAllocation) {
		if(profileAllocation == null || StringUtils.isBlank(profileAllocation.getUsername())) return 0;

		profileAllocation.setZid(sessionManager.getBusinessId());
		return profileAllocationMapper.update(profileAllocation);
	}

	@Override
	public ProfileAllocation findById(Long paid) {
		if(paid == null) return null;
		return profileAllocationMapper.findById(paid);
	}

	@Override
	public List<ProfileAllocation> getAllProfileAllocation() {
		List<ProfileAllocation> list = profileAllocationMapper.getAllProfileAllocation(sessionManager.getBusinessId());
		if(list == null) return Collections.emptyList();
		return list;
	}

	@Override
	public ProfileAllocation findByUsername(String username) {
		if(StringUtils.isBlank(username)) return null;
		return profileAllocationMapper.findByUsername(username, sessionManager.getBusinessId()); 
	}

	
}
