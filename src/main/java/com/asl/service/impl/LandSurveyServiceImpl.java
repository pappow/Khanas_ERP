package com.asl.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.mapper.LandSurveyMapper;
import com.asl.service.LandSurveyService;
import com.asl.entity.LandSurvey;

@Service
public class LandSurveyServiceImpl extends AbstractGenericService implements LandSurveyService{
	
	@Autowired
	private LandSurveyMapper landSurveyMapper;
	
	@Transactional
	@Override
	public long save(LandSurvey landSurvey) {
		if (landSurvey == null)
			return 0;
		landSurvey.setZid(sessionManager.getBusinessId());
		landSurvey.setZauserid(getAuditUser());
		return landSurveyMapper.saveLandSurvey(landSurvey);
	}
	
	@Transactional
	@Override
	public long update(LandSurvey landSurvey) {
		if (landSurvey == null)
			return 0;
		landSurvey.setZid(sessionManager.getBusinessId());
		landSurvey.setZuuserid(getAuditUser());
		return landSurveyMapper.updateLandSurvey(landSurvey);
	}
	
	@Override
	public List<LandSurvey> getAllLandSurvey() {
		return landSurveyMapper.getAllLandSurvey(sessionManager.getBusinessId());
	}
	
	@Override
	public List<LandSurvey> findByXlandSurvey(String xland) {
		if (StringUtils.isBlank(xland))
			return null;
		return landSurveyMapper.findByXlandSurvey(xland, sessionManager.getBusinessId());
	}
	
	@Override
	public LandSurvey findLandSurveydetailByXlandAndXrow(String xland, int xrow) {
		if(StringUtils.isBlank(xland)) return null;
		return landSurveyMapper.findLandSurveydetailByXlandAndXrow(xland,xrow,sessionManager.getBusinessId());
	}
	
	@Override
	public LandSurvey findLandSurveyByXlandAndXperson(String xland, String xperson) {
		if(StringUtils.isBlank(xland) || StringUtils.isBlank(xperson)) return null;
		return landSurveyMapper.findLandSurveyByXlandAndXperson(xland, xperson, sessionManager.getBusinessId());
	}
	
	@Override
	public long deleteDetail(LandSurvey landSurvey) {
		if(landSurvey == null) return 0;
		long count = landSurveyMapper.deleteDetail(landSurvey);
		return count;
	}
}
