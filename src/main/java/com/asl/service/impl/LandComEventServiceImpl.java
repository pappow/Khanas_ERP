package com.asl.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.LandComEvent;
import com.asl.mapper.LandComEventMapper;
import com.asl.service.LandComEventService;

@Service
public class LandComEventServiceImpl extends AbstractGenericService implements LandComEventService {

	
	@Autowired
	private LandComEventMapper landComEventMapper;
	
	@Transactional
	@Override
	public long saveLandComEvent(LandComEvent landComEvent) {
		if (landComEvent == null)
			return 0;
		landComEvent.setZid(sessionManager.getBusinessId());
		landComEvent.setZauserid(getAuditUser());
		return landComEventMapper.saveLandComEvent(landComEvent);
	}

	@Transactional
	@Override
	public long updateLandComEvent(LandComEvent landComEvent) {
		if (landComEvent == null)
			return 0;
		landComEvent.setZid(sessionManager.getBusinessId());
		landComEvent.setZauserid(getAuditUser());
		return landComEventMapper.updateLandComEvent(landComEvent);
	}

	@Override
	public List<LandComEvent> getAllLandComEvent() {
		return landComEventMapper.getAllLandComEvent(sessionManager.getBusinessId());
	}

	@Override
	public LandComEvent findAllLandComEvent(String xevent) {
		if (StringUtils.isBlank(xevent))
			return null;
		return landComEventMapper.findAllLandComEvent(xevent, sessionManager.getBusinessId());
	}

	@Override
	public List<LandComEvent> getAllLandOtherEvent() {
		return landComEventMapper.getAllLandOtherEvent(sessionManager.getBusinessId());
	}

}
