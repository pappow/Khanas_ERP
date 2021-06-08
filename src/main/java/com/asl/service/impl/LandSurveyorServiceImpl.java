package com.asl.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.LandSurveyor;
import com.asl.mapper.LandSurveyorMapper;
import com.asl.service.LandSurveyorService;

@Service
public class LandSurveyorServiceImpl extends AbstractGenericService implements LandSurveyorService{

	@Autowired
	private LandSurveyorMapper landsurveyorMapper;
	
	@Transactional
	@Override
	public long save(LandSurveyor landSurveyor) {
		if (landSurveyor == null)
			return 0;
		landSurveyor.setZid(sessionManager.getBusinessId());
		landSurveyor.setZauserid(getAuditUser());
		return landsurveyorMapper.saveLandSurveyor(landSurveyor);
	}

	@Transactional
	@Override
	public long update(LandSurveyor landSurveyor) {
		if (landSurveyor == null)
			return 0;
		landSurveyor.setZid(sessionManager.getBusinessId());
		landSurveyor.setZuuserid(getAuditUser());
		return landsurveyorMapper.updateLandSurveyor(landSurveyor);
	}

	@Override
	public List<LandSurveyor> getAllLandSurveyor() {
		return landsurveyorMapper.getAllLandSurveyor(sessionManager.getBusinessId());
	}

	@Override
	public LandSurveyor findByLandSurveyor(String xsurveyor) {
		if (StringUtils.isBlank(xsurveyor))
			return null;
		return landsurveyorMapper.findByLandSurveyor(xsurveyor, sessionManager.getBusinessId());
	}

}
